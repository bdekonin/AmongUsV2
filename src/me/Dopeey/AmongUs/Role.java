package me.Dopeey.AmongUs;

import org.bukkit.Color;
import org.bukkit.entity.Player;

public class Role {

	// Globals
	private Player player;
	private boolean isImposter;
	private boolean alive = true;
	private Color color = null;

	// Constructor
	public Role(Player player, boolean isImposter) {
		this.isImposter = isImposter;
		this.player = player;
	}

	// Getters
	public boolean isImposter() {
		return this.isImposter;
	}
	public Player getPlayer() {
		return this.player;
	}
	public String getImposterString() {
		if (this.isImposter == false)
			return new String("Innocent");
		else
			return new String("Imposter");
	}
	public boolean getAlive() {
		return this.alive;
	}
	public Color getColor() {
		return this.color;
	}

	// Setters
	public void setImposter(boolean isImposter) {
		this.isImposter = isImposter;
	}
	public void setAlive() {
		this.alive = true;
	}
	public void setDead() {
		this.alive = false;
	}
	public void setColor(Color color) {
		this.color = color;
	}
}
