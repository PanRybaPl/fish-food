/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.panryba.mc.food.spawners;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.util.Map;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import pl.panryba.mc.food.Spawner;

/**
 *
 * @author PanRyba.pl
 */
public class SimpleEntitySpawn implements Spawner {
    private World world;
    private EntityType entityType;
    private Random rand;
    
    double minx, maxx, miny, maxy, minz, maxz;
    double xDiff, yDiff, zDiff;
    int qty;
    
    private int ticks;

    public SimpleEntitySpawn() {
        this.rand = new Random();        
    }
    
    public void init(Map<String, Object> config) {
        String worldName = (String)config.get("world");        
        String regionName = (String)config.get("region");
        ticks = (Integer)config.get("ticks");
        qty = (Integer)config.get("qty");
        this.entityType = EntityType.valueOf((String)config.get("entity"));
        
        this.world = Bukkit.getWorld(worldName);                
        RegionManager manager = WGBukkit.getRegionManager(world);
        ProtectedRegion region = manager.getRegion(regionName);
        
        BlockVector min = region.getMinimumPoint();
        BlockVector max = region.getMaximumPoint();
        
        if(min.getX() < max.getX()) {
            minx = min.getX();
            maxx = max.getX();
        } else {
            minx = max.getX();
            maxx = min.getX();            
        }
        
        this.xDiff = maxx - minx;
        
        if(min.getY() < max.getY()) {
            miny = min.getY();
            maxy = max.getY();
        } else {
            miny = max.getY();
            maxy = min.getY();            
        }
        
        this.yDiff = maxy - miny;
        
        if(min.getZ() < max.getZ()) {
            minz = min.getZ();
            maxz = max.getZ();
        } else {
            minz = max.getZ();
            maxz = min.getZ();            
        }        
        
        this.zDiff = maxz - minz;
    }
    
    public void spawn() {
        for(int i = 0; i < this.qty; i++) {
            double x = this.minx + rand.nextDouble() * xDiff;
            double y = this.miny + rand.nextDouble() * yDiff;
            double z = this.minz + rand.nextDouble() * zDiff;
        
            Location location = new Location(this.world, x, y, z);
            this.world.spawnEntity(location, this.entityType);
        }        
    }

    public int getTicks() {
        return this.ticks;
    }
}
