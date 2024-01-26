package me.galtap.fudzieconomy;

import me.galtap.fudzieconomy.command.FudziEconomyCMD;
import me.galtap.fudzieconomy.command.FudziMoneyCMD;
import me.galtap.fudzieconomy.config.ConfigManager;
import me.galtap.fudzieconomy.core.EconomyManager;
import me.galtap.fudzieconomy.listener.PluginListener;
import me.galtap.fudzieconomy.task.PluginTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class FudziEconomy extends JavaPlugin {
    private static FudziEconomy instance;
    private ConfigManager configManager;
    private EconomyManager economyManager;
    private PluginTask pluginTask;
    @Override
    public void onEnable() {
        instance = this;
        configManager = new ConfigManager(this);
        economyManager = new EconomyManager(configManager);
        pluginTask = new PluginTask(this,configManager,economyManager);
        pluginTask.autoSaveData(configManager.getStandardConfig().getAutoSaveTime(),economyManager);
        new FudziEconomyCMD("fudzieco",this,economyManager,configManager);
        new FudziMoneyCMD("fudzimoney",this,economyManager,configManager);
        Bukkit.getPluginManager().registerEvents(new PluginListener(economyManager,configManager),this);

    }

    @Override
    public void onDisable() {
        economyManager.saveData();
        Bukkit.getScheduler().cancelTasks(this);
    }

    public void reloadPlugin(){
        configManager.reloadModule();
        economyManager.reloadModule();
        pluginTask.reloadModule();
    }
    public static FudziEconomy getInstance(){
        return instance;
    }


    public EconomyManager getEconomyManager() {
        return economyManager;
    }
}
