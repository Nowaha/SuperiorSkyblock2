package com.bgsoftware.superiorskyblock.commands.player;

import com.bgsoftware.superiorskyblock.SuperiorSkyblockPlugin;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.bgsoftware.superiorskyblock.commands.ISuperiorCommand;
import com.bgsoftware.superiorskyblock.commands.arguments.CommandArguments;
import com.bgsoftware.superiorskyblock.commands.arguments.IslandArgument;
import com.bgsoftware.superiorskyblock.lang.Message;
import org.bukkit.command.CommandSender;
import xyz.nowaha.islandlevels.IslandLevelData;
import xyz.nowaha.islandlevels.configurables.Settings;
import xyz.nowaha.islandlevels.data.database.Database;
import xyz.nowaha.islandlevels.guis.LevelsGui;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class CmdLevel implements ISuperiorCommand {

    @Override
    public List<String> getAliases() {
        return Arrays.asList("level", "levels");
    }

    @Override
    public String getPermission() {
        return "superior.island.level";
    }

    @Override
    public String getUsage(java.util.Locale locale) {
        return "level";
    }

    @Override
    public String getDescription(java.util.Locale locale) {
        return "View your island's level track.";
    }

    @Override
    public int getMinArgs() {
        return 1;
    }

    @Override
    public int getMaxArgs() {
        return 1;
    }

    @Override
    public boolean canBeExecutedByConsole() {
        return false;
    }

    @Override
    public void execute(SuperiorSkyblockPlugin plugin, CommandSender sender, String[] args) {
        IslandArgument arguments = CommandArguments.getSenderIsland(plugin, sender);

        Island island = arguments.getIsland();

        if (island == null)
            return;

        SuperiorPlayer superiorPlayer = arguments.getSuperiorPlayer();
        IslandLevelData levelData = Database.getInstance().getLevelsTable().getIslandLevelData(island.getUniqueId());
        superiorPlayer.asPlayer().openInventory(LevelsGui.generateGui(superiorPlayer.asPlayer(), Math.max(1, levelData.getIslandLevel() - Settings.LEVELS_GUI_CURRENT_OFFSET), levelData.getIslandLevel()));
    }

    @Override
    public List<String> tabComplete(SuperiorSkyblockPlugin plugin, CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

}
