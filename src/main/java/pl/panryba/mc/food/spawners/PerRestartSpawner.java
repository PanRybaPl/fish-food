package pl.panryba.mc.food.spawners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author PanRyba.pl
 */
public class PerRestartSpawner implements InteractSpawner {

    private Set<String> received;
    Location location;
    Location tpLocation;
    ItemStack gift;
    String okMessage;
    String alreadyMessage;

    @Override
    public void init(Map<String, Object> config) {
        received = new HashSet<>();

        this.location = new Location(
                Bukkit.getWorld((String)config.get("world")),
                (int)config.get("x"),
                (int)config.get("y"),
                (int)config.get("z")
        );

        Map<String, Object> tpConfig = (Map<String, Object>)config.get("tp");

        this.tpLocation = new Location(
                Bukkit.getWorld((String)tpConfig.get("world")),
                (int)tpConfig.get("x"),
                (int)tpConfig.get("y"),
                (int)tpConfig.get("z"),
                ((Double)tpConfig.get("yaw")).floatValue(),
                ((Double)tpConfig.get("pitch")).floatValue()
        );

        this.gift = new ItemStack(
                Material.getMaterial((String)config.get("material")),
                (int)config.get("qty"));

        this.okMessage = ChatColor.translateAlternateColorCodes('&', (String)config.get("ok"));
        this.alreadyMessage = ChatColor.translateAlternateColorCodes('&', (String)config.get("already"));
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if(block == null) {
            return;
        }

        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        if(!block.getLocation().equals(this.location)) {
            return;
        }

        Player player = event.getPlayer();
        String lowerName = player.getName().toLowerCase();

        if(!received.contains(lowerName)) {
            player.getInventory().addItem(gift.clone());
            player.sendMessage(this.okMessage);
            received.add(lowerName);
        } else {
            player.sendMessage(this.alreadyMessage);
        }

        player.teleport(tpLocation);
    }
}
