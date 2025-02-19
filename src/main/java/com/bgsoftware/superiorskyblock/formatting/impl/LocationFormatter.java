package com.bgsoftware.superiorskyblock.formatting.impl;

import com.bgsoftware.superiorskyblock.formatting.IFormatter;
import org.bukkit.Location;
import org.bukkit.World;

public final class LocationFormatter implements IFormatter<Location> {

    private static final LocationFormatter INSTANCE = new LocationFormatter();

    public static LocationFormatter getInstance() {
        return INSTANCE;
    }

    private LocationFormatter() {

    }

    @Override
    public String format(Location value) {
        World world = value.getWorld();
        return (world == null ? "null" : world.getName()) + ", " + value.getBlockX() + ", " + value.getBlockY() + ", " + value.getBlockZ();
    }
}
