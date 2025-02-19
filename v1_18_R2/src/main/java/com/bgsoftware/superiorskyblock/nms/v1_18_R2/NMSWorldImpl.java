package com.bgsoftware.superiorskyblock.nms.v1_18_R2;

import com.bgsoftware.common.reflection.ReflectField;
import com.bgsoftware.common.reflection.ReflectMethod;
import com.bgsoftware.superiorskyblock.SuperiorSkyblockPlugin;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.key.Key;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.bgsoftware.superiorskyblock.formatting.Formatters;
import com.bgsoftware.superiorskyblock.key.KeyImpl;
import com.bgsoftware.superiorskyblock.nms.NMSWorld;
import com.bgsoftware.superiorskyblock.nms.v1_18_R2.generator.IslandsGeneratorImpl;
import com.bgsoftware.superiorskyblock.nms.v1_18_R2.mapping.BlockPosition;
import com.bgsoftware.superiorskyblock.nms.v1_18_R2.mapping.level.WorldServer;
import com.bgsoftware.superiorskyblock.nms.v1_18_R2.mapping.level.block.Block;
import com.bgsoftware.superiorskyblock.nms.v1_18_R2.mapping.level.block.SoundEffectType;
import com.bgsoftware.superiorskyblock.nms.v1_18_R2.mapping.level.block.entity.TileEntity;
import com.bgsoftware.superiorskyblock.nms.v1_18_R2.mapping.level.block.state.BlockData;
import com.bgsoftware.superiorskyblock.nms.v1_18_R2.mapping.level.block.state.properties.BlockState;
import com.bgsoftware.superiorskyblock.nms.v1_18_R2.mapping.level.border.WorldBorder;
import com.bgsoftware.superiorskyblock.nms.v1_18_R2.mapping.level.chunk.ChunkAccess;
import com.bgsoftware.superiorskyblock.nms.v1_18_R2.mapping.level.chunk.ChunkSection;
import com.bgsoftware.superiorskyblock.nms.v1_18_R2.mapping.level.lighting.LightEngine;
import com.bgsoftware.superiorskyblock.nms.v1_18_R2.mapping.nbt.NBTTagCompound;
import com.bgsoftware.superiorskyblock.nms.v1_18_R2.mapping.tags.TagsBlock;
import com.bgsoftware.superiorskyblock.nms.v1_18_R2.mapping.world.entity.Entity;
import com.bgsoftware.superiorskyblock.nms.v1_18_R2.world.BlockStatesMapper;
import com.bgsoftware.superiorskyblock.tag.ByteTag;
import com.bgsoftware.superiorskyblock.tag.CompoundTag;
import com.bgsoftware.superiorskyblock.tag.IntArrayTag;
import com.bgsoftware.superiorskyblock.tag.StringTag;
import com.bgsoftware.superiorskyblock.tag.Tag;
import com.bgsoftware.superiorskyblock.utils.StringUtils;
import com.bgsoftware.superiorskyblock.utils.legacy.Materials;
import com.bgsoftware.superiorskyblock.utils.logic.BlocksLogic;
import com.bgsoftware.superiorskyblock.world.blocks.ICachedBlock;
import com.destroystokyo.paper.antixray.ChunkPacketBlockController;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.ClientboundInitializeBorderPacket;
import net.minecraft.network.protocol.game.PacketPlayOutBlockChange;
import net.minecraft.sounds.SoundCategory;
import net.minecraft.world.level.EnumSkyBlock;
import net.minecraft.world.level.World;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.block.BlockStepAbstract;
import net.minecraft.world.level.block.entity.TileEntitySign;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.block.state.properties.BlockPropertySlabType;
import net.minecraft.world.level.block.state.properties.BlockStateBoolean;
import net.minecraft.world.level.block.state.properties.BlockStateEnum;
import net.minecraft.world.level.block.state.properties.BlockStateInteger;
import net.minecraft.world.level.block.state.properties.IBlockState;
import net.minecraft.world.level.chunk.DataPaletteBlock;
import net.minecraft.world.level.chunk.IChunkAccess;
import org.bukkit.Bukkit;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.craftbukkit.v1_18_R2.CraftChunk;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.block.CraftBlock;
import org.bukkit.craftbukkit.v1_18_R2.block.CraftSign;
import org.bukkit.craftbukkit.v1_18_R2.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.generator.ChunkGenerator;

import java.util.List;
import java.util.Map;

public final class NMSWorldImpl implements NMSWorld {

    private static final SuperiorSkyblockPlugin plugin = SuperiorSkyblockPlugin.getPlugin();

    private static final ReflectField<IChunkAccess> CHUNK_ACCESS = new ReflectField<>(
            "org.bukkit.craftbukkit.VERSION.generator.CustomChunkGenerator$CustomBiomeGrid", IChunkAccess.class, "biome");
    private static final ReflectMethod<Object> LINES_SIGN_CHANGE_EVENT = new ReflectMethod<>(SignChangeEvent.class, "lines");
    private static final ReflectField<Object> CHUNK_PACKET_BLOCK_CONTROLLER = new ReflectField<>(World.class,
            Object.class, "chunkPacketBlockController").removeFinal();

    @Override
    public Key getBlockKey(ChunkSnapshot chunkSnapshot, int x, int y, int z) {
        BlockData blockData = new BlockData(((CraftBlockData) chunkSnapshot.getBlockData(x, y, z)).getState());
        Material type = chunkSnapshot.getBlockType(x, y, z);
        short data = (short) (Block.getCombinedId(blockData) >> 12 & 15);

        Location location = new Location(
                Bukkit.getWorld(chunkSnapshot.getWorldName()),
                (chunkSnapshot.getX() << 4) + x,
                y,
                (chunkSnapshot.getZ() << 4) + z
        );

        return KeyImpl.of(KeyImpl.of(type, data), location);
    }

    @Override
    public int getSpawnerDelay(CreatureSpawner creatureSpawner) {
        Location location = creatureSpawner.getLocation();
        org.bukkit.World world = location.getWorld();

        if (world == null)
            return 0;

        WorldServer worldServer = new WorldServer(((CraftWorld) world).getHandle());
        BlockPosition blockPosition = new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        TileEntity mobSpawner = worldServer.getTileEntity(blockPosition);
        return mobSpawner == null ? 0 : mobSpawner.getSpawner().c;
    }

    @Override
    public void setSpawnerDelay(CreatureSpawner creatureSpawner, int spawnDelay) {
        Location location = creatureSpawner.getLocation();
        org.bukkit.World world = location.getWorld();

        if (world == null)
            return;

        WorldServer worldServer = new WorldServer(((CraftWorld) world).getHandle());
        BlockPosition blockPosition = new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        TileEntity mobSpawner = worldServer.getTileEntity(blockPosition);
        if (mobSpawner != null)
            mobSpawner.getSpawner().c = spawnDelay;
    }

    @Override
    public void setWorldBorder(SuperiorPlayer superiorPlayer, Island island) {
        try {
            if (!plugin.getSettings().isWorldBorders())
                return;

            boolean disabled = !superiorPlayer.hasWorldBorderEnabled();

            Player player = superiorPlayer.asPlayer();
            org.bukkit.World world = superiorPlayer.getWorld();

            if (world == null || player == null)
                return;

            WorldServer worldServer = new WorldServer(((CraftWorld) superiorPlayer.getWorld()).getHandle());

            WorldBorder worldBorder;

            if (disabled || island == null || (!plugin.getSettings().getSpawn().isWorldBorder() && island.isSpawn())) {
                worldBorder = worldServer.getWorldBorder();
            } else {
                worldBorder = new WorldBorder();

                worldBorder.setWarningDistance(0);

                worldBorder.setWorld(worldServer);
                worldBorder.setSize((island.getIslandSize() * 2) + 1);

                org.bukkit.World.Environment environment = world.getEnvironment();

                Location center = island.getCenter(environment);
                worldBorder.setCenter(center.getX(), center.getZ());
                double worldBorderSize = worldBorder.getSize();

                switch (superiorPlayer.getBorderColor()) {
                    case GREEN -> worldBorder.transitionSizeBetween(worldBorderSize - 0.1D, worldBorderSize, Long.MAX_VALUE);
                    case RED -> worldBorder.transitionSizeBetween(worldBorderSize, worldBorderSize - 1.0D, Long.MAX_VALUE);
                }
            }

            ClientboundInitializeBorderPacket packetPlayOutWorldBorder = new ClientboundInitializeBorderPacket(worldBorder.getHandle());
            Entity entity = new Entity(((CraftPlayer) player).getHandle());
            entity.getPlayerConnection().sendPacket(packetPlayOutWorldBorder);
        } catch (NullPointerException ignored) {
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void setBiome(ChunkGenerator.BiomeGrid biomeGrid, Biome biome) {
        ChunkAccess chunk = ChunkAccess.ofNullable(CHUNK_ACCESS.get(biomeGrid));

        if (chunk == null)
            return;

        setBiome(chunk, biome);
    }

    private void setBiome(ChunkAccess chunk, Biome biome) {
        Holder<BiomeBase> biomeBase = CraftBlock.biomeToBiomeBase(chunk.getBiomeRegistry(), biome);
        Registry<Holder<BiomeBase>> biomesRegistryHolder = chunk.getBiomeRegistryHolder();

        net.minecraft.world.level.chunk.ChunkSection[] chunkSections = chunk.getSections();
        for (int i = 0; i < chunkSections.length; ++i) {
            ChunkSection currentSection = ChunkSection.ofNullable(chunkSections[i]);
            if (currentSection != null) {
                DataPaletteBlock<IBlockData> dataPaletteBlock = currentSection.getBlocks();
                DataPaletteBlock<Holder<BiomeBase>> biomesDataPalette = new DataPaletteBlock<>(biomesRegistryHolder,
                        biomeBase, DataPaletteBlock.e.e);
                chunkSections[i] = new net.minecraft.world.level.chunk.ChunkSection(
                        currentSection.getYPosition() >> 4, dataPaletteBlock, biomesDataPalette);
            }
        }
    }

    @Override
    public Object getBlockData(org.bukkit.block.Block block) {
        return block.getBlockData();
    }

    @Override
    public void setBlocks(org.bukkit.Chunk bukkitChunk,
                          List<com.bgsoftware.superiorskyblock.world.blocks.BlockData> blockDataList) {
        ChunkAccess chunk = new ChunkAccess(((CraftChunk) bukkitChunk).getHandle());
        blockDataList.forEach(blockData -> {
            NMSUtils.setBlock(chunk, new BlockPosition(blockData.getX(), blockData.getY(), blockData.getZ()),
                    blockData.getCombinedId(), blockData.getStatesTag(), blockData.getClonedTileEntity());
        });
        setBiome(chunk, NMSUtils.getWorldBiome(bukkitChunk.getWorld().getEnvironment()));
    }

    @Override
    public void setBlock(Location location, int combinedId) {
        org.bukkit.World bukkitWorld = location.getWorld();

        if (bukkitWorld == null)
            return;

        WorldServer world = new WorldServer(((CraftWorld) bukkitWorld).getHandle());
        BlockPosition blockPosition = new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        NMSUtils.setBlock(world.getChunkAtWorldCoords(blockPosition), blockPosition, combinedId, null, null);
        NMSUtils.sendPacketToRelevantPlayers(world, blockPosition.getX() >> 4, blockPosition.getZ() >> 4,
                new PacketPlayOutBlockChange(world.getHandle(), blockPosition.getHandle()));
    }

    @Override
    public ICachedBlock cacheBlock(org.bukkit.block.Block block) {
        return new NMSCachedBlock(block);
    }

    @Override
    public CompoundTag readBlockStates(Location location) {
        org.bukkit.World bukkitWorld = location.getWorld();

        if (bukkitWorld == null)
            return null;

        WorldServer world = new WorldServer(((CraftWorld) bukkitWorld).getHandle());
        BlockPosition blockPosition = new BlockPosition(location.getX(), location.getY(), location.getZ());
        BlockData blockData = world.getType(blockPosition);
        CompoundTag compoundTag = null;

        for (Map.Entry<IBlockState<?>, Comparable<?>> entry : blockData.getStateMap().entrySet()) {
            if (compoundTag == null)
                compoundTag = new CompoundTag();

            Tag<?> value;
            Class<?> keyClass = entry.getKey().getClass();
            String name = new BlockState<>(entry.getKey()).getName();

            if (keyClass.equals(BlockStateBoolean.class)) {
                value = new ByteTag((Boolean) entry.getValue() ? (byte) 1 : 0);
            } else if (keyClass.equals(BlockStateInteger.class)) {
                BlockStateInteger key = (BlockStateInteger) entry.getKey();
                value = new IntArrayTag(new int[]{(Integer) entry.getValue(), key.min, key.max});
            } else {
                BlockStateEnum<?> key = (BlockStateEnum<?>) entry.getKey();
                name = BlockStatesMapper.getBlockStateName(key);
                value = new StringTag(((Enum<?>) entry.getValue()).name());
            }

            compoundTag.setTag(name, value);
        }

        return compoundTag;
    }

    @Override
    public byte[] getLightLevels(Location location) {
        org.bukkit.World bukkitWorld = location.getWorld();

        if (bukkitWorld == null)
            return new byte[0];

        WorldServer worldServer = new WorldServer(((CraftWorld) bukkitWorld).getHandle());
        BlockPosition blockPosition = new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());

        LightEngine lightEngine = worldServer.getLightEngine();
        return new byte[]{
                location.getWorld().getEnvironment() != org.bukkit.World.Environment.NORMAL ? 0 :
                        (byte) lightEngine.getLayer(EnumSkyBlock.a).getLightLevel(blockPosition),
                (byte) lightEngine.getLayer(EnumSkyBlock.b).getLightLevel(blockPosition)
        };
    }

    @Override
    public CompoundTag readTileEntity(Location location) {
        org.bukkit.World bukkitWorld = location.getWorld();

        if (bukkitWorld == null)
            return null;

        WorldServer world = new WorldServer(((CraftWorld) bukkitWorld).getHandle());
        BlockPosition blockPosition = new BlockPosition(location.getX(), location.getY(), location.getZ());
        TileEntity tileEntity = world.getTileEntity(blockPosition);

        if (tileEntity == null)
            return null;

        NBTTagCompound tileEntityCompound = tileEntity.save();

        tileEntityCompound.remove("x");
        tileEntityCompound.remove("y");
        tileEntityCompound.remove("z");

        return CompoundTag.fromNBT(tileEntityCompound.getHandle());
    }

    @Override
    public boolean isWaterLogged(org.bukkit.block.Block block) {
        if (Materials.isWater(block.getType()))
            return true;

        org.bukkit.block.data.BlockData blockData = block.getBlockData();

        return blockData instanceof Waterlogged && ((Waterlogged) blockData).isWaterlogged();
    }

    @Override
    public int getDefaultAmount(org.bukkit.block.Block block) {
        BlockData blockData = new BlockData(((CraftBlock) block).getNMS());
        Block nmsBlock = blockData.getBlock();

        // Checks for double slabs
        if ((TagsBlock.isTagged(TagsBlock.SLABS, nmsBlock) || TagsBlock.isTagged(TagsBlock.WOODEN_SLABS, nmsBlock)) &&
                blockData.get(BlockStepAbstract.a) == BlockPropertySlabType.c) {
            return 2;
        }

        return 1;
    }

    @Override
    public void placeSign(Island island, Location location) {
        org.bukkit.World bukkitWorld = location.getWorld();

        if (bukkitWorld == null)
            return;

        BlockPosition blockPosition = new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        WorldServer worldServer = new WorldServer(((CraftWorld) bukkitWorld).getHandle());
        TileEntity tileEntity = worldServer.getTileEntity(blockPosition);
        if (tileEntity.getHandle() instanceof TileEntitySign) {
            String[] lines = new String[4];
            System.arraycopy(CraftSign.revertComponents(tileEntity.getSignLines()), 0, lines, 0, lines.length);
            String[] strippedLines = new String[4];
            for (int i = 0; i < 4; i++)
                strippedLines[i] = Formatters.STRIP_COLOR_FORMATTER.format(lines[i]);

            IChatBaseComponent[] newLines;

            if (BlocksLogic.handleSignPlace(island.getOwner(), island, location, strippedLines, false))
                newLines = CraftSign.sanitizeLines(strippedLines);
            else
                newLines = CraftSign.sanitizeLines(lines);

            System.arraycopy(newLines, 0, tileEntity.getSignLines(), 0, 4);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setSignLines(SignChangeEvent signChangeEvent, String[] lines) {
        if (LINES_SIGN_CHANGE_EVENT.isValid()) {
            for (int i = 0; i < lines.length; i++)
                signChangeEvent.setLine(i, lines[i]);
        }
    }

    @Override
    public void playGeneratorSound(Location location) {
        org.bukkit.World bukkitWorld = location.getWorld();

        if (bukkitWorld == null)
            return;

        WorldServer world = new WorldServer(((CraftWorld) bukkitWorld).getHandle());
        BlockPosition blockPosition = new BlockPosition(location.getX(), location.getY(), location.getZ());
        world.triggerEffect(1501, blockPosition, 0);
    }

    @Override
    public void playBreakAnimation(org.bukkit.block.Block block) {
        WorldServer world = new WorldServer(((CraftWorld) block.getWorld()).getHandle());
        BlockPosition blockPosition = new BlockPosition(block.getX(), block.getY(), block.getZ());
        world.gameEvent(null, 2001, blockPosition, Block.getCombinedId(world.getType(blockPosition)));
    }

    @Override
    public void playPlaceSound(Location location) {
        org.bukkit.World bukkitWorld = location.getWorld();

        if (bukkitWorld == null)
            return;

        BlockPosition blockPosition = new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        WorldServer worldServer = new WorldServer(((CraftWorld) bukkitWorld).getHandle());
        SoundEffectType soundEffectType = worldServer.getType(blockPosition).getStepSound();

        worldServer.playSound(null, blockPosition, soundEffectType.getPlaceSound(), SoundCategory.e,
                (soundEffectType.getVolume() + 1.0F) / 2.0F, soundEffectType.getPitch() * 0.8F);
    }

    @Override
    public int getMinHeight(org.bukkit.World world) {
        return world.getMinHeight();
    }

    @Override
    public void removeAntiXray(org.bukkit.World bukkitWorld) {
        World world = ((CraftWorld) bukkitWorld).getHandle();
        if (CHUNK_PACKET_BLOCK_CONTROLLER.isValid())
            CHUNK_PACKET_BLOCK_CONTROLLER.set(world, ChunkPacketBlockController.NO_OPERATION_INSTANCE);
    }

    @Override
    public ChunkGenerator createGenerator(SuperiorSkyblockPlugin plugin) {
        return new IslandsGeneratorImpl(plugin);
    }

}
