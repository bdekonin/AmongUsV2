package me.Dopeey.AmongUs;

import org.bukkit.entity.Player;

public class Role {

	// Globals
	private Player player;
	private boolean isImposter;

	// Constructor
	public Role(Player player, boolean isImposter) {
		this.isImposter = isImposter;
		this.player = player;
	}

	// Getters
	public boolean getImposter() {
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

	// Setters
	public void setImposter(boolean isImposter) {
		this.isImposter = isImposter;
	}
}
