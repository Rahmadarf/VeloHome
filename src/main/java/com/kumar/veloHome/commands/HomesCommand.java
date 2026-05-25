package com.kumar.veloHome.commands;

import com.kumar.veloHome.VeloHome;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only players can use this command!"));
            return true;
        }

        UUID uuid = player.getUniqueId();

        FileConfiguration config = plugin.getConfig();

        if (command.getName().equalsIgnoreCase("homes")) {

            ConfigurationSection section = config.getConfigurationSection("homes." + uuid);

            if (section == null || section.getKeys(false).isEmpty()) {
                player.sendMessage(VeloHome.PREFIX.append(Component.text("You don't have a home yet!")));
                return true;
            }

            Set<String> homeNames = section.getKeys(false);

            String homeList = String.join(", ", homeNames);

            player.sendMessage(" ");
            player.sendMessage(VeloHome.PREFIX.append(Component.text("Your homes:")));
            player.sendMessage(Component.text("» ", NamedTextColor.DARK_GRAY).append(Component.text(homeList, NamedTextColor.GOLD)));
            player.sendMessage(" ");
        }

        return true;

    }
}
