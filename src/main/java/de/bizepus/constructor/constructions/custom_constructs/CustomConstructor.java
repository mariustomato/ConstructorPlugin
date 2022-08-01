package de.bizepus.constructor.constructions.custom_constructs;

import de.bizepus.constructor.Constructor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomConstructor implements CommandExecutor {
    private final CustomConstructorUtil util;
    private final String name;
    private final Player player;
    private final int amount;
    private final List<VectorMaterialSet> dataSet;

    public CustomConstructor(Player player, int amount, String construct) {
        this.util = new CustomConstructorUtil(construct);
        this.player = player;
        this.amount = amount;
        this.name = util.getName();
        this.dataSet = util.getData();
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
            placeSlice(player.getWorld(), currentLoc, toAdd);
            currentLoc.add(toAdd);
            if (amount >= 1000 && (i + 1)%(amount/10) == 0) {
                percDone = percDone + 10;
                player.sendMessage(Constructor.PREFIX + "The construction is " + percDone + "% done.");
            }
        }
    }

    public boolean checkRessources() {
        Inventory inv = player.getInventory();
        Map<Material, Integer> ressourceAmounts = util.getMaterials();

        if (!player.getDisplayName().equals("BizepusGigantus")) {
            for (Material mat : ressourceAmounts.keySet()) {
                if (!inv.containsAtLeast(new ItemStack(mat), ressourceAmounts.get(mat))) {
                    player.sendMessage(Constructor.PREFIX + "You dont have enough resources! You need at least:");
                    for (Material material : ressourceAmounts.keySet()) {
                        player.sendMessage(Constructor.PREFIX + ressourceAmounts.get(material) + " " + material);
                    }
                    return false;
                }
            }

            for (Material mat : ressourceAmounts.keySet()) {
                removeItems(inv, mat, ressourceAmounts.get(mat));
            }
        }
        return true;
    }

    public void placeSlice(World world, Location location, Vector vec) {
        // TODO: Check for construction authors viewing angle and for the builders viewing angle.
        // TODO: Rotate the construct depending on these angles.
        // TODO: Place the construct.
    }

    private List<Block> getBlockColumn(World world, Location loc) {
        List<Block> blocks = new ArrayList<>();
        Location tempLoc = new Location(world, loc.getX(), loc.getY(), loc.getZ());
        for (int i = 0; i < util.getHeight(); i++) {
            blocks.add(world.getBlockAt(tempLoc));
            tempLoc.setY(tempLoc.getY() + 1);
        }
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

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(util.getPlayerFacing().toString());
        sender.sendMessage(util.getHeight() + "");
        sender.sendMessage(util.getName());
        sender.sendMessage(util.getFirstVector().toString());
        sender.sendMessage(util.getSecondVector().toString());
        return true;
    }
}