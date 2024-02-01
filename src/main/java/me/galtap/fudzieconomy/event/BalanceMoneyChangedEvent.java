package me.galtap.fudzieconomy.event;

import me.galtap.fudzieconomy.core.BalanceAccount;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BalanceMoneyChangedEvent extends Event {
    private final BalanceAccount balanceAccount;

    public BalanceMoneyChangedEvent(BalanceAccount balanceAccount){

        this.balanceAccount = balanceAccount;
    }
    private static final HandlerList handlers = new HandlerList();
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public BalanceAccount getBalanceAccount() {
        return balanceAccount;
    }
}
