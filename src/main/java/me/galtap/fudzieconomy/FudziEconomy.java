package me.galtap.fudzieconomy;

import me.galtap.fudzieconomy.command.FudziEconomyCMD;
import me.galtap.fudzieconomy.command.FudziMoneyCMD;
import me.galtap.fudzieconomy.config.EconomyConfigManager;
import me.galtap.fudzieconomy.core.EconomyManager;
import me.galtap.fudzieconomy.listener.PluginListener;
import me.galtap.fudzieconomy.task.PluginTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class FudziEconomy extends JavaPlugin {
    private static FudziEconomy instance;
    private EconomyConfigManager economyConfigManager;
    private EconomyManager economyManager;
    private PluginTask pluginTask;
    @Override
    public void onEnable() {
        instance = this;
        economyConfigManager = new EconomyConfigManager(this);
        economyManager = new EconomyManager(economyConfigManager);
        pluginTask = new PluginTask(this, economyConfigManager,economyManager);
        pluginTask.autoSaveData(economyConfigManager.getStandardConfig().getAutoSaveTime(),economyManager);
        new FudziEconomyCMD("fudzieco",this,economyManager, economyConfigManager);
        new FudziMoneyCMD("fudzimoney",this,economyManager, economyConfigManager);
        Bukkit.getPluginManager().registerEvents(new PluginListener(economyManager, economyConfigManager),this);

    }

    @Override
    public void onDisable() {
        economyManager.saveData();
        Bukkit.getScheduler().cancelTasks(this);
    }

    public void reloadPlugin(){
        economyConfigManager.reloadModule();
        economyManager.reloadModule();
        pluginTask.reloadModule();
    }
    public static FudziEconomy getInstance(){
        return instance;
    }


    public EconomyManager getEconomyManager() {
        return economyManager;
    }

    public EconomyConfigManager getEconomyConfigManager() {
        return economyConfigManager;
    }
}
