package com.usmcsky.lumberjack;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class Lumberjack extends JavaPlugin implements Listener {

    private static final int MAX_LOG_BLOCKS = 256;
    private static final int MAX_HORIZONTAL_DISTANCE = 8;
    private static final int MAX_UPWARD_DISTANCE = 32;
    private static final int MAX_DOWNWARD_DISTANCE = 1;
    private static final int MIN_TREE_LOGS = 2;
    private static final int MIN_NEARBY_LEAVES = 5;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Block brokenBlock = event.getBlock();
        if (!isLog(brokenBlock.getType())) {
            return;
        }

        Set<BlockPosition> treeLogs = findTreeLogs(brokenBlock);
        if (treeLogs.size() < MIN_TREE_LOGS) {
            return;
        }

        BlockPosition brokenPosition = BlockPosition.from(brokenBlock);
        ItemStack tool = event.getPlayer().getInventory().getItemInMainHand().clone();
        List<BlockPosition> remainingLogs = treeLogs.stream()
                .filter(position -> !position.equals(brokenPosition))
                .sorted(Comparator.comparingInt(BlockPosition::y).reversed())
                .toList();

        getServer().getScheduler().runTask(this, () -> breakRemainingLogs(brokenBlock.getWorld(), remainingLogs, tool));
    }

    private void breakRemainingLogs(World world, List<BlockPosition> logPositions, ItemStack tool) {
        for (BlockPosition position : logPositions) {
            Block block = world.getBlockAt(position.x(), position.y(), position.z());
            if (!isLog(block.getType())) {
                continue;
            }

            block.breakNaturally(tool);
        }
    }

    private Set<BlockPosition> findTreeLogs(Block startBlock) {
        World world = startBlock.getWorld();
        BlockPosition origin = BlockPosition.from(startBlock);
        Set<BlockPosition> visited = new HashSet<>();
        Set<BlockPosition> logs = new HashSet<>();
        Deque<BlockPosition> queue = new ArrayDeque<>();
        queue.add(origin);

        while (!queue.isEmpty()) {
            BlockPosition current = queue.removeFirst();
            if (!visited.add(current) || !isWithinBounds(origin, current)) {
                continue;
            }

            Block block = world.getBlockAt(current.x(), current.y(), current.z());
            if (!isLog(block.getType())) {
                continue;
            }

            logs.add(current);
            if (logs.size() > MAX_LOG_BLOCKS) {
                return Set.of();
            }

            for (BlockPosition neighbor : getNeighbors(current)) {
                if (!visited.contains(neighbor)) {
                    queue.addLast(neighbor);
                }
            }
        }

        if (logs.size() < MIN_TREE_LOGS || !hasEnoughLeaves(world, logs, origin.y())) {
            return Set.of();
        }

        return logs;
    }

    private boolean hasEnoughLeaves(World world, Set<BlockPosition> logs, int minimumY) {
        Set<BlockPosition> nearbyLeaves = new HashSet<>();

        for (BlockPosition log : logs) {
            if (log.y() < minimumY) {
                continue;
            }

            for (int xOffset = -2; xOffset <= 2; xOffset++) {
                for (int yOffset = -1; yOffset <= 2; yOffset++) {
                    for (int zOffset = -2; zOffset <= 2; zOffset++) {
                        BlockPosition candidate = new BlockPosition(
                                log.x() + xOffset,
                                log.y() + yOffset,
                                log.z() + zOffset
                        );

                        if (candidate.y() < minimumY) {
                            continue;
                        }

                        Material material = world.getBlockAt(candidate.x(), candidate.y(), candidate.z()).getType();
                        if (Tag.LEAVES.isTagged(material)) {
                            nearbyLeaves.add(candidate);
                            if (nearbyLeaves.size() >= MIN_NEARBY_LEAVES) {
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    private boolean isWithinBounds(BlockPosition origin, BlockPosition target) {
        int xDistance = Math.abs(target.x() - origin.x());
        int yDistance = target.y() - origin.y();
        int zDistance = Math.abs(target.z() - origin.z());

        return xDistance <= MAX_HORIZONTAL_DISTANCE
                && zDistance <= MAX_HORIZONTAL_DISTANCE
                && yDistance <= MAX_UPWARD_DISTANCE
                && yDistance >= -MAX_DOWNWARD_DISTANCE;
    }

    private List<BlockPosition> getNeighbors(BlockPosition origin) {
        List<BlockPosition> neighbors = new ArrayList<>(26);

        for (int xOffset = -1; xOffset <= 1; xOffset++) {
            for (int yOffset = -1; yOffset <= 1; yOffset++) {
                for (int zOffset = -1; zOffset <= 1; zOffset++) {
                    if (xOffset == 0 && yOffset == 0 && zOffset == 0) {
                        continue;
                    }

                    neighbors.add(new BlockPosition(
                            origin.x() + xOffset,
                            origin.y() + yOffset,
                            origin.z() + zOffset
                    ));
                }
            }
        }

        return neighbors;
    }

    private boolean isLog(Material material) {
        return Tag.LOGS.isTagged(material);
    }

    private record BlockPosition(int x, int y, int z) {
        private static BlockPosition from(Block block) {
            return new BlockPosition(block.getX(), block.getY(), block.getZ());
        }
    }
}
