package com.loudbook.minestom.api.game;

import com.loudbook.minestom.api.event.InstanceLoadEvent;
import com.loudbook.minestom.api.team.PlayerTeam;
import com.loudbook.minestom.api.team.PlayerTeamManager;
import com.loudbook.minestom.api.util.GeneralUtils;
import dev.hypera.scaffolding.Scaffolding;
import dev.hypera.scaffolding.schematic.Schematic;
import lombok.Getter;
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

@Getter
public class GameInstance {
    private final InstanceContainer instance;
    private final GameType gameType;
    private List<PlayerTeam> teams;

    public GameInstance(GameType type){
        this.gameType = type;
        InstanceManager manager = MinecraftServer.getInstanceManager();
        this.instance = manager.createInstanceContainer();
        instance.setGenerator(null);
    }

    public void init() throws IOException, NBTException {
        String type = gameType.name().toLowerCase();
        List<String> maps = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(Paths.get("/schematics/"+type))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(path -> maps.add(path.toString()));
        }
        Map map = new Map(GeneralUtils.randomListElement(maps), gameType);
        String directory = "schematics/" + type + "/" + map.getName() + ".schematic";

        Schematic schematic = Scaffolding.fromFile(new File(directory));
        System.out.println("Loading map " + map.getName() + " from " + map.getMapPath());
        if (schematic != null) {
            schematic.build(this.instance, new Pos(0, 0, 0));
            System.out.println("Loaded map " + map.getName() + "!");
            MinecraftServer.getGlobalEventHandler().call(new InstanceLoadEvent(this.instance, this.instance));
        } else {
            System.out.println("Something went wrong while loading the schematic! ABORTING!");
            return;
        }
        if (gameType == GameType.SURVIVAL){
            PlayerTeamManager playerTeamManager = new PlayerTeamManager(GameType.SURVIVAL.getNumberOfTeams(), GameType.SURVIVAL.getPlayersPerTeam());
            this.teams = playerTeamManager.getTeams();



        }
    }
}
