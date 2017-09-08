package me.xnuminousx.korra.highjump;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.util.Vector;

import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.ChiAbility;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import com.projectkorra.projectkorra.util.ParticleEffect;

public class HighJump extends ChiAbility implements AddonAbility {

	private long height;
	private long distance;
	private long lungeMultiplier;
	private long cooldown;
	private Permission perm;
	
	private Location location;
	private Location origin;
	
	public HighJump(Player player) {
		super(player);
		if (!bPlayer.canBend(this)) {
			return;
		}
		this.height = ConfigManager.getConfig().getLong("ExtraAbilities.xNuminousx.HighJump.Height");
		this.distance = ConfigManager.getConfig().getLong("ExtraAbilities.xNuminousx.HighJump.Distance");
		this.lungeMultiplier = ConfigManager.getConfig().getLong("ExtraAbilities.xNuminousx.HighJump.LungeMultiplier");
		this.cooldown = ConfigManager.getConfig().getLong("ExtraAbilities.xNuminousx.HighJump.Cooldown");
		this.origin = player.getLocation().clone();
		this.location = origin.clone();
		start();
	}

	@Override
	public void progress() {
		if (!com.projectkorra.projectkorra.GeneralMethods.isSolid(player.getLocation().getBlock().getRelative(org.bukkit.block.BlockFace.DOWN)) || player.isDead() || !player.isOnline()) {
			remove();
			return;
		}
		if (player.isSneaking()) {
			onShift();
		} else {
			onClick();
		}
		if (player.isSprinting()) {
			onSprint();
		}
		poof();
		bPlayer.addCooldown(this);
		remove();
		return;
	}
	private void onShift() {
		Vector vec = player.getLocation().getDirection().normalize().multiply(-distance);
		vec.setY(height);
		player.setVelocity(vec);
		return;
	}
	private void onSprint() {
		Vector vec = player.getLocation().getDirection().normalize().multiply(distance * lungeMultiplier);
		vec.setY(height);
		player.setVelocity(vec);
		return;
	}
	private void onClick() {
		Vector vec = player.getVelocity();
		vec.setY(height);
		player.setVelocity(vec);
		return;
	}
	private void poof() {
		player.getLocation();
		ParticleEffect.CRIT.display(location, 0.5F, 1F, 0.5F, 0.5F, 20);
		ParticleEffect.CLOUD.display(location, 0.5F, 1F, 0.5F, 0.002F, 30);
	}
	
	@Override
	public boolean isSneakAbility() {
		return true;
		
	}

	@Override
	public boolean isHarmlessAbility() {
		return true;
	}

	@Override
	public long getCooldown() {
		return cooldown;
	}

	@Override
	public String getName() {
		return "HighJump";
	}
	@Override
	public String getDescription() {
		return "As a replacement for the original HighJump, this ability offers you many modes of mobility. When you tap shift you will be lunged backwards; possibly to escape enemies. When you left click you will jump high in the sky; possibly to dodge obstacles. If you left click WHILE sprinting, you will lunge forward; possibly as a way to get closer to your target. Enjoy!";
	}
	@Override
	public String getInstructions() {
		return "Left-Click: Jump up. Tap-Shift: Lunge backwards. Spint+Click: Lunge Forwards.";
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public String getVersion() {
		return "v1.6";
	}


	@Override
	public String getAuthor() {
		return "xNuminousx";
	}

	@Override
	public void load() {
		ProjectKorra.plugin.getServer().getPluginManager().registerEvents(new HighJumpListener(), ProjectKorra.plugin);
		
		perm = new Permission("bending.ability.highjump");
		ProjectKorra.plugin.getServer().getPluginManager().addPermission(perm);
		perm.setDefault(PermissionDefault.TRUE);
		
		ConfigManager.getConfig().addDefault("ExtraAbilities.xNuminousx.HighJump.Cooldown", 5000);
		ConfigManager.getConfig().addDefault("ExtraAbilities.xNuminousx.HighJump.Height", 1);
		ConfigManager.getConfig().addDefault("ExtraAbilities.xNuminousx.HighJump.Distance", 1);
		ConfigManager.getConfig().addDefault("ExtraAbilities.xNuminousx.HighJump.LungeMultiplier", 2);
		ConfigManager.defaultConfig.save();
		ProjectKorra.log.info("Successfully loaded " + getName() + " by " + getAuthor());
	}

	@Override
	public void stop() {
		ProjectKorra.plugin.getServer().getLogger().info(getName() + " " + getVersion() + " by " + getAuthor() + " has been disabled!");
		
		ProjectKorra.plugin.getServer().getPluginManager().removePermission(this.perm);
		
		super.remove();
	}

}
