package com.kumar.veloHome.commands;

import com.kumar.veloHome.VeloHome;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
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

        var mm = MiniMessage.miniMessage();

        String onlyPlayer = plugin.getMessageConfig().getString("only-player", " ");
        String prefix = plugin.getMessageConfig().getString("prefix", " ");

        if (!(sender instanceof Player player)) {
            sender.sendMessage(onlyPlayer);
            return true;
        }

        UUID uuid = player.getUniqueId();
        FileConfiguration config = plugin.getConfig();

        // ================
        // COMMAND /sethome
        // ================
        if (command.getName().equalsIgnoreCase("sethome")) {

            String sethomeUsage = plugin.getMessageConfig().getString("sethome-usage", " ");

            // CEK APAKAH PLAYER HANYA MENGETIK /SETHOME (TANPA SPASI DLL)
            if (args.length == 0) {
                player.sendMessage(mm.deserialize(prefix + sethomeUsage));
                return true;
            }

            String homeName = args[0].toLowerCase();
            Location loc = player.getLocation();
            String path = plugin.getPath(uuid, homeName);

            String rawSaved = plugin.getMessageConfig().getString("home-saved", " ");
            String saved = rawSaved.replace("{home}", String.valueOf(homeName));

            String rawUpdated = plugin.getMessageConfig().getString("home-updated", " ");
            String updated = rawUpdated.replace("{home}", String.valueOf(homeName));

            boolean isUpdating = plugin.getConfig().contains(path);

            config.set(path + ".world", loc.getWorld().getName());
            config.set(path + ".x", loc.getX());
            config.set(path + ".y", loc.getY());
            config.set(path + ".z", loc.getZ());
            config.set(path + ".yaw", loc.getYaw());
            config.set(path + ".pitch", loc.getPitch());

            plugin.saveConfig();

            if (isUpdating) {
                player.sendMessage(mm.deserialize(prefix + updated));
            } else {
                player.sendMessage(mm.deserialize(prefix + saved));
            }
        }

        return true;
    }
}