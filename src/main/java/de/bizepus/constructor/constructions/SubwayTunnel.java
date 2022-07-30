package de.bizepus.constructor.constructions;

import de.bizepus.constructor.Constructor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class SubwayTunnel {

    private final String name = "SubwayTunnel";
    private final Player player;
    private final int amount;

    public SubwayTunnel(Player player, int amount) {
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
            placeSlice(player.getWorld(), currentLoc, toAdd, player.getFacing().toString(), (i + 1) % 10 == 0);
            currentLoc.add(toAdd);
            if (amount >= 1000 && (i + 1)%(amount/10) == 0) {
                percDone = percDone + 10;
                player.sendMessage(Constructor.PREFIX + "The construction is " + percDone + "% done.");
            }
        }
    }

    public boolean checkRessources() {
        Inventory inv = player.getInventory();
        final int minRails = amount;
        final int minRedstoneBlocks = amount / 10;
        final int minStoneBricks = amount * 7 + amount - minRedstoneBlocks;
        final int minStoneBrickStairs = amount * 4;
        final int minGlowstone = amount;
        boolean result = (inv.containsAtLeast(new ItemStack(Material.STONE_BRICKS), minStoneBricks)
                && inv.containsAtLeast(new ItemStack(Material.STONE_BRICK_STAIRS), minStoneBrickStairs)
                && inv.containsAtLeast(new ItemStack(Material.POWERED_RAIL), minRails)
                && inv.containsAtLeast(new ItemStack(Material.GLOWSTONE), minGlowstone)
                && inv.containsAtLeast(new ItemStack(Material.REDSTONE_BLOCK), minRedstoneBlocks)) || player.getDisplayName().equals("BizepusGigantus");
        if (result) {
            removeItems(inv, Material.STONE_BRICKS, minStoneBricks);
            removeItems(inv, Material.STONE_BRICK_STAIRS, minStoneBrickStairs);
            removeItems(inv, Material.POWERED_RAIL, minRails);
            removeItems(inv, Material.GLOWSTONE, minGlowstone);
            removeItems(inv, Material.REDSTONE_BLOCK, minRedstoneBlocks);
        } else {
            player.sendMessage(Constructor.PREFIX + "You don't have enough resources!");
            player.sendMessage(Constructor.PREFIX + "For " + amount + " blocks of " + name + " you need:");
            player.sendMessage(Constructor.PREFIX + minStoneBricks + " of Stone Bricks");
            player.sendMessage(Constructor.PREFIX + minStoneBrickStairs + " of Stone Brick Stairs");
            player.sendMessage(Constructor.PREFIX + minGlowstone + " of Glowstone");
            player.sendMessage(Constructor.PREFIX + minRedstoneBlocks + " of Redstone Blocks");
            player.sendMessage(Constructor.PREFIX + minRails + " of Powered Rails");
        }
        return result;
    }

    public void placeSlice(World world, Location location, Vector vec, String direction, boolean addPower) {
        Location middle;
        Location leftMiddle;
        Location rightMiddle;
        Location leftLeft;
        Location rightRight;

        switch ((int) vec.getX()) {
            case 1:
            case -1: // Case +/- X as building direction
                middle = location;
                leftMiddle = new Location(world, location.getX(), location.getY(), location.getZ() - 1);
                rightMiddle = new Location(world, location.getX(), location.getY(), location.getZ() + 1);
                leftLeft = new Location(world, location.getX(), location.getY(), location.getZ() - 2);
                rightRight = new Location(world, location.getX(), location.getY(), location.getZ() + 2);
                break;
            default:
                middle = location;
                leftMiddle = new Location(world, location.getX() - 1, location.getY(), location.getZ());
                rightMiddle = new Location(world, location.getX() + 1, location.getY(), location.getZ());
                ;
                leftLeft = new Location(world, location.getX() - 2, location.getY(), location.getZ());
                ;
                rightRight = new Location(world, location.getX() + 2, location.getY(), location.getZ());
                ;
        }
        //<editor-fold defaultstate="collapsed" desc="setBlocks">

        List<Block> middleBlocks = getBlockColumn(world, middle);
        if (addPower) {
            middleBlocks.get(0).setType(Material.REDSTONE_BLOCK);
        } else {
            middleBlocks.get(0).setType(Material.STONE_BRICKS);
        }
        Block rail = middleBlocks.get(1);
        rail.setType(Material.POWERED_RAIL);
        //setRails(direction, rail);
        rail.getState().update(true, true);
        middleBlocks.get(2).setType(Material.AIR);
        middleBlocks.get(3).setType(Material.AIR);
        middleBlocks.get(4).setType(Material.GLOWSTONE);

        List<Block> leftMiddleBlocks = getBlockColumn(world, leftMiddle);
        leftMiddleBlocks.get(0).setType(Material.STONE_BRICKS);
        Block stairBottomLeft = leftMiddleBlocks.get(1);
        stairBottomLeft.setType(Material.STONE_BRICK_STAIRS);
        setStairs(direction, true, false, stairBottomLeft);
        leftMiddleBlocks.get(2).setType(Material.AIR);
        Block stairTopLeft = leftMiddleBlocks.get(3);
        stairTopLeft.setType(Material.STONE_BRICK_STAIRS);
        setStairs(direction, true, true, stairTopLeft);
        leftMiddleBlocks.get(4).setType(Material.STONE_BRICKS);

        {
            List<Block> leftLeftBlocks = getBlockColumn(world, leftLeft);
            //leftLeftBlocks.get(0).setType(Material.STONE_BRICKS); // too expensive, but would be fancier
            //leftLeftBlocks.get(1).setType(Material.STONE_BRICKS);
            leftLeftBlocks.get(2).setType(Material.STONE_BRICKS);
            //leftLeftBlocks.get(3).setType(Material.STONE_BRICKS);
            //leftLeftBlocks.get(4).setType(Material.STONE_BRICKS);
        }

        List<Block> rightMiddleBlocks = getBlockColumn(world, rightMiddle);
        rightMiddleBlocks.get(0).setType(Material.STONE_BRICKS);
        Block stairBottomRight = rightMiddleBlocks.get(1);
        stairBottomRight.setType(Material.STONE_BRICK_STAIRS);
        setStairs(direction, false, false, stairBottomRight);
        rightMiddleBlocks.get(2).setType(Material.AIR);
        Block stairTopRight = rightMiddleBlocks.get(3);
        stairTopRight.setType(Material.STONE_BRICK_STAIRS);
        setStairs(direction, false, true, stairTopRight);
        rightMiddleBlocks.get(4).setType(Material.STONE_BRICKS);

        {
            List<Block> rightRightBlocks = getBlockColumn(world, rightRight);
            //rightRightBlocks.get(0).setType(Material.STONE_BRICKS); // too expensive, but would be fancier
            //rightRightBlocks.get(1).setType(Material.STONE_BRICKS);
            rightRightBlocks.get(2).setType(Material.STONE_BRICKS);
            //rightRightBlocks.get(3).setType(Material.STONE_BRICKS);
            //rightRightBlocks.get(4).setType(Material.STONE_BRICKS);
        }
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
        tempLoc.setY(tempLoc.getY() + 1);
        Block fifth = world.getBlockAt(tempLoc);
        blocks.add(first);
        blocks.add(second);
        blocks.add(third);
        blocks.add(fourth);
        blocks.add(fifth);
        return blocks;
    }

    private void setStairs(String direction, boolean isLeft, boolean isTop, Block block) {
        final Stairs stairs = (Stairs) block.getState().getBlockData();
        ;
        switch (direction) {
            case "NORTH":
            case "SOUTH":
                if (isLeft) {
                    stairs.setFacing(BlockFace.WEST);
                } else {
                    stairs.setFacing(BlockFace.EAST);
                }
                if (isTop) {
                    stairs.setHalf(Bisected.Half.TOP);
                }
                block.setBlockData(stairs);
                break;
            case "EAST":
            case "WEST":
                if (isLeft) {
                    stairs.setFacing(BlockFace.NORTH);
                } else {
                    stairs.setFacing(BlockFace.SOUTH);
                }
                if (isTop) {
                    stairs.setHalf(Bisected.Half.TOP);
                }
                block.setBlockData(stairs);
                break;
            default: {
            }
        }
    }

    private static void removeItems(Inventory inventory, Material type, int value) {
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