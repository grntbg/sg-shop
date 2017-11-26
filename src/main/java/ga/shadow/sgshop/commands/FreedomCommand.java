package ga.shadow.sgshop.commands;

import ga.shadow.sgshop.Main;
import lombok.Getter;
import me.totalfreedom.totalfreedommod.TotalFreedomMod;
import me.totalfreedom.totalfreedommod.rank.Rank;
import net.pravian.aero.command.AbstractCommandBase;
import net.pravian.aero.util.Players;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

//Credit to TFM developers
public abstract class FreedomCommand extends AbstractCommandBase<Main> {

    public static final String NOT_FROM_CONSOLE = "This command may not be used from the console.";
    public static final String PLAYER_NOT_FOUND = ChatColor.GRAY + "Player not found!";
    //
    @Getter
    private final CommandParameters params;
    @Getter
    private final CommandPermissions perms;

    public FreedomCommand() {
        this.params = getClass().getAnnotation(CommandParameters.class);
        if (params == null) {
            Main.plugin().getLogger().warning("Ignoring command usage for command " + getClass().getSimpleName() + ". Command is not annotated!");
        }

        this.perms = getClass().getAnnotation(CommandPermissions.class);
        if (perms == null) {
            Main.plugin().getLogger().warning("Ignoring permissions for command " + getClass().getSimpleName() + ". Command is not annotated!");
        }
    }

    public static FreedomCommand getFrom(Command command) {
        try {
            return (FreedomCommand) ((FreedomCommandExecutor) (((PluginCommand) command).getExecutor())).getCommandBase();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public final boolean runCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        setVariables(sender, command, label, args);

        try {
            return run(sender, playerSender, command, label, args, isConsole());
        } catch (CommandFailException ex) {
            msg(ex.getMessage());
            return true;
        } catch (Exception ex) {
            Main.plugin().getLogger().severe("Uncaught exception executing command: " + command.getName());
            Main.plugin().getLogger().severe(ex.getMessage());
            sender.sendMessage(ChatColor.RED + "Command error: " + (ex.getMessage() == null ? "Unknown cause" : ex.getMessage()));
            return true;
        }
    }

    protected abstract boolean run(final CommandSender sender, final Player playerSender, final Command cmd, final String commandLabel, final String[] args, final boolean senderIsConsole);

    protected void checkConsole() {
        if (!isConsole()) {
            throw new CommandFailException(getHandler().getOnlyConsoleMessage());
        }
    }

    protected void checkPlayer() {
        if (isConsole()) {
            throw new CommandFailException(getHandler().getOnlyPlayerMessage());
        }
    }

    protected void checkRank(Rank rank) {
        if (!TotalFreedomMod.plugin().rm.getRank(sender).isAtLeast(rank)) {
            noPerms();
        }
    }

    protected boolean noPerms() {
        throw new CommandFailException(getHandler().getPermissionMessage());
    }

    protected boolean isConsole() {
        return !(sender instanceof Player);
    }

    protected Player getPlayer(String name) {
        return Players.getPlayer(name);
    }

    protected void msg(final CommandSender sender, final String message, final ChatColor color) {
        if (sender == null) {
            return;
        }
        sender.sendMessage(color + message);
    }

    protected void msg(final String message, final ChatColor color) {
        msg(sender, message, color);
    }

    protected void msg(final CommandSender sender, final String message) {
        msg(sender, message, ChatColor.GRAY);
    }

    protected void msg(final String message) {
        msg(sender, message);
    }
}

//Credit to TFM developers
class CommandFailException extends RuntimeException {
    private static final long serialVersionUID = -92333791173123L;

    public CommandFailException(String message) {
        super(message);
    }
}