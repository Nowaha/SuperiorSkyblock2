package com.bgsoftware.superiorskyblock.serialization.impl;

import com.bgsoftware.superiorskyblock.SuperiorSkyblockPlugin;
import com.bgsoftware.superiorskyblock.serialization.ISerializer;
import com.bgsoftware.superiorskyblock.utils.StringUtils;
import com.bgsoftware.superiorskyblock.utils.debug.PluginDebugger;
import com.bgsoftware.superiorskyblock.utils.locations.SmartLocation;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public final class LocationSerializer implements ISerializer<Location, String> {

    private final String separator;

    public LocationSerializer(String separator) {
        this.separator = separator;
    }

    @NotNull
    @Override
    public String serialize(@Nullable Location serializable) {
        return serializable == null ? "" : serializable.getWorld().getName() + separator +
                serializable.getX() + separator +
                serializable.getY() + separator +
                serializable.getZ() + separator +
                serializable.getYaw() + separator +
                serializable.getPitch();
    }

    @Nullable
    @Override
    public Location deserialize(@Nullable String element) {
        if (StringUtils.isBlank(element))
            return null;

        try {
            String[] sections = element.split(separator);

            double x = Double.parseDouble(sections[1]);
            double y = Double.parseDouble(sections[2]);
            double z = Double.parseDouble(sections[3]);
            float yaw = sections.length > 5 ? Float.parseFloat(sections[4]) : 0;
            float pitch = sections.length > 4 ? Float.parseFloat(sections[5]) : 0;

            return new SmartLocation(sections[0], x, y, z, yaw, pitch);
        } catch (Exception ex) {
            SuperiorSkyblockPlugin.log("Error while parsing location: `" + element + "`");
            PluginDebugger.debug(ex);
            return null;
        }
    }

}
