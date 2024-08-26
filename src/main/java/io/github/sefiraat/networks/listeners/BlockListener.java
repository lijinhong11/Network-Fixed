package io.github.sefiraat.networks.listeners;

import io.github.sefiraat.networks.NetworkStorage;
import io.github.sefiraat.networks.network.NetworkNode;
import io.github.sefiraat.networks.network.NodeDefinition;
import io.github.sefiraat.networks.network.NodeType;
import io.github.sefiraat.networks.slimefun.network.NetworkController;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.Bisected;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener implements Listener {
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e) {
        removeNetwork(e.getBlock().getLocation());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent e) {
        removeNetwork(e.getBlock().getLocation());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockGrow(BlockGrowEvent e) {
        Block block = e.getBlock();
        if (block.getBlockData() instanceof Bisected) {
            Location b1 = block.getLocation().add(0, 1, 0);
            SlimefunItem item = BlockStorage.check(b1);
            if (item != null) {
                removeNetwork(b1);
                BlockStorage.clearBlockInfo(b1);
            }
        }
    }

    private void removeNetwork(Location location) {
        NodeDefinition definition = NetworkStorage.getAllNetworkObjects().get(location);
        if (definition == null) return;
        NetworkNode node = definition.getNode();
        if (node != null && node.getNodeType() == NodeType.CONTROLLER) {
            NetworkController.wipeNetwork(location);
        }
        NetworkStorage.removeNode(location);
    }
}