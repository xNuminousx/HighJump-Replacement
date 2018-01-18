package me.xnuminousx.korra.highjump;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.ChiAbility;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import com.projectkorra.projectkorra.util.ParticleEffect;

public class HighJump extends ChiAbility implements AddonAbility {
	
	public enum HighJumpType {
		CLICK, SHIFT
	}
	private HighJumpType highJumpType;

	private long jumpHeight;
	private long lungeHeight;
	private long evadeHeight;
	private long evadeDistance;
	private long lungeDistance;
	private long cooldown;
	
	private Location location;
	private Location origin;
	
	private boolean enableLunge;
	private boolean enableJump;
	private boolean enableEvade;
	
	public HighJump(Player player, HighJumpType highJumpType) {
		super(player);
		
		if (!bPlayer.canBend(this)) {
			return;
		}
		
		this.highJumpType = highJumpType;
		setFields();
		start();
	}

	private void setFields() {
		this.enableEvade = ConfigManager.getConfig().getBoolean("ExtraAbilities.xNuminousx.HighJump.Evade.Enabled");
		this.enableJump = ConfigManager.getConfig().getBoolean("ExtraAbilities.xNuminousx.HighJump.Jump.Enabled");
		this.enableLunge = ConfigManager.getConfig().getBoolean("ExtraAbilities.xNuminousx.HighJump.Lunge.Enabled");
		
		this.jumpHeight = ConfigManager.getConfig().getLong("ExtraAbilities.xNuminousx.HighJump.Jump.Height");
		
		this.lungeHeight = ConfigManager.getConfig().getLong("ExtraAbilities.xNuminousx.HighJump.Lunge.Height");
		this.lungeDistance = ConfigManager.getConfig().getLong("ExtraAbilities.xNuminousx.HighJump.Lunge.Distance");
		
		this.evadeHeight = ConfigManager.getConfig().getLong("ExtraAbilities.xNuminousx.HighJump.Evade.Height");
		this.evadeDistance = ConfigManager.getConfig().getLong("ExtraAbilities.xNuminousx.HighJump.Evade.Distance");
		
		this.cooldown = ConfigManager.getConfig().getLong("ExtraAbilities.xNuminousx.HighJump.Cooldown");
		this.origin = player.getLocation().clone();
		this.location = origin.clone();
		
	}

	@Override
	public void progress() {
		if (player.isDead() || !player.isOnline() || !GeneralMethods.isSolid(player.getLocation().getBlock().getRelative(BlockFace.DOWN))) {
			remove();
			return;
		}
		if (this.highJumpType == HighJumpType.SHIFT && enableEvade) {
			onShift();
			poof();
			remove();
		} else if (this.highJumpType == HighJumpType.CLICK) {
			if (player.isSprinting() && enableLunge) {
				onSprint();
				poof();
				remove();
			} else if (enableJump) {
				onClick();
				poof();
				remove();
			}
		}
	}
	private void onShift() {
		Vector vec = player.getLocation().getDirection().normalize().multiply(-evadeDistance);
		vec.setY(evadeHeight);
		player.setVelocity(vec);
		return;
	}
	private void onSprint() {
		Vector vec = player.getLocation().getDirection().normalize().multiply(lungeDistance);
		vec.setY(lungeHeight);
		player.setVelocity(vec);
		return;
	}
	private void onClick() {
		Vector vec = player.getVelocity();
		vec.setY(jumpHeight);
		player.setVelocity(vec);
		return;
	}
	private void poof() {
		player.getLocation();
		ParticleEffect.CRIT.display(location, 0.5F, 1F, 0.5F, 0.5F, 20);
		ParticleEffect.CLOUD.display(location, 0.5F, 1F, 0.5F, 0.002F, 30);
	}
	
	@Override
	public void remove() {
		bPlayer.addCooldown(this);
		super.remove();
		return;
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
		return "Chiblockers are skilled acrobats and this HighJump Replacement satisfies those abilities! Now, you can lunge forward, lunge backwards, or use the classic HighJump if you so desire!";
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
		return "1.7";
	}


	@Override
	public String getAuthor() {
		return "xNuminousx";
	}

	@Override
	public void load() {
		ProjectKorra.plugin.getServer().getPluginManager().registerEvents(new HighJumpListener(), ProjectKorra.plugin);
		
		ConfigManager.getConfig().addDefault("ExtraAbilities.xNuminousx.HighJump.Jump.Enabled", true);
		ConfigManager.getConfig().addDefault("ExtraAbilities.xNuminousx.HighJump.Lunge.Enabled", true);
		ConfigManager.getConfig().addDefault("ExtraAbilities.xNuminousx.HighJump.Evade.Enabled", true);
		
		ConfigManager.getConfig().addDefault("ExtraAbilities.xNuminousx.HighJump.Cooldown", 5000);
		
		ConfigManager.getConfig().addDefault("ExtraAbilities.xNuminousx.HighJump.Jump.Height", 1);
		
		ConfigManager.getConfig().addDefault("ExtraAbilities.xNuminousx.HighJump.Lunge.Height", 1);
		ConfigManager.getConfig().addDefault("ExtraAbilities.xNuminousx.HighJump.Lunge.Distance", 2);
		
		ConfigManager.getConfig().addDefault("ExtraAbilities.xNuminousx.HighJump.Evade.Height", 1);
		ConfigManager.getConfig().addDefault("ExtraAbilities.xNuminousx.HighJump.Evade.Distance", 2);
		ConfigManager.defaultConfig.save();
		
		ProjectKorra.log.info("Successfully enabled " + getName() + " by " + getAuthor());
	}

	@Override
	public void stop() {
		ProjectKorra.log.info("Successfully disabled " + getName() + " by " + getAuthor());
		
		super.remove();
	}

}
