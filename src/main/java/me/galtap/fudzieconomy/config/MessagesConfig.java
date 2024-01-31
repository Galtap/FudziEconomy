package me.galtap.fudzieconomy.config;

import me.galtap.fudzieconomy.utill.Debug;
import me.galtap.fudzieconomy.utill.DefaultConfig;
import me.galtap.fudzieconomy.utill.SimpleUtil;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class MessagesConfig {
    private final static String ADMIN_MESSAGES_KEY = "Admin";
    private final List<String> fudziecoHelpCommand = new ArrayList<>();
    private final List<String> fudzimoney_help = new ArrayList<>();
    private final List<String> error_arguments = new ArrayList<>();
    private final List<String> total_money = new ArrayList<>();
    private final List<String> player_not_online = new ArrayList<>();
    private final List<String> balance_exists = new ArrayList<>();
    private final List<String> balance_created = new ArrayList<>();
    private final List<String> balance_not_exists = new ArrayList<>();
    private final List<String> balance_deleted = new ArrayList<>();
    private final List<String> money_pay = new ArrayList<>();
    private final List<String> money_removed = new ArrayList<>();
    private final List<String> money_set = new ArrayList<>();
    private final List<String> money_pay_limit = new ArrayList<>();
    private final List<String> permission_not_exists = new ArrayList<>();
    private final List<String> empty_accounts = new ArrayList<>();
    private final List<String> small_money = new ArrayList<>();
    private final List<String> converting_to_virtual_error = new ArrayList<>();
    private final List<String> converted_to_virtual = new ArrayList<>();
    private final List<String> converting_to_real_error = new ArrayList<>();
    private final List<String> converted_to_real = new ArrayList<>();
    private final List<String> player_not_exists = new ArrayList<>();


    public MessagesConfig(DefaultConfig config){
        ConfigurationSection section = config.getConfig();
        fudzimoney_help.addAll(getMessage(section,"fudzimoney-help",null));
        fudziecoHelpCommand.addAll(getMessage(section,"fudzieco-help",null));
        error_arguments.addAll(getMessage(section,"error_arguments",null));
        player_not_online.addAll(getMessage(section,"player_not_online",null));
        balance_not_exists.addAll(getMessage(section,"balance_not_exists",null));
        money_pay.addAll(getMessage(section,"money_pay",null));
        money_pay_limit.addAll(getMessage(section,"money_pay_limit",null));
        permission_not_exists.addAll(getMessage(section,"permission_not_exists",null));
        empty_accounts.addAll(getMessage(section,"empty_accounts",null));
        small_money.addAll(getMessage(section,"small_money",null));
        converting_to_virtual_error.addAll(getMessage(section,"converting_to_virtual_error",null));
        converted_to_virtual.addAll(getMessage(section,"converted_to_virtual",null));
        converting_to_real_error.addAll(getMessage(section,"converting_to_real_error",null));
        converted_to_real.addAll(getMessage(section,"converted_to_real",null));


        total_money.addAll(getMessage(section,"total_money",ADMIN_MESSAGES_KEY));
        balance_exists.addAll(getMessage(section,"balance_exists",ADMIN_MESSAGES_KEY));
        balance_created.addAll(getMessage(section,"balance_created",ADMIN_MESSAGES_KEY));
        balance_deleted.addAll(getMessage(section,"balance_deleted",ADMIN_MESSAGES_KEY));
        money_removed.addAll(getMessage(section,"money_removed",ADMIN_MESSAGES_KEY));
        money_set.addAll(getMessage(section,"money_set",ADMIN_MESSAGES_KEY));
        player_not_exists.addAll(getMessage(section,"player_not_exists",ADMIN_MESSAGES_KEY));
    }
    private static List<String> getMessage(ConfigurationSection section, String path,String key){
        String newPath;
        if(key != null){
            newPath = key+"."+path;
        } else {
            newPath = path;
        }
        List<String> colorText = SimpleUtil.getColorText(section.getStringList(newPath));
        if(colorText.isEmpty()){
            Debug.customLog("Предупреждение: секция << "+section.getCurrentPath()+"."+path+" >> пуста. Файл: messages.yml", Level.SEVERE);
        }
        return colorText;
    }
    public List<String> getFudziecoHelpCommand() {
        return fudziecoHelpCommand;
    }

    public List<String> getFudzimoney_help() {
        return fudzimoney_help;
    }

    public List<String> getError_arguments() {
        return error_arguments;
    }

    public List<String> getTotal_money() {
        return total_money;
    }

    public List<String> getPlayer_not_online() {
        return player_not_online;
    }

    public List<String> getBalance_exists() {
        return balance_exists;
    }

    public List<String> getBalance_created() {
        return balance_created;
    }

    public List<String> getBalance_not_exists() {
        return balance_not_exists;
    }

    public List<String> getBalance_deleted() {
        return balance_deleted;
    }

    public List<String> getMoney_pay() {
        return money_pay;
    }

    public List<String> getMoney_removed() {
        return money_removed;
    }

    public List<String> getMoney_set() {
        return money_set;
    }

    public List<String> getMoney_pay_limit() {
        return money_pay_limit;
    }

    public List<String> getPermission_not_exists() {
        return permission_not_exists;
    }

    public List<String> getEmpty_accounts() {
        return empty_accounts;
    }

    public List<String> getSmall_money() {
        return small_money;
    }

    public List<String> getConverting_to_virtual_error() {
        return converting_to_virtual_error;
    }

    public List<String> getConverted_to_virtual() {
        return converted_to_virtual;
    }

    public List<String> getConverting_to_real_error() {
        return converting_to_real_error;
    }

    public List<String> getConverted_to_real() {
        return converted_to_real;
    }

    public List<String> getPlayer_not_exists() {
        return player_not_exists;
    }
}
