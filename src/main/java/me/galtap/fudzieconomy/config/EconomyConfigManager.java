package me.galtap.fudzieconomy.config;

import me.galtap.fudzieconomy.FudziEconomy;
import me.galtap.fudzieconomy.utill.DefaultConfig;

public class EconomyConfigManager {
    private final FudziEconomy plugin;
    private final DefaultConfig messageFile;

    private DataConfig dataConfig;
    private StandardConfig standardConfig;
    private MessagesConfig messagesConfig;

    public EconomyConfigManager(FudziEconomy plugin){
        this.plugin = plugin;

        plugin.saveDefaultConfig();
        messageFile = new DefaultConfig(plugin,"messages.yml");
        messageFile.saveConfig();

        createConfigClasses();

    }
    private void createConfigClasses(){
        standardConfig = new StandardConfig(plugin);
        messagesConfig = new MessagesConfig(messageFile);
        dataConfig = new DataConfig();
    }
    public void reloadModule(){
        plugin.reloadConfig();
        messageFile.reloadConfig();
        createConfigClasses();
    }

    public DataConfig getDataConfig() {
        return dataConfig;
    }

    public StandardConfig getStandardConfig() {
        return standardConfig;
    }

    public MessagesConfig getMessagesConfig() {
        return messagesConfig;
    }
}
