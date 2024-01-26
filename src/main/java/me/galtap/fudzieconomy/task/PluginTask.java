package me.galtap.fudzieconomy.task;

import me.galtap.fudzieconomy.config.ConfigManager;
import me.galtap.fudzieconomy.core.EconomyManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


public class PluginTask {
    private final JavaPlugin plugin;
    private final ConfigManager configManager;
    private final EconomyManager economyManager;
    private int autoSaveTask;

    public PluginTask(JavaPlugin plugin, ConfigManager configManager, EconomyManager economyManager){

        this.plugin = plugin;
        this.configManager = configManager;
        this.economyManager = economyManager;
    }
    public void autoSaveData(int time, EconomyManager economyManager){
        autoSaveTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, economyManager::saveData,0,time*20L);
    }
    public void reloadModule(){
        Bukkit.getScheduler().cancelTask(autoSaveTask);
        autoSaveData(configManager.getStandardConfig().getAutoSaveTime(),economyManager);
    }
}
