package me.xnuminousx.korra.highjump;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import com.projectkorra.projectkorra.BendingPlayer;

import me.xnuminousx.korra.highjump.HighJump.HighJumpType;


public class HighJumpListener implements Listener {

	@EventHandler
	public void onSneak(PlayerToggleSneakEvent event) {

		boolean isOnBlock = false;
		Player player = event.getPlayer();
		BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);
		Material block = player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType();
		
		if (block.isSolid()) {
			isOnBlock = true;
		}
		
		if (bPlayer.getBoundAbilityName().equalsIgnoreCase("HighJump")) {
			if (isOnBlock) {
				new HighJump(player, HighJumpType.EVADE);
			} else {
				new HighJump(player, HighJumpType.DOUBLEJUMP);
			}
		}
	}
	@EventHandler
	public void onSwing(PlayerAnimationEvent event) {
		Player player = event.getPlayer();
		BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);

		if (bPlayer.getBoundAbilityName().equalsIgnoreCase("HighJump")) {
			if (player.isSprinting()) {
				new HighJump(player, HighJumpType.LUNGE);
			} else {
				new HighJump(player, HighJumpType.JUMP);
			}
		}
	}
}
