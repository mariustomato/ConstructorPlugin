package de.bizepus.constructor.constructions;

import de.bizepus.constructor.Constructor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class NetherHighway {

    private final String name = "NetherHighway";
    private final Player player;
    private final int amount;

    public NetherHighway(Player player, int amount) {
        this.player = player;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void create() {
        int percDone = 0;
        Vector toAdd = player.getFacing().getDirection();
        Location currentLoc = player.getLocation();
        currentLoc.setY(currentLoc.getY() - 1);
        currentLoc.add(toAdd);
        for (int i = 0; i < amount; i++) {
            if (i%2 == 0) {
                if (i%16 == 0) {
                    placeSlice(player.getWorld(), currentLoc, toAdd, 2);
                } else {
                    placeSlice(player.getWorld(), currentLoc, toAdd, 1);
                }
            } else {
                placeSlice(player.getWorld(), currentLoc, toAdd, 0);
            }
            currentLoc.add(toAdd);
            if (amount >= 1000 && (i + 1)%(amount/10) == 0) {
                percDone = percDone + 10;
                player.sendMessage(Constructor.PREFIX + "The construction is " + percDone + "% done.");
            }
        }
    }

    public boolean checkRessources() {
        Inventory inv = player.getInventory();
        boolean result = false;
        final int minPackedIce = amount/2 == 0 ? 1 : amount/2;
        final int minFence = amount * 2;
        final int minNetherrack = amount * 16;
        boolean containsOakFence = inv.containsAtLeast(new ItemStack(Material.OAK_FENCE), minFence);
        boolean containsSpruceFence = inv.containsAtLeast(new ItemStack(Material.SPRUCE_FENCE), minFence);
        boolean containsBirchFence = inv.containsAtLeast(new ItemStack(Material.BIRCH_FENCE), minFence);
        boolean containsJungleFence = inv.containsAtLeast(new ItemStack(Material.JUNGLE_FENCE), minFence);
        boolean containsAcaciaFence = inv.containsAtLeast(new ItemStack(Material.ACACIA_FENCE), minFence);
        boolean containsDarkOakFence = inv.containsAtLeast(new ItemStack(Material.DARK_OAK_FENCE), minFence);
        boolean containsCrimsonFence = inv.containsAtLeast(new ItemStack(Material.CRIMSON_FENCE), minFence);

        if (inv.containsAtLeast(new ItemStack(Material.PACKED_ICE), minPackedIce) && inv.containsAtLeast(new ItemStack(Material.NETHERRACK), minNetherrack)) {
            if (containsAcaciaFence) {
                removeItems(inv, Material.PACKED_ICE, minPackedIce);
                removeItems(inv, Material.ACACIA_FENCE, minFence);
                result = true;
            } else if (containsBirchFence) {
                removeItems(inv, Material.PACKED_ICE, minPackedIce);
                removeItems(inv, Material.BIRCH_FENCE, minFence);
                result = true;
            } else if (containsCrimsonFence) {
                removeItems(inv, Material.PACKED_ICE, minPackedIce);
                removeItems(inv, Material.CRIMSON_FENCE, minFence);
                result = true;
            } else if (containsDarkOakFence) {
                removeItems(inv, Material.PACKED_ICE, minPackedIce);
                removeItems(inv, Material.DARK_OAK_FENCE, minFence);
                result = true;
            } else if (containsJungleFence) {
                removeItems(inv, Material.PACKED_ICE, minPackedIce);
                removeItems(inv, Material.JUNGLE_FENCE, minFence);
                result = true;
            } else if (containsOakFence) {
                removeItems(inv, Material.PACKED_ICE, minPackedIce);
                removeItems(inv, Material.OAK_FENCE, minFence);
                result = true;
            } else if (containsSpruceFence) {
                removeItems(inv, Material.PACKED_ICE, minPackedIce);
                removeItems(inv, Material.SPRUCE_FENCE, minFence);
                result = true;
            } else {
                player.sendMessage(Constructor.PREFIX + "You need at least " + minFence + " fences to build " + amount + " blocks of " + name);
            }
        } else if (player.getDisplayName().equals("BizepusGigantus")) {
            return true;
        } else {
            player.sendMessage(Constructor.PREFIX + "You don't have enough resources to build " + amount + " blocks of " + name);
            player.sendMessage(Constructor.PREFIX + "You need:");
            player.sendMessage(Constructor.PREFIX + minFence + " fences");
            player.sendMessage(Constructor.PREFIX + minPackedIce + " packed ice");
            player.sendMessage(Constructor.PREFIX + minNetherrack + " netherrack");
        }

        return result;
    }

    public void placeSlice(World world, Location location, Vector vec, int iceType) {
        Location middle;
        Location leftMiddle;
        Location rightMiddle;
        Location wallLeft;
        Location wallRight;

        switch ((int) vec.getX()) {
            case 1:
            case -1: // Case +/- X as building direction
                middle = location;
                leftMiddle = new Location(world, location.getX(), location.getY(), location.getZ() - 1);
                rightMiddle = new Location(world, location.getX(), location.getY(), location.getZ() + 1);
                wallLeft = new Location(world, location.getX(), location.getY(), location.getZ() - 2);
                wallRight = new Location(world, location.getX(), location.getY(), location.getZ() + 2);
                break;
            default:
                middle = location;
                leftMiddle = new Location(world, location.getX() - 1, location.getY(), location.getZ());
                rightMiddle = new Location(world, location.getX() + 1, location.getY(), location.getZ());
                wallLeft = new Location(world, location.getX() - 2, location.getY(), location.getZ());
                wallRight = new Location(world, location.getX() + 2, location.getY(), location.getZ());
        }
        //<editor-fold defaultstate="collapsed" desc="setBlocks">
        List<Block> middleBlocks = getBlockColumn(world, middle);
        switch (iceType) {
            case 0:
                middleBlocks.get(0).setType(Material.AIR);
                break;
            case 1:
                middleBlocks.get(0).setType(Material.PACKED_ICE);
                break;
            default:
                middleBlocks.get(0).setType(Material.BLUE_ICE);
        }
        middleBlocks.get(1).setType(Material.AIR);
        middleBlocks.get(2).setType(Material.AIR);
        middleBlocks.get(3).setType(Material.NETHERRACK);
        middleBlocks.get(4).setType(Material.NETHERRACK);

        List<Block> leftMiddleBlocks = getBlockColumn(world, leftMiddle);
        leftMiddleBlocks.get(0).setType(Material.AIR);
        leftMiddleBlocks.get(1).setType(Material.OAK_FENCE);
        leftMiddleBlocks.get(2).setType(Material.AIR);
        leftMiddleBlocks.get(3).setType(Material.NETHERRACK);
        leftMiddleBlocks.get(4).setType(Material.NETHERRACK);

        List<Block> rightMiddleBlocks = getBlockColumn(world, rightMiddle);
        rightMiddleBlocks.get(0).setType(Material.AIR);
        rightMiddleBlocks.get(1).setType(Material.OAK_FENCE);
        rightMiddleBlocks.get(2).setType(Material.AIR);
        rightMiddleBlocks.get(3).setType(Material.NETHERRACK);
        rightMiddleBlocks.get(4).setType(Material.NETHERRACK);

        List<Block> leftWallBlocks = getBlockColumn(world, wallLeft);
        leftWallBlocks.get(0).setType(Material.NETHERRACK);
        leftWallBlocks.get(1).setType(Material.NETHERRACK);
        leftWallBlocks.get(2).setType(Material.NETHERRACK);
        leftWallBlocks.get(3).setType(Material.NETHERRACK);
        leftWallBlocks.get(4).setType(Material.NETHERRACK);

        List<Block> rightWallBlocks = getBlockColumn(world, wallRight);
        rightWallBlocks.get(0).setType(Material.NETHERRACK);
        rightWallBlocks.get(1).setType(Material.NETHERRACK);
        rightWallBlocks.get(2).setType(Material.NETHERRACK);
        rightWallBlocks.get(3).setType(Material.NETHERRACK);
        rightWallBlocks.get(4).setType(Material.NETHERRACK);
        //</editor-fold>
    }

    private List<Block> getBlockColumn(World world, Location loc) {
        List<Block> blocks = new ArrayList<>();
        Location tempLoc = new Location(world, loc.getX(), loc.getY(), loc.getZ());
        Block first = world.getBlockAt(tempLoc);
        tempLoc.setY(tempLoc.getY() + 1);
        Block second = world.getBlockAt(tempLoc);
        tempLoc.setY(tempLoc.getY() + 1);
        Block third = world.getBlockAt(tempLoc);
        tempLoc.setY(tempLoc.getY() + 1);
        Block fourth = world.getBlockAt(tempLoc);
        tempLoc.setY(tempLoc.getY() - 4);
        Block fifth = world.getBlockAt(tempLoc);
        blocks.add(first);
        blocks.add(second);
        blocks.add(third);
        blocks.add(fourth);
        blocks.add(fifth);
        return blocks;
    }

    public static void removeItems(Inventory inventory, Material type, int value) {
        if (value <= 0) return;
        int size = inventory.getSize();
        for (int slot = 0; slot < size; slot++) {
            ItemStack is = inventory.getItem(slot);
            if (is == null) continue;
            if (type == is.getType()) {
                int newAmount = is.getAmount() - value;
                if (newAmount > 0) {
                    is.setAmount(newAmount);
                    break;
                } else {
                    inventory.clear(slot);
                    value = -newAmount;
                    if (value == 0) break;
                }
            }
        }
    }
}