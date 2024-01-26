package me.galtap.fudzieconomy.command;

import me.galtap.fudzieconomy.command.subcommand.SubCommand;
import me.galtap.fudzieconomy.command.subcommand.impl.MoneyConclusionSubCommand;
import me.galtap.fudzieconomy.command.subcommand.impl.MoneyTranslationSubCommand;
import me.galtap.fudzieconomy.command.subcommand.impl.PayMoneySubCommand;
import me.galtap.fudzieconomy.config.ConfigManager;
import me.galtap.fudzieconomy.config.MessagesConfig;
import me.galtap.fudzieconomy.core.EconomyManager;
import me.galtap.fudzieconomy.model.BalanceAccount;
import me.galtap.fudzieconomy.utill.SimpleUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class FudziMoneyCMD extends AbstractCommand{
    private final EconomyManager economyManager;
    private final ConfigManager configManager;
    private final Map<String, SubCommand> subCommands = new HashMap<>();

    public FudziMoneyCMD(String command, JavaPlugin plugin, EconomyManager economyManager, ConfigManager configManager) {
        super(command, plugin);
        this.economyManager = economyManager;
        this.configManager = configManager;

        registerCommand(new PayMoneySubCommand(economyManager,configManager));
        registerCommand(new MoneyConclusionSubCommand(economyManager,configManager));
        registerCommand(new MoneyTranslationSubCommand(economyManager,configManager));
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        MessagesConfig messagesConfig = configManager.getMessagesConfig();
        if(args.length == 0){
            if(sender instanceof Player){
                Player player = (Player) sender;
                Collection<BalanceAccount> balanceAccounts = economyManager.getPlayerAccounts(player.getUniqueId());
                if(balanceAccounts.isEmpty()){
                    messagesConfig.getEmpty_accounts().forEach(player::sendMessage);
                    return;
                }
                player.sendMessage(ChatColor.GREEN+"Ваши счета:");
                sender.sendMessage(ChatColor.GOLD+"-----------------------------------------------");
                for(BalanceAccount balanceAccount: economyManager.getPlayerAccounts(player.getUniqueId())){
                    sender.sendMessage(ChatColor.YELLOW+"имя счета: "+balanceAccount.getAccountName());
                    sender.sendMessage(ChatColor.YELLOW+"баланс: "+balanceAccount.getBalance());
                    sender.sendMessage(" ");
                }
                sender.sendMessage(ChatColor.GOLD+"-----------------------------------------------");
            }
            return;
        }
        if(args.length == 1 && args[0].equalsIgnoreCase("help")){
            if(SimpleUtil.notHasPermission(sender,"fudzimoney.help",messagesConfig)) return;
            messagesConfig.getFudzimoney_help().forEach(sender::sendMessage);
            return;
        }

        commandProcess(messagesConfig,args,sender,subCommands);
    }
    private void registerCommand(SubCommand subCommand){
        subCommands.put(subCommand.getName(),subCommand);
    }
}
