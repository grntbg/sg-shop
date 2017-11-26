package ga.shadow.sgshop;
//Credits to TotalFreedomMod developers, Prozza & Madgeek

import net.pravian.aero.component.PluginComponent;
import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.EnumMap;
import java.util.List;

//Credit to TFM developers
public class Config extends PluginComponent<Main> {

    public static final String CONFIG_FILENAME = "config.yml";
    //
    private final EnumMap<ConfigEntry, Object> entries;
    private final ConfigDefaults defaults;

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public Config(Main plugin) {
        super(plugin);

        entries = new EnumMap<>(ConfigEntry.class);

        ConfigDefaults tempDefaults = null;
        try {
            try {
                try (InputStream defaultConfig = getDefaultConfig()) {
                    tempDefaults = new ConfigDefaults(defaultConfig);
                    for (ConfigEntry entry : ConfigEntry.values()) {
                        entries.put(entry, tempDefaults.get(entry.getConfigName()));
                    }
                }
            } catch (IOException ex) {
                getLogger().severe(ex);
            }

            copyDefaultConfig(getConfigFile());

            load();
        } catch (Exception ex) {
            getLogger().severe(ex);
        }

        defaults = tempDefaults;
    }

    public void load() {
        try {
            YamlConfiguration config = new YamlConfiguration();

            config.load(getConfigFile());

            for (ConfigEntry entry : ConfigEntry.values()) {
                String path = entry.getConfigName();
                if (config.contains(path)) {
                    Object value = config.get(path);
                    if (value == null || entry.getType().isAssignableFrom(value.getClass())) {
                        entries.put(entry, value);
                    } else {
                        getLogger().warning("Value for " + entry.getConfigName() + " is of type "
                                + value.getClass().getSimpleName() + ". Needs to be " + entry.getType().getSimpleName() + ". Using default value.");
                    }
                } else {
                    getLogger().warning("Missing configuration entry " + entry.getConfigName() + ". Using default value.");
                }
            }
        } catch (IOException | InvalidConfigurationException ex) {
            getLogger().severe(ex);
        }
    }

    private File getConfigFile() {
        return new File(plugin.getDataFolder(), "config.yml");
    }

    public String getString(ConfigEntry entry) {
        try {
            return get(entry, String.class);
        } catch (IllegalArgumentException ex) {
            getLogger().severe(ex);
        }
        return null;
    }

    public void setString(ConfigEntry entry, String value) {
        try {
            set(entry, value, String.class);
        } catch (IllegalArgumentException ex) {
            getLogger().severe(ex);
        }
    }

    public Double getDouble(ConfigEntry entry) {
        try {
            return get(entry, Double.class);
        } catch (IllegalArgumentException ex) {
            getLogger().severe(ex);
        }
        return null;
    }

    public void setDouble(ConfigEntry entry, Double value) {
        try {
            set(entry, value, Double.class);
        } catch (IllegalArgumentException ex) {
            getLogger().severe(ex);
        }
    }

    public Boolean getBoolean(ConfigEntry entry) {
        try {
            return get(entry, Boolean.class);
        } catch (IllegalArgumentException ex) {
            getLogger().severe(ex);
        }
        return null;
    }

    public void setBoolean(ConfigEntry entry, Boolean value) {
        try {
            set(entry, value, Boolean.class);
        } catch (IllegalArgumentException ex) {
            getLogger().severe(ex);
        }
    }

    public Integer getInteger(ConfigEntry entry) {
        try {
            return get(entry, Integer.class);
        } catch (IllegalArgumentException ex) {
            getLogger().severe(ex);
        }
        return null;
    }

    public void setInteger(ConfigEntry entry, Integer value) {
        try {
            set(entry, value, Integer.class);
        } catch (IllegalArgumentException ex) {
            getLogger().severe(ex);
        }
    }

    public List getList(ConfigEntry entry) {
        try {
            return get(entry, List.class);
        } catch (IllegalArgumentException ex) {
            getLogger().severe(ex);
        }
        return null;
    }

    public <T> T get(ConfigEntry entry, Class<T> type) throws IllegalArgumentException {
        Object value = entries.get(entry);
        try {
            return type.cast(value);
        } catch (ClassCastException ex) {
            throw new IllegalArgumentException(entry.name() + " is not of type " + type.getSimpleName());
        }
    }

    public <T> void set(ConfigEntry entry, T value, Class<T> type) throws IllegalArgumentException {
        if (!type.isAssignableFrom(entry.getType())) {
            throw new IllegalArgumentException(entry.name() + " is not of type " + type.getSimpleName());
        }
        if (value != null && !type.isAssignableFrom(value.getClass())) {
            throw new IllegalArgumentException("Value is not of type " + type.getSimpleName());
        }
        entries.put(entry, value);
    }

    private void copyDefaultConfig(File targetFile) {
        if (targetFile.exists()) {
            return;
        }

        getLogger().info("Installing default configuration file template: " + targetFile.getPath());

        try {
            try (InputStream defaultConfig = getDefaultConfig()) {
                FileUtils.copyInputStreamToFile(defaultConfig, targetFile);
            }
        } catch (IOException ex) {
            getLogger().severe(ex);
        }
    }

    private InputStream getDefaultConfig() {
        return plugin.getResource(Main.CONFIG_FILENAME);
    }

    public ConfigDefaults getDefaults() {
        return defaults;
    }

    public static class ConfigDefaults {

        private YamlConfiguration defaults = null;

        @SuppressWarnings("ConvertToTryWithResources")
        private ConfigDefaults(InputStream defaultConfig) {
            try {
                defaults = new YamlConfiguration();
                final InputStreamReader isr = new InputStreamReader(defaultConfig);
                defaults.load(isr);
                isr.close();
            } catch (IOException | InvalidConfigurationException ex) {
                Main.plugin().getLogger().severe(ex.getMessage());
            }
        }

        public Object get(String path) {
            return defaults.get(path);
        }
    }

}