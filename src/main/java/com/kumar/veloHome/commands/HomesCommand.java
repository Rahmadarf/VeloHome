package com.kumar.veloHome.commands;

import com.kumar.veloHome.VeloHome;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;

public class HomesCommand implements CommandExecutor {

    private final VeloHome plugin;

    public HomesCommand(VeloHome plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        var mm = MiniMessage.miniMessage();

        String prefix = plugin.getMessageConfig().getString("prefix", " ");
        String onlyPlayer = plugin.getMessageConfig().getString("only-player", " ");
        String noHome = plugin.getMessageConfig().getString("no-home", " ");

        if (!(sender instanceof Player player)) {
            sender.sendMessage(mm.deserialize(onlyPlayer));
            return true;
        }

        UUID uuid = player.getUniqueId();

        FileConfiguration config = plugin.getConfig();

        if (command.getName().equalsIgnoreCase("homes")) {

            ConfigurationSection section = config.getConfigurationSection("homes." + uuid);

            if (section == null || section.getKeys(false).isEmpty()) {
                player.sendMessage(mm.deserialize(prefix + noHome));
                return true;
            }

            Set<String> homeNames = section.getKeys(false);

            String homeList = String.join(", ", homeNames);

            String homes = plugin.getMessageConfig().getString("home-list", " ");
            homes = homes.replace("{homeList}", String.valueOf(homeList));

            player.sendMessage(" ");
            player.sendMessage(mm.deserialize(prefix + homes));
            player.sendMessage(" ");
        }

        return true;

    }
}
