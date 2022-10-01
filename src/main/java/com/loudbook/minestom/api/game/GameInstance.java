package com.loudbook.minestom.api.game;

import com.loudbook.minestom.api.event.InstanceLoadEvent;
import com.loudbook.minestom.api.util.GeneralUtils;
import dev.hypera.scaffolding.Scaffolding;
import dev.hypera.scaffolding.schematic.Schematic;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import org.jglrxavpok.hephaistos.nbt.NBTException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class GameInstance {
    private final InstanceContainer instance;
    private final GameType gameType;

    public GameInstance(GameType type){
        this.gameType = type;
        InstanceManager manager = MinecraftServer.getInstanceManager();
        this.instance = manager.createInstanceContainer();
        this.instance.setGenerator(null);
    }

    public void init() {
        String type = gameType.name().toLowerCase();
        List<Map> maps = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(Paths.get("/schematics/"+type))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(path -> maps.add(
                            new Map(
                                    path.toString(),
                                    "schematics/" + type + "/" + path + ".schematic"
                            )
                    ));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map map = GeneralUtils.randomListElement(maps);

        try {
            Schematic schematic = Scaffolding.fromFile(new File(map.getPath()));
            MinecraftServer.LOGGER.info("Loading map " + map.getName() + " from " + map.getPath());
            if (schematic != null) {
                schematic.build(instance, new Pos(0, 0, 0));
                MinecraftServer.LOGGER.info("Loaded map " + map.getName() + "!");
                MinecraftServer.getGlobalEventHandler().call(new InstanceLoadEvent(instance));
            } else {
                MinecraftServer.LOGGER.error("Something went wrong while loading the schematic! ABORTING!");
            }
        } catch (IOException | NBTException e) {
            e.printStackTrace();
        }
    }
}
