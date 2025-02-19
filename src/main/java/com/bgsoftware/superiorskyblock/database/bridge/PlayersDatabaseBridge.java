package com.bgsoftware.superiorskyblock.database.bridge;

import com.bgsoftware.superiorskyblock.api.data.DatabaseFilter;
import com.bgsoftware.superiorskyblock.api.missions.Mission;
import com.bgsoftware.superiorskyblock.api.objects.Pair;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unchecked")
public final class PlayersDatabaseBridge {

    private static final Map<UUID, Map<FutureSave, Set<Object>>> SAVE_METHODS_TO_BE_EXECUTED = new ConcurrentHashMap<>();

    private PlayersDatabaseBridge() {
    }

    public static void saveTextureValue(SuperiorPlayer superiorPlayer) {
        superiorPlayer.getDatabaseBridge().updateObject("players",
                createFilter("uuid", superiorPlayer),
                new Pair<>("last_used_skin", superiorPlayer.getTextureValue()));
    }

    public static void savePlayerName(SuperiorPlayer superiorPlayer) {
        superiorPlayer.getDatabaseBridge().updateObject("players",
                createFilter("uuid", superiorPlayer),
                new Pair<>("last_used_name", superiorPlayer.getName()));
    }

    public static void saveUserLocale(SuperiorPlayer superiorPlayer) {
        Locale userLocale = superiorPlayer.getUserLocale();
        superiorPlayer.getDatabaseBridge().updateObject("players_settings",
                createFilter("player", superiorPlayer),
                new Pair<>("language", userLocale.getLanguage() + "-" + userLocale.getCountry()));
    }

    public static void saveToggledBorder(SuperiorPlayer superiorPlayer) {
        superiorPlayer.getDatabaseBridge().updateObject("players_settings",
                createFilter("player", superiorPlayer),
                new Pair<>("toggled_border", superiorPlayer.hasWorldBorderEnabled()));
    }

    public static void saveDisbands(SuperiorPlayer superiorPlayer) {
        superiorPlayer.getDatabaseBridge().updateObject("players",
                createFilter("uuid", superiorPlayer),
                new Pair<>("disbands", superiorPlayer.getDisbands()));
    }

    public static void saveToggledPanel(SuperiorPlayer superiorPlayer) {
        superiorPlayer.getDatabaseBridge().updateObject("players_settings",
                createFilter("player", superiorPlayer),
                new Pair<>("toggled_panel", superiorPlayer.hasToggledPanel()));
    }

    public static void saveIslandFly(SuperiorPlayer superiorPlayer) {
        superiorPlayer.getDatabaseBridge().updateObject("players_settings",
                createFilter("player", superiorPlayer),
                new Pair<>("island_fly", superiorPlayer.hasIslandFlyEnabled()));
    }

    public static void saveBorderColor(SuperiorPlayer superiorPlayer) {
        superiorPlayer.getDatabaseBridge().updateObject("players_settings",
                createFilter("player", superiorPlayer),
                new Pair<>("border_color", superiorPlayer.getBorderColor().name()));
    }

    public static void saveLastTimeStatus(SuperiorPlayer superiorPlayer) {
        superiorPlayer.getDatabaseBridge().updateObject("players",
                createFilter("uuid", superiorPlayer),
                new Pair<>("last_time_updated", superiorPlayer.getLastTimeStatus()));
    }

    public static void saveMission(SuperiorPlayer superiorPlayer, Mission<?> mission, int finishCount) {
        superiorPlayer.getDatabaseBridge().insertObject("players_missions",
                new Pair<>("player", superiorPlayer.getUniqueId().toString()),
                new Pair<>("name", mission.getName().toLowerCase(Locale.ENGLISH)),
                new Pair<>("finish_count", finishCount));
    }

    public static void removeMission(SuperiorPlayer superiorPlayer, Mission<?> mission) {
        superiorPlayer.getDatabaseBridge().deleteObject("players_missions",
                createFilter("player", superiorPlayer, new Pair<>("name", mission.getName().toLowerCase(Locale.ENGLISH)))
        );
    }

    public static void savePersistentDataContainer(SuperiorPlayer superiorPlayer) {
        superiorPlayer.getDatabaseBridge().insertObject("players_custom_data",
                new Pair<>("player", superiorPlayer.getUniqueId().toString()),
                new Pair<>("data", superiorPlayer.getPersistentDataContainer().serialize())
        );
    }

    public static void insertPlayer(SuperiorPlayer superiorPlayer) {
        Locale userLocale = superiorPlayer.getUserLocale();

        superiorPlayer.getDatabaseBridge().insertObject("players",
                new Pair<>("uuid", superiorPlayer.getUniqueId().toString()),
                new Pair<>("last_used_name", superiorPlayer.getName()),
                new Pair<>("last_used_skin", superiorPlayer.getTextureValue()),
                new Pair<>("disbands", superiorPlayer.getDisbands()),
                new Pair<>("last_time_updated", superiorPlayer.getLastTimeStatus())
        );

        superiorPlayer.getDatabaseBridge().insertObject("players_settings",
                new Pair<>("player", superiorPlayer.getUniqueId().toString()),
                new Pair<>("language", userLocale.getLanguage() + "-" + userLocale.getCountry()),
                new Pair<>("toggled_panel", superiorPlayer.hasToggledPanel()),
                new Pair<>("border_color", superiorPlayer.getBorderColor().name()),
                new Pair<>("toggled_border", superiorPlayer.hasWorldBorderEnabled()),
                new Pair<>("island_fly", superiorPlayer.hasIslandFlyEnabled())
        );
    }

    public static void updatePlayer(SuperiorPlayer superiorPlayer) {
        Locale userLocale = superiorPlayer.getUserLocale();
        superiorPlayer.getDatabaseBridge().updateObject("players",
                createFilter("uuid", superiorPlayer),
                new Pair<>("last_used_name", superiorPlayer.getName()),
                new Pair<>("last_used_skin", superiorPlayer.getTextureValue()),
                new Pair<>("disbands", superiorPlayer.getDisbands()),
                new Pair<>("last_time_updated", superiorPlayer.getLastTimeStatus())
        );

        superiorPlayer.getDatabaseBridge().updateObject("players_custom_data",
                createFilter("player", superiorPlayer),
                new Pair<>("data", superiorPlayer.getPersistentDataContainer().serialize())
        );

        superiorPlayer.getDatabaseBridge().updateObject("players_settings",
                createFilter("player", superiorPlayer),
                new Pair<>("language", userLocale.getLanguage() + "-" + userLocale.getCountry()),
                new Pair<>("toggled_panel", superiorPlayer.hasToggledPanel()),
                new Pair<>("border_color", superiorPlayer.getBorderColor().name()),
                new Pair<>("toggled_border", superiorPlayer.hasWorldBorderEnabled()),
                new Pair<>("island_fly", superiorPlayer.hasIslandFlyEnabled())
        );

        superiorPlayer.getDatabaseBridge().deleteObject("players_missions",
                createFilter("player", superiorPlayer));

        for (Map.Entry<Mission<?>, Integer> missionEntry : superiorPlayer.getCompletedMissionsWithAmounts().entrySet()) {
            saveMission(superiorPlayer, missionEntry.getKey(), missionEntry.getValue());
        }
    }

    public static void deletePlayer(SuperiorPlayer superiorPlayer) {
        DatabaseFilter playerFilter = createFilter("player", superiorPlayer);
        superiorPlayer.getDatabaseBridge().deleteObject("players", createFilter("uuid", superiorPlayer));
        superiorPlayer.getDatabaseBridge().deleteObject("players_custom_data", playerFilter);
        superiorPlayer.getDatabaseBridge().deleteObject("players_settings", playerFilter);
        superiorPlayer.getDatabaseBridge().deleteObject("players_missions", playerFilter);
    }

    public static void markPersistentDataContainerToBeSaved(SuperiorPlayer superiorPlayer) {
        Set<Object> varsForPersistentData = SAVE_METHODS_TO_BE_EXECUTED.computeIfAbsent(superiorPlayer.getUniqueId(), u -> new EnumMap<>(FutureSave.class))
                .computeIfAbsent(FutureSave.PERSISTENT_DATA, e -> new HashSet<>());
        if (varsForPersistentData.isEmpty())
            varsForPersistentData.add(new Object());
    }

    public static boolean isModified(SuperiorPlayer superiorPlayer) {
        return SAVE_METHODS_TO_BE_EXECUTED.containsKey(superiorPlayer.getUniqueId());
    }

    public static void executeFutureSaves(SuperiorPlayer superiorPlayer) {
        Map<FutureSave, Set<Object>> futureSaves = SAVE_METHODS_TO_BE_EXECUTED.remove(superiorPlayer.getUniqueId());
        if (futureSaves != null) {
            for (Map.Entry<FutureSave, Set<Object>> futureSaveEntry : futureSaves.entrySet()) {
                switch (futureSaveEntry.getKey()) {
                    case PERSISTENT_DATA:
                        savePersistentDataContainer(superiorPlayer);
                        break;
                }
            }
        }
    }

    private static DatabaseFilter createFilter(String id, SuperiorPlayer superiorPlayer, Pair<String, Object>... others) {
        List<Pair<String, Object>> filters = new ArrayList<>();
        filters.add(new Pair<>(id, superiorPlayer.getUniqueId().toString()));
        if (others != null)
            filters.addAll(Arrays.asList(others));
        return new DatabaseFilter(filters);
    }

    private enum FutureSave {

        PERSISTENT_DATA

    }

}
