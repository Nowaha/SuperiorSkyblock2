package com.bgsoftware.superiorskyblock.module.bank.commands;

import com.bgsoftware.superiorskyblock.SuperiorSkyblockPlugin;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.bgsoftware.superiorskyblock.commands.CommandTabCompletes;
import com.bgsoftware.superiorskyblock.commands.ISuperiorCommand;
import com.bgsoftware.superiorskyblock.commands.arguments.CommandArguments;
import com.bgsoftware.superiorskyblock.commands.arguments.IslandArgument;
import com.bgsoftware.superiorskyblock.lang.Message;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CmdBalance implements ISuperiorCommand {

    @Override
    public List<String> getAliases() {
        return Arrays.asList("balance", "bal", "money");
    }

    @Override
    public String getPermission() {
        return "superior.island.balance";
    }

    @Override
    public String getUsage(java.util.Locale locale) {
        return "balance [" +
                Message.COMMAND_ARGUMENT_PLAYER_NAME.getMessage(locale) + "/" +
                Message.COMMAND_ARGUMENT_ISLAND_NAME.getMessage(locale) + "]";
    }

    @Override
    public String getDescription(java.util.Locale locale) {
        return Message.COMMAND_DESCRIPTION_BALANCE.getMessage(locale);
    }

    @Override
    public int getMinArgs() {
        return 1;
    }

    @Override
    public int getMaxArgs() {
        return 2;
    }

    @Override
    public boolean canBeExecutedByConsole() {
        return false;
    }

    @Override
    public void execute(SuperiorSkyblockPlugin plugin, CommandSender sender, String[] args) {
        IslandArgument arguments = args.length == 1 ? CommandArguments.getIslandWhereStanding(plugin, sender) :
                CommandArguments.getIsland(plugin, sender, args[1]);

        Island island = arguments.getIsland();

        if (island == null)
            return;

        SuperiorPlayer superiorPlayer = plugin.getPlayers().getSuperiorPlayer(sender);
        SuperiorPlayer targetPlayer = arguments.getSuperiorPlayer();

        if (island == superiorPlayer.getIsland())
            Message.ISLAND_BANK_SHOW.send(sender, island.getIslandBank().getBalance());
        else if (targetPlayer == null)
            Message.ISLAND_BANK_SHOW_OTHER_NAME.send(sender, island.getName(), island.getIslandBank().getBalance());
        else
            Message.ISLAND_BANK_SHOW_OTHER.send(sender, targetPlayer.getName(), island.getIslandBank().getBalance());
    }

    @Override
    public List<String> tabComplete(SuperiorSkyblockPlugin plugin, CommandSender sender, String[] args) {
        return args.length == 2 ? CommandTabCompletes.getPlayerIslandsExceptSender(plugin, sender, args[1],
                plugin.getSettings().isTabCompleteHideVanished()) : new ArrayList<>();
    }

}
