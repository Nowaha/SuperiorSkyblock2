package com.bgsoftware.superiorskyblock.values.container;

import com.bgsoftware.superiorskyblock.SuperiorSkyblockPlugin;
import com.bgsoftware.superiorskyblock.key.KeyImpl;
import com.bgsoftware.superiorskyblock.utils.debug.PluginDebugger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.math.BigDecimal;

public final class BlockWorthValuesContainer extends BlockValuesContainer {

    @Override
    public void loadDefaultValues(SuperiorSkyblockPlugin plugin) {
        File worthFile = new File(plugin.getDataFolder(), "block-values/worth.yml");

        File blockValuesFile = new File(plugin.getDataFolder(), "blockvalues.yml");
        if (blockValuesFile.exists()) {
            if (!worthFile.getParentFile().mkdirs() || !blockValuesFile.renameTo(worthFile))
                SuperiorSkyblockPlugin.log("&cFailed to convert old block values to the new format.");
        }

        if (!worthFile.exists())
            plugin.saveResource("block-values/worth.yml", true);

        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(worthFile);
        ConfigurationSection valuesSection = cfg.contains("block-values") ? cfg.getConfigurationSection("block-values") : cfg.getConfigurationSection("");

        for (String key : valuesSection.getKeys(false)) {
            String value = valuesSection.getString(key);
            try {
                setBlockValue(KeyImpl.of(key), new BigDecimal(value));
            } catch (Exception ex) {
                SuperiorSkyblockPlugin.log("&cInvalid worth value: " + value);
                PluginDebugger.debug(ex);
            }
        }
    }

}
