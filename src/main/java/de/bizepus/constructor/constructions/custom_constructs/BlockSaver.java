package de.bizepus.constructor.constructions.custom_constructs;

import de.bizepus.constructor.Constructor;
import de.bizepus.constructor.utils.BlocksToFile;
import de.bizepus.constructor.utils.FileConfig;
import de.bizepus.constructor.utils.LocationSet;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import java.util.List;

public class BlockSaver implements CommandExecutor, Listener {

    private Location[] coordinates = new Location[2];
    private Location prevFirstLoc;
    private Location prevSecondLoc;
    private List<Block> copiedBlocks = Constructor.savedCopy;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Constructor.PREFIX + "§cNot possible!");
            return true;
        }
        if (label.equalsIgnoreCase("//save") && args.length == 1) {
            if (args[0].contains(":") || args[0].contains(";")) {
                sender.sendMessage(Constructor.PREFIX + "Don't use ';' or ':' in the Construction-Name");
            }
            FileConfig fileConfig = new FileConfig("constructions.yml");

            if (Constructor.consturctions.contains(args[0]) && fileConfig.getString(args[0]).equals("empty")) {
                sender.sendMessage(Constructor.PREFIX + "This name is already chosen!");
                return true;
            }

            String toSave = BlocksToFile.saveBlocks(copiedBlocks, (Player) sender);
            fileConfig.set(args[0], toSave);
            String[] data = fileConfig.getString(args[0]).split("Blockdata");
            for (String str : data) {
                Constructor.INSTANCE.log(str);
            }
            fileConfig.safeConfig();
        }
        if (label.equalsIgnoreCase("//set")) {
            try {
                final Material material = Material.valueOf(args[0]);
                if (!copiedBlocks.isEmpty()) {
                    copiedBlocks.forEach(block -> block.setType(material));
                }
            } catch (IllegalArgumentException e) {
                sender.sendMessage("This block does not exist!");
                return true;
            }
        }
        if (label.equalsIgnoreCase("//delete") && args.length == 1) {
            FileConfig config = new FileConfig("constructions.yml");
            if (config.contains(args[0])) {
                config.set(args[0], "empty");
            } else {
                sender.sendMessage(Constructor.PREFIX + "This construct does not exist!");
                return true;
            }
        }
        return true;
    }

    @EventHandler
    public void selectedArea(PlayerInteractEvent event) {
        if (!event.hasBlock()) {
            return;
        }
        if (!event.hasItem()) {
            return;
        }
        Location loc = event.getClickedBlock().getLocation();
        ItemStack mainItem = event.getPlayer().getInventory().getItemInMainHand();
        if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            event.getPlayer().getWorld().getBlockAt(event.getClickedBlock().getLocation()).setType(event.getClickedBlock().getType());
        }

        if (loc.equals(prevFirstLoc) || loc.equals(prevSecondLoc)) {
            return;
        }
        if (mainItem == null) {
            return;
        }

        if (mainItem.getItemMeta().getDisplayName().equals("Copynator") && mainItem.getType().equals(Material.BOOK)) {
            if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                coordinates[0] = loc;
                event.getPlayer().sendMessage("Saved first position (" + loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + ")");
                prevFirstLoc = loc;
            } else if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                coordinates[1] = loc;
                event.getPlayer().sendMessage("Saved second position (" + loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + ")");
                prevSecondLoc = loc;
            }

            if (coordinates[0] != null && coordinates[1] != null) {
                calcBlocks(event.getPlayer().getWorld());
                Constructor.markedSpots.put(event.getPlayer().getUniqueId(), new LocationSet(prevFirstLoc, prevSecondLoc));
            }
        }
    }

    // Calculates all blocks inbetween the square resulting of coordinates[0]
    // and coordinates[1]
    private void calcBlocks(World world) {
        copiedBlocks.clear();
        int loopYStart = (int) (Math.min(prevFirstLoc.getY(), prevSecondLoc.getY()));
        int loopYEnd = (int) (loopYStart == prevFirstLoc.getY() ? prevSecondLoc.getY() : prevFirstLoc.getY());

        while (loopYStart <= loopYEnd) {
            calcSquare(world, loopYStart);
            loopYStart += 1;
        }

        copiedBlocks.stream().filter(block -> block.getType().equals(Material.NETHERRACK)).forEach(block -> copiedBlocks.remove(block));

        coordinates[0] = null;
        coordinates[1] = null;

        copiedBlocks.forEach(block -> Constructor.INSTANCE.log(block.toString()));
    }

    // Calculates the blocks for the current square consisting of x- & z-Value
    private void calcSquare(World world, int yValue) {
        int loopXStart = (int) (Math.min(prevFirstLoc.getX(), prevSecondLoc.getX()));
        int  loopXEnd = (int) (loopXStart == prevFirstLoc.getX() ? prevSecondLoc.getX() : prevFirstLoc.getX());

        while (loopXStart <= loopXEnd) {
            calcRow(world, yValue, loopXStart);
            loopXStart += 1;
        }
    }

    // Calculates the blocks for the current z-Column
    private void calcRow(World world, int yValue, int xValue) {
        int loopZStart = (int) (Math.min(prevFirstLoc.getZ(), prevSecondLoc.getZ()));
        int loopZEnd = (int) (loopZStart == prevFirstLoc.getZ() ? prevSecondLoc.getZ() : prevFirstLoc.getZ());
        Location startLoc = new Location(world, xValue, yValue, loopZStart);

        while (loopZStart <= loopZEnd) {
            copiedBlocks.add(world.getBlockAt(startLoc));
            loopZStart += 1;
            startLoc = new Location(world, xValue, yValue, loopZStart);
        }
    }
}