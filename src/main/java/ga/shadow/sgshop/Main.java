package ga.shadow.sgshop;

import ga.shadow.sgshop.commands.CommandLoader;
import ga.shadow.sgshop.services.Lightning;
import ga.shadow.sgshop.services.Minigun;
import ga.shadow.sgshop.services.Scythe;
import net.pravian.aero.component.service.ServiceManager;
import net.pravian.aero.plugin.AeroPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class Main extends AeroPlugin<Main> {
    public static final String CONFIG_FILENAME = "config.yml";
    public Config config;
    public Shop sh;
    public ShopGUIListener sg;
    public ServiceManager<Main> services;
    public CommandLoader loader;
    public Minigun mg;
    public Scythe sc;
    public Lightning th;


    public static Main plugin() {
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            if (plugin.getName().equalsIgnoreCase("sg-shop")) {
                return (Main) plugin;
            }
        }
        return null;
    }

    public static void registerVotifierEvents() {
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            if (plugin.getName().equalsIgnoreCase("Votifier")) {
                Bukkit.getPluginManager().registerEvents(new VotifierListener(), plugin());
            }
        }
    }

    @Override
    public void enable() {

        config = new Config(this);
        config.load();
        services = new ServiceManager<>(plugin);
        sh = services.registerService(Shop.class);
        sg = services.registerService(ShopGUIListener.class);
        loader = services.registerService(CommandLoader.class);
        th = services.registerService(Lightning.class);
        sc = services.registerService(Scythe.class);
        mg = services.registerService(Minigun.class);
        services.start();
        registerVotifierEvents();
    }

    @Override
    public void disable() {
        services.stop();
    }

}
