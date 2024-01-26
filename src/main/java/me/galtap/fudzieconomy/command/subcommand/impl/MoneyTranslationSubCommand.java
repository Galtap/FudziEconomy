package me.galtap.fudzieconomy.command.subcommand.impl;

import me.galtap.fudzieconomy.command.subcommand.PlayerSubCommand;
import me.galtap.fudzieconomy.command.subcommand.SubCommandType;
import me.galtap.fudzieconomy.config.ConfigManager;
import me.galtap.fudzieconomy.config.MessagesConfig;
import me.galtap.fudzieconomy.core.EconomyManager;
import me.galtap.fudzieconomy.model.BalanceAccount;
import me.galtap.fudzieconomy.model.ItemMoneyType;
import me.galtap.fudzieconomy.utill.SimpleUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class MoneyTranslationSubCommand implements PlayerSubCommand {
    private final EconomyManager economyManager;
    private final ConfigManager configManager;

    public MoneyTranslationSubCommand(EconomyManager economyManager, ConfigManager configManager){

        this.economyManager = economyManager;
        this.configManager = configManager;
    }
    @Override
    public void perform(@NotNull Player player, @NotNull String[] args) {
        MessagesConfig messagesConfig = configManager.getMessagesConfig();
        if(args.length != 4){
            messagesConfig.getError_arguments().forEach(player::sendMessage);
            return;
        }
        try {
            String accountName = args[3];
            BalanceAccount balanceAccount = economyManager.getBalanceAccount(player.getUniqueId(),accountName);
            if(balanceAccount == null){
                SimpleUtil.replacePlaceholders("{ACCOUNT_NAME}",messagesConfig.getBalance_not_exists(),accountName);
                return;
            }
            ItemMoneyType itemMoneyType = ItemMoneyType.valueOf(args[1].toUpperCase(Locale.ENGLISH));
            int count = Integer.parseInt(args[2]);
            int virtualMoney = economyManager.convertToVirtual(itemMoneyType,count,player.getUniqueId());
            if(virtualMoney == 0){
                messagesConfig.getError_arguments().forEach(player::sendMessage);
                return;
            }
            if(virtualMoney == -1){
                messagesConfig.getConverting_to_virtual_error().forEach(player::sendMessage);
                return;
            }
            balanceAccount.addMoney(virtualMoney);
            messagesConfig.getConverted_to_virtual().forEach(player::sendMessage);
        }catch (IllegalArgumentException e){
            messagesConfig.getError_arguments().forEach(player::sendMessage);
        }
    }

    @Override
    public SubCommandType getSubCommandType() {
        return SubCommandType.PLAYER;
    }

    @Override
    public String getName() {
        return "translation";
    }
}
