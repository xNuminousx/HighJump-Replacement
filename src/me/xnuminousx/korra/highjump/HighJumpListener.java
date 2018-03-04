package me.xnuminousx.korra.highjump;

import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.GeneralMethods;

import me.xnuminousx.korra.highjump.HighJump.HighJumpType;


public class HighJumpListener implements Listener {
	
	private boolean isOnBlock;
	private boolean isSprinting;

	@EventHandler
	public void onSneak(PlayerToggleSneakEvent event) {

		isOnBlock = false;
		Player player = event.getPlayer();
		BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);
		
		if (GeneralMethods.isSolid(player.getLocation().getBlock().getRelative(BlockFace.DOWN))) {
			isOnBlock = true;
		}

		if (event.isCancelled() || bPlayer == null) {
			return;
		} else if (bPlayer.getBoundAbilityName().equalsIgnoreCase(null)) {
			return;
		} else if (bPlayer.getBoundAbilityName().equalsIgnoreCase("HighJump") && (isOnBlock == true)) {
			new HighJump(player, HighJumpType.EVADE);
		} else if (bPlayer.getBoundAbilityName().equalsIgnoreCase("HighJump") && (isOnBlock == false)) {
			new HighJump(player, HighJumpType.DOUBLEJUMP);
		}

	}
	@EventHandler
	public void onSwing(PlayerAnimationEvent event) {

		isSprinting = false;
		isOnBlock = false;
		Player player = event.getPlayer();
		BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);
		
		if (player.isSprinting()) {
			isSprinting = true;
		}
		if (GeneralMethods.isSolid(player.getLocation().getBlock().getRelative(BlockFace.DOWN))) {
			isOnBlock = true;
		}

		if (event.isCancelled() || bPlayer == null) {
			return;

		} else if (bPlayer.getBoundAbilityName().equalsIgnoreCase(null)) {
			return;
		} else if (bPlayer.getBoundAbilityName().equalsIgnoreCase("HighJump") && (isSprinting == true) && (isOnBlock == true)) {
			new HighJump(player, HighJumpType.LUNGE);
		} else if (bPlayer.getBoundAbilityName().equalsIgnoreCase("HighJump") && (isSprinting == false) && (isOnBlock == false)) {
			new HighJump(player, HighJumpType.JUMP);
		}
	}

}
