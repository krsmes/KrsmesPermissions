package net.krsmes.bukkit.plugins.permissions;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author krsmes
 */
public class PermissionDef implements ConfigurationSerializable {

    private Map<String, Boolean> permissions = new HashMap<String, Boolean>();
    private List<String> groups = new ArrayList<String>();

    public PermissionDef() {
    }

    @SuppressWarnings("unchecked")
    public PermissionDef(Map<String, Object> values) {
        Object temp = values.get("permissions");
        if (temp instanceof Map) {
            permissions = (Map<String, Boolean>) temp;
        }
        temp = values.get("groups");
        if (temp instanceof List) {
            groups = (List<String>) temp;
        }
    }

    public Map<String, Boolean> getPermissions() {
        return new HashMap<String, Boolean>(permissions);
    }

    public void addPermission(String name, boolean value) {
        permissions.put(name, value);
    }

    public List<String> getGroups() {
        return new ArrayList<String>(groups);
    }

    public void addGroup(String name) {
        groups.add(name);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new HashMap<String, Object>();
        if (!permissions.isEmpty()) {
            result.put("permissions", permissions);
        }
        if (!groups.isEmpty()) {
            result.put("groups", groups);
        }
        return result;
    }

}
