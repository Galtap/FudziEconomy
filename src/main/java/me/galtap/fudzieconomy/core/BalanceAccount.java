package me.galtap.fudzieconomy.core;

import java.util.Objects;
import java.util.UUID;

public class BalanceAccount {
    private final UUID owner;
    private final String accountName;
    private int money;

    public BalanceAccount(UUID owner, String accountName, int money) {
        this.owner = owner;
        this.accountName = accountName;
        this.money = money;
    }

    public UUID getOwner() {
        return owner;
    }

    public String getAccountName() {
        return accountName;
    }

    public int getBalance() {
        return money;
    }

    public void addMoney(int amount) {
        checkPositiveAmount(amount);
        this.money += amount;
    }

    public void subtractMoney(int amount) {
        checkPositiveAmount(amount);
        this.money -= amount;
    }


    public void setMoney(int money) {
        this.money = money;
    }

    private static void checkPositiveAmount(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Количество денег не может быть негативным.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BalanceAccount that = (BalanceAccount) o;
        return Objects.equals(owner, that.owner) && Objects.equals(accountName, that.accountName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner, accountName);
    }
}
