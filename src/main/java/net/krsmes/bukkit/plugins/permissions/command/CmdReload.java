package net.krsmes.bukkit.plugins.permissions.command;

import net.krsmes.bukkit.plugins.permissions.BukkitUtil;
import net.krsmes.bukkit.plugins.permissions.Permissions;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 * @author krsmes
 */
public class CmdReload implements Cmd {

    @Override
    public List<String> execute(List<String> parameters, Permissions permissions, Player player) {
        permissions.load(BukkitUtil.instance.getConfig());
        return Arrays.asList("Permissions reloaded.");
    }

    @Override
    public boolean allowed(CommandSender sender) {
        return sender.isOp();
    }

    @Override
    public String getDescription() {
        return "- reload permissions configuration";
    }
}
