package com.kumar.veloHome.listeners;

import com.kumar.veloHome.VeloHome;
import com.kumar.veloHome.commands.HomeCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class TeleportListener implements Listener {

    private final HomeCommand homeCommand;

    public TeleportListener(HomeCommand homeCommand) {
        this.homeCommand = homeCommand;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (homeCommand.getPendingTP().containsKey(uuid)) {
            Location from = event.getFrom();
            Location to = event.getTo();

            if (from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ()) {

                BukkitTask task = homeCommand.getPendingTP().get(uuid);
                if (task != null) {
                    task.cancel();
                }

                homeCommand.getPendingTP().remove(uuid);

                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                player.sendActionBar(Component.text("Teleport canceled!", NamedTextColor.RED));
                player.sendMessage(VeloHome.PREFIX.append(Component.text("You move! Teleport canceled.", NamedTextColor.RED)));

            }

        }
    }
}