package com.noctify.Main.Listeners;

import com.noctify.Main.FXItems;
import com.noctify.Main.Utils.OneTimeCraftUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class LegendaryItemCraftListener implements Listener {

    private final FXItems plugin;

    public LegendaryItemCraftListener(FXItems plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPrepareItemCraft(PrepareItemCraftEvent event) {
        FileConfiguration config = plugin.getConfig();
        ItemStack result = event.getInventory().getResult();

        if (result == null) return;

        for (OneTimeCraftUtils.OneTimeCraftItem item : OneTimeCraftUtils.getRegisteredItems().values()) {
            if (isLegendaryItem(result, item) && config.getBoolean(item.getName() + "_crafted")) {
                event.getInventory().setResult(null);
                return;
            }
        }
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        FileConfiguration config = plugin.getConfig();
        ItemStack result = event.getCurrentItem();

        if (result == null) return;

        Player player = (Player) event.getWhoClicked();

        for (OneTimeCraftUtils.OneTimeCraftItem item : OneTimeCraftUtils.getRegisteredItems().values()) {
            if (isLegendaryItem(result, item) && !config.getBoolean(item.getName() + "_crafted")) {
                handleLegendaryCraft(player, item);
                return;
            }
        }
    }

    private boolean isLegendaryItem(ItemStack item, OneTimeCraftUtils.OneTimeCraftItem registeredItem) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null || item.getType() != registeredItem.getMaterial()) {
            return false;
        }

        return meta.getPersistentDataContainer().has(new NamespacedKey(plugin, registeredItem.getName() + "_crafted"), PersistentDataType.BYTE);
    }

    private void handleLegendaryCraft(Player player, OneTimeCraftUtils.OneTimeCraftItem item) {
        FileConfiguration config = plugin.getConfig();
        config.set(item.getName() + "_crafted", true);
        plugin.saveConfig();

        Bukkit.broadcastMessage(ChatColor.GOLD + "The " + item.getName() + " has been crafted by " + player.getName() +
                "! This legendary can not be crafted again!");

        Bukkit.getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.8f));
    }
}