package me.galtap.fudzieconomy.command;

import me.galtap.fudzieconomy.command.subcommand.ConsoleSubCommand;
import me.galtap.fudzieconomy.command.subcommand.PlayerSubCommand;
import me.galtap.fudzieconomy.command.subcommand.SubCommand;
import me.galtap.fudzieconomy.command.subcommand.SubCommandType;
import me.galtap.fudzieconomy.config.MessagesConfig;
import me.galtap.fudzieconomy.utill.SimpleUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Map;

public abstract class AbstractCommand implements CommandExecutor {
    protected AbstractCommand(String command, JavaPlugin plugin){
        PluginCommand pluginCommand = plugin.getCommand(command);
        if(pluginCommand != null){
            pluginCommand.setExecutor(this);
        }
    }
    public abstract void execute(CommandSender sender, String label, String[] args);
    
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        execute(sender,label,args);
        return false;
    }

    protected void commandProcess(MessagesConfig messagesConfig, String[] args, CommandSender sender, Map<String,SubCommand> subCommands){
        SubCommand subCommand = subCommands.get(args[0].toLowerCase(Locale.ENGLISH));
        if(subCommand == null){
            messagesConfig.getError_arguments().forEach(sender::sendMessage);
            return;
        }
        if(sender instanceof Player && subCommand.getSubCommandType() == SubCommandType.PLAYER){
            PlayerSubCommand playerSubCommand = (PlayerSubCommand) subCommand;
            playerSubCommand.perform((Player) sender,args);
            return;
        }
        if(subCommand.getSubCommandType() == SubCommandType.CONSOLE){
            ConsoleSubCommand consoleSubCommand = (ConsoleSubCommand) subCommand;
            consoleSubCommand.perform(sender,args);
            return;
        }
        SimpleUtil.sendMessage(sender,messagesConfig.getError_arguments());
    }

}
