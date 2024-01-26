package me.galtap.fudzieconomy.utill;


import me.galtap.fudzieconomy.FudziEconomy;

import java.util.logging.Level;
import java.util.logging.Logger;


public enum Debug {
    PATH_ERROR_DEFAULT(Level.SEVERE,"Обнаружена ошибка в %s. Неверное значение в пути: << %s >>. Исправьте ошибку. Изменение на базовое значение: %s"),
    PATH_ERROR(Level.SEVERE,"Обнаружена ошибка в %s. Недопустимое значение в пути: << %s >>"),
    ENUM_PATH_ERROR(Level.SEVERE,"Обнаружена ошибка в %s. Не существует такого элемента, как %s. Путь - << %s >> Пожалуйста, исправьте ошибку."),
    DERICTORY_CREATE_ERROR(Level.SEVERE,"Не удалось создать папку %s"),
    FILE_CREATE_ERROR(Level.SEVERE,"Не удалось создать файл %s"),
    FILE_SAVE_ERROR(Level.SEVERE,"Не удалось сохранить файл %s"),
    FILE_RELOAD_ERROR(Level.SEVERE,"Не удалось перезагрузить файл %s"),
    ENUM_PATH_ERROR_DEFAULT(Level.SEVERE,"Обнаружена ошибка в %s. Такого элемента как %s не существует. Путь - << %s >> Исправьте ошибку. Переход в первоначальное значение: %s"),
    LOAD_UUID_ERROR(Level.SEVERE,"Обнаружена ошибка в %s. Такого uuid игрока как %s не существует.");
    private static final Logger LOGGER = FudziEconomy.getInstance().getLogger();
    private final Level logLevel;
    private final String messageTemplate;
    Debug(Level logLevel, String messageTemplate) {
        this.logLevel = logLevel;
        this.messageTemplate = messageTemplate;
    }

    public void log(Object... args) {
        log(null, args);
    }

    public void log(Throwable thrown, Object... args) {
        String message = String.format(messageTemplate, args);
        if (thrown == null) {
            LOGGER.log(logLevel, message);
        } else {
            LOGGER.log(logLevel, message, thrown);
        }
    }
    public static void customLog(String message, Level logLevel){
        if(message == null || logLevel == null) return;
        LOGGER.log(logLevel,message);
    }

}
