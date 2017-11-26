package ga.shadow.sgshop.services;

import ga.shadow.sgshop.ConfigEntry;
import ga.shadow.sgshop.Main;
import ga.shadow.sgshop.ShopData;
import me.totalfreedom.totalfreedommod.TotalFreedomMod;
import me.totalfreedom.totalfreedommod.util.FUtil;
import net.pravian.aero.component.service.AbstractService;
import org.bukkit.*;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Scythe extends AbstractService<Main> {
    public final long cooldownTime = 30;
    public final int use_price = ConfigEntry.SHOP_CRESCENT_ROSE_USE_PRICE.getInteger();
    public HashMap<String, Long> cooldowns = new HashMap<>();
    public List<Integer> bullets = new ArrayList<>();

    public Scythe(Main plugin) {
        super(plugin);
    }

    @Override
    protected void onStart() {
    }

    @Override
    protected void onStop() {
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBulletImpact(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow && bullets.contains(event.getDamager().getEntityId())) {
            Arrow bullet = (Arrow) event.getDamager();
            bullets.remove((Integer) bullet.getEntityId());

            if (event.getEntity() != null && event.getEntity() instanceof LivingEntity) {
                if (bullet.getShooter() != null && bullet.getShooter() instanceof Player) {
                    Player shooter = (Player) bullet.getShooter();
                    LivingEntity hitEntity = (LivingEntity) event.getEntity();
                    if (event.getEntity() instanceof Player) {
                        Player target = (Player) event.getEntity();
                        if (TotalFreedomMod.plugin().al.isAdmin(target) && !TotalFreedomMod.plugin().al.isSeniorAdmin(shooter)) {
                            shooter.sendMessage(ChatColor.RED + "Sorry, but you can't attack staff members with the scythe!");
                            return;
                        }
                        if (target.getGameMode().equals(GameMode.CREATIVE) && !TotalFreedomMod.plugin().al.isSeniorAdmin(shooter)) {
                            return;
                        }
                    }

                    hitEntity.setHealth(0);
                    Location l = hitEntity.getLocation();
                    final Firework fw = (Firework) l.getWorld().spawn(l, Firework.class);
                    FireworkMeta fm = fw.getFireworkMeta();
                    fm.addEffect(FireworkEffect.builder().trail(true).with(Type.BALL_LARGE).withColor(Color.RED).build());
                    fm.setPower(0);
                    fw.setFireworkMeta(fm);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            fw.detonate();
                        }
                    }.runTaskLater(plugin, 2L);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerRightClick(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Player p = event.getPlayer();
            if (p.getInventory().getItemInMainHand().equals(getCrescentRose())) {

                ShopData sd = plugin.sh.getData(p);
                if (plugin.sg.canAfford(use_price, sd.getCoins())) {
                    sd.setCoins(sd.getCoins() - use_price);
                    plugin.sh.save(sd);
                } else {
                    int coins_needed = use_price - sd.getCoins();
                    FUtil.playerMsg(p, ChatColor.RED + "You only have " + ChatColor.DARK_RED + sd.getCoins() + ChatColor.RED + " coins. You need " + ChatColor.DARK_RED + coins_needed + ChatColor.RED + " more coins to use Crescent Rose!");
                    return;
                }

                if (cooldowns.containsKey(p.getName())) {
                    long secondsLeft = ((cooldowns.get(p.getName()) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000);
                    if (secondsLeft > 0) {
                        FUtil.playerMsg(p, "You can't use the Scythe for another " + secondsLeft + " seconds!", ChatColor.RED);
                        return;
                    }
                }
                Arrow bullet = p.launchProjectile(Arrow.class, p.getLocation().getDirection());
                bullets.add(bullet.getEntityId());
                bullet.setVelocity(bullet.getVelocity().normalize().multiply(50));
                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 50, 2.5f);

                if (!TotalFreedomMod.plugin().al.isSeniorAdmin(p)) {
                    cooldowns.put(p.getName(), System.currentTimeMillis());
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager();
        Entity target = event.getEntity();
        if (attacker instanceof Player && target instanceof LivingEntity) {
            Player p = (Player) attacker;
            ItemStack i = p.getInventory().getItemInMainHand();
            if (i != null && i.equals(getCrescentRose())) {
                ShopData sd = plugin.sh.getData(p);
                if (sd.isScythe()) {
                    if (plugin.sg.canAfford(use_price, sd.getCoins())) {
                        sd.setCoins(sd.getCoins() - use_price);
                    } else {
                        int coins_needed = use_price - sd.getCoins();
                        FUtil.playerMsg(p, ChatColor.RED + "You only have " + ChatColor.DARK_RED + sd.getCoins() + ChatColor.RED + " coins. You need " + ChatColor.DARK_RED + coins_needed + ChatColor.RED + " more coins to use Crescent Rose!");
                        return;
                    }

                    if (cooldowns.containsKey(p.getName())) {
                        long secondsLeft = ((cooldowns.get(p.getName()) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000);
                        if (secondsLeft > 0) {
                            FUtil.playerMsg(p, "You can't use the Scythe for another " + secondsLeft + " seconds!", ChatColor.RED);
                            return;
                        }
                    }
                    if (target instanceof Player && !TotalFreedomMod.plugin().al.isAdmin(p) && TotalFreedomMod.plugin().al.isAdmin(target)) {
                        FUtil.playerMsg(p, "Sorry, but you can't attack staff members with the Scythe!", ChatColor.RED);
                        return;
                    }
                    if (!TotalFreedomMod.plugin().al.isSeniorAdmin(p)) {
                        cooldowns.put(p.getName(), System.currentTimeMillis());
                    }

                    // Play attack sound
                    target.getWorld().playSound(target.getLocation(), Sound.ENTITY_PLAYER_ATTACK_STRONG, 100F, 0.1F);

                    // Deliver the final blow
                    LivingEntity t = (LivingEntity) target;
                    t.setHealth(0);
                }
            }
        }
    }

    public ItemStack getCrescentRose() {
        ItemStack NEEDED_A_RWBY_REFERENCE = new ItemStack(Material.DIAMOND_HOE);
        ItemMeta datMeta = NEEDED_A_RWBY_REFERENCE.getItemMeta();
        datMeta.setDisplayName(ChatColor.DARK_RED + "Scythe");
        List<String> lore = new ArrayList();
        lore.add(ChatColor.GOLD + "WARNING: THIS WEAPON IS OVER POWERED");
        lore.add(ChatColor.GOLD + "AND BY OVER POWERED I MEAN INSTANT KILL");
        lore.add(ChatColor.YELLOW + "It costs " + ChatColor.RED + use_price + ChatColor.YELLOW + " coins per use in order to use this item.");
        datMeta.setLore(lore);
        datMeta.addEnchant(Enchantment.DAMAGE_UNDEAD, 420, true);
        datMeta.addEnchant(Enchantment.DAMAGE_ALL, 420, true);
        NEEDED_A_RWBY_REFERENCE.setItemMeta(datMeta);
        return NEEDED_A_RWBY_REFERENCE;
    }
}
