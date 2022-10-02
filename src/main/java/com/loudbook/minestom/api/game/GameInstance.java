package com.loudbook.minestom.api.game;

import com.loudbook.minestom.api.event.SchematicLoadEvent;
import com.loudbook.minestom.api.team.PlayerTeam;
import com.loudbook.minestom.api.util.GeneralUtils;
import dev.hypera.scaffolding.Scaffolding;
import dev.hypera.scaffolding.schematic.Schematic;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import org.jglrxavpok.hephaistos.nbt.NBTException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.loudbook.minestom.Main.fullbright;

@Getter
public class GameInstance {
    private final InstanceContainer instance;
    private final GameType gameType;
    private List<PlayerTeam> teams;

    public GameInstance(GameType type){
        this.gameType = type;
        InstanceManager manager = MinecraftServer.getInstanceManager();
        this.instance = manager.createInstanceContainer(fullbright);
        instance.setGenerator(null);
    }

    public void init() throws IOException {
        String type = gameType.name().toLowerCase();

        List<String> results = new ArrayList<>();
        File[] files = new File("./extensions/schematics/"+type).listFiles();

        for (File file : files) {
            if (file.isFile()) {
                results.add(file.getName().replace(".schematic",""));
            }
        }
        Map map = new Map(GeneralUtils.randomListElement(results), gameType);
        String directory = "./extensions/schematics/" + type + "/" + map.getName() + ".schematic";
        map.setMapPath(directory);


        System.out.println("Loading map " + map.getName() + " from " + map.getMapPath());
        Thread thread = new Thread(()->{
            Schematic schematic;
            try {
                schematic = Scaffolding.fromFile(new File(directory));
            } catch (IOException | NBTException e) {
                throw new RuntimeException(e);
            }
            schematic.build(instance, new Pos(0, 40, 0)).whenComplete((result, exception) -> {
                System.out.println("Map loaded!");
                new SchematicLoadEvent(instance, gameType, map);
            });

        });
        thread.start();
    }
    public void init(Player player) throws IOException {
        String type = gameType.name().toLowerCase();

        List<String> results = new ArrayList<>();
        File[] files = new File("./extensions/schematics/" + type).listFiles();

        for (File file : files) {
            if (file.isFile()) {
                results.add(file.getName().replace(".schematic", ""));
            }
        }
        Map map = new Map(GeneralUtils.randomListElement(results), gameType);
        String directory = "./extensions/schematics/" + type + "/" + map.getName() + ".schematic";
        player.sendMessage(Component.text("Selected Map: " + map.getName()).color(NamedTextColor.GREEN));
        player.sendMessage(Component.text("Directory: " + directory).color(NamedTextColor.GREEN));
        map.setMapPath(directory);


        System.out.println("Loading map " + map.getName() + " from " + map.getMapPath());
        player.sendMessage(Component.text("Loading map " + map.getName() + " from " + map.getMapPath()).color(NamedTextColor.GREEN));
        Thread thread = new Thread(() -> {
            Schematic schematic;
            try {
                schematic = Scaffolding.fromFile(new File(directory));
            } catch (IOException | NBTException e) {
                throw new RuntimeException(e);
            }
            schematic.build(instance, new Pos(0, 40, 0)).whenComplete((result, exception) -> {
                System.out.println("Map loaded!");
                player.sendMessage(Component.text("Map loaded.").color(NamedTextColor.GREEN));
                new SchematicLoadEvent(instance, gameType, map, player);
            });

        });
        thread.start();
    }
}
