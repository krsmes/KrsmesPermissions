package net.krsmes.bukkit.plugins.permissions.command;

import net.krsmes.bukkit.plugins.permissions.Permissions;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 * @author krsmes
 */
public class CmdHelp implements Cmd {

    @Override
    public List<String> execute(List<String> parameters, Permissions permissions, Player player) {
        return Arrays.asList("execute: " + parameters);
    }

    @Override
    public boolean allowed(CommandSender sender) {
        return true;
    }

    @Override
    public String getDescription() {
        return "- an unhelpful description";
    }
}
