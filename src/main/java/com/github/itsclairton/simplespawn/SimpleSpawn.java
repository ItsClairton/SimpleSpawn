package com.github.itsclairton.simplespawn;

import com.github.itsclairton.simplespawn.command.SetSpawnCommand;
import com.github.itsclairton.simplespawn.command.SpawnCommand;
import com.github.itsclairton.simplespawn.data.Configuration;
import com.github.itsclairton.simplespawn.data.DataManager;
import com.github.itsclairton.simplespawn.data.Location;
import lombok.Getter;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.FolderName;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.File;

@Mod("simplespawn")
public class SimpleSpawn {

    @Getter
    private static SimpleSpawn instance;

    @Getter
    private final Configuration config;

    private Location location;
    private ServerWorld world;

    public void saveLocation(File file, Location loc) {
        DataManager.saveToFile(file, loc);
        this.location = loc;
        this.world = null;
    }

    public Location getLocation(MinecraftServer server) {
        if (this.location != null) return this.location; // cache

        File file = server.getWorldPath(new FolderName("spdata")).toFile();
        if(!file.exists()) {
            return null;
        }

        this.location = DataManager.loadFromFile(file, Location.class);
        return this.location;
    }

    public ServerWorld getWorld(MinecraftServer server) {
        if (world != null) return world; // cache

        Location location = getLocation(server);
        if (location == null) return null;

        world = server.getLevel(RegistryKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(location.getWorld())));
        return world;
    }

    private Configuration loadConfig() {
        File file = new File("./config/SimpleSpawn.json");

        if (!file.exists()) {
            DataManager.saveToFile(file, new Configuration());
            return new Configuration();
        } else {
            return DataManager.loadFromFile(file, Configuration.class);
        }
    }

    public SimpleSpawn() {
        instance = this;
        config = loadConfig();

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onCommandRegister(RegisterCommandsEvent e) {
        new SpawnCommand(e.getDispatcher());
        new SetSpawnCommand(e.getDispatcher());
    }

}