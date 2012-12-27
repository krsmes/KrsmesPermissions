package net.krsmes.bukkit.plugins.permissions;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author krsmes
 */
public class Permissions {

    private static final Logger LOG = Logger.getLogger("Minecraft");

    private static String GROUP_DEFAULT = "default";
    private static String ATTR_GROUPS = "groups";
    private static String ATTR_USERS = "users";
    private static Map<String, Boolean> EMPTY_PERMISSIONS = Collections.emptyMap();

    private Map<String, PermissionAttachment> playerPermissionMap = new HashMap<String, PermissionAttachment>();

    private Map<String, PermissionDef> groupPermissions = new HashMap<String, PermissionDef>();
    private Map<String, PermissionDef> userPermissions = new HashMap<String, PermissionDef>();

    private Plugin plugin;

    public Permissions(Plugin plugin) {
        this.plugin = plugin;
    }



//
// public api
//

    public void load(ConfigurationSection config) {
        unregisterOnlinePlayers();
        loadPermissions(config.getConfigurationSection(ATTR_GROUPS), groupPermissions);
        loadPermissions(config.getConfigurationSection(ATTR_USERS), userPermissions);

        if (groupPermissions.isEmpty()) {
            PermissionDef def = new PermissionDef();
            def.addPermission("testPermission", true);
            def.addPermission("testFalsePermission", false);
            groupPermissions.put(GROUP_DEFAULT, def);
        }
        if (userPermissions.isEmpty()) {
            PermissionDef def = new PermissionDef();
            def.addPermission("*", true);
            def.addGroup(GROUP_DEFAULT);
            userPermissions.put("krsmes", def);
        }
        registerOnlinePlayers();
    }



    public void save(ConfigurationSection config) {
        config.set(ATTR_GROUPS, groupPermissions);
        config.set(ATTR_USERS, userPermissions);
    }


    public void registerPlayer(Player player) {
        if (playerPermissionMap.containsKey(player.getName())) {
            debug("Registering " + player.getName() + ": was already registered");
            unregisterPlayer(player);
        }
        playerPermissionMap.put(player.getName(), player.addAttachment(plugin));
        updatePlayer(player);
    }


    public void unregisterPlayer(Player player) {
        if (playerPermissionMap.containsKey(player.getName())) {
            try {
                player.removeAttachment(playerPermissionMap.get(player.getName()));
            }
            catch (IllegalArgumentException ex) {
                debug("Unregistering " + player.getName() + ": player did not have attachment");
            }
            playerPermissionMap.remove(player.getName());
        } else {
            debug("Unregistering " + player.getName() + ": was not registered");
        }

    }


    public void registerOnlinePlayers() {
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            registerPlayer(p);
        }
    }

    public void unregisterOnlinePlayers() {
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            unregisterPlayer(p);
        }
    }

    public void updatePlayer(Player player) {
        if (player != null) {
            PermissionAttachment attachment = playerPermissionMap.get(player.getName());
            if (attachment == null) {
                debug("Calculating permissions on " + player.getName() + ": attachment was null");
                return;
            }

            for (String key : attachment.getPermissions().keySet()) {
                attachment.unsetPermission(key);
            }

            for (Map.Entry<String, Boolean> entry : calculatePlayerPermissions(player.getName().toLowerCase()).entrySet()) {
                attachment.setPermission(entry.getKey(), entry.getValue());
            }

            player.recalculatePermissions();
        }
    }


//
// helper methods
//

    protected void loadPermissions(ConfigurationSection config, Map<String, PermissionDef> permissionDefMap) {
        permissionDefMap.clear();
        if (config != null) {
            Map<String, Object> data = config.getValues(true);

            for (Map.Entry<String, Object> entry : data.entrySet()) {
                Object value = entry.getValue();
                if (value instanceof PermissionDef) {
                    permissionDefMap.put(entry.getKey(), (PermissionDef) value);
                }
            }
        }
    }


    protected Map<String, Boolean> calculatePlayerPermissions(String name) {
        Map<String, Boolean> result;

        if (userPermissions.containsKey(name)) {
            result = calculatePermissions(userPermissions.get(name));
        }
        else {
            result = calculateGroupPermissions(GROUP_DEFAULT);
        }

        return result;
    }


    protected Map<String, Boolean> calculateGroupPermissions(String name) {
        if (groupPermissions.containsKey(name)) {
            return calculatePermissions(groupPermissions.get(name));
        }
        return EMPTY_PERMISSIONS;
    }


    protected Map<String, Boolean> calculatePermissions(PermissionDef permissionDef) {
        Map<String, Boolean> result = permissionDef.getPermissions();
        // merge group permissions
        for (String groupName : permissionDef.getGroups()) {
            for (Map.Entry<String, Boolean> entry : calculateGroupPermissions(groupName).entrySet()) {
                // User overrides group
                if (!result.containsKey(entry.getKey())) {
                    result.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return result;
    }


    protected void debug(String msg) {
        LOG.info("Permissions:: " + msg);
    }

}
