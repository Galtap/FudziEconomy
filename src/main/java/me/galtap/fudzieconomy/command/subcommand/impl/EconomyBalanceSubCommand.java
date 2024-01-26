package me.galtap.fudzieconomy.command.subcommand.impl;

import me.galtap.fudzieconomy.command.subcommand.ConsoleSubCommand;
import me.galtap.fudzieconomy.command.subcommand.SubCommandType;
import me.galtap.fudzieconomy.config.ConfigManager;
import me.galtap.fudzieconomy.config.MessagesConfig;
import me.galtap.fudzieconomy.core.EconomyManager;
import me.galtap.fudzieconomy.utill.SimpleUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class EconomyBalanceSubCommand implements ConsoleSubCommand {
    private final EconomyManager economyManager;
    private final ConfigManager configManager;

    public EconomyBalanceSubCommand(EconomyManager economyManager, ConfigManager configManager) {
        this.economyManager = Objects.requireNonNull(economyManager, "economyManager must not be null");
        this.configManager = Objects.requireNonNull(configManager, "configManager must not be null");
    }

    @Override
    public void perform(@NotNull CommandSender sender, @NotNull String[] args) {
        MessagesConfig messagesConfig = configManager.getMessagesConfig();

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
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            messagesConfig.getPlayer_not_online().forEach(sender::sendMessage);
            return;
        }
        if (economyManager.balanceAccountExists(player.getUniqueId(), accountName)) {
            SimpleUtil.replacePlaceholders("{ACCOUNT_NAME}",messagesConfig.getBalance_exists(),accountName).forEach(sender::sendMessage);
            return;
        }
        economyManager.createBalanceAccount(player.getUniqueId(), accountName, startBalance);
        SimpleUtil.replacePlaceholders("{BALANCE}", messagesConfig.getBalance_created(), String.valueOf(startBalance)).forEach(sender::sendMessage);
    }

    private void deleteBalance(String playerName, CommandSender sender, MessagesConfig messagesConfig, String accountName) {
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            messagesConfig.getPlayer_not_online().forEach(sender::sendMessage);
            return;
        }
        if (!economyManager.balanceAccountExists(player.getUniqueId(), accountName)) {
            SimpleUtil.replacePlaceholders("{ACCOUNT_NAME}",messagesConfig.getBalance_not_exists(),accountName);
            return;
        }
        economyManager.removeBalanceAccount(player.getUniqueId(), accountName);
        messagesConfig.getBalance_deleted().forEach(sender::sendMessage);
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
