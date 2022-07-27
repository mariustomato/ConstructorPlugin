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
//            if (!sender.hasPermission("de.constructor.list")) {
//                sender.sendMessage(Constructor.PREFIX + "You don't have the permissions needed.");
//                return true;
//            }
            StringBuilder message = new StringBuilder(Constructor.PREFIX + "Possible buildings: ");
            for (String constr : Constructor.consturctions) {
                message.append(constr).append(",");
            }
            message = new StringBuilder(message.substring(0, message.length() - 1));
            message.append(".");
            sender.sendMessage(Constructor.PREFIX + message);
            return true;
        }
        return false;
    }
}
