package me.Dopeey.AmongUs;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.util.EulerAngle;

import java.util.ArrayList;
import java.util.HashMap;


//~ ~-1.3 ~
//		{
//		Pose:{Body:[92f,0f,0f]
//		LeftArm:[89f,0f,0f]
//		RightArm:[89f,0f,0f]
public class Deadbodies {

	ArrayList<ArmorStand> bodies = new ArrayList<ArmorStand>();

	public void spawnBody(Color color, Location location) {
		location.setY(location.getY() - 1.3);
		ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);

		armorStand.getEquipment().setChestplate(this.colorItem(color, Material.LEATHER_CHESTPLATE));
		armorStand.setBodyPose(new EulerAngle(140,0f,0f));
		//armorStand.setLeftArmPose(new EulerAngle(90f,0f,0f));
		//armorStand.setRightArmPose(new EulerAngle(90f,0f,0f));
		armorStand.setGravity(false);
		armorStand.setVisible(false);
		armorStand.setBasePlate(false);
		this.bodies.add(armorStand);
	}

	private ItemStack colorItem(Color color, Material material) {
		ItemStack item = new ItemStack(material, 1);
		LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
		meta.setColor(color);
		item.setItemMeta(meta);
		return item;
	}

	public void removeBodies() {
		for (int i = 0; i < this.bodies.size(); i++)
			this.bodies.get(i).remove();
		this.bodies.clear();
	}

	// Getters
	public boolean isBody(Player p) {
		for (Entity e : p.getNearbyEntities(3, 3, 3)) {
			if (e.getType() == EntityType.ARMOR_STAND)
				return true;
		}
		return false;
	}
}
