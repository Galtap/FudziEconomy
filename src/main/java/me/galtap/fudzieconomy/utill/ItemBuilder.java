package me.galtap.fudzieconomy.utill;


import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ItemBuilder {
    private Material material;
    private String displayName;
    private final List<String> lore = new ArrayList<>();
    private final Map<Enchantment,Integer> unsafeEnchantments =  new HashMap<>();


    public void setMaterial(Material material) {
        this.material = material;
    }


    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setLore(List<String> lore) {
        this.lore.addAll(lore);
    }

    public void setUnsafeEnchantments(Map<Enchantment, Integer> enchantments) {
        this.unsafeEnchantments.putAll(enchantments);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemBuilder that = (ItemBuilder) o;
        return material == that.material && Objects.equals(displayName, that.displayName) && Objects.equals(lore, that.lore) && Objects.equals(unsafeEnchantments, that.unsafeEnchantments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(material, displayName, lore, unsafeEnchantments);
    }
}
