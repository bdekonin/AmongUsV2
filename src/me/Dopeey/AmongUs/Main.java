package me.Dopeey.AmongUs;


import org.bukkit.*;
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
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

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
			Player p = (Player) sender;
			this.setKillTimer(p, this.getRoleOfPlayer(p));
		}
		return false;
	}

	private void setGameStatus(CommandSender sender, String argument) {
		if (argument.equalsIgnoreCase("start")) { // starts the game
			sender.sendMessage(ChatColor.YELLOW + "starting");
			if (this.gameStatus == true) {
				sender.sendMessage(ChatColor.YELLOW + "Game Already Started!");
				return ;
			}
			this.startGame(sender);
		}
		else if (argument.equalsIgnoreCase("stop")) { // stops game
			sender.sendMessage(ChatColor.YELLOW + "stopping");
			if (this.gameStatus == false) {
				sender.sendMessage(ChatColor.YELLOW + "Game Already stopped!");
				return ;
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


		// Set Colors
		this.list.clear();
		for (Player p : Bukkit.getOnlinePlayers()) // adding players
			this.list.add(new Role(p, false));

		int num = 0;
		// change to amount of imposters
		for (int i = 0; i < 1; i++) {
			num = ThreadLocalRandom.current().nextInt(0, this.list.size());
			if (!this.list.get(num).isImposter())
				this.list.get(num).setImposter(true);
			else
				i--;
		}

		ArrayList<Color> colorList = new ArrayList<Color>();

		colorList.add(Color.AQUA);
		colorList.add(Color.BLACK);
		colorList.add(Color.BLUE);
		colorList.add(Color.FUCHSIA);
		colorList.add(Color.GRAY);
		colorList.add(Color.GREEN);
		colorList.add(Color.LIME);
		colorList.add(Color.MAROON);
		colorList.add(Color.NAVY);
		colorList.add(Color.OLIVE);
		colorList.add(Color.ORANGE);
		colorList.add(Color.PURPLE);
		colorList.add(Color.RED);
		colorList.add(Color.SILVER);
		colorList.add(Color.TEAL);
		colorList.add(Color.YELLOW);
		colorList.add(Color.WHITE);

		ArrayList<String> stringList = new ArrayList<String>();

		num = 0;
		for (int i = 0; i < this.list.size(); i++) {
			num = ThreadLocalRandom.current().nextInt(0, colorList.size());
			Color c = colorList.get(num);
			if (this.hasColorBeenUsed(c))
				continue;
			this.list.get(i).setColor(c);
		}

		for (int i = 0; i < list.size(); i++) {
			Player p = list.get(i).getPlayer();
			Bukkit.broadcastMessage(p.getName() + " " + list.get(i).getImposterString() + ", Color = " + this.getColorName(this.getRoleOfPlayer(p).getColor()));
		}
	}
	private void stopGame(CommandSender sender) {
		this.gameStatus = false;

		// free objects
		this.classes = null;
		this.bodies = null;

		this.list.clear();
	}
	private void setInventory(Block block,  Role role, Player p) {
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
	private void setKillTimer(Player p, Role role) {
		if (role.isImposter() == false)
			return ;
		if (p.getInventory().getItem(0) != null)
			return;
		new SelfCancelingTask(this, 25, p).runTaskTimer(this, 0, 20L);
	}

	// Useful Functions
	public Role getRoleOfPlayer(Player p) {
		for (int i = 0; i < this.list.size(); i++) {
			if (p.getUniqueId() == this.list.get(i).getPlayer().getUniqueId())
				return this.list.get(i);
		}
		return null;
	}
	private boolean hasColorBeenUsed(Color color) {
		for (int i = 0; i < this.list.size(); i++) {
			if (this.list.get(i).getColor() == null)
				return false;
			if (this.list.get(i).getColor().asRGB() == color.asRGB())
				return true;
		}
		return false;
	}
	public String getColorName(Color color) {
		if (color == null)
			return "error";
		if (color.asRGB() == Color.AQUA.asRGB())
			return "aqua";
		else if (color.asRGB() == Color.BLACK.asRGB())
			return "black";
		else if (color.asRGB() == Color.BLUE.asRGB())
			return "blue";
		else if (color.asRGB() == Color.FUCHSIA.asRGB())
			return "fuchsia";
		else if (color.asRGB() == Color.GRAY.asRGB())
			return "gray";
		else if (color.asRGB() == Color.GREEN.asRGB())
			return "green";
		else if (color.asRGB() == Color.LIME.asRGB())
			return "lime";
		else if (color.asRGB() == Color.MAROON.asRGB())
			return "maroon";
		else if (color.asRGB() == Color.NAVY.asRGB())
			return "navy";
		else if (color.asRGB() == Color.OLIVE.asRGB())
			return "olive";
		else if (color.asRGB() == Color.ORANGE.asRGB())
			return "orange";
		else if (color.asRGB() == Color.PURPLE.asRGB())
			return "purple";
		else if (color.asRGB() == Color.RED.asRGB())
			return "red";
		else if (color.asRGB() == Color.SILVER.asRGB())
			return "silver";
		else if (color.asRGB() == Color.TEAL.asRGB())
			return "teal";
		else if (color.asRGB() == Color.YELLOW.asRGB())
			return "yellow";
		else if (color.asRGB() == Color.WHITE.asRGB())
			return "white";
		else
			return null;
	}
	public Classes getClasses() {
		return this.classes;
	}
	public Deadbodies getBodies() {
		return this.bodies;
	}

	// 0 kill. 1 vent. 2 report. 3 sabotage
	// Listeners
	@EventHandler
	public void onLook(PlayerMoveEvent event) {
		if (!this.gameStatus)
			return ;
		Player p = event.getPlayer();
		Role role = this.getRoleOfPlayer(p);
		if (role == null)
			return ;
		Block block = event.getPlayer().getTargetBlock(null, 4);
		this.setInventory(block, role, p);
		this.setKillTimer(p, this.getRoleOfPlayer(p));
	}

	@EventHandler
	public void onThrow(ProjectileLaunchEvent event) {
		if (event.getEntity() instanceof EnderPearl)
			event.setCancelled(true);
	}

	@EventHandler
	public void onRightClick(PlayerInteractEvent event) {
		if (!this.gameStatus) {
			return;
		}
		ItemStack item = event.getItem();
		if (item == null || item.getType() == Material.AIR)
			return ;
		Player p = event.getPlayer();
		if (item.equals(this.classes.getInnocent().getReport()))
			p.sendMessage("report g");
		else if (item.equals(this.classes.getImposter().getVent()))
			p.sendMessage("vent g");
	}

	@EventHandler
	public void onPlayerClick(PlayerInteractEntityEvent event) {
		if (!this.gameStatus) {
			Bukkit.broadcastMessage("false");
			return;
		}
		Player p = event.getPlayer();
		if (!this.getRoleOfPlayer(p).isImposter()) {// if innocent tries killing
			Bukkit.broadcastMessage("false");
			return;
		}
		if (!(p.getInventory().getItemInMainHand().equals(this.classes.getImposter().getKill()))) {
			Bukkit.broadcastMessage("false hand");
			return ;
		}
		if (event.getRightClicked().getType() != EntityType.PLAYER)  {// type is not player
			Bukkit.broadcastMessage("not player");
			return;
		}
		if (this.getRoleOfPlayer((Player) event.getRightClicked()).isImposter() == true) {
			Bukkit.broadcastMessage("rightclicker is impost");
			return ;
		}

		Player killer = p;
		Player dead = (Player) event.getRightClicked();

		this.getRoleOfPlayer(dead).setDead(); // player has died
		dead.setGameMode(GameMode.SPECTATOR);
		this.bodies.spawnBody(this.getRoleOfPlayer(dead).getColor(), dead.getLocation());

		Bukkit.broadcastMessage(killer.getName() + " killed " + dead.getName());
		this.classes.getImposter().removeKill(killer);
	}
}

