package ga.shadow.sgshop;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;


public class VotifierListener implements Listener {
    @EventHandler(priority = EventPriority.NORMAL)
    public void onVotifierEvent(VotifierEvent event) {
        Vote vote = event.getVote();

        ShopData sd = Main.plugin().sh.getData(vote.getUsername());
        if (sd == null) {
            return;
        }
        sd.setCoins(sd.getCoins() + ConfigEntry.COINS_PER_VOTE.getInteger());
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', ConfigEntry.VOTE_MESSAGE.getString().replace("{username}", sd.getUsername())));
    }
}
