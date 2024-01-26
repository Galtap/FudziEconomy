package me.galtap.fudzieconomy.command.subcommand.impl;

import me.galtap.fudzieconomy.command.subcommand.ConsoleSubCommand;
import me.galtap.fudzieconomy.command.subcommand.SubCommandType;
import me.galtap.fudzieconomy.config.ConfigManager;
import me.galtap.fudzieconomy.config.MessagesConfig;
import me.galtap.fudzieconomy.core.EconomyManager;
import me.galtap.fudzieconomy.model.BalanceAccount;
import me.galtap.fudzieconomy.utill.SimpleUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class MoneySubCommand implements ConsoleSubCommand {
    private final EconomyManager economyManager;
    private final ConfigManager configManager;

    public MoneySubCommand(EconomyManager economyManager, ConfigManager configManager) {
        this.economyManager = economyManager;
        this.configManager = configManager;
    }

    @Override
    public void perform(@NotNull CommandSender sender, @NotNull String[] args) {
        MessagesConfig messagesConfig = configManager.getMessagesConfig();
        if (args.length == 5) {
            String action = args[1].toLowerCase(Locale.ENGLISH);
            if (!action.equalsIgnoreCase("give") && !action.equalsIgnoreCase("remove") && !action.equalsIgnoreCase("set")) {
                messagesConfig.getError_arguments().forEach(sender::sendMessage);
                return;
            }
            action = action.toLowerCase(Locale.ENGLISH);
            if (SimpleUtil.notHasPermission(sender, "fudzieco.money." + action, messagesConfig)) return;

            processCommand(args[2], args[3], args[4], sender, messagesConfig, action);
        } else {
            messagesConfig.getError_arguments().forEach(sender::sendMessage);
        }
    }

    private void processCommand(String playerName, String accountName, String countString, CommandSender sender, MessagesConfig messagesConfig, String subCommandType) {
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            messagesConfig.getPlayer_not_online().forEach(sender::sendMessage);
            return;
        }

        BalanceAccount balanceAccount = economyManager.getBalanceAccount(player.getUniqueId(), accountName);
        if (balanceAccount == null) {
            SimpleUtil.replacePlaceholders("{ACCOUNT_NAME}", messagesConfig.getBalance_not_exists(), accountName);
            return;
        }

        Integer count = SimpleUtil.parseInt(countString, messagesConfig, sender);
        if (count == null) return;

        switch (subCommandType.toLowerCase(Locale.ENGLISH)) {
            case "give":
                balanceAccount.addMoney(count);
                messagesConfig.getMoney_pay().forEach(sender::sendMessage);
                break;
            case "remove":
                balanceAccount.subtractMoney(count);
                messagesConfig.getMoney_removed().forEach(sender::sendMessage);
                break;
            case "set":
                balanceAccount.setMoney(count);
                messagesConfig.getMoney_set().forEach(sender::sendMessage);
                break;
            default:
                messagesConfig.getError_arguments().forEach(sender::sendMessage);
        }
    }

    @Override
    public SubCommandType getSubCommandType() {
        return SubCommandType.CONSOLE;
    }

    @Override
    public String getName() {
        return "money";
    }
}
