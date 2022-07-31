package de.bizepus.constructor;
import de.bizepus.constructor.commands.BuildConstruct;
import de.bizepus.constructor.commands.ConstructionList;
import de.bizepus.constructor.constructions.custom_constructs.BlockSaver;
import de.bizepus.constructor.utils.BlocksToFile;
import de.bizepus.constructor.utils.LocationSet;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public final class Constructor extends JavaPlugin {

    public static String PREFIX = "§aConstructor: §7§o";
    public static Constructor INSTANCE;
    public static Set<String> consturctions = new HashSet<>();
    public static List<Block> savedCopy = new ArrayList<>();
    public static Map<UUID, LocationSet> markedSpots = new HashMap<>();

    public Constructor() {
        INSTANCE = this;
        consturctions.add("SubwayTunnel");
        consturctions.add("NetherHighway");
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        register();
        registerListener(new BlockSaver());
        createFolders();
        BlocksToFile.updateConstructList();
        //getServer().getPluginManager().registerEvents(new );
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void register() {
        Bukkit.getPluginCommand("construct").setExecutor(new BuildConstruct());
        Bukkit.getPluginCommand("constructionlist").setExecutor(new ConstructionList());
        Bukkit.getPluginCommand("//set").setExecutor(new BlockSaver());
    }

    private void registerListener(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }

    public void log(String str) {
        Bukkit.getConsoleSender().sendMessage(str);
    }

    private void createFolders() {
        String directoryName = "plugins\\Constructor";
        File directory = new File(directoryName);
        File constructions = new File(directoryName + "\\constructions.yml");
        if (!directory.exists()) {
            directory.mkdir();
        }
        try {
            constructions.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
