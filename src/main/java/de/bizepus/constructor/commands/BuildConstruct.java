package de.bizepus.constructor.commands;

import de.bizepus.constructor.Constructor;
import de.bizepus.constructor.constructions.SubwayTunnel;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BuildConstruct implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && args.length == 2) {
            Player player = (Player) sender;
            if (!player.hasPermission("de.constructor.build")) {
                player.sendMessage(Constructor.PREFIX + "You don't have to permission to build a construct");
                return true;
            }
            Location currentLoc = player.getLocation();
            currentLoc.setY(currentLoc.getY() - 1);
            int distance = -1;
            String construct = args[0];
            try {
                distance = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                player.sendMessage(Constructor.PREFIX + "The 2nd argument has to be a valid number greater 0.");
                return true;
            }
            if (distance > 0) {
                findConstruct(construct, player, distance);
            } else {
                player.sendMessage(Constructor.PREFIX + "The value has to be greater than 0");
            }
        } else {
            sender.sendMessage(Constructor.PREFIX + "Too few/many arguments!");
        }
        return true;
    }


    private void findConstruct(String constr, Player player, int amount) {
        switch (constr) {
            case "SubwayTunnel":
                SubwayTunnel construct = new SubwayTunnel(player, amount);
                if (construct.checkRessources()) {
                    construct.create();
                    player.sendMessage(Constructor.PREFIX + construct.getName() + "successfully built.");
                }
                break;
            case "NetherHighway":
                player.sendMessage(Constructor.PREFIX + "Coming soon.");
                break;
            default:
                player.sendMessage(Constructor.PREFIX + "This construct does not exist.");
        }
    }
}
