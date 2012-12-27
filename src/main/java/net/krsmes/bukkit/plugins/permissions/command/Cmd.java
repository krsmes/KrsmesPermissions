package net.krsmes.bukkit.plugins.permissions.command;

import net.krsmes.bukkit.plugins.permissions.Permissions;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;


/**
 * @author krsmes
 */
public interface Cmd {

    List<String> execute(List<String> parameters, Permissions permissions, Player player);

    boolean allowed(CommandSender sender);

    String getDescription();

}
