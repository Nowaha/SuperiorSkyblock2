package com.bgsoftware.superiorskyblock.player;

import com.bgsoftware.superiorskyblock.SuperiorSkyblockPlugin;
import com.bgsoftware.superiorskyblock.api.handlers.PlayersManager;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.island.PlayerRole;
import com.bgsoftware.superiorskyblock.api.player.container.PlayersContainer;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.bgsoftware.superiorskyblock.database.DatabaseResult;
import com.bgsoftware.superiorskyblock.database.bridge.PlayersDatabaseBridge;
import com.bgsoftware.superiorskyblock.database.cache.CachedPlayerInfo;
import com.bgsoftware.superiorskyblock.database.cache.DatabaseCache;
import com.bgsoftware.superiorskyblock.handler.AbstractHandler;
import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@SuppressWarnings("WeakerAccess")
public final class PlayersHandler extends AbstractHandler implements PlayersManager {

    private PlayersContainer playersContainer;

    public PlayersHandler(SuperiorSkyblockPlugin plugin) {
        super(plugin);
    }

    public void setPlayersContainer(@NotNull PlayersContainer playersContainer) {
        this.playersContainer = playersContainer;
    }

    @Override
    public void loadData() {
        // Data is loaded by the database bridge.
        if (this.playersContainer == null)
            throw new RuntimeException("PlayersManager was not initialized correctly. Contact Ome_R regarding this!");
    }

    @Override
    public SuperiorPlayer getSuperiorPlayer(String name) {
        Preconditions.checkNotNull(name, "name parameter cannot be null.");
        return this.playersContainer.getSuperiorPlayer(name);
    }

    @Override
    public SuperiorPlayer getSuperiorPlayer(Player player) {
        Preconditions.checkNotNull(player, "player parameter cannot be null.");
        return player.hasMetadata("NPC") ? new SuperiorNPCPlayer(player) : getSuperiorPlayer(player.getUniqueId());
    }

    @Override
    public SuperiorPlayer getSuperiorPlayer(UUID uuid) {
        Preconditions.checkNotNull(uuid, "uuid parameter cannot be null.");
        SuperiorPlayer superiorPlayer = this.playersContainer.getSuperiorPlayer(uuid);

        if (superiorPlayer == null) {
            superiorPlayer = plugin.getFactory().createPlayer(uuid);
            this.playersContainer.addPlayer(superiorPlayer);
            PlayersDatabaseBridge.insertPlayer(superiorPlayer);
        }

        return superiorPlayer;
    }

    @Override
    public List<SuperiorPlayer> getAllPlayers() {
        return this.playersContainer.getAllPlayers();
    }

    @Override
    @Deprecated
    public PlayerRole getPlayerRole(int index) {
        return plugin.getRoles().getPlayerRole(index);
    }

    @Override
    @Deprecated
    public PlayerRole getPlayerRoleFromId(int id) {
        return plugin.getRoles().getPlayerRoleFromId(id);
    }

    @Override
    @Deprecated
    public PlayerRole getPlayerRole(String name) {
        return plugin.getRoles().getPlayerRole(name);
    }

    @Override
    @Deprecated
    public PlayerRole getDefaultRole() {
        return plugin.getRoles().getDefaultRole();
    }

    @Override
    @Deprecated
    public PlayerRole getLastRole() {
        return plugin.getRoles().getLastRole();
    }

    @Override
    @Deprecated
    public PlayerRole getGuestRole() {
        return plugin.getRoles().getGuestRole();
    }

    @Override
    @Deprecated
    public PlayerRole getCoopRole() {
        return plugin.getRoles().getCoopRole();
    }

    @Override
    @Deprecated
    public List<PlayerRole> getRoles() {
        return plugin.getRoles().getRoles();
    }

    @Override
    public PlayersContainer getPlayersContainer() {
        return this.playersContainer;
    }

    public SuperiorPlayer getSuperiorPlayer(CommandSender commandSender) {
        return getSuperiorPlayer((Player) commandSender);
    }

    public List<SuperiorPlayer> matchAllPlayers(Predicate<? super SuperiorPlayer> predicate) {
        return Collections.unmodifiableList(this.playersContainer.getAllPlayers().stream()
                .filter(predicate)
                .collect(Collectors.toList())
        );
    }

    public void loadPlayer(DatabaseCache<CachedPlayerInfo> databaseCache, DatabaseResult resultSet) {
        Optional<SuperiorPlayer> superiorPlayer = plugin.getFactory().createPlayer(databaseCache, resultSet);
        superiorPlayer.ifPresent(this.playersContainer::addPlayer);
    }

    public void replacePlayers(SuperiorPlayer originPlayer, SuperiorPlayer newPlayer) {
        this.playersContainer.removePlayer(originPlayer);

        newPlayer.merge(originPlayer);

        for (Island island : plugin.getGrid().getIslands())
            island.replacePlayers(originPlayer, newPlayer);

        plugin.getEventsBus().callPlayerReplaceEvent(originPlayer, newPlayer);
    }

    // Updating last time status
    public void savePlayers() {
        Bukkit.getOnlinePlayers().stream().map(this::getSuperiorPlayer)
                .forEach(PlayersDatabaseBridge::saveLastTimeStatus);

        List<SuperiorPlayer> modifiedPlayers = getAllPlayers().stream()
                .filter(PlayersDatabaseBridge::isModified)
                .collect(Collectors.toList());

        if (!modifiedPlayers.isEmpty())
            modifiedPlayers.forEach(PlayersDatabaseBridge::executeFutureSaves);

    }

}
