package de.bizepus.constructor.constructions.custom_constructs;

import org.bukkit.Material;
import org.bukkit.util.Vector;

public class VectorMaterialSet {
        private final Vector vec;
        private final Material mat;

        public VectorMaterialSet(Vector location, Material material) {
            this.vec = location;
            this.mat = material;
        }

        public Vector getLocation() {
            return this.vec;
        }

        public Material getMaterial() {
            return this.mat;
        }
}
