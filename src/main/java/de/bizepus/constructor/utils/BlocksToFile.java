package de.bizepus.constructor.utils;

import de.bizepus.constructor.Constructor;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

public class BlocksToFile {
    public static String saveBlocks(List<Block> blocks, Player player) {
        StringBuilder data = new StringBuilder();
        if (!Constructor.markedSpots.containsKey(player.getUniqueId())) {
            return null;
        }
        LocationSet locSet = Constructor.markedSpots.get(player.getUniqueId());
        data.append(locSet.getFirst().toString());
        data.append(";");
        data.append(locSet.getSecond().toString());
        data.append(";");
        data.append(player.getFacing());
        data.append(";");
        blocks.forEach(block -> data
                .append("Blockdata:{")
                .append("Location:x=")
                .append(block.getLocation().getX())
                .append(",y=")
                .append(block.getLocation().getY())
                .append(",z=")
                .append(block.getLocation().getZ())
                .append(";")
                .append("Material:")
                .append(block.getType())
                .append("};"));
        return data.toString();
    }

    public static void updateConstructList() {
        YamlConfiguration config = new FileConfig("constructions.yml");
        Set<String> fullConfig = config.getKeys(false);
        Constructor.consturctions.addAll(fullConfig);
    }
}
