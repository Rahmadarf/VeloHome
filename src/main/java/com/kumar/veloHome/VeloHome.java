package com.kumar.veloHome;

import com.kumar.veloHome.commands.DeleteCommand;
import com.kumar.veloHome.commands.HomesCommand;
import com.kumar.veloHome.commands.SetHomeCommand;
import com.kumar.veloHome.commands.HomeCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class VeloHome extends JavaPlugin {


    public static String getPath(UUID uuid, String homeName) {
        return "homes." + uuid + "." + homeName.toLowerCase();
    }

    public static final Component PREFIX = Component.text("▪ ", NamedTextColor.DARK_PURPLE)
            .append(Component.text("VeloHome", NamedTextColor.LIGHT_PURPLE))
            .append(Component.text(" » ", NamedTextColor.DARK_GRAY));

    private static VeloHome instance;


    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        SetHomeCommand setHomeCmd = new SetHomeCommand(this);
        HomeCommand homeCmd = new HomeCommand(this);
        HomesCommand homesCmd = new HomesCommand(this);
        DeleteCommand deleteCmd = new DeleteCommand(this);

        getCommand("sethome").setExecutor(setHomeCmd);

        getCommand("home").setExecutor(homeCmd);
        getCommand("home").setTabCompleter(homeCmd);

        getCommand("homes").setExecutor(homesCmd);

        getCommand("delhome").setExecutor(deleteCmd);

        getLogger().info("VeloHome Actived!");
    }

    public static VeloHome getInstance() {
        return instance;
    }
}