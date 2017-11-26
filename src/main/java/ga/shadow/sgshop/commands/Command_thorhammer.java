package ga.shadow.sgshop.commands;

import ga.shadow.sgshop.ShopData;
import me.totalfreedom.totalfreedommod.rank.Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.OP, source = SourceType.ONLY_IN_GAME)
@CommandParameters(description = "Gives you Thor's hammer", usage = "/<command>")
public class Command_thorhammer extends FreedomCommand {
    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole) {
        ShopData sd = plugin.sh.getData(playerSender);
        if (!sd.isThorHammer()) {
            msg("You have not yet purchased Thor's Hammer from the shop!", ChatColor.RED);
            return true;
        }
        playerSender.getInventory().addItem(plugin.th.getThorHammer());
        msg("You have been given Thor's Hammer!", ChatColor.GREEN);
        return true;
    }
}