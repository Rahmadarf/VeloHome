package com.kumar.veloHome;

import com.kumar.veloHome.commands.DeleteCommand;
import com.kumar.veloHome.commands.HomesCommand;
import com.kumar.veloHome.commands.SetHomeCommand;
import com.kumar.veloHome.commands.HomeCommand;
import com.kumar.veloHome.listeners.TeleportListener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.UUID;

public final class VeloHome extends JavaPlugin {


    public static String getPath(UUID uuid, String homeName) {
        return "homes." + uuid + "." + homeName.toLowerCase();
    }

    public static final Component PREFIX = Component.text("▪ ", NamedTextColor.DARK_PURPLE)
            .append(Component.text("VeloHome", NamedTextColor.LIGHT_PURPLE))
            .append(Component.text(" » ", NamedTextColor.DARK_GRAY));

    private static VeloHome instance;

    private File messageFile;
    private FileConfiguration messageConfig;



    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        createMessageConfig();

        SetHomeCommand setHomeCmd = new SetHomeCommand(this);
        HomeCommand homeCmd = new HomeCommand(this);
        HomesCommand homesCmd = new HomesCommand(this);
        DeleteCommand deleteCmd = new DeleteCommand(this);

        // COMMAND /SETHOME
        getCommand("sethome").setExecutor(setHomeCmd);

        // COMMAND /HOME
        getCommand("home").setExecutor(homeCmd);
        getCommand("home").setTabCompleter(homeCmd);

        // COMMAND /HOMES
        getCommand("homes").setExecutor(homesCmd);

        // COMMAND /DELHOME
        getCommand("delhome").setExecutor(deleteCmd);

        // REGISTER LISTENER
        getServer().getPluginManager().registerEvents(new TeleportListener(homeCmd), this);

        getLogger().info("VeloHome Actived!");
    }

    public static VeloHome getInstance() {
        return instance;
    }


    // ===========================
    // CUSTOM FILE MANAGER
    // ===========================
    public FileConfiguration getMessageConfig() {
        return this.messageConfig;
    }

    private void createMessageConfig() {
        messageFile = new File(getDataFolder(), "velohome.yml");

        if (!messageFile.exists()) {
            messageFile.getParentFile().mkdirs();
            saveResource("velohome.yml", false);
        }

        messageConfig = YamlConfiguration.loadConfiguration(messageFile);
    }

    public void reloadMessageConfig() {
        if (messageFile == null) {
            messageFile = new File(getDataFolder(), "velohome.yml");
        }
        messageConfig = YamlConfiguration.loadConfiguration(messageFile);
    }
}