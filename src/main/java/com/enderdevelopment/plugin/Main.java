package com.enderdevelopment.plugin;

import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Dropper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public final class Main extends JavaPlugin implements Listener {
    private final Random random = new Random();
    private BukkitTask generatorTask;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        generatorTask = Bukkit.getScheduler().runTaskTimer(this, this::generateItems, 3L, 3L);
        if (getCommand("itemgenerator") != null) {
            getCommand("itemgenerator").setExecutor((sender, command, label, args) -> {
                if (!sender.hasPermission("itemgenerator.admin")) {
                    sender.sendMessage(Component.text("You do not have permission.", NamedTextColor.RED));
                    return true;
                }
                if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                    sender.sendMessage(Component.text("Item generators are running.", NamedTextColor.GREEN));
                    return true;
                }
                sender.sendMessage(Component.text("Generators are dropper blocks with an item frame attached. Put the desired item in the frame.", NamedTextColor.YELLOW));
                return true;
            });
        }
    }

    @Override
    public void onDisable() {
        if (generatorTask != null) generatorTask.cancel();
    }

    private void generateItems() {
        for (var world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (!(entity instanceof ItemFrame frame) || !frame.isValid()) continue;
                ItemStack displayed = frame.getItem();
                if (displayed.getType().isAir()) continue;
                Block attached = frame.getLocation().getBlock().getRelative(frame.getAttachedFace());
                if (attached.getType() != Material.DROPPER) continue;
                BlockState state = attached.getState();
                if (!(state instanceof Dropper dropper)) continue;
                int amount = random.nextInt(5) + 1;
                ItemStack generated = displayed.clone();
                generated.setAmount(amount);
                if (!canFit(dropper.getInventory(), generated)) continue;
                dropper.getInventory().addItem(generated);
            }
        }
    }

    private boolean canFit(Inventory inventory, ItemStack item) {
        int remaining = item.getAmount();
        for (ItemStack slot : inventory.getStorageContents()) {
            if (slot == null || slot.getType().isAir()) {
                remaining -= item.getMaxStackSize();
            } else if (slot.isSimilar(item)) {
                remaining -= Math.max(0, slot.getMaxStackSize() - slot.getAmount());
            }
            if (remaining <= 0) return true;
        }
        return remaining <= 0;
    }

    @EventHandler
    public void onDropperBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() != Material.DROPPER) return;
        for (Entity entity : event.getBlock().getWorld().getNearbyEntities(event.getBlock().getLocation().add(0.5, 0.5, 0.5), 1.5, 1.5, 1.5)) {
            if (entity instanceof ItemFrame frame && frame.getAttachedFace() != null) {
                Block attached = frame.getLocation().getBlock().getRelative(frame.getAttachedFace());
                if (attached.equals(event.getBlock())) frame.remove();
            }
        }
    }

    @EventHandler
    public void onFrameDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof ItemFrame frame)) return;
        Block attached = frame.getLocation().getBlock().getRelative(frame.getAttachedFace());
        if (attached.getType() == Material.DROPPER) event.setCancelled(true);
    }
}
