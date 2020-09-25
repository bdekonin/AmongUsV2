package me.Dopeey.AmongUs;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class SelfCancelingTask extends BukkitRunnable {

	private final Main plugin;
	private Classes classes;
	private Player player;
	private int counter;

	private String displayNameOne = ChatColor.RED + "Ready in ";
	private String displayNametwo = ChatColor.RED + "Seconds";

	public SelfCancelingTask(Main plugin, int counter, Player player) {
		this.plugin = plugin;
		this.player = player;
		if (counter <= 0) {
			throw new IllegalArgumentException("counter must be greater than 0");
		}
		else {
			this.counter = counter;
		}
		this.classes = this.plugin.getClasses();
	}

	@Override
	public void run() {
		if (counter > 0) {
			this.player.getInventory().setItem(0, this.classes.getImposter().getkillBuff(counter, "test"));
		}
		else {
			this.classes.getImposter().kill(this.player);
			this.cancel();
		}
		counter--;
	}

}
