package me.galtap.fudzieconomy.model;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class ItemMoney {
    private final ItemStack itemStack;
    private final int convertToVirtualValue;
    private final ItemMoneyType itemMoneyType;

    public ItemMoney(ItemStack itemStack, int convertToVirtualValue, ItemMoneyType itemMoneyType){
        this.itemStack = itemStack;

        this.convertToVirtualValue = convertToVirtualValue;
        this.itemMoneyType = itemMoneyType;
    }


    public int getConvertToVirtualValue() {
        return convertToVirtualValue;
    }


    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemMoney itemMoney = (ItemMoney) o;
        return itemMoneyType == itemMoney.itemMoneyType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemMoneyType);
    }
}
