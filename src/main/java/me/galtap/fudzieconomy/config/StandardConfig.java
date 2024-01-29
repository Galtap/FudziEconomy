package me.galtap.fudzieconomy.config;

import me.galtap.fudzieconomy.model.ItemMoney;
import me.galtap.fudzieconomy.model.ItemMoneyType;
import me.galtap.fudzieconomy.utill.Debug;
import me.galtap.fudzieconomy.utill.ErrorHandler;
import me.galtap.fudzieconomy.utill.ItemBuilder;
import me.galtap.fudzieconomy.utill.SimpleUtil;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.logging.Level;

public class StandardConfig {
    private final ErrorHandler errorHandler;
    private int autoSaveTime;
    private final int startBalance;
    private final String firstBalanceName;
    private final int maxPay;
    private final int minPay;
    private final Map<ItemMoneyType, ItemMoney> moneyTypes;

    private static final String MATERIAL_PATH = "material";
    private static final String NAME_PATH = "name";
    private static final String LORE_PATH = "lore";
    private static final String ENCHANTMENT_PATH = "enchantment";

    public StandardConfig(JavaPlugin plugin) {
        ConfigurationSection section = plugin.getConfig();
        errorHandler = new ErrorHandler("config.yml");

        autoSaveTime = errorHandler.checkInt(section, 60, "Auto-save", -1);
        startBalance = section.getInt("Start-balance",0);
        firstBalanceName = SimpleUtil.getColorText(section.getString("First-balance-name","{PLAYER_NAME}"));
        maxPay = errorHandler.checkInt(section,10_000_000,"Max-pay",-1);
        minPay = errorHandler.checkInt(section,1,"Min-pay",-1);
        if (autoSaveTime < 1) autoSaveTime = 60;
        moneyTypes = new EnumMap<>(ItemMoneyType.class);

        for (ItemMoneyType itemMoneyType : ItemMoneyType.values()) {
            ItemMoney itemMoney = loadMoneyInfo(section, itemMoneyType);
            moneyTypes.put(itemMoneyType, itemMoney);
        }
    }

    private ItemMoney loadMoneyInfo(ConfigurationSection section, ItemMoneyType itemMoneyType) {
        ConfigurationSection typeSection = SimpleUtil.createSectionIfNotExists(section, "Item-money."+itemMoneyType.name());
        ItemStack itemStack = loadMoneyMeta(typeSection);
        int convertTo = errorHandler.checkInt(typeSection, 1, "convert-to", -1);
        return new ItemMoney(itemStack, convertTo, itemMoneyType);
    }

    private ItemStack loadMoneyMeta(ConfigurationSection typeSection) {
        Material material = errorHandler.checkEnum(Material.class, typeSection, Material.BARRIER, MATERIAL_PATH);
        String name = typeSection.getString(NAME_PATH);
        List<String> lore = new ArrayList<>(typeSection.getStringList(LORE_PATH));
        Map<Enchantment, Integer> enchantments = new HashMap<>();
        ConfigurationSection enchantmentSection = SimpleUtil.createSectionIfNotExists(typeSection, ENCHANTMENT_PATH);

        for (String typeName : enchantmentSection.getKeys(false)) {
            Enchantment enchantment = Enchantment.getByName(typeName.toUpperCase(Locale.ENGLISH));

            if (enchantment == null) {
                Debug.ENUM_PATH_ERROR.log("config.yml", typeName, enchantmentSection.getCurrentPath());
                continue;
            }

            int level = errorHandler.checkInt(enchantmentSection, 0, typeName, -1);
            if (level < 1) {
                Debug.customLog("Invalid enchantment level in config.yml. Path: " + enchantmentSection.getCurrentPath(), Level.SEVERE);
                continue;
            }

            enchantments.put(enchantment, level);
        }

        return new ItemBuilder(material)
                .setDisplayName(name)
                .setLore(lore)
                .setUnsafeEnchantments(enchantments)
                .build();
    }


    public ItemMoney getItemMoney(ItemMoneyType itemMoneyType) {
        return moneyTypes.get(itemMoneyType);
    }

    public int getAutoSaveTime() {
        return autoSaveTime;
    }

    public int getStartBalance() {
        return startBalance;
    }

    public int getMaxPay() {
        return maxPay;
    }

    public int getMinPay() {
        return minPay;
    }

    public String getFirstBalanceName() {
        return firstBalanceName;
    }
}
