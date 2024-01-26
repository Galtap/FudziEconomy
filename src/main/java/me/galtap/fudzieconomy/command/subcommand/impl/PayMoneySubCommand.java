package me.galtap.fudzieconomy.command.subcommand.impl;

import me.galtap.fudzieconomy.command.subcommand.PlayerSubCommand;
import me.galtap.fudzieconomy.command.subcommand.SubCommandType;
import me.galtap.fudzieconomy.config.ConfigManager;
import me.galtap.fudzieconomy.config.MessagesConfig;
import me.galtap.fudzieconomy.config.StandardConfig;
import me.galtap.fudzieconomy.core.EconomyManager;
import me.galtap.fudzieconomy.model.BalanceAccount;
import me.galtap.fudzieconomy.utill.SimpleUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PayMoneySubCommand implements PlayerSubCommand {
    private final EconomyManager economyManager;
    private final ConfigManager configManager;

    public PayMoneySubCommand(EconomyManager economyManager, ConfigManager configManager){

        this.economyManager = economyManager;
        this.configManager = configManager;
    }
    @Override
    public void perform(@NotNull Player player, @NotNull String[] args) {
        MessagesConfig messagesConfig = configManager.getMessagesConfig();
        if(args.length != 5){
            messagesConfig.getError_arguments().forEach(player::sendMessage);
            return;
        }
        if(SimpleUtil.notHasPermission(player,"fudzimoney.money.pay",messagesConfig)) return;
        Player target = Bukkit.getPlayer(args[1]);
        if(target == null){
            messagesConfig.getPlayer_not_online().forEach(player::sendMessage);
            return;
        }
        BalanceAccount yourAccount = economyManager.getBalanceAccount(player.getUniqueId(),args[2]);
        if(yourAccount == null){
            SimpleUtil.replacePlaceholders("{ACCOUNT_NAME}",messagesConfig.getBalance_not_exists(),args[2]);
            return;
        }
        BalanceAccount targetAccount = economyManager.getBalanceAccount(target.getUniqueId(),args[3]);
        if(targetAccount == null){
            SimpleUtil.replacePlaceholders("{ACCOUNT_NAME}",messagesConfig.getBalance_not_exists(),args[3]);
            return;
        }
        Integer count = SimpleUtil.parseInt(args[4],messagesConfig,player);
        if(count == null) return;

        StandardConfig standardConfig = configManager.getStandardConfig();
        if(count > standardConfig.getMaxPay() || count < standardConfig.getMinPay()){
            messagesConfig.getMoney_pay_limit().forEach(player::sendMessage);
            return;
        }
        if(yourAccount.getBalance() < count){
            messagesConfig.getSmall_money().forEach(player::sendMessage);
            return;
        }
        yourAccount.subtractMoney(count);
        targetAccount.addMoney(count);
        messagesConfig.getMoney_pay().forEach(player::sendMessage);
    }

    @Override
    public SubCommandType getSubCommandType() {
        return SubCommandType.PLAYER;
    }

    @Override
    public String getName() {
        return "pay";
    }
}
