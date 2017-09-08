package me.xnuminousx.korra.highjump;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import com.projectkorra.projectkorra.ability.CoreAbility;


public class HighJumpListener implements Listener {

	@EventHandler
	public void onSneak(PlayerToggleSneakEvent event) {
		if (event.isCancelled()) {
			return;
		
		} else if (CoreAbility.hasAbility(event.getPlayer(), HighJump.class)) {
			return;
	
		}
		new HighJump(event.getPlayer());

	}
	@EventHandler
	public void onSwing(PlayerAnimationEvent event) {
		if (event.isCancelled()) {
		    return;
		} else if (CoreAbility.hasAbility(event.getPlayer(), HighJump.class)) {
			return;
		}
		new HighJump(event.getPlayer());
	}

}
