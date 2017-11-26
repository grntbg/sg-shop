package ga.shadow.sgshop.commands;

import ga.shadow.sgshop.ConfigEntry;
import ga.shadow.sgshop.ShopData;
import me.totalfreedom.totalfreedommod.rank.Rank;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

@CommandParameters(usage = "/<command", aliases = "", description = "Open Shop GUI")
@CommandPermissions(source = SourceType.ONLY_IN_GAME, level = Rank.OP)
public class Command_shop extends FreedomCommand {
    public final int customLoginMessagePrice = ConfigEntry.SHOP_LOGIN_MESSAGE_PRICE.getInteger();
    public final int thorHammerPrice = ConfigEntry.SHOP_THOR_HAMMER_PRICE.getInteger();
    public final int crescentRosePrice = ConfigEntry.SHOP_CRESCENT_ROSE_PRICE.getInteger();
    public final int minigunPrice = ConfigEntry.SHOP_MINIGUN_PRICE.getInteger();
    public int coins;

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole) {
        ShopData sd = plugin.sh.getData(playerSender);
        coins = sd.getCoins();
        Boolean hasCustomLoginMessages = sd.isCustomLoginMessage();
        Boolean hasThorHammer = sd.isThorHammer();
        Boolean hasCrescentRose = sd.isScythe();
        Boolean hasMinigun = sd.isMinigun();
        Inventory i = server.createInventory(null, 36, plugin.sh.GUIName);
        for (int slot = 0; slot < 36; slot++) {
            ItemStack blank = new ItemStack(Material.STAINED_GLASS_PANE);
            ItemMeta meta = blank.getItemMeta();
            meta.setDisplayName(ChatColor.RESET + "");
            blank.setItemMeta(meta);
            i.setItem(slot, blank);
        }

        ItemStack customLoginMessage = newShopItem(new ItemStack(Material.NAME_TAG), ChatColor.BLUE, "Custom Login Messages", customLoginMessagePrice, hasCustomLoginMessages);
        i.setItem(10, customLoginMessage);
        ItemStack thorHammer = newShopItem(new ItemStack(Material.IRON_PICKAXE), ChatColor.GREEN, "Thor's Hammer", thorHammerPrice, hasThorHammer);
        i.setItem(14, thorHammer);
        ItemStack crescentRose = newShopItem(new ItemStack(Material.DIAMOND_HOE), ChatColor.DARK_RED, "Scythe", crescentRosePrice, hasCrescentRose);
        i.setItem(16, crescentRose);
        ItemStack minigun = newShopItem(new ItemStack(Material.IRON_BARDING), ChatColor.YELLOW, "Minigun", minigunPrice, hasMinigun);
        i.setItem(12, minigun);
        // Coins
        ItemStack coins = new ItemStack(Material.GOLD_NUGGET);
        ItemMeta m = coins.getItemMeta();
        m.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&lYou have &e&l" + sd.getCoins() + "&c&l coins"));
        coins.setItemMeta(m);
        i.setItem(35, coins);

        playerSender.openInventory(i);
        return true;
    }

    public boolean canOfford(int p, int c) {
        return c >= p;
    }

    public int amountNeeded(int p) {
        return p - coins;
    }

    public ItemStack newShopItem(ItemStack is, ChatColor color, String name, int price, Boolean purchased) {
        ItemMeta m = is.getItemMeta();
        m.setDisplayName(color + name);
        Boolean co = canOfford(price, coins);
        List<String> l = new ArrayList();
        if (!purchased) {
            l.add(ChatColor.GOLD + "Price: " + (co ? ChatColor.DARK_GREEN : ChatColor.RED) + price);
            if (!co) {
                l.add(ChatColor.RED + "You can not afford this item!");
                l.add(ChatColor.RED + "You need " + amountNeeded(price) + " more coins to buy this item.");
            }
        } else {
            l.add(ChatColor.RED + "You already purchased this item");
        }
        m.setLore(l);
        is.setItemMeta(m);
        return is;
    }

    public ItemStack newShopItem(Material mat, ChatColor color, String name, int price, Boolean purchased) {
        return newShopItem(new ItemStack(mat), color, name, price, purchased);
    }

}
/*
        Shop layout:

        Dimensions: 9x4
        Key: c = Chat Color, l = Login Message, t = Thor's Hammer, r = Scythe, m = Minigun $ = Coins}
        ---------
        -c-l-t-r-
        --m------
        --------$
*/
