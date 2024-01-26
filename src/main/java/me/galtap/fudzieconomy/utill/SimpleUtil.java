package me.galtap.fudzieconomy.utill;


import me.galtap.fudzieconomy.config.MessagesConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class SimpleUtil {
    private SimpleUtil(){
    }
    public static ConfigurationSection createSectionIfNotExists(ConfigurationSection main, String name) {
        return main.contains(name) ? main.getConfigurationSection(name) : main.createSection(name);
    }
    public static String getColorText(String text) {
        if (text == null) return null;
        text = ChatColor.translateAlternateColorCodes('&',text);
        return text;
    }

    public static List<String> getColorText(List<String> list) {
        return Optional.ofNullable(list).orElseGet(ArrayList::new).stream()
                .map(SimpleUtil::getColorText)
                .collect(Collectors.toList());
    }

    public static List<String> replacePlaceholders(String placeholder, List<String> text, String value) {
        return text.stream().map(s -> s.replace(placeholder, value)).collect(Collectors.toList());
    }
    public static Integer parseInt(String value, MessagesConfig messagesConfig, CommandSender sender) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            messagesConfig.getError_arguments().forEach(sender::sendMessage);
            return null;
        }
    }
    public static boolean notHasPermission(CommandSender sender, String permission, MessagesConfig messagesConfig){
        if(!sender.hasPermission(permission)){
            messagesConfig.getPermission_not_exists().forEach(sender::sendMessage);
            return true;
        }
        return false;
    }
    public static boolean canFitInInventory(Player player, List<ItemStack> items) {
        Inventory playerInventory = player.getInventory();
        Inventory tempInventory = Bukkit.createInventory(null, InventoryType.PLAYER,"copyInventory");
        tempInventory.setContents(playerInventory.getContents());
        for (ItemStack item : items) {
            HashMap<Integer, ItemStack> leftoverItems = tempInventory.addItem(item);
            if (!leftoverItems.isEmpty()) {
                return false;
            }
        }
        return true;
    }


}
