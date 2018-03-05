package me.xnuminousx.korra.highjump;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.ChiAbility;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import com.projectkorra.projectkorra.util.ParticleEffect;

public class HighJump extends ChiAbility implements AddonAbility {
	
	public enum HighJumpType {
		EVADE, LUNGE, DOUBLEJUMP, JUMP
	}
	private HighJumpType highJumpType;

	private long jumpCooldown;
	private long jumpHeight;
	private long lungeCooldown;
	private long lungeHeight;
	private long evadeCooldown;
	private long evadeHeight;
	private long doubleJumpCooldown;
	private long doubleJumpHeight;
	private long evadeDistance;
	private long lungeDistance;
	
	private Location location;
	private Location origin;
	
	private boolean enableLunge;
	private boolean enableJump;
	private boolean enableDoubleJump;
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
		this.enableDoubleJump = ConfigManager.getConfig().getBoolean("ExtraAbilities.xNuminousx.HighJump.DoubleJump.Enabled");
		this.enableLunge = ConfigManager.getConfig().getBoolean("ExtraAbilities.xNuminousx.HighJump.Lunge.Enabled");
		
		this.jumpCooldown = ConfigManager.getConfig().getLong("ExtraAbilities.xNuminousx.HighJump.Jump.Cooldown");
		this.jumpHeight = ConfigManager.getConfig().getLong("ExtraAbilities.xNuminousx.HighJump.Jump.Height");
		
		this.doubleJumpCooldown = ConfigManager.getConfig().getLong("ExtraAbilities.xNuminousx.HighJump.DoubleJump.Cooldown");
		this.doubleJumpHeight = ConfigManager.getConfig().getLong("ExtraAbilities.xNuminousx.HighJump.DoubleJump.Height");
		
		this.lungeCooldown = ConfigManager.getConfig().getLong("ExtraAbilities.xNuminousx.HighJump.Lunge.Cooldown");
		this.lungeHeight = ConfigManager.getConfig().getLong("ExtraAbilities.xNuminousx.HighJump.Lunge.Height");
		this.lungeDistance = ConfigManager.getConfig().getLong("ExtraAbilities.xNuminousx.HighJump.Lunge.Distance");
		
		this.evadeCooldown = ConfigManager.getConfig().getLong("ExtraAbilities.xNuminousx.HighJump.Evade.Cooldown");
		this.evadeHeight = ConfigManager.getConfig().getLong("ExtraAbilities.xNuminousx.HighJump.Evade.Height");
		this.evadeDistance = ConfigManager.getConfig().getLong("ExtraAbilities.xNuminousx.HighJump.Evade.Distance");
		this.origin = player.getLocation().clone();
		this.location = origin.clone();
		
	}

	@Override
	public void progress() {
		if (player.isDead() || !player.isOnline()) {
			remove();
			return;
		}
		if (this.highJumpType == HighJumpType.EVADE && enableEvade) {
			onEvade();
			bPlayer.addCooldown(this, evadeCooldown);
		} else if (this.highJumpType == HighJumpType.LUNGE && enableLunge) {
			if (player.isSprinting()) {
				onLunge();
				bPlayer.addCooldown(this, lungeCooldown);
			}
		} else if (this.highJumpType == HighJumpType.JUMP && enableJump) {
			onJump();
			bPlayer.addCooldown(this, jumpCooldown);
		} else if (this.highJumpType == HighJumpType.DOUBLEJUMP && enableDoubleJump) {
			onDoubleJump();
			bPlayer.addCooldown(this, doubleJumpCooldown);
		}
	}
	private void onEvade() {
		Vector vec = player.getLocation().getDirection().normalize().multiply(-evadeDistance);
		vec.setY(evadeHeight);
		player.setVelocity(vec);
		poof();
		remove();
		return;
	}
	private void onLunge() {
		Vector vec = player.getLocation().getDirection().normalize().multiply(lungeDistance);
		vec.setY(lungeHeight);
		player.setVelocity(vec);
		poof();
		remove();
		return;
	}
	private void onJump() {
		Vector vec = player.getVelocity();
		vec.setY(jumpHeight);
		player.setVelocity(vec);
		poof();
		remove();
		return;
	}
	private void onDoubleJump() {
		Vector vec = player.getVelocity();
		vec.setY(doubleJumpHeight);
		player.setVelocity(vec);
		poof();
		remove();
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
		return 0;
	}

	@Override
	public String getName() {
		return "HighJump";
	}
	@Override
	public String getDescription() {
		return "Chiblockers are skilled acrobats and this HighJump Replacement satisfies those abilities! Now, you can lunge forward, lunge backwards, activate a double jump, or use the classic HighJump if you so desire!";
	}
	@Override
	public String getInstructions() {
		return "Left-Click: Jump up. Tap-Shift: Lunge backwards. Spint+Click: Lunge Forwards. Tap-Shift in the air: Double Jump";
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public String getVersion() {
		return "1.9";
	}


	@Override
	public String getAuthor() {
		return "xNuminousx";
	}

	@Override
	public void load() {
		ProjectKorra.plugin.getServer().getPluginManager().registerEvents(new HighJumpListener(), ProjectKorra.plugin);
		
		ConfigManager.getConfig().addDefault("ExtraAbilities.xNuminousx.HighJump.Jump.Enabled", true);
		ConfigManager.getConfig().addDefault("ExtraAbilities.xNuminousx.HighJump.DoubleJump.Enabled", true);
		ConfigManager.getConfig().addDefault("ExtraAbilities.xNuminousx.HighJump.Lunge.Enabled", true);
		ConfigManager.getConfig().addDefault("ExtraAbilities.xNuminousx.HighJump.Evade.Enabled", true);
		
		ConfigManager.getConfig().addDefault("ExtraAbilities.xNuminousx.HighJump.Jump.Cooldown", 3000);
		ConfigManager.getConfig().addDefault("ExtraAbilities.xNuminousx.HighJump.Jump.Height", 1);
		
		ConfigManager.getConfig().addDefault("ExtraAbilities.xNuminousx.HighJump.DoubleJump.Cooldown", 2000);
		ConfigManager.getConfig().addDefault("ExtraAbilities.xNuminousx.HighJump.DoubleJump.Height", 1);
		
		ConfigManager.getConfig().addDefault("ExtraAbilities.xNuminousx.HighJump.Lunge.Cooldown", 5000);
		ConfigManager.getConfig().addDefault("ExtraAbilities.xNuminousx.HighJump.Lunge.Height", 1);
		ConfigManager.getConfig().addDefault("ExtraAbilities.xNuminousx.HighJump.Lunge.Distance", 2);
		
		ConfigManager.getConfig().addDefault("ExtraAbilities.xNuminousx.HighJump.Evade.Cooldown", 5000);
		ConfigManager.getConfig().addDefault("ExtraAbilities.xNuminousx.HighJump.Evade.Height", 1);
		ConfigManager.getConfig().addDefault("ExtraAbilities.xNuminousx.HighJump.Evade.Distance", 2);
		ConfigManager.defaultConfig.save();
		
		ProjectKorra.log.info("Successfully enabled " + getName() + " by " + getAuthor());
	}

	@Override
	public void stop() {
		super.remove();
		ProjectKorra.log.info("Successfully disabled " + getName() + " by " + getAuthor());
	}

}
