package ga.shadow.sgshop.commands;

import ga.shadow.sgshop.Main;
import lombok.Getter;
import net.pravian.aero.command.handler.SimpleCommandHandler;
import net.pravian.aero.component.service.AbstractService;
import org.bukkit.ChatColor;

public class CommandLoader extends AbstractService<Main> {
    //Credit to TFM developers
    @Getter
    private final SimpleCommandHandler<Main> handler;

    public CommandLoader(Main plugin) {
        super(plugin);

        handler = new SimpleCommandHandler<>(plugin);
    }

    @Override
    protected void onStart() {
        handler.clearCommands();
        handler.setExecutorFactory(new FreedomCommandExecutor.FreedomExecutorFactory(plugin));
        handler.setCommandClassPrefix("Command_");
        handler.setPermissionMessage(ChatColor.RED + "You do not have permission to use this command.");
        handler.setOnlyConsoleMessage(ChatColor.RED + "This command can only be used from the console.");
        handler.setOnlyPlayerMessage(ChatColor.RED + "This command can only be used by players.");

        handler.loadFrom(FreedomCommand.class.getPackage());
        handler.registerAll("sg-shop", true);

    }

    @Override
    protected void onStop() {
        handler.clearCommands();
    }
}