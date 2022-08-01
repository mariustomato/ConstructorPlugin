package de.bizepus.constructor.commands;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Rail;
import org.bukkit.material.Directional;

import java.util.ArrayList;
import java.util.List;

public class BlockRotator {

    // A quarter == 45 degree.
    public static Block rotateBlock(Block block, int quarters) {
        Block rotatedBlock = null;
        if (block instanceof Rail) {
            return rotateRail(block, quarters);
        }
        if (block instanceof Directional) {
            Directional directional = (Directional) block;
            List<BlockFace> face = new ArrayList<>();
            face.add(BlockFace.NORTH);
            face.add(BlockFace.EAST);
            face.add(BlockFace.SOUTH);
            face.add(BlockFace.WEST);
            int start = face.indexOf(block.getFace(block));
            if (start != -1) { // Rotate the blocks depending on the amount of quarters.
                directional.setFacingDirection(face.get((start + quarters) % face.size()));
                rotatedBlock = (Block) directional;
            }
        }
        return rotatedBlock;
    }

    private static Block rotateRail(Block block, int quarter) {
        // TODO: Rotate rails.
        return null;
    }
}
