package pl.panryba.mc.food.spawners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import pl.panryba.mc.food.Spawner;

import java.util.Map;

public class ContainerSpawner implements Spawner {
    private Material material;

    Location location;
    Chest chest;

    int qty;
    Integer damage;
    Integer max;

    private int ticks;

    public ContainerSpawner() {
    }

    public void init(Map<String, Object> config) {
        String worldName = (String)config.get("world");

        ticks = (Integer)config.get("ticks");
        qty = (Integer)config.get("qty");
        damage = (Integer)config.get("damage");
        max = (Integer)config.get("max");
        material = Material.valueOf((String) config.get("material"));

        int x = (Integer)config.get("x");
        int y = (Integer)config.get("y");
        int z = (Integer)config.get("z");
        World world = Bukkit.getWorld(worldName);

        location = new Location(world, x, y, z);
    }

    public void spawn() {
        Block block = this.location.getWorld().getBlockAt(this.location);
        if(!(block.getState() instanceof InventoryHolder)) {
            return;
        }

        InventoryHolder holder = (InventoryHolder)block.getState();

        int current = 0;
        Inventory inventory = holder.getInventory();

        for(ItemStack inChest : inventory) {
            if(inChest != null && inChest.getType() == this.material) {
                current += inChest.getAmount();

                // If there's already at least a maximum of same items then don't spawn
                if(current >= this.max) {
                    return;
                }
            }
        }

        int toAdd = Math.min(this.qty, max - current);
        if(toAdd <= 0) {
            return;
        }

        ItemStack stack;
        if(this.damage != null) {
            stack = new ItemStack(this.material, toAdd, damage.shortValue());
        } else {
            stack = new ItemStack(this.material, toAdd);
        }

        inventory.addItem(stack);
    }

    public int getTicks() {
        return this.ticks;
    }
}
