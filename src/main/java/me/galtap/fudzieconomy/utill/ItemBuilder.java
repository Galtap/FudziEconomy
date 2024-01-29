package me.galtap.fudzieconomy.utill;


import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ItemBuilder {
    private final Material material;
    private String displayName;
    private final List<String> lore = new ArrayList<>();
    private final Map<Enchantment,Integer> unsafeEnchantments =  new HashMap<>();

    public ItemBuilder(Material material){
        this.material = material;
    }


    public ItemBuilder setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        this.lore.addAll(lore);
        return this;
    }

    public ItemBuilder setUnsafeEnchantments(Map<Enchantment, Integer> enchantments) {
        this.unsafeEnchantments.putAll(enchantments);
        return this;
    }


    public ItemStack build() {
        if(material == null ) return null;
        ItemStack itemStack = new ItemStack(this.material, 1);
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return itemStack;
        }
        if (this.displayName != null) {
            meta.setDisplayName(SimpleUtil.getColorText(displayName));
        }
        List<String> colorLore = new ArrayList<>();
        lore.forEach(text -> colorLore.add(SimpleUtil.getColorText(text)));
        meta.setLore(SimpleUtil.getColorText(colorLore));


        itemStack.setItemMeta(meta);
        itemStack.addUnsafeEnchantments(unsafeEnchantments);
        return itemStack;
    }
}
