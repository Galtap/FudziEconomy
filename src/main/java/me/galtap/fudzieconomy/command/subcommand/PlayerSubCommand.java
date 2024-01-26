package me.galtap.fudzieconomy.command.subcommand;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface PlayerSubCommand extends SubCommand {
    void perform(@NotNull Player player, @NotNull String[] args);
}
