package com.bgsoftware.superiorskyblock.nms.v1_17_R1.dragon;

import com.bgsoftware.common.reflection.ReflectField;
import net.minecraft.core.BlockPosition;
import net.minecraft.world.level.levelgen.feature.WorldGenEndTrophy;

import java.lang.reflect.Modifier;

public final class DragonUtils {

    private static final ReflectField<BlockPosition> END_PODIUM_LOCATION = new ReflectField<BlockPosition>(
            WorldGenEndTrophy.class, BlockPosition.class,
            Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL, 1)
            .removeFinal();

    private static BlockPosition currentPodiumPosition;

    private DragonUtils() {

    }

    public static void runWithPodiumPosition(BlockPosition podiumPosition, Runnable runnable) {
        try {
            END_PODIUM_LOCATION.set(null, podiumPosition);
            currentPodiumPosition = podiumPosition;
            runnable.run();
        } finally {
            END_PODIUM_LOCATION.set(null, BlockPosition.b);
            currentPodiumPosition = BlockPosition.b;
        }
    }

    public static BlockPosition getCurrentPodiumPosition() {
        return currentPodiumPosition;
    }

}
