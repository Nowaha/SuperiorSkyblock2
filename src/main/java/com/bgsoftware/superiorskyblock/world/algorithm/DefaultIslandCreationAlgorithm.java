package com.bgsoftware.superiorskyblock.world.algorithm;

import com.bgsoftware.superiorskyblock.SuperiorSkyblockPlugin;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.schematic.Schematic;
import com.bgsoftware.superiorskyblock.api.world.algorithm.IslandCreationAlgorithm;
import com.bgsoftware.superiorskyblock.api.wrappers.BlockPosition;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.bgsoftware.superiorskyblock.formatting.Formatters;
import com.bgsoftware.superiorskyblock.utils.debug.PluginDebugger;
import com.bgsoftware.superiorskyblock.utils.events.EventResult;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class DefaultIslandCreationAlgorithm implements IslandCreationAlgorithm {

    private static final DefaultIslandCreationAlgorithm INSTANCE = new DefaultIslandCreationAlgorithm();

    private static final SuperiorSkyblockPlugin plugin = SuperiorSkyblockPlugin.getPlugin();

    private DefaultIslandCreationAlgorithm() {

    }

    public static DefaultIslandCreationAlgorithm getInstance() {
        return INSTANCE;
    }

    @Override
    public CompletableFuture<IslandCreationResult> createIsland(UUID islandUUID, SuperiorPlayer owner, BlockPosition lastIsland,
                                                                String islandName, Schematic schematic) {
        CompletableFuture<IslandCreationResult> completableFuture = new CompletableFuture<>();

        System.out.println("_a");

        Location islandLocation = plugin.getProviders().getWorldsProvider().getNextLocation(
                lastIsland.parse().clone(),
                plugin.getSettings().getIslandHeight(),
                plugin.getSettings().getMaxIslandSize(),
                owner.getUniqueId(),
                islandUUID
        );

        PluginDebugger.debug("Action: Calculate Next Island, Location: " + Formatters.LOCATION_FORMATTER.format(islandLocation));

        System.out.println("_b");

        Island island = plugin.getFactory().createIsland(owner, islandUUID, islandLocation.add(0.5, 0, 0.5),
                islandName, schematic.getName());

        EventResult<Boolean> event = plugin.getEventsBus().callIslandCreateEvent(owner, island, schematic.getName());

        System.out.println("_c");
        if (!event.isCancelled()) {
            System.out.println("_d");
            schematic.pasteSchematic(island, islandLocation.getBlock().getRelative(BlockFace.DOWN).getLocation(), () -> {
                plugin.getProviders().getWorldsProvider().finishIslandCreation(islandLocation,
                        owner.getUniqueId(), islandUUID);
                System.out.println("_e");
                completableFuture.complete(new IslandCreationResult(island, islandLocation, event.getResult()));
            }, error -> {
                System.out.println("_f");
                plugin.getProviders().getWorldsProvider().finishIslandCreation(islandLocation,
                        owner.getUniqueId(), islandUUID);
                completableFuture.completeExceptionally(error);
            });
        }

        System.out.println("_g");
        return completableFuture;
    }


}
