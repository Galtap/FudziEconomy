package me.galtap.fudzieconomy.command.subcommand.impl;

import me.galtap.fudzieconomy.command.subcommand.PlayerSubCommand;
import me.galtap.fudzieconomy.command.subcommand.SubCommandType;
import me.galtap.fudzieconomy.config.EconomyConfigManager;
import me.galtap.fudzieconomy.config.MessagesConfig;
import me.galtap.fudzieconomy.core.EconomyManager;
import me.galtap.fudzieconomy.core.BalanceAccount;
import me.galtap.fudzieconomy.money.ItemMoneyType;
import me.galtap.fudzieconomy.utill.SimpleUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class MoneyConclusionSubCommand implements PlayerSubCommand {
    private final EconomyManager economyManager;
    private final EconomyConfigManager economyConfigManager;

    public MoneyConclusionSubCommand(EconomyManager economyManager, EconomyConfigManager economyConfigManager){

        this.economyManager = economyManager;
        this.economyConfigManager = economyConfigManager;
    }
    @Override
    public void perform(@NotNull Player player, @NotNull String[] args) {
        MessagesConfig messagesConfig = economyConfigManager.getMessagesConfig();
        if(args.length != 4){
            messagesConfig.getError_arguments().forEach(player::sendMessage);
            return;
        }
        String accountName = args[3];
        BalanceAccount balanceAccount = economyManager.getBalanceAccount(player.getUniqueId(),accountName);
        if(balanceAccount == null){
            SimpleUtil.sendMessage(player,messagesConfig.getBalance_not_exists(),"{ACCOUNT_NAME}",accountName);
            return;
        }
        try {
            ItemMoneyType itemMoneyType = ItemMoneyType.valueOf(args[1].toUpperCase(Locale.ENGLISH));
            int count = Integer.parseInt(args[2]);
            economyManager.convertToItemMoney(itemMoneyType,count,balanceAccount,player);
        }catch (IllegalArgumentException e){
            SimpleUtil.sendMessage(player,messagesConfig.getError_arguments());
            e.printStackTrace();
        }
    }

    @Override
    public SubCommandType getSubCommandType() {
        return SubCommandType.PLAYER;
    }

    @Override
    public String getName() {
        return "conclusion";
    }
}
