package com.kumar.veloHome.commands;

import com.kumar.veloHome.VeloHome;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class SetHomeCommand implements CommandExecutor {

    private final VeloHome plugin;

    public SetHomeCommand(VeloHome plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only players can use this command!", NamedTextColor.RED));
            return true;
        }

        UUID uuid = player.getUniqueId();
        FileConfiguration config = plugin.getConfig();

        // ================
        // COMMAND /sethome
        // ================
        if (command.getName().equalsIgnoreCase("sethome")) {

            // CEK APAKAH PLAYER HANYA MENGETIK /SETHOME (TANPA SPASI DLL)
            if (args.length == 0) {
                player.sendMessage(VeloHome.PREFIX.append(Component.text("Usage: /sethome [name]", NamedTextColor.GRAY)));
                return true;
            }

            String homeName = args[0].toLowerCase();
            Location loc = player.getLocation();
            String path = plugin.getPath(uuid, homeName);

            var homeSection = config.createSection(path);

            homeSection.set("world", loc.getWorld().getName());
            homeSection.set("x", loc.getX());
            homeSection.set("y", loc.getY());
            homeSection.set("z", loc.getZ());
            homeSection.set("yaw", loc.getYaw());
            homeSection.set("pitch", loc.getPitch());

            plugin.saveConfig();

            player.sendMessage(VeloHome.PREFIX.append(Component.text(homeName, NamedTextColor.YELLOW))
                    .append(Component.text(" saved successfully!", NamedTextColor.GREEN)));
        }

        return true;
    }
}