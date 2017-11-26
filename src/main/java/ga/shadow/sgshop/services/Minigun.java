package ga.shadow.sgshop.services;

import ga.shadow.sgshop.Main;
import ga.shadow.sgshop.ShopData;
import me.totalfreedom.totalfreedommod.util.FUtil;
import net.pravian.aero.component.service.AbstractService;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Minigun extends AbstractService<Main> {
    public List<Integer> bullets = new ArrayList<>();

    public Minigun(Main plugin) {
        super(plugin);
    }

    @Override
    protected void onStart() {
    }

    @Override
    protected void onStop() {
    }


    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerRightClick(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Player p = event.getPlayer();
            ShopData sd = plugin.sh.getData(p);
            if (p.getInventory().getItemInMainHand().equals(getMinigun()) && sd.isMinigun()) {
                Arrow bullet = p.launchProjectile(Arrow.class, p.getLocation().getDirection());
                bullets.add(bullet.getEntityId());
                bullet.setVelocity(bullet.getVelocity().normalize().multiply(25));
                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 30, 2.0f);
            }
        }
    }

    public ItemStack getMinigun() {
        ItemStack minigun = new ItemStack(Material.IRON_BARDING);
        ItemMeta datMeta = minigun.getItemMeta();
        datMeta.setDisplayName(ChatColor.DARK_RED + "Minigun");
        List<String> lore = new ArrayList();
        lore.add(FUtil.colorize("&6&oEvery game needs a minigun"));
        datMeta.setLore(lore);
        datMeta.addEnchant(Enchantment.ARROW_DAMAGE, 420, true);
        minigun.setItemMeta(datMeta);
        return minigun;
    }
}