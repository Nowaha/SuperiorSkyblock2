package com.bgsoftware.superiorskyblock.wrappers;

import com.bgsoftware.superiorskyblock.api.wrappers.BlockPosition;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.Objects;

public final class SBlockPosition implements BlockPosition {

    private final int x;
    private final int y;
    private final int z;
    private final String world;

    public SBlockPosition(Location location) {
        this(location.getWorld() == null ? null : location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public SBlockPosition(String world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String getWorldName() {
        return world;
    }

    @Override
    public World getWorld() {
        return Bukkit.getWorld(world);
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getZ() {
        return z;
    }

    @Override
    public Block getBlock() {
        return parse().getBlock();
    }

    @Override
    public Location parse(World world) {
        return new Location(world, getX(), getY(), getZ());
    }

    @Override
    public Location parse() {
        return new Location(getWorld(), getX(), getY(), getZ());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SBlockPosition that = (SBlockPosition) o;
        return x == that.x && y == that.y && z == that.z && world.equals(that.world);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, world);
    }

    @Override
    public String toString() {
        return world + ", " + x + ", " + y + ", " + z;
    }

}
