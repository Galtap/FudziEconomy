package me.galtap.fudzieconomy.command.subcommand.impl;

import me.galtap.fudzieconomy.command.subcommand.PlayerSubCommand;
import me.galtap.fudzieconomy.command.subcommand.SubCommandType;
import me.galtap.fudzieconomy.config.EconomyConfigManager;
import me.galtap.fudzieconomy.config.MessagesConfig;
import me.galtap.fudzieconomy.config.StandardConfig;
import me.galtap.fudzieconomy.core.EconomyManager;
import me.galtap.fudzieconomy.core.BalanceAccount;
import me.galtap.fudzieconomy.event.BalanceChangedEvent;
import me.galtap.fudzieconomy.utill.SimpleUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PayMoneySubCommand implements PlayerSubCommand {
    private final EconomyManager economyManager;
    private final EconomyConfigManager economyConfigManager;

    public PayMoneySubCommand(EconomyManager economyManager, EconomyConfigManager economyConfigManager){

        this.economyManager = economyManager;
        this.economyConfigManager = economyConfigManager;
    }
    @Override
    public void perform(@NotNull Player player, @NotNull String[] args) {
        MessagesConfig messagesConfig = economyConfigManager.getMessagesConfig();
        if(args.length != 5){
            messagesConfig.getError_arguments().forEach(player::sendMessage);
            return;
        }
        if(SimpleUtil.notHasPermission(player,"fudzimoney.money.pay",messagesConfig)) return;
        Player target = Bukkit.getPlayer(args[1]);
        if(target == null){
            SimpleUtil.sendMessage(player,messagesConfig.getPlayer_not_online());
            return;
        }
        BalanceAccount yourAccount = economyManager.getBalanceAccount(player.getUniqueId(),args[2]);
        if(yourAccount == null){
            SimpleUtil.sendMessage(player,messagesConfig.getBalance_not_exists(),"{ACCOUNT_NAME}",args[2]);
            return;
        }
        BalanceAccount targetAccount = economyManager.getBalanceAccount(target.getUniqueId(),args[3]);
        if(targetAccount == null){
            SimpleUtil.sendMessage(player,messagesConfig.getBalance_not_exists(),"{ACCOUNT_NAME}",args[3]);
            return;
        }
        Integer count = SimpleUtil.parseInt(args[4],messagesConfig,player);
        if(count == null) return;

        StandardConfig standardConfig = economyConfigManager.getStandardConfig();
        if(count > standardConfig.getMaxPay() || count < standardConfig.getMinPay()){
            SimpleUtil.sendMessage(player,messagesConfig.getMoney_pay_limit());
            return;
        }
        if(yourAccount.getBalance() < count){
            SimpleUtil.sendMessage(player,messagesConfig.getSmall_money());
            return;
        }
        yourAccount.subtractMoney(count);
        targetAccount.addMoney(count);

        SimpleUtil.sendMessage(player,messagesConfig.getMoney_pay());
        Bukkit.getPluginManager().callEvent(new BalanceChangedEvent(targetAccount));
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
