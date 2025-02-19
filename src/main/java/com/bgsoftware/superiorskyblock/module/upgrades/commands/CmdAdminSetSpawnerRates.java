package com.bgsoftware.superiorskyblock.module.upgrades.commands;

import com.bgsoftware.superiorskyblock.SuperiorSkyblockPlugin;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.bgsoftware.superiorskyblock.commands.IAdminIslandCommand;
import com.bgsoftware.superiorskyblock.commands.arguments.CommandArguments;
import com.bgsoftware.superiorskyblock.commands.arguments.NumberArgument;
import com.bgsoftware.superiorskyblock.lang.Message;
import com.bgsoftware.superiorskyblock.utils.events.EventResult;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public final class CmdAdminSetSpawnerRates implements IAdminIslandCommand {

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("setspawnerrates");
    }

    @Override
    public String getPermission() {
        return "superior.admin.setspawnerrates";
    }

    @Override
    public String getUsage(java.util.Locale locale) {
        return "admin setspawnerrates <" +
                Message.COMMAND_ARGUMENT_PLAYER_NAME.getMessage(locale) + "/" +
                Message.COMMAND_ARGUMENT_ISLAND_NAME.getMessage(locale) + "/" +
                Message.COMMAND_ARGUMENT_ALL_ISLANDS.getMessage(locale) + "> <" +
                Message.COMMAND_ARGUMENT_MULTIPLIER.getMessage(locale) + ">";
    }

    @Override
    public String getDescription(java.util.Locale locale) {
        return Message.COMMAND_DESCRIPTION_ADMIN_SET_SPAWNER_RATES.getMessage(locale);
    }

    @Override
    public int getMinArgs() {
        return 4;
    }

    @Override
    public int getMaxArgs() {
        return 4;
    }

    @Override
    public boolean canBeExecutedByConsole() {
        return true;
    }

    @Override
    public boolean supportMultipleIslands() {
        return true;
    }

    @Override
    public void execute(SuperiorSkyblockPlugin plugin, CommandSender sender, SuperiorPlayer targetPlayer, List<Island> islands, String[] args) {
        NumberArgument<Double> arguments = CommandArguments.getMultiplier(sender, args[3]);

        if (!arguments.isSucceed())
            return;

        double multiplier = arguments.getNumber();

        boolean anyIslandChanged = false;

        for (Island island : islands) {
            EventResult<Double> eventResult = plugin.getEventsBus().callIslandChangeSpawnerRatesEvent(sender, island, multiplier);
            anyIslandChanged |= !eventResult.isCancelled();
            if (!eventResult.isCancelled())
                island.setSpawnerRatesMultiplier(eventResult.getResult());
        }

        if (!anyIslandChanged)
            return;

        if (islands.size() > 1)
            Message.CHANGED_SPAWNER_RATES_ALL.send(sender);
        else if (targetPlayer == null)
            Message.CHANGED_SPAWNER_RATES_NAME.send(sender, islands.get(0).getName());
        else
            Message.CHANGED_SPAWNER_RATES.send(sender, targetPlayer.getName());
    }

}
