package me.galtap.fudzieconomy.utill;

import org.bukkit.configuration.ConfigurationSection;

import java.util.Locale;

public class ErrorHandler {
    private final String configName;

    public ErrorHandler(String configName){
        this.configName = configName;
    }

    public Integer checkInt(ConfigurationSection section, Integer def, String path, int invalidValue){
        int value = section.getInt(path, invalidValue);
        return value == invalidValue ? logEndReturnDefault(def, section, path) : value;
    }

    public <T extends Enum<T>> T checkEnum(Class<T> enumClass, ConfigurationSection section, T def, String path){
        String enumName = section.getString(path);
        if(enumName == null) return logEndReturnDefault(def, section, path);
        try {
            return Enum.valueOf(enumClass, enumName.toUpperCase(Locale.ENGLISH));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return logEndReturnDefault(def, section, path, enumName);
        }
    }

    private  <V> V logEndReturnDefault(V def, ConfigurationSection section, String path){
        return logEndReturnDefault(def, section, path, null);
    }

    private  <V> V logEndReturnDefault(V def, ConfigurationSection section, String path, String enumName){
        String fullPath = section.getCurrentPath() + "." + path;
        if(enumName != null){
            if(def == null){
                Debug.ENUM_PATH_ERROR.log(configName,enumName,fullPath);
                return null;
            }
            Debug.ENUM_PATH_ERROR_DEFAULT.log(configName, enumName, fullPath, def);
            return def;
        }
        if(def == null){
            Debug.PATH_ERROR.log(configName, fullPath);
            return null;
        }
        Debug.PATH_ERROR_DEFAULT.log(configName, fullPath, def);
        return def;
    }
}
