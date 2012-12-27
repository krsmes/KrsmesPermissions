package net.krsmes.bukkit.plugins.permissions.command

import net.krsmes.bukkit.plugins.permissions.BukkitFixtures
import net.krsmes.bukkit.plugins.permissions.BukkitUtil
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;


class PermissionsCommandTest {

    PermissionsCommand command = new PermissionsCommand();

    @Before void initialize() {
        BukkitUtil.initialize(BukkitFixtures.plugin)
    }

    @Test public void commandShouldNotBeNull() {
        assertNotNull(command);
    }

}
