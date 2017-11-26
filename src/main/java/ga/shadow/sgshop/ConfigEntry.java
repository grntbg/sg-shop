package ga.shadow.sgshop;

import java.util.List;

//Credits to TotalFreedomMod developers, Prozza & Madgeek
public enum ConfigEntry {
    SHOP_PREFIX(String.class, "shop.prefix"),
    SHOP_COLORED_CHAT_PRICE(Integer.class, "shop.colored_chat_price"),
    SHOP_LOGIN_MESSAGE_PRICE(Integer.class, "shop.login_message_price"),
    SHOP_THOR_HAMMER_PRICE(Integer.class, "shop.thor_hammer_price"),
    SHOP_CRESCENT_ROSE_PRICE(Integer.class, "shop.scythe_price"),
    SHOP_CRESCENT_ROSE_USE_PRICE(Integer.class, "shop.scythe_use_price"),
    SHOP_MINIGUN_PRICE(Integer.class, "shop.minigun_price"),
    VOTE_MESSAGE(String.class, "shop.vote_message"),
    COINS_PER_VOTE(Integer.class, "shop.coins_per_vote");

    //

    private final Class<?> type;
    private final String configName;

    ConfigEntry(Class<?> type, String configName) {
        this.type = type;
        this.configName = configName;
    }

    public static ConfigEntry findConfigEntry(String name) {
        name = name.toLowerCase().replace("_", "");
        for (ConfigEntry entry : values()) {
            if (entry.toString().toLowerCase().replace("_", "").equals(name)) {
                return entry;
            }
        }
        return null;
    }

    public Class<?> getType() {
        return type;
    }

    public String getConfigName() {
        return configName;
    }

    public String getString() {
        return getConfig().getString(this);
    }

    public String setString(String value) {
        getConfig().setString(this, value);
        return value;
    }

    public Double getDouble() {
        return getConfig().getDouble(this);
    }

    public Double setDouble(Double value) {
        getConfig().setDouble(this, value);
        return value;
    }

    public Boolean getBoolean() {
        return getConfig().getBoolean(this);
    }

    public Boolean setBoolean(Boolean value) {
        getConfig().setBoolean(this, value);
        return value;
    }

    public Integer getInteger() {
        return getConfig().getInteger(this);
    }

    public Integer setInteger(Integer value) {
        getConfig().setInteger(this, value);
        return value;
    }

    public List<?> getList() {
        return getConfig().getList(this);
    }

    @SuppressWarnings("unchecked")
    public List<String> getStringList() {
        return (List<String>) getList();
    }

    private Config getConfig() {
        return Main.plugin().config;
    }
}
