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


	// Constructor
	public Classes() {
		this.imposter = new Imposter();
		this.innocent = new Innocent();

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


// 0: Use. 1: Report. 2: Kill. 3: Sabotage. 4: Vent

// Imposter
// 1: Kill. 2: Report. 3: Sabotage

// Innocent
// 2: Report
class Imposter {
	// Items
		final private ItemStack kill = new ItemStack(Material.NETHERITE_AXE, 1);
		final private String killName = ChatColor.DARK_RED + "Kill";
		final private ItemStack sabotage = new ItemStack(Material.IRON_HORSE_ARMOR, 1);
		final private String sabotageName = ChatColor.AQUA + "Sabotage";
		final private ItemStack vent = new ItemStack(Material.ENDER_PEARL, 1);
		final private String ventName = ChatColor.YELLOW + "Vent";

	// Constructor
	public Imposter() {
		this.setKill();
		this.setSabotage();
		this.setVent();
	}

	// Main Functions
	public void vent(Player player) {
		player.getInventory().setItem(4, this.getVent());
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
		private ItemStack use = new ItemStack(Material.BRICK, 1);
		private String useName = ChatColor.GREEN + "Use";
		private ItemStack report = new ItemStack(Material.EMERALD, 1); // temp item
		private String reportName = ChatColor.DARK_PURPLE + "Report";

	// Constructor
	public Innocent( ) {
		this.setUse();
		this.setReport();
	}

	// Main Functions
	// blabla
	// blabla
	// blabla

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