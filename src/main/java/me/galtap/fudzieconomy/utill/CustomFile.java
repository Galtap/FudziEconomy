package me.galtap.fudzieconomy.utill;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class CustomFile {
    private final FileConfiguration section;
    private final File file;

    public CustomFile(String name, JavaPlugin plugin) {
        File dataFolder = plugin.getDataFolder();

        if (!dataFolder.exists()) {
            boolean created = dataFolder.mkdirs();
            if (!created) {
                Debug.DERICTORY_CREATE_ERROR.log(name);
            }
        }

        file = new File(dataFolder, name);

        if (!file.exists()) {
            try {
                boolean created = file.createNewFile();
                if (!created) {
                    Debug.FILE_CREATE_ERROR.log(name);
                }
            } catch (IOException e) {
                Debug.FILE_CREATE_ERROR.log(e,name);
            }
        }

        section = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getSection() {
        return section;
    }

    public void save() {
        try {
            section.save(file);
        } catch (IOException e) {
            Debug.FILE_SAVE_ERROR.log(e,file.getName());
        }
    }

    public void reload() {
        try {
            section.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            Debug.FILE_RELOAD_ERROR.log(e,file.getName());
        }
    }
    public void saveAndReload(){
        save();
        reload();
    }
    public void delete(){
        if(file.exists()){
            file.delete();
        }
    }

}


