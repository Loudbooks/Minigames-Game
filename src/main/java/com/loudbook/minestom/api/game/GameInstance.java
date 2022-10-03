package com.loudbook.minestom.api.game;

import com.loudbook.minestom.api.event.SchematicLoadEvent;
import com.loudbook.minestom.api.player.PlayerManager;
import com.loudbook.minestom.api.team.PlayerTeamManager;
import com.loudbook.minestom.api.util.GeneralUtils;
import dev.hypera.scaffolding.Scaffolding;
import dev.hypera.scaffolding.schematic.Schematic;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.TaskSchedule;
import org.jglrxavpok.hephaistos.nbt.NBTException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.loudbook.minestom.Main.fullbright;

@Getter
public class GameInstance {
    @Setter
    private boolean isReady = false;
    private final Countdown countdown;
    private final InstanceContainer instance;
    private final GameType gameType;
    private final PlayerTeamManager teamManager;
    private final PlayerManager playerManager;
    @Setter
    private List<Player> playersWaiting = new ArrayList<>();
    private final int capacity;
    private final GameInstanceManager manager;

    public GameInstance(GameType type, GameInstanceManager gameInstanceManager, PlayerManager playerManager) {
        this.teamManager = new PlayerTeamManager(type.getNumberOfTeams(), type.getPlayersPerTeam());
        this.playerManager = playerManager;
        this.gameType = type;
        InstanceManager manager = MinecraftServer.getInstanceManager();
        this.instance = manager.createInstanceContainer(fullbright);
        gameInstanceManager.getGameInstances().put(instance, this);
        this.manager = gameInstanceManager;
        instance.setGenerator(null);
        this.capacity = this.gameType.getPlayersPerTeam() * this.gameType.getNumberOfTeams();
        this.countdown = new Countdown(20, 9, this);
    }

    public void init(Player player) {
        playersWaiting.add(player);
        System.out.println("---------------------------------");
        System.out.println("Running initialization for player " + player.getUsername());
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
        map.setMapPath(directory);


        System.out.println("Loading map " + map.getName() + " from " + map.getMapPath());
        parseSchematic(player, map, directory);
    }
    public void init(Player player, boolean debug) throws IOException {
        if (debug) {
            System.out.println("Running initialization for player " + player.getUsername() + " in debug mode");
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
            parseSchematic(player, map, directory);
        } else {
            init(player);
        }
    }

    private void parseSchematic(Player player, Map map, String directory) {
        MinecraftServer.getSchedulerManager().scheduleTask(() -> {
            Schematic schematic;
            try {
                schematic = Scaffolding.fromFile(new File(directory));
            } catch (IOException | NBTException e) {
                throw new RuntimeException(e);
            }
            schematic.build(instance, new Pos(0, 40, 0)).whenComplete((result, exception) -> {
                System.out.println("Map loaded!");
                new SchematicLoadEvent(instance, gameType, map, player, this, playerManager, teamManager);
            });

        }, TaskSchedule.millis(0), TaskSchedule.park(), ExecutionType.ASYNC);
    }
}
