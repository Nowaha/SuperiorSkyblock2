package com.bgsoftware.superiorskyblock.commands;

import com.bgsoftware.superiorskyblock.SuperiorSkyblockPlugin;
import com.bgsoftware.superiorskyblock.api.commands.SuperiorCommand;
import com.bgsoftware.superiorskyblock.api.handlers.CommandsManager;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.objects.Pair;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.bgsoftware.superiorskyblock.formatting.Formatters;
import com.bgsoftware.superiorskyblock.handler.AbstractHandler;
import com.bgsoftware.superiorskyblock.lang.Message;
import com.bgsoftware.superiorskyblock.lang.PlayerLocales;
import com.bgsoftware.superiorskyblock.utils.FileUtils;
import com.bgsoftware.superiorskyblock.utils.debug.PluginDebugger;
import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.lang.reflect.Constructor;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public final class CommandsHandler extends AbstractHandler implements CommandsManager {

    private final Map<UUID, Map<String, Long>> commandsCooldown = new HashMap<>();

    private final CommandsMap playerCommandsMap;
    private final CommandsMap adminCommandsMap;

    private Set<Runnable> pendingCommands = new HashSet<>();

    private PluginCommand pluginCommand;
    private String label = null;

    public CommandsHandler(SuperiorSkyblockPlugin plugin, CommandsMap playerCommandsMap, CommandsMap adminCommandsMap) {
        super(plugin);
        this.playerCommandsMap = playerCommandsMap;
        this.adminCommandsMap = adminCommandsMap;
    }

    @Override
    public void loadData() {
        String islandCommand = plugin.getSettings().getIslandCommand();
        label = islandCommand.split(",")[0];

        pluginCommand = new PluginCommand(label);

        String[] commandSections = islandCommand.split(",");

        if (commandSections.length > 1) {
            pluginCommand.setAliases(Arrays.asList(Arrays.copyOfRange(commandSections, 1, commandSections.length)));
        }

        plugin.getNMSAlgorithms().registerCommand(pluginCommand);

        playerCommandsMap.loadDefaultCommands();
        adminCommandsMap.loadDefaultCommands();

        loadCommands();

        if (this.pendingCommands != null) {
            Set<Runnable> pendingCommands = new HashSet<>(this.pendingCommands);
            this.pendingCommands = null;
            pendingCommands.forEach(Runnable::run);
        }
    }

    @Override
    public void registerCommand(SuperiorCommand superiorCommand) {
        Preconditions.checkNotNull(superiorCommand, "superiorCommand parameter cannot be null.");
        registerCommand(superiorCommand, true);
    }

    @Override
    public void unregisterCommand(SuperiorCommand superiorCommand) {
        playerCommandsMap.unregisterCommand(superiorCommand);
    }

    @Override
    public void registerAdminCommand(SuperiorCommand superiorCommand) {
        if (pendingCommands != null) {
            pendingCommands.add(() -> registerAdminCommand(superiorCommand));
            return;
        }

        Preconditions.checkNotNull(superiorCommand, "superiorCommand parameter cannot be null.");
        adminCommandsMap.registerCommand(superiorCommand, true);
    }

    @Override
    public void unregisterAdminCommand(SuperiorCommand superiorCommand) {
        Preconditions.checkNotNull(superiorCommand, "superiorCommand parameter cannot be null.");
        adminCommandsMap.unregisterCommand(superiorCommand);
    }

    @Override
    public List<SuperiorCommand> getSubCommands() {
        return getSubCommands(false);
    }

    @Override
    public List<SuperiorCommand> getSubCommands(boolean includeDisabled) {
        return playerCommandsMap.getSubCommands(includeDisabled);
    }

    @Nullable
    @Override
    public SuperiorCommand getCommand(String commandLabel) {
        return playerCommandsMap.getCommand(commandLabel);
    }

    @Override
    public List<SuperiorCommand> getAdminSubCommands() {
        return adminCommandsMap.getSubCommands(true);
    }

    @Nullable
    @Override
    public SuperiorCommand getAdminCommand(String commandLabel) {
        return adminCommandsMap.getCommand(commandLabel);
    }

    @Override
    public void dispatchSubCommand(CommandSender sender, String subCommand) {
        dispatchSubCommand(sender, subCommand, "");
    }

    @Override
    public void dispatchSubCommand(CommandSender sender, String subCommand, String args) {
        String[] argsSplit = args.split(" ");
        String[] commandArguments;

        if (argsSplit.length == 1 && argsSplit[0].isEmpty()) {
            commandArguments = new String[1];
            commandArguments[0] = subCommand;
        } else {
            commandArguments = new String[argsSplit.length + 1];
            commandArguments[0] = subCommand;
            System.arraycopy(argsSplit, 0, commandArguments, 1, argsSplit.length);
        }

        pluginCommand.execute(sender, "", commandArguments);
    }

    public String getLabel() {
        return label;
    }

    public void registerCommand(SuperiorCommand superiorCommand, boolean sort) {
        if (pendingCommands != null) {
            pendingCommands.add(() -> registerCommand(superiorCommand, sort));
            return;
        }

        playerCommandsMap.registerCommand(superiorCommand, sort);
    }

    @SuppressWarnings({"ResultOfMethodCallIgnored", "ConstantConditions"})
    private void loadCommands() {
        File commandsFolder = new File(plugin.getDataFolder(), "commands");

        if (!commandsFolder.exists()) {
            commandsFolder.mkdirs();
            return;
        }

        for (File file : commandsFolder.listFiles()) {
            if (!file.getName().endsWith(".jar"))
                continue;

            try {
                //noinspection deprecation
                Optional<Class<?>> commandClass = FileUtils.getClasses(file.toURL(), SuperiorCommand.class).stream().findFirst();

                if (!commandClass.isPresent())
                    continue;

                SuperiorCommand superiorCommand = createInstance(commandClass.get());

                if (file.getName().toLowerCase(Locale.ENGLISH).contains("admin")) {
                    registerAdminCommand(superiorCommand);
                    SuperiorSkyblockPlugin.log("Successfully loaded external admin command: " + file.getName().split("\\.")[0]);
                } else {
                    registerCommand(superiorCommand);
                    SuperiorSkyblockPlugin.log("Successfully loaded external command: " + file.getName().split("\\.")[0]);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                PluginDebugger.debug(ex);
            }
        }

    }

    private SuperiorCommand createInstance(Class<?> clazz) throws Exception {
        Preconditions.checkArgument(SuperiorCommand.class.isAssignableFrom(clazz), "Class " + clazz + " is not a SuperiorCommand.");

        for (Constructor<?> constructor : clazz.getConstructors()) {
            if (constructor.getParameterCount() == 0) {
                if (!constructor.isAccessible())
                    constructor.setAccessible(true);

                return (SuperiorCommand) constructor.newInstance();
            }
        }

        throw new IllegalArgumentException("Class " + clazz + " has no valid constructors.");
    }

    private class PluginCommand extends BukkitCommand {

        PluginCommand(String islandCommandLabel) {
            super(islandCommandLabel);
        }

        @Override
        public boolean execute(CommandSender sender, String label, String[] args) {
            java.util.Locale locale = PlayerLocales.getLocale(sender);

            if (args.length > 0) {
                SuperiorCommand command = playerCommandsMap.getCommand(args[0]);
                if (command != null) {
                    if (!(sender instanceof Player) && !command.canBeExecutedByConsole()) {
                        Message.CUSTOM.send(sender, "&cCan be executed only by players!", true);
                        return false;
                    }

                    if (!command.getPermission().isEmpty() && !sender.hasPermission(command.getPermission())) {
                        PluginDebugger.debug("Action: Execute Command, Player: " + sender.getName() + ", Command: " + args[0] + ", Missing Permission: " + command.getPermission());
                        Message.NO_COMMAND_PERMISSION.send(sender, locale);
                        return false;
                    }

                    if (args.length < command.getMinArgs() || args.length > command.getMaxArgs()) {
                        Message.COMMAND_USAGE.send(sender, locale, getLabel() + " " + command.getUsage(locale));
                        return false;
                    }

                    String commandLabel = command.getAliases().get(0);

                    if (sender instanceof Player) {
                        UUID uuid = ((Player) sender).getUniqueId();
                        SuperiorPlayer superiorPlayer = plugin.getPlayers().getSuperiorPlayer(uuid);
                        if (!superiorPlayer.hasPermission("superior.admin.bypass.cooldowns")) {
                            Pair<Integer, String> commandCooldown = plugin.getSettings().getCommandsCooldown().get(commandLabel);
                            if (commandCooldown != null) {
                                Map<String, Long> playerCooldowns = commandsCooldown.get(uuid);
                                long timeNow = System.currentTimeMillis();

                                if (playerCooldowns != null) {
                                    Long timeToExecute = playerCooldowns.get(commandLabel);
                                    if (timeToExecute != null) {
                                        if (timeNow < timeToExecute) {
                                            String formattedTime = Formatters.TIME_FORMATTER.format(Duration.ofMillis(timeToExecute - timeNow), locale);
                                            Message.COMMAND_COOLDOWN_FORMAT.send(sender, locale, formattedTime);
                                            return false;
                                        }
                                    }
                                }

                                commandsCooldown.computeIfAbsent(uuid, u -> new HashMap<>()).put(commandLabel,
                                        timeNow + commandCooldown.getKey());
                            }
                        }
                    }

                    command.execute(plugin, sender, args);
                    return false;
                }
            }

            if (sender instanceof Player) {
                SuperiorPlayer superiorPlayer = plugin.getPlayers().getSuperiorPlayer(sender);

                if (superiorPlayer != null) {
                    Island island = superiorPlayer.getIsland();

                    if (args.length != 0) {
                        Bukkit.dispatchCommand(sender, label + " help");
                    } else if (island == null) {
                        Bukkit.dispatchCommand(sender, label + " create");
                    } else if (superiorPlayer.hasToggledPanel()) {
                        Bukkit.dispatchCommand(sender, label + " panel");
                    } else {
                        Bukkit.dispatchCommand(sender, label + " tp");
                    }

                    return false;
                }
            }

            Message.NO_COMMAND_PERMISSION.send(sender, locale);

            return false;
        }


        @Override
        public List<String> tabComplete(CommandSender sender, String label, String[] args) {
            if (args.length > 0) {
                SuperiorCommand command = playerCommandsMap.getCommand(args[0]);
                if (command != null) {
                    return command.getPermission() != null && !sender.hasPermission(command.getPermission()) ?
                            new ArrayList<>() : command.tabComplete(plugin, sender, args);
                }
            }

            List<String> list = new ArrayList<>();

            for (SuperiorCommand subCommand : getSubCommands()) {
                if (subCommand.getPermission() == null || sender.hasPermission(subCommand.getPermission())) {
                    List<String> aliases = new ArrayList<>(subCommand.getAliases());
                    aliases.addAll(plugin.getSettings().getCommandAliases().getOrDefault(aliases.get(0).toLowerCase(Locale.ENGLISH), new ArrayList<>()));
                    for (String _aliases : aliases) {
                        if (_aliases.contains(args[0].toLowerCase(Locale.ENGLISH))) {
                            list.add(_aliases);
                        }
                    }
                }
            }

            return list;
        }

    }

}
