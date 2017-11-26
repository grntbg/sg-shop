package ga.shadow.sgshop.commands;

import ga.shadow.sgshop.ConfigEntry;
import ga.shadow.sgshop.ShopData;
import me.totalfreedom.totalfreedommod.rank.Rank;
import me.totalfreedom.totalfreedommod.util.FUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


@CommandPermissions(level = Rank.OP, source = SourceType.BOTH)
@CommandParameters(description = "Shows the amount of coins you have or another player", usage = "/<command> [playername]")
public class Command_coins extends FreedomCommand {

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole) {

        Player p;
        final String prefix = FUtil.colorize(ConfigEntry.SHOP_PREFIX.getString() + " ");
        if (args.length > 0) {
            if (getPlayer(args[0]) != null) {
                p = getPlayer(args[0]);
            } else {
                msg(PLAYER_NOT_FOUND);
                return true;
            }
        } else {
            if (senderIsConsole) {
                msg(prefix + ChatColor.RED + "You are not a player, use /coins <playername>");
                return true;
            } else {
                p = playerSender;
            }
        }
        ShopData sd = plugin.sh.getData(p);
        msg(prefix + ChatColor.GREEN + (args.length > 0 ? p.getName() + " has " : "You have ") + ChatColor.RED + sd.getCoins() + ChatColor.GREEN + " coins.");
        return true;
    }
}