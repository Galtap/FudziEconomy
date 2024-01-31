package me.galtap.fudzieconomy.command.subcommand.impl;

import me.galtap.fudzieconomy.command.subcommand.ConsoleSubCommand;
import me.galtap.fudzieconomy.command.subcommand.SubCommandType;
import me.galtap.fudzieconomy.config.EconomyConfigManager;
import me.galtap.fudzieconomy.config.MessagesConfig;
import me.galtap.fudzieconomy.core.BalanceAccount;
import me.galtap.fudzieconomy.core.EconomyManager;
import me.galtap.fudzieconomy.event.BalanceChangedEvent;
import me.galtap.fudzieconomy.utill.SimpleUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class MoneySubCommand implements ConsoleSubCommand {
    private final EconomyManager economyManager;
    private final EconomyConfigManager economyConfigManager;

    public MoneySubCommand(EconomyManager economyManager, EconomyConfigManager economyConfigManager) {
        this.economyManager = economyManager;
        this.economyConfigManager = economyConfigManager;
    }

    @Override
    public void perform(@NotNull CommandSender sender, @NotNull String[] args) {
        MessagesConfig messagesConfig = economyConfigManager.getMessagesConfig();
        if (args.length == 5) {
            String action = args[1].toLowerCase(Locale.ENGLISH);
            if (!action.equalsIgnoreCase("give") && !action.equalsIgnoreCase("remove") && !action.equalsIgnoreCase("set")) {
                SimpleUtil.sendMessage(sender,messagesConfig.getError_arguments());
                return;
            }
            action = action.toLowerCase(Locale.ENGLISH);
            if (SimpleUtil.notHasPermission(sender, "fudzieco.money." + action, messagesConfig)) return;

            processCommand(args[2], args[3], args[4], sender, messagesConfig, action);
        } else {
            SimpleUtil.sendMessage(sender,messagesConfig.getError_arguments());
        }
    }

    private void processCommand(String playerName, String accountName, String countString, CommandSender sender, MessagesConfig messagesConfig, String subCommandType) {
        OfflinePlayer player = Bukkit.getPlayer(playerName);
        if (player == null) {
            SimpleUtil.sendMessage(sender,messagesConfig.getPlayer_not_exists());
            return;
        }

        BalanceAccount balanceAccount = economyManager.getBalanceAccount(player.getUniqueId(), accountName);
        if (balanceAccount == null) {
            SimpleUtil.sendMessage(sender,messagesConfig.getBalance_not_exists(),"{ACCOUNT_NAME}",accountName);
            return;
        }

        Integer count = SimpleUtil.parseInt(countString, messagesConfig, sender);
        if (count == null) return;

        switch (subCommandType.toLowerCase(Locale.ENGLISH)) {
            case "give":
                balanceAccount.addMoney(count);
                SimpleUtil.sendMessage(sender,messagesConfig.getMoney_pay());
                Bukkit.getPluginManager().callEvent(new BalanceChangedEvent(balanceAccount));
                break;
            case "remove":
                balanceAccount.subtractMoney(count);
                SimpleUtil.sendMessage(sender,messagesConfig.getMoney_removed());
                Bukkit.getPluginManager().callEvent(new BalanceChangedEvent(balanceAccount));
                break;
            case "set":
                balanceAccount.setMoney(count);
                SimpleUtil.sendMessage(sender,messagesConfig.getMoney_set());
                Bukkit.getPluginManager().callEvent(new BalanceChangedEvent(balanceAccount));
                break;
            default:
                SimpleUtil.sendMessage(sender,messagesConfig.getError_arguments());
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
