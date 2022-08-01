package de.bizepus.constructor.constructions.custom_constructs;

import de.bizepus.constructor.utils.FileConfig;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomConstructorUtil {
    private final String name;
    private final List<VectorMaterialSet> data = new ArrayList<>();
    private Vector first;
    private Vector second;
    private final Map<Material, Integer> materialAmounts = new HashMap<>();
    private final int height;
    private BlockFace playerFacing;

    public CustomConstructorUtil(String construct) {
        this.name = construct;
        interpreteConfig();
        this.height = calculateHeight();
    }

    public String getName() {
        return this.name;
    }

    public Map<Material, Integer> getMaterials()  {
        return materialAmounts;
    }

    public List<VectorMaterialSet> getData() {
        return this.data;
    }

    public Vector getFirstVector() {
        return this.first;
    }

    public Vector getSecondVector() {
        return this.second;
    }

    public int getHeight() {
        return this.height;
    }

    public BlockFace getPlayerFacing() {
        return this.playerFacing;
    }

    private void interpreteConfig() {
        FileConfig config = new FileConfig("constructions.yml");
        String configString = config.getString(this.name);
        String locs = configString.substring(9, configString.indexOf("Blockdata:"));
        configString = configString.substring(configString.indexOf("Blockdata:"));
        String[] blockdata = configString.split("Blockdata:");
        this.first = strToLoc(locs.substring(0, locs.indexOf("Location")));
        String tempStr = locs.substring(locs.indexOf("Location"));
        this.second = strToLoc(tempStr.substring(0, tempStr.indexOf(";")));
        this.playerFacing = strToFace(tempStr.substring(tempStr.indexOf(";")));
        for (String str : blockdata) {
            if (!(str.length() <= 1)) {
                Vector loc = strToLoc(str.substring(str.indexOf("Location:") + 9, str.indexOf(";")));
                String temp = str.substring(str.indexOf(";") + 1);
                Material mat = Material.valueOf(temp.substring(temp.indexOf("Material:") + 9, temp.indexOf("}")));
                this.data.add(new VectorMaterialSet(loc, mat));
                if (materialAmounts.containsKey(mat)) {
                    materialAmounts.put(mat, materialAmounts.get(mat) + 1);
                } else {
                    materialAmounts.put(mat, 1);
                }
            }
        }
    }

    private Vector strToLoc(String loc) {
        String tempX = loc.substring(loc.indexOf("x") + 2);
        tempX = tempX.substring(0, tempX.indexOf(","));
        String tempY = loc.substring(loc.indexOf("y") + 2);
        tempY = tempY.substring(0, tempY.indexOf(","));
        String tempZ = loc.substring(loc.indexOf("z") + 2);
        tempZ = !tempZ.contains(",") ? tempZ : tempZ.substring(0, tempZ.indexOf(","));
        double x = Double.parseDouble(tempX);
        double y = Double.parseDouble(tempY);
        double z = Double.parseDouble(tempZ);
        return new Vector(x, y, z);
    }

    private BlockFace strToFace(String face) {
        face = face.substring(1);
        face = face.substring(0, face.indexOf(";"));
        return BlockFace.valueOf(face);
    }

    private int calculateHeight() {
        return (int) Math.abs(this.first.getY() - this.second.getY());
    }
}
