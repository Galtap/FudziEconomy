package me.galtap.fudzieconomy.config;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import me.galtap.fudzieconomy.FudziEconomy;
import me.galtap.fudzieconomy.core.BalanceAccount;
import me.galtap.fudzieconomy.utill.CustomFile;
import me.galtap.fudzieconomy.utill.Debug;
import me.galtap.fudzieconomy.utill.SimpleUtil;
import org.bukkit.configuration.ConfigurationSection;

import java.util.UUID;

public class DataConfig {
    private final FudziEconomy plugin = FudziEconomy.getInstance();

    public void saveData(Multimap<UUID, BalanceAccount> balanceAccounts) {
        CustomFile dataFile = new CustomFile("data.yml", plugin);
        dataFile.delete();
        dataFile = new CustomFile("data.yml", plugin);
        ConfigurationSection section = dataFile.getSection();

        balanceAccounts.entries().forEach(entry -> {
            UUID uuid = entry.getKey();
            BalanceAccount balanceAccount = entry.getValue();
            String path = uuid.toString() + "." + balanceAccount.getAccountName();
            section.set(path, balanceAccount.getBalance());
        });

        dataFile.saveAndReload();
    }

    public Multimap<UUID, BalanceAccount> loadData() {
        Multimap<UUID, BalanceAccount> balanceAccounts = ArrayListMultimap.create();
        CustomFile dataFile = new CustomFile("data.yml", plugin);
        ConfigurationSection section = dataFile.getSection();

        section.getKeys(false).forEach(uuidPath -> {
            try {
                UUID uuid = UUID.fromString(uuidPath);
                ConfigurationSection uuidSection = SimpleUtil.createSectionIfNotExists(section, uuidPath);

                uuidSection.getKeys(false).forEach(accountName -> {
                    int money = uuidSection.getInt(accountName);
                    BalanceAccount balanceAccount = new BalanceAccount(uuid, accountName, money);
                    balanceAccounts.put(balanceAccount.getOwner(), balanceAccount);
                });
            } catch (IllegalArgumentException e) {
                Debug.LOAD_UUID_ERROR.log(e, "data.yml", uuidPath);
            }
        });

        return balanceAccounts;
    }
}
