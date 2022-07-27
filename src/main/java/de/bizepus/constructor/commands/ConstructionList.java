package de.bizepus.constructor.commands;

import de.bizepus.constructor.Constructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ConstructionList implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            String message = Constructor.PREFIX + "Possible buildings: ";

            sender.sendMessage(Constructor.PREFIX + "Possible");
        }
        return false;
    }
}
