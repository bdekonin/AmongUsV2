package me.Dopeey.AmongUs;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Classes {

	// Globals
		final private int inventorySize = 36;
		private ItemStack buffer = new ItemStack(Material.CLAY_BALL, 1);
		final private String bufferName = ChatColor.GRAY + "No Usage";

	// Objects
		private final Imposter imposter;
		private final Innocent innocent;

	// Materials for Classes
		final private Material useItem = Material.BRICK; // Both
		final private Material reportItem = Material.BOOK; // Both
		final private Material killItem = Material.NETHERITE_AXE; // Imposter
		final private Material ventItem = Material.ENDER_PEARL; // Imposter
		final private Material sabotageItem = Material.TNT; // Imposter


	// Constructor
	public Classes() {
		this.innocent = new Innocent(this.useItem, this.reportItem);
		this.imposter = new Imposter(this.innocent, this.killItem, this.sabotageItem, this.ventItem);

		this.setName();
	}

	public Inventory getDefaultInventory() {
		Inventory inv = Bukkit.createInventory(null, this.inventorySize);

		for (int i = 0; i < this.inventorySize; i++)
			inv.setItem(i, this.buffer);
		return inv;
	}

	// Getters
	public Imposter getImposter() {
		return this.imposter;
	}
	public Innocent getInnocent() {
		return this.innocent;
	}

	// Helpers
	private void setName() {
		ItemMeta meta = this.buffer.getItemMeta();
		meta.setDisplayName(this.bufferName);
		this.buffer.setItemMeta(meta);
	}
}

// Imposter
// 0 kill. 1 vent. 2 report. 3 sabotage
// 0 kill. 1 Use. 2 report. 3 sabotage

// Innocent
// 0. Use 1: Report

class Imposter {
	// Globals
		private Innocent innocent;
	// Items
		final private ItemStack kill;
		final private String killName = ChatColor.DARK_RED + "Kill";

		final private ItemStack sabotage;
		final private String sabotageName = ChatColor.AQUA + "Sabotage";

		final private ItemStack vent;
		final private String ventName = ChatColor.YELLOW + "Vent";

	// Constructor
	public Imposter(Innocent innocent, Material kill, Material sabotage, Material vent) {
		this.innocent = innocent;
		this.kill = new ItemStack(kill, 1);
		this.sabotage = new ItemStack(sabotage, 1);
		this.vent = new ItemStack(vent, 1);

		this.setKill();
		this.setSabotage();
		this.setVent();
	}

	// Main Functions
	public void kill(Player player) {
		player.getInventory().setItem(0, this.getKill());
	}
	public void use(Player player) {
		player.getInventory().setItem(1, this.innocent.getUse());
	}
	public void vent(Player player) {
		player.getInventory().setItem(1, this.getVent());
	}
	public void report(Player player) {
		player.getInventory().setItem(2, this.innocent.getReport());
	}
	public void sabotage(Player player) {
		player.getInventory().setItem(3, this.getSabotage());
	}


	// Getters
	public ItemStack getKill() {
		return this.kill;
	}
	public ItemStack getVent() {
		return this.vent;
	}
	public ItemStack getSabotage() {
		return this.sabotage;
	}

	// Helpers
	private void setKill() {
		ItemMeta meta = this.kill.getItemMeta();
		meta.setDisplayName(this.killName);
		this.kill.setItemMeta(meta);
	}
	private void setSabotage() {
		ItemMeta meta = this.sabotage.getItemMeta();
		meta.setDisplayName(this.sabotageName);
		this.sabotage.setItemMeta(meta);
	}
	private void setVent() {
		ItemMeta meta = this.vent.getItemMeta();
		meta.setDisplayName(this.ventName);
		this.vent.setItemMeta(meta);
	}
}



class Innocent {
	// Items
		private ItemStack use;
		private String useName = ChatColor.GREEN + "Use";
		private ItemStack report;
		private String reportName = ChatColor.DARK_PURPLE + "Report";

	// Constructor
	public Innocent(Material use, Material report) {
		this.use = new ItemStack(use, 1);
		this.report = new ItemStack(report, 1);

		this.setUse();
		this.setReport();

	}

	// Main Functions
	public void use(Player player) {
		player.getInventory().setItem(0, this.getUse());
	}
	public void report(Player player) {
		player.getInventory().setItem(1, this.getReport());
	}

	// Getters
	public ItemStack getUse() {
		return this.use;
	}
	public ItemStack getReport() {
		return this.report;
	}

	// Helpers
	private void setUse() {
		ItemMeta meta = this.use.getItemMeta();
		meta.setDisplayName(this.useName);
		this.use.setItemMeta(meta);
	}
	private void setReport() {
		ItemMeta meta = this.report.getItemMeta();
		meta.setDisplayName(this.reportName);
		this.report.setItemMeta(meta);
	}
}