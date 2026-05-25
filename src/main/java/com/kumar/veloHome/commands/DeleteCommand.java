package com.kumar.veloHome.commands;

import com.kumar.veloHome.VeloHome;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DeleteCommand implements CommandExecutor, TabCompleter {

    private final VeloHome plugin;

    public DeleteCommand(VeloHome plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only player can use this command!", NamedTextColor.RED));
            return true;
        }

        UUID uuid = player.getUniqueId();
        FileConfiguration config = plugin.getConfig();

        if (command.getName().equalsIgnoreCase("delhome")) {

            if (args.length == 0) {
                player.sendMessage(VeloHome.PREFIX.append(Component.text("Usage: /delhome [name]", NamedTextColor.GRAY)));
                return true;
            }

            String homeName = args[0].toLowerCase();
            String path = plugin.getPath(uuid, homeName);

            if (!config.contains(path)) {
                player.sendMessage(VeloHome.PREFIX.append(Component.text("You don't have a home with that name!")));
                return true;
            }

            config.set(path, null);
            plugin.saveConfig();

            player.sendMessage(VeloHome.PREFIX.append(Component.text(homeName + " Deleted successfully!")));
        }


        return true;
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        List<String> suggestions = new ArrayList<>();
        FileConfiguration config = plugin.getConfig();

        if (args.length == 1 && sender instanceof Player player) {

            UUID uuid = player.getUniqueId();
            ConfigurationSection section = config.getConfigurationSection("homes." + uuid);

            if (section != null) {

                List<String> homeName = new ArrayList<>(section.getKeys(false));

                StringUtil.copyPartialMatches(args[0], homeName, suggestions);
            }
        }

        return suggestions;
    }
}
