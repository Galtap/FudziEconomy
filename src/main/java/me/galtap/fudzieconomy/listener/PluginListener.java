package me.galtap.fudzieconomy.listener;

import me.galtap.fudzieconomy.config.EconomyConfigManager;
import me.galtap.fudzieconomy.config.StandardConfig;
import me.galtap.fudzieconomy.core.EconomyManager;
import me.galtap.fudzieconomy.core.BalanceAccount;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PluginListener implements Listener {
    private final EconomyManager economyManager;
    private final EconomyConfigManager economyConfigManager;

    public PluginListener(EconomyManager economyManager, EconomyConfigManager economyConfigManager){

        this.economyManager = economyManager;
        this.economyConfigManager = economyConfigManager;
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        StandardConfig standardConfig = economyConfigManager.getStandardConfig();
        String firstBalanceName = standardConfig.getFirstBalanceName();
        BalanceAccount balanceAccount = economyManager.getBalanceAccount(player.getUniqueId(),firstBalanceName.replace("{PLAYER_NAME}",player.getName()));
        if(balanceAccount == null){
            economyManager.createBalanceAccount(player.getUniqueId(),firstBalanceName.replace("{PLAYER_NAME}",player.getName()), economyConfigManager.getStandardConfig().getStartBalance());
        }
    }
}
