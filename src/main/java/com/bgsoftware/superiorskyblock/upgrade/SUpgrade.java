package com.bgsoftware.superiorskyblock.upgrade;

import com.bgsoftware.superiorskyblock.api.key.KeyMap;
import com.bgsoftware.superiorskyblock.api.upgrades.Upgrade;
import com.bgsoftware.superiorskyblock.key.dataset.KeyMapImpl;
import com.bgsoftware.superiorskyblock.upgrade.cost.EmptyUpgradeCost;
import org.bukkit.World;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public class SUpgrade implements Upgrade {

    private static final SUpgradeLevel NULL_LEVEL = new SUpgradeLevel(0, EmptyUpgradeCost.getInstance(),
            Collections.emptyList(), "", Collections.emptySet(), UpgradeValue.NEGATIVE_DOUBLE,
            UpgradeValue.NEGATIVE_DOUBLE, UpgradeValue.NEGATIVE_DOUBLE, UpgradeValue.NEGATIVE, UpgradeValue.NEGATIVE,
            UpgradeValue.NEGATIVE, UpgradeValue.NEGATIVE, KeyMapImpl.createEmptyMap(), KeyMapImpl.createEmptyMap(),
            new KeyMap[World.Environment.values().length], Collections.emptyMap(), UpgradeValue.NEGATIVE_BIG_DECIMAL,
            Collections.emptyMap());

    private final String name;

    private SUpgradeLevel[] upgradeLevels = new SUpgradeLevel[0];
    private int slot = -1;

    public SUpgrade(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public SUpgradeLevel getUpgradeLevel(int level) {
        return level <= 0 || level > upgradeLevels.length ? NULL_LEVEL : upgradeLevels[level - 1];
    }

    @Override
    public int getMaxUpgradeLevel() {
        return upgradeLevels.length;
    }

    @Override
    public int getSlot() {
        return slot;
    }

    @Override
    public void setSlot(int slot) {
        this.slot = slot;
    }

    public void addUpgradeLevel(int level, SUpgradeLevel upgradeLevel) {
        if (level > upgradeLevels.length)
            upgradeLevels = Arrays.copyOf(upgradeLevels, level);

        upgradeLevels[level - 1] = upgradeLevel;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SUpgrade upgrade = (SUpgrade) o;
        return name.equals(upgrade.name);
    }

}
