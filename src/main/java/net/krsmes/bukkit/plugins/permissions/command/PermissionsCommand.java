package net.krsmes.bukkit.plugins.permissions.command;


import net.krsmes.bukkit.plugins.permissions.BukkitUtil;
import net.krsmes.bukkit.plugins.permissions.Permissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;


public class PermissionsCommand implements CommandExecutor {

    static Map<String, Cmd> commands = new LinkedHashMap<String, Cmd>(1);
    static {
        commands.put("help", new CmdHelp());
        commands.put("show", new CmdShow());
        commands.put("reload", new CmdReload());
    }

    private Permissions permissions;

    public PermissionsCommand(Permissions permissions) {
        this.permissions = permissions;
    }



//
// CommandExecutor
//

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        List<String> parameters = new ArrayList<String>(Arrays.asList(args));

        if (parameters.isEmpty()) {
            for (Map.Entry<String, Cmd> entry: commands.entrySet()) {
                if (entry.getValue().allowed(sender)) {
                    sender.sendMessage(entry.getKey() + " " + entry.getValue().getDescription());
                }
            }
        }
        else {
            String cmdName = parameters.remove(0);
            Cmd cmd = commands.get(cmdName);

            Player player = sender instanceof Player ? (Player) sender : null;

            if (cmd == null) {
                sender.sendMessage(BukkitUtil.chatColor + "Unknown command: " + cmdName);
                sender.sendMessage(BukkitUtil.chatColor + "  try: " + commands.keySet());
            }
            else if (!cmd.allowed(sender)) {
                sender.sendMessage(BukkitUtil.chatColor + "You are not allowed to perform command: " + cmdName);
            }
            else {
                try {
                    List<String> result = cmd.execute(parameters, permissions, player);
                    for (String msg : result) {
                        sender.sendMessage(BukkitUtil.chatColor + msg);
                    }
                }
                catch (Throwable t) {
                    sender.sendMessage("ERROR: (" + t.getClass().getSimpleName() + ") " + t.getMessage());
                    return false;
                }
            }
        }
        return true;
    }


//
// helper methods
//

}
