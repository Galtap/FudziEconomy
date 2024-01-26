package me.galtap.fudzieconomy.command.subcommand;

public interface SubCommand {
    SubCommandType getSubCommandType();
    String getName();
}
