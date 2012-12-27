package net.krsmes.bukkit.plugins.permissions;

import net.krsmes.bukkit.plugins.permissions.command.PermissionsCommand;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;


public class KrsmesPermissionsPlugin extends JavaPlugin implements Listener {

    private static final Logger LOG = Logger.getLogger("Minecraft");

    private Permissions permissions;

    public KrsmesPermissionsPlugin() {
        BukkitUtil.initialize(this);
    }

//
// JavaPlugin
//

    @Override
    public void onDisable() {
        LOGinfo("onDisable");
        save();
        unregister();
    }


    @Override
    public void onEnable() {
        LOGinfo("onEnable");
        load();
        register();
    }



//
// Bukkit events
//

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        LOGinfo("onPlayerLogin", player.getName());
        permissions.registerPlayer(player);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerKick(PlayerKickEvent event) {
        if (!event.isCancelled()) {
            Player player = event.getPlayer();
            LOGinfo("onPlayerKick", player.getName());
            permissions.unregisterPlayer(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        LOGinfo("onPlayerQuit", player.getName());
        permissions.unregisterPlayer(player);
    }


//
// public methods
//



//
// helper methods
//

    protected void load() {
        LOGinfo("load");
        if (permissions == null) {
            permissions = new Permissions(this);
        }
        permissions.load(getConfig());
    }

    protected void save() {
        LOGinfo("save");
        permissions.save(getConfig());
        saveConfig();
    }

    protected void register() {
        LOGinfo("register");
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("perm").setExecutor(new PermissionsCommand(permissions));
    }

    protected void unregister() {
        LOGinfo("unregister");
        HandlerList.unregisterAll((Plugin) this);
        permissions.unregisterOnlinePlayers();
        getCommand("perm").setExecutor(null);
    }

    protected void LOGinfo(String method) {
        LOG.info(getName() + "::" + method + "()");
    }

    protected void LOGinfo(String method, String message) {
        LOG.info(getName() + "::" + method + "() " + message);
    }


//
// static helpers
//

    static {
        ConfigurationSerialization.registerClass(PermissionDef.class);
    }


}
