package com.bgsoftware.superiorskyblock.commands.player;

import com.bgsoftware.superiorskyblock.SuperiorSkyblockPlugin;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.island.IslandPrivilege;
import com.bgsoftware.superiorskyblock.api.island.PlayerRole;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.bgsoftware.superiorskyblock.commands.CommandTabCompletes;
import com.bgsoftware.superiorskyblock.commands.IPermissibleCommand;
import com.bgsoftware.superiorskyblock.island.SPlayerRole;
import com.bgsoftware.superiorskyblock.island.permissions.IslandPrivileges;
import com.bgsoftware.superiorskyblock.lang.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public final class CmdPermissions implements IPermissibleCommand {

    @Override
    public List<String> getAliases() {
        return Arrays.asList("permissions", "perms", "setpermission", "setperm");
    }

    @Override
    public String getPermission() {
        return "superior.island.permissions";
    }

    @Override
    public String getUsage(java.util.Locale locale) {
        return "permissions [" + Message.COMMAND_ARGUMENT_PLAYER_NAME.getMessage(locale) + "] [reset]";
    }

    @Override
    public String getDescription(java.util.Locale locale) {
        return Message.COMMAND_DESCRIPTION_PERMISSIONS.getMessage(locale);
    }

    @Override
    public int getMinArgs() {
        return 1;
    }

    @Override
    public int getMaxArgs() {
        return 3;
    }

    @Override
    public boolean canBeExecutedByConsole() {
        return false;
    }

    @Override
    public IslandPrivilege getPrivilege() {
        return IslandPrivileges.SET_PERMISSION;
    }

    @Override
    public Message getPermissionLackMessage() {
        return Message.NO_PERMISSION_CHECK_PERMISSION;
    }

    @Override
    public void execute(SuperiorSkyblockPlugin plugin, SuperiorPlayer superiorPlayer, Island island, String[] args) {
        Object permissionHolder = SPlayerRole.guestRole();

        boolean setToDefault = (args.length == 2 ? args[1] : args.length == 3 ? args[2] : "").equalsIgnoreCase("reset");

        if ((!setToDefault && args.length == 2) || args.length == 3) {
            SuperiorPlayer targetPlayer = plugin.getPlayers().getSuperiorPlayer(args[1]);

            if (targetPlayer == null) {
                Message.INVALID_PLAYER.send(superiorPlayer, args[1]);
                return;
            }

            if (island.isMember(targetPlayer) && !superiorPlayer.getPlayerRole().isHigherThan(targetPlayer.getPlayerRole())) {
                Message.CHANGE_PERMISSION_FOR_HIGHER_ROLE.send(superiorPlayer);
                return;
            }

            permissionHolder = targetPlayer;
        }

        if (!setToDefault) {
            if (permissionHolder instanceof PlayerRole) {
                plugin.getMenus().openPermissions(superiorPlayer, null, island, (PlayerRole) permissionHolder);
            } else {
                plugin.getMenus().openPermissions(superiorPlayer, null, island, (SuperiorPlayer) permissionHolder);
            }
        } else {
            if (permissionHolder instanceof PlayerRole) {
                if (plugin.getEventsBus().callIslandClearRolesPrivilegesEvent(island, superiorPlayer)) {
                    island.resetPermissions();
                    Message.PERMISSIONS_RESET_ROLES.send(superiorPlayer);
                }
            } else {
                if (plugin.getEventsBus().callIslandClearPlayerPrivilegesEvent(island, superiorPlayer, (SuperiorPlayer) permissionHolder)) {
                    island.resetPermissions((SuperiorPlayer) permissionHolder);
                    Message.PERMISSIONS_RESET_PLAYER.send(superiorPlayer, ((SuperiorPlayer) permissionHolder).getName());
                }
            }
        }
    }

    @Override
    public List<String> tabComplete(SuperiorSkyblockPlugin plugin, SuperiorPlayer superiorPlayer, Island island, String[] args) {
        List<String> tabVariables = new ArrayList<>();

        switch (args.length) {
            case 2:
                if ("reset".contains(args[1].toLowerCase(Locale.ENGLISH)))
                    tabVariables.add("reset");
                tabVariables.addAll(CommandTabCompletes.getOnlinePlayers(plugin, args[1],
                        plugin.getSettings().isTabCompleteHideVanished()));
                break;
            case 3:
                break;
        }

        if (args.length == 2) {
            if ("reset".contains(args[1].toLowerCase(Locale.ENGLISH)))
                tabVariables.add("reset");
            tabVariables.addAll(CommandTabCompletes.getOnlinePlayers(plugin, args[1],
                    plugin.getSettings().isTabCompleteHideVanished()));
        } else if (args.length == 3) {
            if ("reset".contains(args[2].toLowerCase(Locale.ENGLISH)))
                tabVariables.add("reset");
        }

        return tabVariables;
    }

}
