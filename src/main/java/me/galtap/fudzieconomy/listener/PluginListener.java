package me.galtap.fudzieconomy.listener;

import me.galtap.fudzieconomy.config.ConfigManager;
import me.galtap.fudzieconomy.config.StandardConfig;
import me.galtap.fudzieconomy.core.EconomyManager;
import me.galtap.fudzieconomy.model.BalanceAccount;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PluginListener implements Listener {
    private final EconomyManager economyManager;
    private final ConfigManager configManager;

    public PluginListener(EconomyManager economyManager,ConfigManager configManager){

        this.economyManager = economyManager;
        this.configManager = configManager;
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        StandardConfig standardConfig = configManager.getStandardConfig();
        String firstBalanceName = standardConfig.getFirstBalanceName();
        firstBalanceName = firstBalanceName.replace("{PLAYER_NAME}",player.getName());
        BalanceAccount balanceAccount = economyManager.getBalanceAccount(player.getUniqueId(),firstBalanceName);
        if(balanceAccount == null){
            economyManager.createBalanceAccount(player.getUniqueId(),firstBalanceName,configManager.getStandardConfig().getStartBalance());
        }
    }
}
