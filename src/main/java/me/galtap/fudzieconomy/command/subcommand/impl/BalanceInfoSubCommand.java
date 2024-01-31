package me.galtap.fudzieconomy.command.subcommand.impl;

import me.galtap.fudzieconomy.command.subcommand.ConsoleSubCommand;
import me.galtap.fudzieconomy.command.subcommand.SubCommandType;
import me.galtap.fudzieconomy.config.EconomyConfigManager;
import me.galtap.fudzieconomy.config.MessagesConfig;
import me.galtap.fudzieconomy.core.EconomyManager;
import me.galtap.fudzieconomy.core.BalanceAccount;
import me.galtap.fudzieconomy.utill.SimpleUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BalanceInfoSubCommand implements ConsoleSubCommand {
    private final EconomyManager economyManager;
    private final EconomyConfigManager economyConfigManager;
    public BalanceInfoSubCommand(EconomyManager economyManager, EconomyConfigManager economyConfigManager){

        this.economyManager = economyManager;
        this.economyConfigManager = economyConfigManager;
    }
    @Override
    public void perform(@NotNull CommandSender sender, @NotNull String[] args) {
        MessagesConfig messagesConfig = economyConfigManager.getMessagesConfig();
        if(args.length == 2){
            if(SimpleUtil.notHasPermission(sender,"fudzieco.info",messagesConfig)) return;
            Player player = Bukkit.getPlayer(args[1]);
            if(player == null){
                messagesConfig.getPlayer_not_online().forEach(sender::sendMessage);
                return;
            }
            sender.sendMessage(ChatColor.GREEN+"информация о счетах игрока "+player.getName());
            sender.sendMessage(ChatColor.GOLD+"-----------------------------------------------");
            for(BalanceAccount balanceAccount: economyManager.getPlayerAccounts(player.getUniqueId())){
                sender.sendMessage(ChatColor.YELLOW+"имя счета: "+balanceAccount.getAccountName());
                sender.sendMessage(ChatColor.YELLOW+"баланс: "+balanceAccount.getBalance());
                sender.sendMessage(" ");
            }
            sender.sendMessage(ChatColor.GOLD+"-----------------------------------------------");
            return;
        }
        messagesConfig.getError_arguments().forEach(sender::sendMessage);
    }

    @Override
    public SubCommandType getSubCommandType() {
        return SubCommandType.CONSOLE;
    }

    @Override
    public String getName() {
        return "info";
    }
}
