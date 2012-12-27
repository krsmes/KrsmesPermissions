package net.krsmes.bukkit.plugins.permissions.command;

import net.krsmes.bukkit.plugins.permissions.Permissions;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author krsmes
 */
public class CmdShow implements Cmd {

    @Override
    public List<String> execute(List<String> parameters, Permissions permissions, Player player) {
        List<String> perms = new ArrayList<String>();
        for (PermissionAttachmentInfo pai : player.getEffectivePermissions()) {
            if (pai.getValue()) {
                perms.add(pai.getPermission());
            }
        }
        return Arrays.asList(perms.toString());
    }

    @Override
    public boolean allowed(CommandSender sender) {
        return true;
    }

    @Override
    public String getDescription() {
        return "- show effective permissions";
    }
}
