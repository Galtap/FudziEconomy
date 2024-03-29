package me.galtap.fudzieconomy.core;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import me.galtap.fudzieconomy.config.EconomyConfigManager;
import me.galtap.fudzieconomy.config.MessagesConfig;
import me.galtap.fudzieconomy.money.ItemMoney;
import me.galtap.fudzieconomy.money.ItemMoneyType;
import me.galtap.fudzieconomy.utill.SimpleUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class EconomyManager {
    private final Multimap<UUID, BalanceAccount> playerAccounts = ArrayListMultimap.create();
    private final EconomyConfigManager economyConfigManager;

    public EconomyManager(EconomyConfigManager economyConfigManager) {
        this.economyConfigManager = economyConfigManager;
        reloadModule();
    }

    public final void reloadModule() {
        clearMaps();
        loadPlayerAccounts();
    }

    public void saveData() {
        economyConfigManager.getDataConfig().saveData(playerAccounts);
    }

    public void createBalanceAccount(UUID target, String accountName, int startBalance) {
        if(validateArguments(target, accountName)) return;
        playerAccounts.put(target, new BalanceAccount(target, accountName, startBalance));
    }

    public void removeBalanceAccount(UUID target, String accountName) {
        if(validateArguments(target, accountName)) return;
        playerAccounts.remove(target, findAccountByName(target, accountName));
    }
    public BalanceAccount getBalanceAccount(UUID target, String accountName) {
        if(validateArguments(target, accountName)) return null;
        return findAccountByName(target, accountName);
    }
    public boolean balanceAccountExists(UUID target, String accountName){
        return findAccountByName(target,accountName) != null;
    }

    public int giveTotalMoney() {
        return playerAccounts.values().stream()
                .mapToInt(BalanceAccount::getBalance)
                .sum();
    }

    public Collection<BalanceAccount> getPlayerAccounts(UUID target) {
        return playerAccounts.get(target);
    }

    public void convertToItemMoney(ItemMoneyType itemMoneyType, int itemAmount, BalanceAccount balanceAccount, Player player){
        MessagesConfig messagesConfig = economyConfigManager.getMessagesConfig();
        if(validateArguments(itemMoneyType,balanceAccount) || itemAmount < 1){
            SimpleUtil.sendMessage(player,messagesConfig.getError_arguments());
            return;
        }

        ItemMoney itemMoney = economyConfigManager.getStandardConfig().getItemMoney(itemMoneyType);
        if(itemMoney == null){
            SimpleUtil.sendMessage(player,messagesConfig.getError_arguments());
            return;
        }

        int balance = balanceAccount.getBalance();
        int removeMoneyCount = itemAmount*itemMoney.getConvertToVirtualValue();

        if(balance < removeMoneyCount){
            SimpleUtil.sendMessage(player,messagesConfig.getConverting_to_real_error());
            return;
        }
        List<ItemStack> items = new ArrayList<>();
        ItemStack convertTo = itemMoney.getItemStack().clone();
        if(itemAmount <= convertTo.getMaxStackSize()) {
            convertTo.setAmount(itemAmount);
            items.add(convertTo);
            if(SimpleUtil.canFitInInventory(player,items)){
                balanceAccount.subtractMoney(removeMoneyCount);
                player.getInventory().addItem(convertTo);
                SimpleUtil.sendMessage(player,messagesConfig.getConverted_to_real(),"{MONEY_TYPE}",itemMoneyType.name());
            }
            return;
        }
        int fullStacks = itemAmount / convertTo.getMaxStackSize();
        int remainingItems = itemAmount % convertTo.getMaxStackSize();

        for (int i = 0; i < fullStacks; i++) {
            ItemStack fullStack = convertTo.clone();
            fullStack.setAmount(convertTo.getMaxStackSize());
            items.add(fullStack);
        }

        if (remainingItems > 0) {
            ItemStack partialStack = convertTo.clone();
            partialStack.setAmount(remainingItems);
            items.add(partialStack);
        }

        balanceAccount.subtractMoney(removeMoneyCount);
        if(SimpleUtil.canFitInInventory(player,items)){
            balanceAccount.subtractMoney(removeMoneyCount);
            items.forEach(itemStack -> player.getInventory().addItem(itemStack));
            SimpleUtil.sendMessage(player,messagesConfig.getConverted_to_real(),"{MONEY_TYPE}",itemMoneyType.name());
        }
    }
    public int convertToVirtual(ItemMoneyType itemMoneyType, int itemMoneyAmount, UUID uuid) {
        if (validateArguments(itemMoneyType, uuid) || itemMoneyAmount < 1) {
            return 0;
        }

        Player player = Bukkit.getPlayer(uuid);
        ItemMoney itemMoney = economyConfigManager.getStandardConfig().getItemMoney(itemMoneyType);

        if (itemMoney == null || player == null) {
            return 0;
        }

        ItemStack convertFrom = itemMoney.getItemStack().clone();
        int moneyInInventory = 0;

        for (ItemStack inventoryItem : player.getInventory().getContents()) {
            if (inventoryItem == null || !inventoryItem.isSimilar(convertFrom)) continue;
            moneyInInventory += inventoryItem.getAmount();
        }

        if (moneyInInventory < itemMoneyAmount) {
            return -1;
        }

        int remainingAmount = itemMoneyAmount;

        for (int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack inventoryItem = player.getInventory().getItem(i);

            if (inventoryItem == null || !inventoryItem.isSimilar(convertFrom)) continue;

            int stackAmount = inventoryItem.getAmount();

            if (stackAmount <= remainingAmount) {
                player.getInventory().setItem(i, null);
                remainingAmount -= stackAmount;
            } else {
                inventoryItem.setAmount(stackAmount - remainingAmount);
                player.getInventory().setItem(i, inventoryItem);
                remainingAmount = 0;
                break;
            }
        }

        return remainingAmount == 0 ? itemMoneyAmount * itemMoney.getConvertToVirtualValue() : 0;
    }

    private BalanceAccount findAccountByName(UUID target, String accountName) {
        if(validateArguments(target,accountName)) return null;
        return playerAccounts.get(target).stream()
                .filter(account -> account.getAccountName().equals(accountName))
                .findFirst()
                .orElse(null);
    }

    private void loadPlayerAccounts() {
        playerAccounts.putAll(economyConfigManager.getDataConfig().loadData());
    }

    private void clearMaps() {
        playerAccounts.clear();
    }

    private static boolean validateArguments(Object... args) {
        return Arrays.stream(args).anyMatch(Objects::isNull);
    }
}
