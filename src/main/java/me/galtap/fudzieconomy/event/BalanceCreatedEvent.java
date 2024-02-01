package me.galtap.fudzieconomy.event;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BalanceCreatedEvent extends Event {

    private final String name;
    private final int startBalance;
    private final OfflinePlayer offlinePlayer;

    public BalanceCreatedEvent(String name, int startBalance, OfflinePlayer offlinePlayer){

        this.name = name;
        this.startBalance = startBalance;
        this.offlinePlayer = offlinePlayer;
    }
    private static final HandlerList handlers = new HandlerList();
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public String getName() {
        return name;
    }

    public int getStartBalance() {
        return startBalance;
    }

    public OfflinePlayer getOfflinePlayer() {
        return offlinePlayer;
    }
}
