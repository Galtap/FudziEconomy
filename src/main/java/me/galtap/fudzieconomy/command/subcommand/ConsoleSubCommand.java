package me.galtap.fudzieconomy.command.subcommand;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public interface ConsoleSubCommand extends SubCommand {
    void perform(@NotNull CommandSender sender, @NotNull String[] args);
}
