package com.bgsoftware.superiorskyblock.commands.arguments;

import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;

import javax.annotation.Nullable;
import java.util.List;

public final class IslandsListArgument extends Argument<List<Island>, SuperiorPlayer> {

    public IslandsListArgument(List<Island> islands, @Nullable SuperiorPlayer superiorPlayer) {
        super(islands, superiorPlayer);
    }

    public List<Island> getIslands() {
        return super.k;
    }

    @Nullable
    public SuperiorPlayer getSuperiorPlayer() {
        return super.v;
    }

}
