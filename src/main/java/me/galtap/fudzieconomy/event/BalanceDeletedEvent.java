package me.galtap.fudzieconomy.event;

import me.galtap.fudzieconomy.core.BalanceAccount;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BalanceDeletedEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final CommandSender sender;
    private final BalanceAccount balanceAccount;

    public BalanceDeletedEvent(CommandSender sender, BalanceAccount balanceAccount){

        this.sender = sender;
        this.balanceAccount = balanceAccount;
    }
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public CommandSender getSender() {
        return sender;
    }

    public BalanceAccount getBalanceAccount() {
        return balanceAccount;
    }
}
