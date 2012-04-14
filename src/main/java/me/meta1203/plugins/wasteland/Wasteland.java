package me.meta1203.plugins.wasteland;

import java.io.File;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

public class Wasteland extends JavaPlugin implements Listener {
	static File folder = null;
	
    public void onDisable() {
        // TODO: Place any custom disable code here.
    }

    public void onEnable() {
        folder = this.getDataFolder();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage("Welcome, " + event.getPlayer().getDisplayName() + "!");
    }
    
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return new WasteLandChunkGen();
    }
}

