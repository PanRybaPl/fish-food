package pl.panryba.mc.food.spawners;

import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Map;

/**
 * @author PanRyba.pl
 */
public interface InteractSpawner
{
    void init(Map<String, Object> config);
    void onInteract(PlayerInteractEvent event);
}
