package com.bgsoftware.superiorskyblock.nms.v1_8_R3;

import com.bgsoftware.superiorskyblock.SuperiorSkyblockPlugin;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.key.Key;
import com.bgsoftware.superiorskyblock.api.key.KeyMap;
import com.bgsoftware.superiorskyblock.key.KeyImpl;
import com.bgsoftware.superiorskyblock.key.dataset.KeyMapImpl;
import com.bgsoftware.superiorskyblock.nms.NMSChunks;
import com.bgsoftware.superiorskyblock.nms.v1_8_R3.chunks.CropsTickingTileEntity;
import com.bgsoftware.superiorskyblock.world.blocks.BlockData;
import com.bgsoftware.superiorskyblock.world.chunks.CalculatedChunk;
import com.bgsoftware.superiorskyblock.world.chunks.ChunkPosition;
import com.bgsoftware.superiorskyblock.world.chunks.ChunksTracker;
import com.bgsoftware.superiorskyblock.world.generator.IslandsGenerator;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.BlockDoubleStep;
import net.minecraft.server.v1_8_R3.BlockDoubleStepAbstract;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.Chunk;
import net.minecraft.server.v1_8_R3.ChunkCoordIntPair;
import net.minecraft.server.v1_8_R3.ChunkSection;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EnumSkyBlock;
import net.minecraft.server.v1_8_R3.IBlockData;
import net.minecraft.server.v1_8_R3.MinecraftKey;
import net.minecraft.server.v1_8_R3.PacketPlayOutMapChunk;
import net.minecraft.server.v1_8_R3.TileEntity;
import net.minecraft.server.v1_8_R3.World;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_8_R3.CraftChunk;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftBlock;
import org.bukkit.craftbukkit.v1_8_R3.generator.CustomChunkGenerator;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.v1_8_R3.util.UnsafeList;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public final class NMSChunksImpl implements NMSChunks {

    private static final SuperiorSkyblockPlugin plugin = SuperiorSkyblockPlugin.getPlugin();

    private static void removeEntities(Chunk chunk) {
        for (int i = 0; i < chunk.entitySlices.length; i++) {
            chunk.entitySlices[i].forEach(entity -> {
                if (!(entity instanceof EntityHuman))
                    entity.dead = true;
            });
            chunk.entitySlices[i] = new UnsafeList<>();
        }
    }

    private static void removeBlocks(Chunk chunk) {
        WorldServer worldServer = (WorldServer) chunk.world;

        if (worldServer.generator != null && !(worldServer.generator instanceof IslandsGenerator)) {
            CustomChunkGenerator customChunkGenerator = new CustomChunkGenerator(worldServer, 0L, worldServer.generator);
            Chunk generatedChunk = customChunkGenerator.getOrCreateChunk(chunk.locX, chunk.locZ);

            for (int i = 0; i < 16; i++)
                chunk.getSections()[i] = generatedChunk.getSections()[i];

            for (Map.Entry<BlockPosition, TileEntity> entry : generatedChunk.getTileEntities().entrySet())
                worldServer.setTileEntity(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void setBiome(List<ChunkPosition> chunkPositions, Biome biome, Collection<Player> playersToUpdate) {
        if (chunkPositions.isEmpty())
            return;

        List<ChunkCoordIntPair> chunksCoords = chunkPositions.stream()
                .map(chunkPosition -> new ChunkCoordIntPair(chunkPosition.getX(), chunkPosition.getZ()))
                .collect(Collectors.toList());

        WorldServer worldServer = ((CraftWorld) chunkPositions.get(0).getWorld()).getHandle();
        byte biomeBase = (byte) CraftBlock.biomeToBiomeBase(biome).id;

        NMSUtils.runActionOnChunks(worldServer, chunksCoords, true, null, chunk -> {
            Arrays.fill(chunk.getBiomeIndex(), biomeBase);
            chunk.e();
        }, null);
    }

    @Override
    public void deleteChunks(Island island, List<ChunkPosition> chunkPositions, Runnable onFinish) {
        if (chunkPositions.isEmpty())
            return;

        List<ChunkCoordIntPair> chunksCoords = chunkPositions.stream()
                .map(chunkPosition -> new ChunkCoordIntPair(chunkPosition.getX(), chunkPosition.getZ()))
                .collect(Collectors.toList());

        chunkPositions.forEach(chunkPosition -> ChunksTracker.markEmpty(island, chunkPosition, false));

        WorldServer worldServer = ((CraftWorld) chunkPositions.get(0).getWorld()).getHandle();

        NMSUtils.runActionOnChunks(worldServer, chunksCoords, true, onFinish, chunk -> {
            Arrays.fill(chunk.getSections(), null);

            removeEntities(chunk);

            for (Map.Entry<BlockPosition, TileEntity> tileEntityEntry : chunk.tileEntities.entrySet()) {
                worldServer.tileEntityList.remove(tileEntityEntry.getValue());
                try {
                    // This field doesn't exist in Taco 1.8
                    worldServer.h.remove(tileEntityEntry.getValue());
                } catch (Throwable ignored) {
                }
                worldServer.capturedTileEntities.remove(tileEntityEntry.getKey());
            }

            chunk.tileEntities.clear();

            removeBlocks(chunk);
        }, chunk -> {
            NMSUtils.sendPacketToRelevantPlayers(worldServer, chunk.locX, chunk.locZ,
                    new PacketPlayOutMapChunk(chunk, false, 65535));
        });
    }

    @Override
    public CompletableFuture<List<CalculatedChunk>> calculateChunks(List<ChunkPosition> chunkPositions) {
        CompletableFuture<List<CalculatedChunk>> completableFuture = new CompletableFuture<>();
        List<CalculatedChunk> allCalculatedChunks = new ArrayList<>();

        List<ChunkCoordIntPair> chunksCoords = chunkPositions.stream()
                .map(chunkPosition -> new ChunkCoordIntPair(chunkPosition.getX(), chunkPosition.getZ()))
                .collect(Collectors.toList());
        WorldServer worldServer = ((CraftWorld) chunkPositions.get(0).getWorld()).getHandle();

        NMSUtils.runActionOnChunks(worldServer, chunksCoords, false, () -> {
            completableFuture.complete(allCalculatedChunks);
        }, chunk -> {
            ChunkPosition chunkPosition = ChunkPosition.of(worldServer.getWorld(), chunk.locX, chunk.locZ);

            KeyMap<Integer> blockCounts = KeyMapImpl.createHashMap();
            Set<Location> spawnersLocations = new HashSet<>();

            for (ChunkSection chunkSection : chunk.getSections()) {
                if (chunkSection != null) {
                    for (BlockPosition bp : BlockPosition.b(new BlockPosition(0, 0, 0), new BlockPosition(15, 15, 15))) {
                        IBlockData blockData = chunkSection.getType(bp.getX(), bp.getY(), bp.getZ());
                        Block block = blockData.getBlock();
                        if (block != Blocks.AIR) {
                            Location location = new Location(worldServer.getWorld(),
                                    (chunkPosition.getX() << 4) + bp.getX(),
                                    chunkSection.getYPosition() + bp.getY(),
                                    (chunkPosition.getZ() << 4) + bp.getZ());
                            int blockAmount = 1;

                            if (block instanceof BlockDoubleStep) {
                                blockAmount = 2;
                                // Converts the block data to a regular slab
                                MinecraftKey blockKey = Block.REGISTRY.c(block);
                                blockData = Block.REGISTRY.get(new MinecraftKey(blockKey.a()
                                                .replace("double_", ""))).getBlockData()
                                        .set(BlockDoubleStepAbstract.VARIANT, blockData.get(BlockDoubleStepAbstract.VARIANT));
                                block = blockData.getBlock();
                            }

                            Material type = CraftMagicNumbers.getMaterial(block);
                            byte data = (byte) block.toLegacyData(blockData);
                            Key blockKey = KeyImpl.of(type, data, location);
                            blockCounts.put(blockKey, blockCounts.getOrDefault(blockKey, 0) + blockAmount);
                            if (type == Material.MOB_SPAWNER) {
                                spawnersLocations.add(location);
                            }
                        }
                    }
                }
            }

            allCalculatedChunks.add(new CalculatedChunk(chunkPosition, blockCounts, spawnersLocations));
        }, null);

        return completableFuture;
    }

    @Override
    public void injectChunkSections(org.bukkit.Chunk chunk) {
        // No implementation
    }

    @Override
    public boolean isChunkEmpty(org.bukkit.Chunk bukkitChunk) {
        Chunk chunk = ((CraftChunk) bukkitChunk).getHandle();
        return Arrays.stream(chunk.getSections()).allMatch(chunkSection -> chunkSection == null || chunkSection.a());
    }

    @Override
    public void refreshChunk(org.bukkit.Chunk bukkitChunk) {
        Chunk chunk = ((CraftChunk) bukkitChunk).getHandle();
        NMSUtils.sendPacketToRelevantPlayers((WorldServer) chunk.world, chunk.locX, chunk.locZ,
                new PacketPlayOutMapChunk(chunk, false, 65535));
    }

    @Override
    public void refreshLights(org.bukkit.Chunk chunk, List<BlockData> blockDataList) {
        World world = ((CraftChunk) chunk).getHandle().getWorld();

        // Update lights for the blocks.
        for (BlockData blockData : blockDataList) {
            BlockPosition blockPosition = new BlockPosition(blockData.getX(), blockData.getY(), blockData.getZ());
            if (plugin.getSettings().isLightsUpdate() && blockData.getBlockLightLevel() > 0)
                world.a(EnumSkyBlock.BLOCK, blockPosition, blockData.getBlockLightLevel());

            byte skyLight = plugin.getSettings().isLightsUpdate() ? blockData.getSkyLightLevel() : 15;

            if (skyLight > 0 && blockData.getWorld().getEnvironment() == org.bukkit.World.Environment.NORMAL)
                world.a(EnumSkyBlock.SKY, blockPosition, skyLight);
        }
    }

    @Override
    public org.bukkit.Chunk getChunkIfLoaded(ChunkPosition chunkPosition) {
        Chunk chunk = ((CraftWorld) chunkPosition.getWorld()).getHandle().chunkProviderServer
                .getChunkIfLoaded(chunkPosition.getX(), chunkPosition.getZ());
        return chunk == null ? null : chunk.bukkitChunk;
    }

    @Override
    public void startTickingChunk(Island island, org.bukkit.Chunk chunk, boolean stop) {
        if (plugin.getSettings().getCropsInterval() <= 0)
            return;

        if (stop) {
            ChunkCoordIntPair chunkCoords = new ChunkCoordIntPair(chunk.getX(), chunk.getZ());
            CropsTickingTileEntity cropsTickingTileEntity = CropsTickingTileEntity.remove(chunkCoords);
            if (cropsTickingTileEntity != null)
                cropsTickingTileEntity.getWorld().tileEntityList.remove(cropsTickingTileEntity);
        } else {
            CropsTickingTileEntity.create(island, ((CraftChunk) chunk).getHandle());
        }
    }

}
