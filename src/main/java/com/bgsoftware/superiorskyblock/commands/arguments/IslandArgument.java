package com.bgsoftware.superiorskyblock.commands.arguments;

import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;

import javax.annotation.Nullable;

public final class IslandArgument extends Argument<Island, SuperiorPlayer> {

    public static final IslandArgument EMPTY = new IslandArgument(null, null);

    public IslandArgument(@Nullable Island island, SuperiorPlayer superiorPlayer) {
        super(island, superiorPlayer);
    }

    @Nullable
    public Island getIsland() {
        return super.k;
    }

    public SuperiorPlayer getSuperiorPlayer() {
        return super.v;
    }

}
