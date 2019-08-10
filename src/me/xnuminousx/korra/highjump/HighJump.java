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

	private Permission perm;
	
	private boolean enableLunge;
	private boolean enableJump;
	private boolean enableDoubleJump;
	private boolean enableEvade;
	private boolean playParticles;
	
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
		this.playParticles = ConfigManager.getConfig().getBoolean("ExtraAbilities.xNuminousx.HighJump.Particles.Enabled");
		
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

		this.location = player.getLocation().clone();
	}

	@Override
	public void progress() {
		if (player.isDead() || !player.isOnline()) {
			remove();
			return;
		}

		switch (this.highJumpType) {
			case DOUBLEJUMP: this.onDoubleJump();
			case EVADE: this.onEvade();
			case JUMP: this.onJump();
			case LUNGE: this.onLunge();
		}
	}
	private void onDoubleJump() {
		if (enableDoubleJump && this.highJumpType == HighJumpType.DOUBLEJUMP) {
			Vector vec = player.getVelocity();
			vec.setY(doubleJumpHeight);
			player.setVelocity(vec);
			poof();
			bPlayer.addCooldown(this, doubleJumpCooldown);
			remove();
		}
	}
	private void onEvade() {
		if (enableEvade && this.highJumpType == HighJumpType.EVADE) {
			Vector vec = player.getLocation().getDirection().normalize().multiply(-evadeDistance);
			vec.setY(evadeHeight);
			player.setVelocity(vec);
			poof();
			bPlayer.addCooldown(this, evadeCooldown);
			remove();
		}
	}
	private void onJump() {
		if (enableJump && this.highJumpType == HighJumpType.JUMP) {
			Vector vec = player.getVelocity();
			vec.setY(jumpHeight);
			player.setVelocity(vec);
			poof();
			bPlayer.addCooldown(this, jumpCooldown);
			remove();
		}
	}
	private void onLunge() {
		if (enableLunge && this.highJumpType == HighJumpType.LUNGE) {
			Vector vec = player.getLocation().getDirection().normalize().multiply(lungeDistance);
			vec.setY(lungeHeight);
			player.setVelocity(vec);
			poof();
			bPlayer.addCooldown(this, lungeCooldown);
			remove();
		}
	}
	private void poof() {
		if (playParticles) {
			player.getLocation();
			ParticleEffect.CRIT.display(location, 20, 0.5F, 1F, 0.5F, 0.5F);
			ParticleEffect.CLOUD.display(location, 30, 0.5F, 1F, 0.5F, 0.002F);
		}
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
		switch (this.highJumpType) {
			case JUMP: return jumpCooldown;
			case EVADE: return evadeCooldown;
			case LUNGE: return lungeCooldown;
			case DOUBLEJUMP: return doubleJumpCooldown;
			default: return 0;
		}
	}

	@Override
	public String getName() {
		return "HighJump";
	}
	@Override
	public String getDescription() {
		return ConfigManager.languageConfig.get().getString("ExtraAbilities.xNuminousx.HighJump.Description");
	}
	@Override
	public String getInstructions() {
		return ConfigManager.languageConfig.get().getString("ExtraAbilities.xNuminousx.HighJump.Instructions");
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public String getVersion() {
		return "MC-1.13.2 / PK-1.8.8 / v1.12";
	}

	@Override
	public String getAuthor() {
		return "Numin";
	}

	@Override
	public void load() {
		ProjectKorra.plugin.getServer().getPluginManager().registerEvents(new HighJumpListener(), ProjectKorra.plugin);
		
		ConfigManager.languageConfig.get().addDefault("ExtraAbilities.xNuminousx.HighJump.Description", "Chiblockers are skilled acrobats and this HighJump Replacement satisfies those abilities! Now, you can lunge forward, lunge backwards, activate a double jump, or use the classic HighJump if you so desire!");
		ConfigManager.languageConfig.get().addDefault("ExtraAbilities.xNuminousx.HighJump.Instructions", "Left-Click: Jump up. Tap-Shift: Lunge backwards. Spint+Click: Lunge Forwards. Tap-Shift in the air: Double Jump");
		
		ConfigManager.getConfig().addDefault("ExtraAbilities.xNuminousx.HighJump.Particles.Enabled", true);
		
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
		ConfigManager.languageConfig.save();

		this.perm = new Permission("bending.ability." + this.getName().toLowerCase());
		perm.setDefault(PermissionDefault.TRUE);
		ProjectKorra.plugin.getServer().getPluginManager().addPermission(perm);

		ProjectKorra.log.info("Successfully enabled " + getName() + " by " + getAuthor());
	}

	@Override
	public void stop() {
		ProjectKorra.plugin.getServer().getPluginManager().removePermission(perm);
		super.remove();
		ProjectKorra.log.info("Successfully disabled " + getName() + " by " + getAuthor());
	}
}