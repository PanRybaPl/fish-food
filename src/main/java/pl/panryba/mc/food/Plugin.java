package pl.panryba.mc.food;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import pl.panryba.mc.food.spawners.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author PanRyba.pl
 */
public class Plugin extends JavaPlugin {

    List<InteractSpawner> interactSpawners;
    List<BukkitTask> spawnTasks;

    private Object getSpawner(String typeName) {
        if(typeName.equals("simple")) {
            return new SimpleEntitySpawn();
        } else if(typeName.equals("item")) {
            return new ItemSpawner();
        } else if(typeName.equals("container")) {
            return new ContainerSpawner();
        } else if(typeName.equals("perrestart")) {
            return new PerRestartSpawner();
        }
        
        return null;
    }

    public void onInteract(PlayerInteractEvent event) {
        for(InteractSpawner interactSpawner : interactSpawners) {
            interactSpawner.onInteract(event);
        }
    }

    private class SpawnFoodTask implements Runnable {
        private final Spawner spawner;

        public SpawnFoodTask(Spawner spawner) {
            this.spawner = spawner;
        }
        
        public void run() {
            this.spawner.spawn();
        }
        
    }

    @Override
    public void onEnable() {
        List<Spawner> spawners = new ArrayList<>();
        interactSpawners = new ArrayList<>();

        FileConfiguration config = getConfig();
        List<?> spawnsConfig = config.getList("spawns");
        
        for(Object spawnConfigObject : spawnsConfig) {
            try {
                Map<String, Object> spawnConfig = (Map<String, Object>)spawnConfigObject;
                
                String typeName = (String)spawnConfig.get("type");
                Object spawner = getSpawner(typeName);

                if(spawner instanceof InteractSpawner) {
                    InteractSpawner iSpawner = (InteractSpawner)spawner;
                    iSpawner.init(spawnConfig);
                    interactSpawners.add(iSpawner);
                } else if(spawner instanceof Spawner) {
                    Spawner sSpawner = (Spawner)spawner;
                    sSpawner.init(spawnConfig);
                    spawners.add(sSpawner);
                }
            } catch (Exception ex) {
                Logger.getLogger(Plugin.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        spawnTasks = new ArrayList<BukkitTask>();
        for(Spawner spawner : spawners) {
            int ticks = spawner.getTicks();
            
            if(ticks > 0) {
                Runnable spawnRun = new SpawnFoodTask(spawner);
                BukkitTask spawnTask = getServer().getScheduler().runTaskTimer(this, spawnRun, ticks, ticks);
                
                spawnTasks.add(spawnTask);
            }
        }

        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }
    
    @Override
    public void onDisable() {
        if(this.spawnTasks == null) {
            return;
        }
        
        for(BukkitTask task : this.spawnTasks) {
            task.cancel();
        }
        
        this.spawnTasks.clear();
    }
    
    
}
