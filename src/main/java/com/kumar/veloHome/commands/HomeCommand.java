package com.kumar.veloHome.commands;

import com.kumar.veloHome.VeloHome;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.bukkit.Sound;

import java.util.*;


public class HomeCommand implements CommandExecutor, TabCompleter {

    private final VeloHome plugin;

    private final Map<UUID, BukkitTask> pendingTp = new HashMap<>();

    public Map<UUID, BukkitTask> getPendingTP() {
        return this.pendingTp;
    }

    public HomeCommand(VeloHome plugin) {
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

        if (command.getName().equalsIgnoreCase("home")) {

            // CEK APAKAH PLAYER HANYA MENGETIK /HOME (TANPA SPASI DLL)
            if (args.length == 0) {
                player.sendMessage(VeloHome.PREFIX.append(Component.text("Usage: /home [name]", NamedTextColor.GRAY)));
                return true;
            }

            String homeName = args[0].toLowerCase();

            String path = plugin.getPath(uuid, homeName);

            // CEK APAKAH USER MEMILIKI HOME YANG DI KETIK DI COMMAND
            if (!config.contains(path)) {
                player.sendMessage(VeloHome.PREFIX.append(Component.text("You don't have a home with that name!", NamedTextColor.RED)));
                return true;
            }

            if (pendingTp.containsKey(uuid)) {
                player.sendMessage(VeloHome.PREFIX.append(Component.text("You are already in the queue!")));
                return true;
            }


            // MENGAMBIL COORDINAT DARI FILE CONFIG.YML
            String worldName = config.getString(path + ".world");
            double x = config.getDouble(path + ".x");
            double y = config.getDouble(path + ".y");
            double z = config.getDouble(path + ".z");
            float yaw = (float) config.getDouble(path + ".yaw");
            float pitch = (float) config.getDouble(path + "pitch");

            if (worldName == null) return true;
            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                player.sendMessage(VeloHome.PREFIX.append(Component.text("World not found!", NamedTextColor.RED)));
                return true;
            }

            Location homeLocation = new Location(world, x, y, z, yaw, pitch);
            player.sendActionBar(Component.text("⚡ Don't move!", NamedTextColor.GOLD));

            BukkitTask teleportTask = new BukkitRunnable() {

                int sec = 3;
                final Location startLocation = player.getLocation();

                @Override
                public void run() {

                    if (!player.isOnline()) {
                        pendingTp.remove(uuid);
                        this.cancel();
                        return;
                    }

                    if (sec <= 0 ) {
                        player.teleport(homeLocation);

                        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                        player.sendMessage(VeloHome.PREFIX.append(Component.text("Teleport Successfully!", NamedTextColor.GREEN)));
                        player.sendActionBar(Component.text("⚡ Teleport Successfully", NamedTextColor.GREEN));

                        pendingTp.remove(uuid);
                        cancel();
                        return;
                    }

                    player.sendActionBar(Component.text("⚡ Teleport In " + sec, NamedTextColor.GOLD));
                    float nada = 0.5f + ((4 - sec) * 0.35f);
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1.0f, nada);
                    sec--;
                }
            }.runTaskTimer(plugin, 0L, 20L);

            pendingTp.put(uuid, teleportTask);

        }

        return true;
    }



    // ===========
    // Suggestions
    // ===========
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        List<String> suggestions = new ArrayList<>();
        FileConfiguration config = plugin.getConfig();

        if (args.length == 1 && sender instanceof Player player) {

            ConfigurationSection section = config.getConfigurationSection("homes." + player.getUniqueId());

            if (section != null) {

                List<String> homeName = new ArrayList<>(section.getKeys(false));

                StringUtil.copyPartialMatches(args[0], homeName, suggestions);
            }
        }

        return suggestions;
    }
}
