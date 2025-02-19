package com.bgsoftware.superiorskyblock.nms.v1_18_R1.mapping.level.border;

import com.bgsoftware.superiorskyblock.nms.v1_18_R1.mapping.MappedObject;
import com.bgsoftware.superiorskyblock.nms.v1_18_R1.mapping.level.WorldServer;

public final class WorldBorder extends MappedObject<net.minecraft.world.level.border.WorldBorder> {

    public WorldBorder() {
        this(new net.minecraft.world.level.border.WorldBorder());
    }

    public WorldBorder(net.minecraft.world.level.border.WorldBorder handle) {
        super(handle);
    }

    public void setWorld(WorldServer worldServer) {
        handle.world = worldServer.getHandle();
    }

    public void setSize(double size) {
        handle.a(size);
    }

    public void setCenter(double x, double z) {
        handle.c(x, z);
    }

    public void transitionSizeBetween(double startSize, double endSize, long time) {
        handle.a(startSize, endSize, time);
    }

    public double getSize() {
        return handle.i();
    }

    public void setWarningDistance(int warningBlocks) {
        handle.c(warningBlocks);
    }

}
