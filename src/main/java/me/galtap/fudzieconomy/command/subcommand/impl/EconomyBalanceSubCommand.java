package me.galtap.fudzieconomy.command.subcommand.impl;

import me.galtap.fudzieconomy.command.subcommand.ConsoleSubCommand;
import me.galtap.fudzieconomy.command.subcommand.SubCommandType;
import me.galtap.fudzieconomy.config.EconomyConfigManager;
import me.galtap.fudzieconomy.config.MessagesConfig;
import me.galtap.fudzieconomy.core.BalanceAccount;
import me.galtap.fudzieconomy.core.EconomyManager;
import me.galtap.fudzieconomy.event.BalanceDeletedEvent;
import me.galtap.fudzieconomy.utill.SimpleUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class EconomyBalanceSubCommand implements ConsoleSubCommand {
    private final EconomyManager economyManager;
    private final EconomyConfigManager economyConfigManager;

    public EconomyBalanceSubCommand(EconomyManager economyManager, EconomyConfigManager economyConfigManager) {
        this.economyManager = Objects.requireNonNull(economyManager, "economyManager must not be null");
        this.economyConfigManager = Objects.requireNonNull(economyConfigManager, "configManager must not be null");
    }

    @Override
    public void perform(@NotNull CommandSender sender, @NotNull String[] args) {
        MessagesConfig messagesConfig = economyConfigManager.getMessagesConfig();

        if (args.length == 4) {
            if ("create".equalsIgnoreCase(args[1])) {
                if(SimpleUtil.notHasPermission(sender,"fudzieco.balance.create",messagesConfig)) return;
                createBalance(args[2], sender, messagesConfig, args[3], 0);
                return;
            }
            if ("delete".equalsIgnoreCase(args[1])) {
                if(SimpleUtil.notHasPermission(sender,"fudzieco.balance.delete",messagesConfig)) return;
                deleteBalance(args[2], sender, messagesConfig, args[3]);
                return;
            }
        }

        if (args.length == 5 && "create".equalsIgnoreCase(args[1])) {
            if(SimpleUtil.notHasPermission(sender,"fudzieco.balance.create",messagesConfig)) return;
            Integer count = SimpleUtil.parseInt(args[4], messagesConfig,sender);
            if(count == null) return;
            createBalance(args[2], sender, messagesConfig, args[3],count);
            return;
        }
        messagesConfig.getError_arguments().forEach(sender::sendMessage);
    }

    private void createBalance(String playerName, CommandSender sender, MessagesConfig messagesConfig, String accountName, int startBalance) {
        OfflinePlayer player = Bukkit.getPlayer(playerName);
        if (player == null) {
            SimpleUtil.sendMessage(sender,messagesConfig.getPlayer_not_exists());
            return;
        }
        if (economyManager.balanceAccountExists(player.getUniqueId(), accountName)) {
            SimpleUtil.sendMessage(sender,messagesConfig.getBalance_exists(),"{ACCOUNT_NAME}",accountName);
            return;
        }
        economyManager.createBalanceAccount(player.getUniqueId(), accountName, startBalance);
        SimpleUtil.sendMessage(sender,messagesConfig.getBalance_created(),"{BALANCE}",String.valueOf(startBalance));
    }

    private void deleteBalance(String playerName, CommandSender sender, MessagesConfig messagesConfig, String accountName) {
        OfflinePlayer player = Bukkit.getPlayer(playerName);
        if (player == null) {
            SimpleUtil.sendMessage(sender,messagesConfig.getPlayer_not_exists());
            return;
        }
        BalanceAccount balanceAccount = economyManager.getBalanceAccount(player.getUniqueId(),accountName);
        if (balanceAccount == null) {
            SimpleUtil.sendMessage(sender,messagesConfig.getBalance_not_exists(),"{ACCOUNT_NAME}",accountName);
            return;
        }
        Bukkit.getPluginManager().callEvent(new BalanceDeletedEvent(sender,balanceAccount));
        economyManager.removeBalanceAccount(player.getUniqueId(), accountName);
        SimpleUtil.sendMessage(sender,messagesConfig.getBalance_deleted());
    }


    @Override
    public SubCommandType getSubCommandType() {
        return SubCommandType.CONSOLE;
    }

    @Override
    public String getName() {
        return "balance";
    }
}
