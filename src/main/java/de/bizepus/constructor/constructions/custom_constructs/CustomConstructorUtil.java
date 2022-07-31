package de.bizepus.constructor.constructions.custom_constructs;

import de.bizepus.constructor.utils.FileConfig;
import org.bukkit.Material;
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

    public CustomConstructorUtil(String construct) {
        this.name = construct;
        interpreteConfig();
        this.height = calculateHeight();
    }

    public String getName() {
        return this.name;
    }

    public Map<Material, Integer> getMaterails()  {
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

    private void interpreteConfig() {
        FileConfig config = new FileConfig("constructions.yml");
        String configString = config.getString(this.name);
        String locs = configString.substring(9, configString.indexOf("Blockdata"));
        configString = configString.substring(configString.indexOf("Blockdata"));
        String[] blockdata = configString.split("Blockdata");
        this.first = strToLoc(locs.substring(0, locs.indexOf("Location:")));
        this.second = strToLoc(locs.substring(locs.indexOf("Location:")));
        for (String str : blockdata) {
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

    private Vector strToLoc(String loc) {
        String temp = loc.substring(loc.indexOf("x"));
        double x = Double.parseDouble(loc.substring(loc.indexOf("x") + 2, loc.indexOf(",")));
        double y = Double.parseDouble(loc.substring(loc.indexOf("y") + 2, loc.indexOf(",")));
        double z = Double.parseDouble(loc.substring(loc.indexOf("z") + 2, loc.indexOf(",")));
        return new Vector(x, y, z);
    }

    private int calculateHeight() {
        return (int) Math.abs(this.first.getY() - this.second.getY());
    }
}
