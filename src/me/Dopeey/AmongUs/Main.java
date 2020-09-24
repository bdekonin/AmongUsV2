package me.Dopeey.AmongUs;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.Hash;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;


public class Main extends JavaPlugin implements Listener {

	private boolean gameStatus = false;

	private Classes classes = null;
	private Deadbodies bodies = null;

	// List of players
	ArrayList<Role> list = new ArrayList<Role>();

	@Override
	public void onEnable() {
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
	}
	@Override
	public void onDisable() {
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (label.equalsIgnoreCase("game")) {
			if (args.length != 1) {
				sender.sendMessage(ChatColor.RED + "Syntax is '/game <start/stop>'");
				return true;
			}
			this.setGameStatus(sender, args[0]);
			return true;
		}
/*		if (this.gameStatus == false) {
			sender.sendMessage(ChatColor.GOLD + "Start game with /amongus start");
			return true;
		}*/
		if (label.equalsIgnoreCase("inventory")) {
			if (args.length != 1)
				return false;
			if (args[0].equalsIgnoreCase("imposter")) {
				Player player = (Player) sender;
				player.getInventory().setContents(this.classes.getDefaultInventory().getContents());
				this.classes.getImposter().kill(player);
				this.classes.getImposter().vent(player);
				this.classes.getImposter().report(player);
				this.classes.getImposter().sabotage(player);

			}
			if (args[0].equalsIgnoreCase("innocent")) {
				Player player = (Player) sender;
				player.getInventory().setContents(this.classes.getDefaultInventory().getContents());
				this.classes.getInnocent().use(player);
				this.classes.getInnocent().report(player);

			}
		}
		else if (label.equalsIgnoreCase("body")) {
			this.startGame(sender);
			Player player = (Player) sender;
			this.bodies.spawnBody(Color.BLUE, player.getLocation());
			this.stopGame(sender);
		}
		else if (label.equalsIgnoreCase("bodyclear"))
			this.bodies.removeBodies();
		else if (label.equalsIgnoreCase("test")) {
			list.clear();
			for (Player p : Bukkit.getOnlinePlayers())
				list.add(new Role(p, false));
			int num = ThreadLocalRandom.current().nextInt(0, list.size());
			list.get(num).setImposter(true);


			for (int i = 0; i < list.size(); i++) {
				Player p = list.get(i).getPlayer();
				Bukkit.broadcastMessage(p.getName() + " = " + list.get(i).getImposterString() + " num = " + num);
			}
			Bukkit.broadcastMessage(ChatColor.GOLD + "----");
		}
		return false;
	}

	private void setGameStatus(CommandSender sender, String argument) {
		if (argument.equalsIgnoreCase("start")) { // starts the game
			sender.sendMessage(ChatColor.YELLOW + "starting");
			if (this.gameStatus == true) {
				sender.sendMessage(ChatColor.YELLOW + "Game Already Started!");
			}
			this.startGame(sender);
		}
		else if (argument.equalsIgnoreCase("stop")) { // stops game
			sender.sendMessage(ChatColor.YELLOW + "stopping");
			if (this.gameStatus == false) {
				sender.sendMessage(ChatColor.YELLOW + "Game Already stopped!");
			}
			this.stopGame(sender);
		}
		else { // nothing
			sender.sendMessage(ChatColor.RED + "Syntax is '/game <start/stop>'");
		}
	}

	// Game Functions
	private void startGame(CommandSender sender) {
		this.gameStatus = true;

		// Init objects
		this.classes = new Classes();
		this.bodies = new Deadbodies();
	}
	private void stopGame(CommandSender sender) {
		this.gameStatus = false;

		// free objects
		this.classes = null;
		this.bodies = null;

		this.list.clear();
	}

	// Useful Functions
	private Role getRoleOfPlayer(Player p) {
		for (int i = 0; i < this.list.size(); i++) {
			if (p.getUniqueId() == this.list.get(i).getPlayer().getUniqueId())
				return this.list.get(i);
		}
		return null;
	}

	// 0 kill. 1 vent. 2 report. 3 sabotage
	// Listeners
	@EventHandler
	public void onLook(PlayerMoveEvent event) {
		if (this.gameStatus == false)
			return ;
		Player p = event.getPlayer();
		Role role = this.getRoleOfPlayer(p);
		if (role == null)
			return ;

		Block block = event.getPlayer().getTargetBlock(null, 4);
		// Imposter
		if (role.isImposter()) { // Imposter
			// Kill
			if (block.getType() == Material.OBSIDIAN)
				this.classes.getImposter().kill(p);

			// Vent
			if (block.getType() == Material.LOOM) // Vent
				this.classes.getImposter().vent(p);
			else
				this.classes.getImposter().removeVent(p);

			// Report
			if (this.bodies.isBody(p))
				this.classes.getImposter().report(p);
			else
				this.classes.getImposter().removeReport(p);

			// Sabotage
			// blabla
		}
		else { // Innocent
			// Use


			// Report
			if (this.bodies.isBody(p))
				this.classes.getInnocent().report(p);
			else
				this.classes.getInnocent().removeReport(p);
		}
	}

	@EventHandler
	public void onThrow(ProjectileLaunchEvent event) {
		if (event.getEntity() instanceof EnderPearl)
			event.setCancelled(true);
	}

	@EventHandler
	public void onRightClick(PlayerInteractEvent event) {
		if (this.gameStatus == false)
			return ;
		ItemStack item = event.getItem();
		if (item == null || item.getType() == Material.AIR)
			return ;
		Player p = event.getPlayer();
		if (item.equals(this.classes.getInnocent().getReport()))
			p.sendMessage("report g");
		else if (item.equals(this.classes.getImposter().getVent()))
			p.sendMessage("vent g");
	}

	public void onPlayerClick(PlayerInteractEntityEvent event) {
		if (this.gameStatus == false)
			return ;
		Player p = event.getPlayer();
		if (this.getRoleOfPlayer(p).isImposter() == false) // if innocent tries killing
			return ;
		if (event.getRightClicked().getType() != EntityType.PLAYER) // type is not player
			return ;

		Player killer = p;
		Player dead = (Player) event.getRightClicked();

		Bukkit.broadcastMessage(killer.getName() + " killed " + dead.getName());
		this.classes.getImposter().removeKill(killer);
	}
}
