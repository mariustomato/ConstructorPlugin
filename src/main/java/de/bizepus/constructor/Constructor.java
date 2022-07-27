package de.bizepus.constructor;
import de.bizepus.constructor.commands.BuildConstruct;
import de.bizepus.constructor.commands.ConstructionList;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class Constructor extends JavaPlugin {

    public static String PREFIX = "§aConstructor: §7§o";
    public static Constructor INSTANCE;
    public static List<String> consturctions = new ArrayList<>();

    public Constructor() {
        INSTANCE = this;
        consturctions.add("SubwayTunnel");
        consturctions.add("NetherHighway");
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        register();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void register() {
        Bukkit.getPluginCommand("construct").setExecutor(new BuildConstruct());
        Bukkit.getPluginCommand("constructionlist").setExecutor(new ConstructionList());
    }
}
