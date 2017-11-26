package ga.shadow.sgshop;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import net.pravian.aero.base.ConfigLoadable;
import net.pravian.aero.base.ConfigSavable;
import net.pravian.aero.base.Validatable;
import org.apache.commons.lang3.Validate;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class ShopData implements ConfigLoadable, ConfigSavable, Validatable {

    private final List<String> ips = Lists.newArrayList();
    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    private int coins;
    @Getter
    @Setter
    private boolean customLoginMessage = false;
    @Getter
    @Setter
    private String loginMessage = "none";
    @Getter
    @Setter
    private boolean thorHammer = false;
    @Getter
    @Setter
    private boolean scythe = false;
    @Getter
    @Setter
    private boolean minigun = false;
    private String level = "placeholder";

    public ShopData(Player player) {
        this(player.getName());
    }

    public ShopData(String username) {
        this.username = username;
    }

    @Override
    public void loadFrom(ConfigurationSection cs) {
        this.username = cs.getString("username", username);
        this.ips.clear();
        this.ips.addAll(cs.getStringList("ips"));
        this.coins = cs.getInt("coins", coins);
        this.customLoginMessage = cs.getBoolean("customLoginMessage", customLoginMessage);
        this.loginMessage = cs.getString("loginMessage", loginMessage);
        this.thorHammer = cs.getBoolean("thorHammer", thorHammer);
        this.scythe = cs.getBoolean("scythe", scythe);
        this.minigun = cs.getBoolean("minigun", minigun);
        this.level = cs.getString("level", level);
    }

    @Override
    public void saveTo(ConfigurationSection cs) {
        Validate.isTrue(isValid(), "Could not save shop entry: " + username + ". Entry not valid!");
        cs.set("username", username);
        cs.set("ips", ips);
        cs.set("coins", coins);
        cs.set("customLoginMessage", customLoginMessage);
        cs.set("loginMessage", loginMessage);
        cs.set("thorHammer", thorHammer);
        cs.set("scythe", scythe);
        cs.set("minigun", minigun);
        cs.set("level", level);
    }

    public List<String> getIps() {
        return Collections.unmodifiableList(ips);
    }

    // IP utils
    public boolean addIp(String ip) {
        return ips.contains(ip) ? false : ips.add(ip);
    }

    public boolean removeIp(String ip) {
        return ips.remove(ip);
    }

    @Override
    public boolean isValid() {
        return username != null
                && !ips.isEmpty()
                && !loginMessage.isEmpty()
                && !level.isEmpty();
    }


}