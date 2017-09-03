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

	private int height;
	private int distance;
	private long cooldown;
	private Permission perm;
	
	private Location location;
	private Location origin;
	
	public HighJump(Player player) {
		super(player);
		if (!bPlayer.canBend(this)) {
			return;
		}
		this.height = ConfigManager.getConfig().getInt("ExtraAbilities.xNuminousx.HighJump.Height");
		this.distance = ConfigManager.getConfig().getInt("ExtraAbilities.xNuminousx.HighJump.Distance");
		this.cooldown = ConfigManager.getConfig().getLong("ExtraAbilities.xNuminousx.HighJump.Cooldown");
		this.origin = player.getLocation().clone();
		this.location = origin.clone();
		start();
	}

	@Override
	public void progress() {
		if (!com.projectkorra.projectkorra.GeneralMethods.isSolid(player.getLocation().getBlock().getRelative(org.bukkit.block.BlockFace.DOWN))) {
			remove();
			return;
		}
		if (!bPlayer.canBendIgnoreBindsCooldowns(this)) {
			return;
		}
		if (player.isSneaking()) {
			Vector vec = player.getLocation().getDirection().normalize().multiply(-distance);
			vec.setY(height);
			player.setVelocity(vec);
			poof ();
			bPlayer.addCooldown(this);
			remove();
			return;
		}

	}
	private void poof() {
		player.getLocation();
		ParticleEffect.CRIT.display(location, 1F, 0, 1F, 0.5F, 50);
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
	
	public String getDescription() {
		return "A replacement for the original HighJump, tap shift and you will jump backwards!";
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public String getVersion() {
		return "v1.4.0";
	}


	@Override
	public String getAuthor() {
		return "xNuminousx";
	}

	@Override
	public void load() {
		ProjectKorra.plugin.getServer().getPluginManager().registerEvents(new HighJumpListener(), ProjectKorra.plugin);
		ProjectKorra.log.info("Successfully loaded" + getName() + "by " + getAuthor());
		
		perm = new Permission("bending.ability.HighJump");
		ProjectKorra.plugin.getServer().getPluginManager().addPermission(perm);
		perm.setDefault(PermissionDefault.TRUE);
		
		ConfigManager.getConfig().addDefault("ExtraAbilities.xNuminousx.HighJump.Cooldown", 5000);
		ConfigManager.getConfig().addDefault("ExtraAbilities.xNuminousx.HighJump.Height", 1);
		ConfigManager.getConfig().addDefault("ExtraAbilities.xNuminousx.HighJump.Distance", 1);
		ConfigManager.defaultConfig.save();
		

	}

	@Override
	public void stop() {

	}

}
