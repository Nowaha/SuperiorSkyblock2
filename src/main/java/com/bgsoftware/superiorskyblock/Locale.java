package com.bgsoftware.superiorskyblock;

import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.bgsoftware.superiorskyblock.config.CommentedConfiguration;
import com.bgsoftware.superiorskyblock.utils.LocaleUtils;
import com.bgsoftware.superiorskyblock.utils.threads.Executor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public enum Locale {

    ADMIN_DEPOSIT_MONEY,
    ADMIN_DEPOSIT_MONEY_NAME,
    ADMIN_HELP_FOOTER,
    ADMIN_HELP_HEADER,
    ADMIN_HELP_LINE,
    ADMIN_HELP_NEXT_PAGE,
    ALREADY_IN_ISLAND,
    ALREADY_IN_ISLAND_OTHER,
    BANNED_FROM_ISLAND,
    BAN_ANNOUNCEMENT,
    BAN_PLAYERS_WITH_LOWER_ROLE,
    BLOCK_LEVEL,
    BLOCK_VALUE,
    BLOCK_VALUE_WORTHLESS,
    BONUS_SET_SUCCESS,
    BORDER_PLAYER_COLOR_UPDATED,
    BUILD_OUTSIDE_ISLAND,
    CANNOT_SET_ROLE,
    CHANGED_BIOME,
    CHANGED_BLOCK_LIMIT,
    CHANGED_BLOCK_LIMIT_ALL,
    CHANGED_BLOCK_LIMIT_NAME,
    CHANGED_CROP_GROWTH,
    CHANGED_CROP_GROWTH_ALL,
    CHANGED_CROP_GROWTH_NAME,
    CHANGED_DISCORD,
    CHANGED_ISLAND_SIZE,
    CHANGED_ISLAND_SIZE_NAME,
    CHANGED_LANGUAGE,
    CHANGED_MOB_DROPS,
    CHANGED_MOB_DROPS_ALL,
    CHANGED_MOB_DROPS_NAME,
    CHANGED_NAME,
    CHANGED_NAME_OTHER,
    CHANGED_NAME_OTHER_NAME,
    CHANGED_PAYPAL,
    CHANGED_SPAWNER_RATES,
    CHANGED_SPAWNER_RATES_ALL,
    CHANGED_SPAWNER_RATES_NAME,
    CHANGED_TEAM_LIMIT,
    CHANGED_TEAM_LIMIT_ALL,
    CHANGED_TEAM_LIMIT_NAME,
    CHANGED_TELEPORT_LOCATION,
    CHANGED_WARPS_LIMIT,
    CHANGED_WARPS_LIMIT_ALL,
    CHANGED_WARPS_LIMIT_NAME,
    CHANGE_PERMISSION_FOR_HIGHER_ROLE,
    COMMAND_ARGUMENT_ALL_ISLANDS("*"),
    COMMAND_ARGUMENT_AMOUNT("amount"),
    COMMAND_ARGUMENT_DISCORD("discord..."),
    COMMAND_ARGUMENT_EMAIL("email"),
    COMMAND_ARGUMENT_ISLAND_NAME("island-name"),
    COMMAND_ARGUMENT_ISLAND_ROLE("island-role"),
    COMMAND_ARGUMENT_LEADER("leader"),
    COMMAND_ARGUMENT_LEVEL("level"),
    COMMAND_ARGUMENT_LIMIT("limit"),
    COMMAND_ARGUMENT_MATERIAL("material"),
    COMMAND_ARGUMENT_MESSAGE("message..."),
    COMMAND_ARGUMENT_MISSION_NAME("mission-name"),
    COMMAND_ARGUMENT_MULTIPLIER("multiplier"),
    COMMAND_ARGUMENT_NEW_LEADER("new-leader"),
    COMMAND_ARGUMENT_PAGE("page"),
    COMMAND_ARGUMENT_PERMISSION("permission"),
    COMMAND_ARGUMENT_PLAYER_NAME("player-name"),
    COMMAND_ARGUMENT_PRIVATE("private"),
    COMMAND_ARGUMENT_RATING("rating"),
    COMMAND_ARGUMENT_SCHEMATIC_NAME("schematic-name"),
    COMMAND_ARGUMENT_SETTINGS("settings"),
    COMMAND_ARGUMENT_SIZE("size"),
    COMMAND_ARGUMENT_UPGRADE_NAME("upgrade-name"),
    COMMAND_ARGUMENT_VALUE("value"),
    COMMAND_ARGUMENT_WARP_NAME("warp-name"),
    COMMAND_COOLDOWN_FORMAT,
    COMMAND_COOLDOWN_SECONDS_NAME,
    COMMAND_COOLDOWN_SECOND_NAME,
    COMMAND_COOLDOWN_MINUTES_NAME,
    COMMAND_COOLDOWN_MINUTE_NAME,
    COMMAND_COOLDOWN_HOURS_NAME,
    COMMAND_COOLDOWN_HOUR_NAME,
    COMMAND_COOLDOWN_DAYS_NAME,
    COMMAND_COOLDOWN_DAY_NAME,
    COMMAND_DESCRIPTION_ACCEPT,
    COMMAND_DESCRIPTION_ADMIN,
    COMMAND_DESCRIPTION_ADMIN_BONUS,
    COMMAND_DESCRIPTION_ADMIN_BYPASS,
    COMMAND_DESCRIPTION_ADMIN_CLOSE,
    COMMAND_DESCRIPTION_ADMIN_DEMOTE,
    COMMAND_DESCRIPTION_ADMIN_DEPOSIT,
    COMMAND_DESCRIPTION_ADMIN_DISBAND,
    COMMAND_DESCRIPTION_ADMIN_GIVE_DISBANDS,
    COMMAND_DESCRIPTION_ADMIN_IGNORE,
    COMMAND_DESCRIPTION_ADMIN_JOIN,
    COMMAND_DESCRIPTION_ADMIN_MISSION,
    COMMAND_DESCRIPTION_ADMIN_MSG,
    COMMAND_DESCRIPTION_ADMIN_MSG_ALL,
    COMMAND_DESCRIPTION_ADMIN_NAME,
    COMMAND_DESCRIPTION_ADMIN_OPEN,
    COMMAND_DESCRIPTION_ADMIN_PROMOTE,
    COMMAND_DESCRIPTION_ADMIN_RECALC,
    COMMAND_DESCRIPTION_ADMIN_RELOAD,
    COMMAND_DESCRIPTION_ADMIN_REMOVE_RATINGS,
    COMMAND_DESCRIPTION_ADMIN_SCHEMATIC,
    COMMAND_DESCRIPTION_ADMIN_SET_BLOCK_LIMIT,
    COMMAND_DESCRIPTION_ADMIN_SET_CROP_GROWTH,
    COMMAND_DESCRIPTION_ADMIN_SET_DISBANDS,
    COMMAND_DESCRIPTION_ADMIN_SET_HOPPERS_LIMIT,
    COMMAND_DESCRIPTION_ADMIN_SET_LEADER,
    COMMAND_DESCRIPTION_ADMIN_SET_MOB_DROPS,
    COMMAND_DESCRIPTION_ADMIN_SET_PERMISSION,
    COMMAND_DESCRIPTION_ADMIN_SET_RATE,
    COMMAND_DESCRIPTION_ADMIN_SET_SETTINGS,
    COMMAND_DESCRIPTION_ADMIN_SET_GENERATOR,
    COMMAND_DESCRIPTION_ADMIN_SET_SIZE,
    COMMAND_DESCRIPTION_ADMIN_SET_SPAWNER_RATES,
    COMMAND_DESCRIPTION_ADMIN_SET_TEAM_LIMIT,
    COMMAND_DESCRIPTION_ADMIN_SET_UPGRADE,
    COMMAND_DESCRIPTION_ADMIN_SET_WARPS_LIMIT,
    COMMAND_DESCRIPTION_ADMIN_SHOW,
    COMMAND_DESCRIPTION_ADMIN_SPY,
    COMMAND_DESCRIPTION_ADMIN_UNIGNORE,
    COMMAND_DESCRIPTION_ADMIN_WITHDRAW,
    COMMAND_DESCRIPTION_BAN,
    COMMAND_DESCRIPTION_BIOME,
    COMMAND_DESCRIPTION_BORDER,
    COMMAND_DESCRIPTION_CLOSE,
    COMMAND_DESCRIPTION_COOP,
    COMMAND_DESCRIPTION_CREATE,
    COMMAND_DESCRIPTION_DEL_WARP,
    COMMAND_DESCRIPTION_DEMOTE,
    COMMAND_DESCRIPTION_DEPOSIT,
    COMMAND_DESCRIPTION_DISBAND,
    COMMAND_DESCRIPTION_EXPEL,
    COMMAND_DESCRIPTION_FLY,
    COMMAND_DESCRIPTION_HELP,
    COMMAND_DESCRIPTION_INVITE,
    COMMAND_DESCRIPTION_KICK,
    COMMAND_DESCRIPTION_LANG,
    COMMAND_DESCRIPTION_LEAVE,
    COMMAND_DESCRIPTION_MISSION,
    COMMAND_DESCRIPTION_MISSIONS,
    COMMAND_DESCRIPTION_NAME,
    COMMAND_DESCRIPTION_OPEN,
    COMMAND_DESCRIPTION_PANEL,
    COMMAND_DESCRIPTION_PARDON,
    COMMAND_DESCRIPTION_PERMISSIONS,
    COMMAND_DESCRIPTION_PROMOTE,
    COMMAND_DESCRIPTION_RANKUP,
    COMMAND_DESCRIPTION_RATE,
    COMMAND_DESCRIPTION_RATINGS,
    COMMAND_DESCRIPTION_SETTINGS,
    COMMAND_DESCRIPTION_RECALC,
    COMMAND_DESCRIPTION_SET_DISCORD,
    COMMAND_DESCRIPTION_SET_PAYPAL,
    COMMAND_DESCRIPTION_SET_ROLE,
    COMMAND_DESCRIPTION_SET_TELEPORT,
    COMMAND_DESCRIPTION_SET_WARP,
    COMMAND_DESCRIPTION_SHOW,
    COMMAND_DESCRIPTION_TEAM,
    COMMAND_DESCRIPTION_TEAM_CHAT,
    COMMAND_DESCRIPTION_TELEPORT,
    COMMAND_DESCRIPTION_TOGGLE,
    COMMAND_DESCRIPTION_TOP,
    COMMAND_DESCRIPTION_TRANSFER,
    COMMAND_DESCRIPTION_UNCOOP,
    COMMAND_DESCRIPTION_UPGRADE,
    COMMAND_DESCRIPTION_VALUE,
    COMMAND_DESCRIPTION_VISIT,
    COMMAND_DESCRIPTION_WARP,
    COMMAND_DESCRIPTION_WARPS,
    COMMAND_DESCRIPTION_WITHDRAW,
    COMMAND_USAGE,
    COOP_ANNOUNCEMENT,
    COOP_BANNED_PLAYER,
    CREATE_ISLAND,
    DELETE_WARP,
    DELETE_WARP_SIGN_BROKE,
    DEMOTED_MEMBER,
    DEMOTE_PLAYERS_WITH_LOWER_ROLE,
    DEPOSIT_ANNOUNCEMENT,
    DESTROY_OUTSIDE_ISLAND,
    DISBANDED_ISLAND,
    DISBANDED_ISLAND_OTHER,
    DISBANDED_ISLAND_OTHER_NAME,
    DISBAND_ANNOUNCEMENT,
    DISBAND_GIVE,
    DISBAND_GIVE_OTHER,
    DISBAND_SET,
    DISBAND_SET_OTHER,
    ENTER_PVP_ISLAND,
    EXPELLED_PLAYER,
    GENERATOR_UPDATED,
    GENERATOR_UPDATED_ALL,
    GENERATOR_UPDATED_NAME,
    GLOBAL_MESSAGE_SENT,
    GLOBAL_MESSAGE_SENT_NAME,
    GOT_BANNED,
    GOT_DEMOTED,
    GOT_EXPELLED,
    GOT_INVITE,
    GOT_INVITE_TOOLTIP,
    GOT_KICKED,
    GOT_PROMOTED,
    GOT_REVOKED,
    GOT_UNBANNED,
    HIT_PLAYER_IN_ISLAND,
    IGNORED_ISLAND,
    IGNORED_ISLAND_NAME,
    INTERACT_OUTSIDE_ISLAND,
    INVALID_AMOUNT,
    INVALID_ISLAND,
    INVALID_ISLAND_LOCATION,
    INVALID_ISLAND_OTHER,
    INVALID_ISLAND_OTHER_NAME,
    INVALID_ISLAND_PERMISSION,
    INVALID_LEVEL,
    INVALID_LIMIT,
    INVALID_MATERIAL,
    INVALID_MISSION,
    INVALID_MULTIPLIER,
    INVALID_PERCENTAGE,
    INVALID_PLAYER,
    INVALID_RATE,
    INVALID_ROLE,
    INVALID_SETTINGS,
    INVALID_SIZE,
    INVALID_TOGGLE_MODE,
    INVALID_UPGRADE,
    INVALID_VISIT_LOCATION,
    INVALID_VISIT_LOCATION_BYPASS,
    INVALID_WARP,
    INVITE_ANNOUNCEMENT,
    INVITE_BANNED_PLAYER,
    INVITE_TO_FULL_ISLAND,
    ISLAND_ALREADY_EXIST,
    ISLAND_BANK_EMPTY,
    ISLAND_CALC_ANNOUNCEMENT,
    ISLAND_CLOSED,
    ISLAND_CREATE_PROCCESS_REQUEST,
    ISLAND_FLY_DISABLED,
    ISLAND_FLY_ENABLED,
    ISLAND_GOT_DELETED_WHILE_INSIDE,
    ISLAND_GOT_PVP_ENABLED_WHILE_INSIDE,
    ISLAND_HELP_FOOTER,
    ISLAND_HELP_HEADER,
    ISLAND_HELP_LINE,
    ISLAND_HELP_NEXT_PAGE,
    ISLAND_INFO_ADMIN_BLOCKS_LIMITS,
    ISLAND_INFO_ADMIN_BLOCKS_LIMITS_LINE,
    ISLAND_INFO_ADMIN_CROPS_MULTIPLIER,
    ISLAND_INFO_ADMIN_DROPS_MULTIPLIER,
    ISLAND_INFO_ADMIN_GENERATOR_RATES,
    ISLAND_INFO_ADMIN_GENERATOR_RATES_LINE,
    ISLAND_INFO_ADMIN_SIZE,
    ISLAND_INFO_ADMIN_SPAWNERS_MULTIPLIER,
    ISLAND_INFO_ADMIN_TEAM_LIMIT,
    ISLAND_INFO_ADMIN_UPGRADES,
    ISLAND_INFO_ADMIN_UPGRADE_LINE,
    ISLAND_INFO_BANK,
    ISLAND_INFO_DISCORD,
    ISLAND_INFO_FOOTER,
    ISLAND_INFO_HEADER,
    ISLAND_INFO_LOCATION,
    ISLAND_INFO_NAME,
    ISLAND_INFO_OWNER,
    ISLAND_INFO_PAYPAL,
    ISLAND_INFO_PLAYER_LINE,
    ISLAND_INFO_RATE,
    ISLAND_INFO_RATE_EMPTY_SYMBOL,
    ISLAND_INFO_RATE_SYMBOL,
    ISLAND_INFO_RATE_ONE_COLOR,
    ISLAND_INFO_RATE_TWO_COLOR,
    ISLAND_INFO_RATE_THREE_COLOR,
    ISLAND_INFO_RATE_FOUR_COLOR,
    ISLAND_INFO_RATE_FIVE_COLOR,
    ISLAND_INFO_ROLES,
    ISLAND_INFO_WORTH,
    ISLAND_OPENED,
    ISLAND_PROTECTED,
    ISLAND_TEAM_STATUS_FOOTER,
    ISLAND_TEAM_STATUS_HEADER,
    ISLAND_TEAM_STATUS_OFFLINE,
    ISLAND_TEAM_STATUS_ONLINE,
    ISLAND_TEAM_STATUS_ROLES,
    ISLAND_TOP_STATUS_OFFLINE,
    ISLAND_TOP_STATUS_ONLINE,
    ISLAND_WARP_PUBLIC,
    ISLAND_WARP_PRIVATE,
    ISLAND_WAS_CLOSED,
    ISLAND_WORTH_RESULT,
    JOINED_ISLAND,
    JOINED_ISLAND_NAME,
    JOINED_ISLAND_AS_COOP,
    JOINED_ISLAND_AS_COOP_NAME,
    JOIN_ANNOUNCEMENT,
    JOIN_FULL_ISLAND,
    JOIN_WHILE_IN_ISLAND,
    KICK_ANNOUNCEMENT,
    KICK_PLAYERS_WITH_LOWER_ROLE,
    LACK_CHANGE_PERMISSION,
    LAST_ROLE_DEMOTE,
    LAST_ROLE_PROMOTE,
    LEAVE_ANNOUNCEMENT,
    LEAVE_ISLAND_AS_LEADER,
    LEFT_ISLAND,
    LEFT_ISLAND_COOP,
    LEFT_ISLAND_COOP_NAME,
    MATERIAL_NOT_SOLID,
    MAXIMUM_LEVEL,
    MESSAGE_SENT,
    MISSION_CANNOT_COMPLETE,
    MISSION_NO_AUTO_REWARD,
    MISSION_NOT_COMPLETE_REQUIRED_MISSIONS,
    MISSION_STATUS_COMPLETE,
    MISSION_STATUS_RESET,
    NAME_ANNOUNCEMENT,
    NAME_BLACKLISTED,
    NAME_CHAT_FORMAT,
    NAME_TOO_LONG,
    NAME_TOO_SHORT,
    NOT_ENOUGH_MONEY_TO_DEPOSIT,
    NOT_ENOUGH_MONEY_TO_UPGRADE,
    NO_BAN_PERMISSION,
    NO_CLOSE_BYPASS,
    NO_CLOSE_PERMISSION,
    NO_COMMAND_PERMISSION,
    NO_COOP_PERMISSION,
    NO_DELETE_WARP_PERMISSION,
    NO_DEMOTE_PERMISSION,
    NO_DEPOSIT_PERMISSION,
    NO_DISBAND_PERMISSION,
    NO_EXPEL_PERMISSION,
    NO_INVITE_PERMISSION,
    NO_ISLAND_INVITE,
    NO_KICK_PERMISSION,
    NO_MORE_DISBANDS,
    NO_MORE_WARPS,
    NO_NAME_PERMISSION,
    NO_OPEN_PERMISSION,
    NO_PERMISSION_CHECK_PERMISSION,
    NO_PROMOTE_PERMISSION,
    NO_RANKUP_PERMISSION,
    NO_RATINGS_PERMISSION,
    NO_SET_BIOME_PERMISSION,
    NO_SET_DISCORD_PERMISSION,
    NO_SET_HOME_PERMISSION,
    NO_SET_PAYPAL_PERMISSION,
    NO_SET_ROLE_PERMISSION,
    NO_SET_SETTINGS_PERMISSION,
    NO_SET_WARP_PERMISSION,
    NO_TRANSFER_PERMISSION,
    NO_UNCOOP_PERMISSION,
    NO_WITHDRAW_PERMISSION,
    PANEL_TOGGLE_OFF,
    PANEL_TOGGLE_ON,
    PERMISSION_CHANGED,
    PERMISSION_CHANGED_ALL,
    PERMISSION_CHANGED_NAME,
    PLAYER_ALREADY_BANNED,
    PLAYER_ALREADY_COOP,
    PLAYER_ALREADY_IN_ROLE,
    PLAYER_EXPEL_BYPASS,
    PLAYER_JOIN_ANNOUNCEMENT,
    PLAYER_NOT_BANNED,
    PLAYER_NOT_COOP,
    PLAYER_NOT_INSIDE_ISLAND,
    PLAYER_NOT_ONLINE,
    PLAYER_QUIT_ANNOUNCEMENT,
    PROMOTED_MEMBER,
    PROMOTE_PLAYERS_WITH_LOWER_ROLE,
    RATE_ANNOUNCEMENT,
    RATE_CHANGE_OTHER,
    RATE_OWN_ISLAND,
    RATE_REMOVE_ALL,
    RATE_SUCCESS,
    REACHED_BLOCK_LIMIT,
    RECALC_ALL_ISLANDS,
    RECALC_ALL_ISLANDS_DONE,
    RECALC_PROCCESS_REQUEST,
    RELOAD_COMPLETED,
    RELOAD_PROCCESS_REQUEST,
    REVOKE_INVITE_ANNOUNCEMENT,
    SAME_NAME_CHANGE,
    SCHEMATIC_LEFT_SELECT,
    SCHEMATIC_NOT_READY,
    SCHEMATIC_PROCCESS_REQUEST,
    SCHEMATIC_READY_TO_CREATE,
    SCHEMATIC_RIGHT_SELECT,
    SCHEMATIC_SAVED,
    SELF_ROLE_CHANGE,
    SET_UPGRADE_LEVEL,
    SET_UPGRADE_LEVEL_NAME,
    SET_WARP,
    SET_WARP_OUTSIDE,
    SETTINGS_UPDATED,
    SETTINGS_UPDATED_NAME,
    SETTINGS_UPDATED_ALL,
    SPY_TEAM_CHAT_FORMAT,
    TEAM_CHAT_FORMAT,
    TELEPORTED_FAILED,
    TELEPORTED_SUCCESS,
    TELEPORTED_TO_WARP,
    TELEPORT_LOCATION_OUTSIDE_ISLAND,
    TELEPORT_WARMUP,
    TELEPORT_WARMUP_CANCEL,
    TOGGLE_FLY_OUTSIDE_ISLAND,
    TOGGLED_BYPASS_OFF,
    TOGGLED_BYPASS_ON,
    TOGGLED_FLY_OFF,
    TOGGLED_FLY_ON,
    TOGGLED_SCHEMATIC_OFF,
    TOGGLED_SCHEMATIC_ON,
    TOGGLED_SPY_OFF,
    TOGGLED_SPY_ON,
    TOGGLED_STACKED_BLOCKS_OFF,
    TOGGLED_STACKED_BLOCKS_ON,
    TOGGLED_TEAM_CHAT_OFF,
    TOGGLED_TEAM_CHAT_ON,
    TOGGLED_WORLD_BORDER_OFF,
    TOGGLED_WORLD_BORDER_ON,
    TRANSFER_ADMIN,
    TRANSFER_ADMIN_ALREADY_LEADER,
    TRANSFER_ADMIN_DIFFERENT_ISLAND,
    TRANSFER_ADMIN_NOT_LEADER,
    TRANSFER_ALREADY_LEADER,
    TRANSFER_BROADCAST,
    UNBAN_ANNOUNCEMENT,
    UNCOOP_ANNOUNCEMENT,
    UNCOOP_LEFT_ANNOUNCEMENT,
    UNIGNORED_ISLAND,
    UNIGNORED_ISLAND_NAME,
    UNSAFE_WARP,
    UPDATED_PERMISSION,
    UPDATED_SETTINGS,
    VISITOR_BLOCK_COMMAND,
    WARP_ALREADY_EXIST,
    WITHDRAWN_MONEY,
    WITHDRAWN_MONEY_NAME,
    WITHDRAW_ALL_MONEY,
    WITHDRAW_ANNOUNCEMENT;

    private static Set<java.util.Locale> locales = new HashSet<>();

    private String defaultMessage;
    private Map<java.util.Locale, String> messages = new HashMap<>();

    Locale(){
        this(null);
    }

    Locale(String defaultMessage){
        this.defaultMessage = defaultMessage;
    }

    public boolean isEmpty(java.util.Locale locale){
        return messages.getOrDefault(locale, "").isEmpty();
    }

    public String getMessage(java.util.Locale locale, Object... objects){
        if(!isEmpty(locale)) {
            String msg = messages.get(locale);

            for (int i = 0; i < objects.length; i++)
                msg = msg.replace("{" + i + "}", objects[i].toString());

            return msg;
        }

        return defaultMessage;
    }

    public void send(SuperiorPlayer superiorPlayer, Object... objects){
        send(superiorPlayer.asPlayer(), superiorPlayer.getUserLocale(), objects);
    }

    public void send(CommandSender sender, Object... objects){
        send(sender, LocaleUtils.getLocale(sender), objects);
    }

    public void send(CommandSender sender, java.util.Locale locale, Object... objects){
        String message = getMessage(locale, objects);
        if(message != null && sender != null)
            sender.sendMessage(message);
    }

    private void setMessage(java.util.Locale locale, String message){
        if(message == null)
            message = "";
        messages.put(locale, message);
    }

    private static SuperiorSkyblockPlugin plugin = SuperiorSkyblockPlugin.getPlugin();

    public static void reload(){
        SuperiorSkyblockPlugin.log("Loading messages started...");
        long startTime = System.currentTimeMillis();

        convertOldFile();
        locales = new HashSet<>();

        File langFolder = new File(plugin.getDataFolder(), "lang");

        if(!langFolder.exists()){
            plugin.saveResource("lang/en-US.yml", false);
            plugin.saveResource("lang/iw-IL.yml", false);
        }

        int messagesAmount = 0;
        boolean countMessages = true;

        for(File langFile : Objects.requireNonNull(langFolder.listFiles())){
            String fileName = langFile.getName().split("\\.")[0];
            java.util.Locale fileLocale;

            try{
                fileLocale = LocaleUtils.getLocale(fileName);
            }catch(IllegalArgumentException ex){
                SuperiorSkyblockPlugin.log("&cThe language \"" + fileName + "\" is invalid. Please correct the file name.");
                continue;
            }

            locales.add(fileLocale);

            CommentedConfiguration cfg = CommentedConfiguration.loadConfiguration(langFile);
            cfg.syncWithConfig(langFile, plugin.getResource("lang/en-US.yml"));

            for(Locale locale : values()){
                locale.setMessage(fileLocale, ChatColor.translateAlternateColorCodes('&', cfg.getString(locale.name(), "")));

                if(countMessages)
                    messagesAmount++;
            }

            countMessages = false;
        }

        SuperiorSkyblockPlugin.log(" - Found " + messagesAmount + " messages in the language files.");
        SuperiorSkyblockPlugin.log("Loading messages done (Took " + (System.currentTimeMillis() - startTime) + "ms)");
    }

    public static void sendMessage(SuperiorPlayer superiorPlayer, String message){
        sendMessage(superiorPlayer.asPlayer(), message);
    }

    public static void sendMessage(CommandSender sender, String message){
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    private static Set<UUID> noInteractMessages = new HashSet<>();

    public static void sendProtectionMessage(SuperiorPlayer superiorPlayer){
        sendProtectionMessage(superiorPlayer.asPlayer(), superiorPlayer.getUserLocale());
    }

    public static void sendProtectionMessage(Player player){
        sendProtectionMessage(player, LocaleUtils.getLocale(player));
    }

    public static void sendProtectionMessage(Player player, java.util.Locale locale){
        if(!noInteractMessages.contains(player.getUniqueId())){
            noInteractMessages.add(player.getUniqueId());
            ISLAND_PROTECTED.send(player, locale);
            Executor.sync(() -> noInteractMessages.remove(player.getUniqueId()), 60L);
        }
    }

    public static boolean isValidLocale(java.util.Locale locale){
        return locales.contains(locale);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void convertOldFile(){
        File file = new File(plugin.getDataFolder(), "lang/en-US.yml");
        if(file.exists()){
            File dest = new File(plugin.getDataFolder(), "lang/en-US.yml");
            dest.getParentFile().mkdirs();
            file.renameTo(dest);
        }
    }

    public static java.util.Locale getDefaultLocale(){
        for(java.util.Locale locale : locales)
            return locale;

        return null;
    }

}
