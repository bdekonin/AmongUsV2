package me.Dopeey.AmongUs;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin implements Listener {

	Classes classes = new Classes();
	@Override
	public void onEnable() {
		Bukkit.broadcastMessage("Among Us has been Enabled!");
		Bukkit.getServer(). getPluginManager().registerEvents(this, this);
	}
	@Override
	public void onDisable() {
		Bukkit.broadcastMessage("Among Us has been Disabled!");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (label.equalsIgnoreCase("inventory")) {
			if (args.length != 1)
				return false;
			if (args[0].equalsIgnoreCase("imposter")) {
				Player player = (Player) sender;
				player.getInventory().setContents(classes.getDefaultInventory().getContents());
				classes.getImposter().kill(player);
				classes.getImposter().vent(player);
				classes.getImposter().report(player);
				classes.getImposter().sabotage(player);

			}
			if (args[0].equalsIgnoreCase("innocent")) {
				Player player = (Player) sender;
				player.getInventory().setContents(classes.getDefaultInventory().getContents());
				classes.getInnocent().use(player);
				classes.getInnocent().report(player);

			}
		}
		else if (label.equalsIgnoreCase("print")) {
			Player player = (Player) sender;

			player.sendMessage("vec: ");
			player.sendMessage("" + player.getLocation().getDirection());
			player.sendMessage(" ");
		}
		return false;
	}

	@EventHandler
	public void onLook(PlayerMoveEvent event) {
		// Testing for Loom
		if (event.getPlayer().getTargetBlock(null, 4).getType() == Material.LOOM)
			classes.getImposter().vent(event.getPlayer());
		else
			event.getPlayer().getInventory().setContents(classes.getDefaultInventory().getContents());
	}
}
