package com.bgsoftware.superiorskyblock.mission.container;

import com.bgsoftware.superiorskyblock.api.missions.Mission;
import com.bgsoftware.superiorskyblock.api.missions.MissionCategory;
import com.bgsoftware.superiorskyblock.mission.MissionData;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class DefaultMissionsContainer implements MissionsContainer {

    private final Map<String, Mission<?>> missionMap = new HashMap<>();
    private final Map<Mission<?>, MissionData> missionDataMap = new HashMap<>();
    private final Map<String, MissionCategory> missionCategoryMap = new HashMap<>();

    @Override
    public void addMission(Mission<?> mission) {
        this.missionMap.put(mission.getName().toLowerCase(Locale.ENGLISH), mission);
    }

    @Nullable
    @Override
    public Mission<?> getMission(String name) {
        return this.missionMap.get(name.toLowerCase(Locale.ENGLISH));
    }

    @Override
    public List<Mission<?>> getAllMissions() {
        return getFilteredMissions(missionData -> true);
    }

    @Override
    public List<Mission<?>> getPlayerMissions() {
        return getFilteredMissions(missionData -> !missionData.isIslandMission());
    }

    @Override
    public List<Mission<?>> getIslandMissions() {
        return getFilteredMissions(MissionData::isIslandMission);
    }

    @Override
    public void addMissionData(MissionData missionData) {
        this.missionDataMap.put(missionData.getMission(), missionData);
    }

    @Nullable
    @Override
    public MissionData getMissionData(Mission<?> mission) {
        return this.missionDataMap.get(mission);
    }

    @Override
    public void addMissionCategory(MissionCategory missionCategory) {
        this.missionCategoryMap.put(missionCategory.getName().toLowerCase(Locale.ENGLISH), missionCategory);
    }

    @Nullable
    @Override
    public MissionCategory getMissionCategory(String name) {
        return this.missionCategoryMap.get(name.toLowerCase(Locale.ENGLISH));
    }

    @Override
    public List<MissionCategory> getMissionCategories() {
        return Collections.unmodifiableList(new ArrayList<>(missionCategoryMap.values()));
    }

    private List<Mission<?>> getFilteredMissions(Predicate<MissionData> predicate) {
        return missionDataMap.values().stream().filter(predicate)
                .sorted(Comparator.comparingInt(MissionData::getIndex))
                .map(MissionData::getMission)
                .collect(Collectors.toList());
    }

}
