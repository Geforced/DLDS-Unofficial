package eu.bitflare.dlds;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class FileManager {

    private final DLDSPlugin plugin;
    private final File saveFile;
    private final Gson gson;

    public FileManager(DLDSPlugin plugin) {
        this.plugin = plugin;
        this.saveFile = new File(plugin.getDataFolder(), "gamestate.json");
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public void saveGameState() {
        try {
            if (!plugin.getDataFolder().exists()) {
                plugin.getDataFolder().mkdir();
            }

            GameState state = new GameState(
                    plugin.getGameManager().getPlayers(),
                    plugin.getGameManager().isGameRunning()
            );

            FileWriter writer = new FileWriter(saveFile);
            gson.toJson(state, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadGameState() {
        if (!saveFile.exists()) {
            return;
        }

        try {
            FileReader reader = new FileReader(saveFile);
            GameState state = gson.fromJson(reader, GameState.class);
            reader.close();

            plugin.getGameManager().setGameRunning(state.isGameRunning());
            plugin.getGameManager().setPlayers(state.getRegisteredPlayers());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class GameState {

        private final Map<UUID, PlayerData> registeredPlayers;
        private final boolean isGameRunning;

        public GameState(Map<UUID, PlayerData> registeredPlayers, boolean isGameRunning) {
            this.registeredPlayers = registeredPlayers;
            this.isGameRunning = isGameRunning;
        }

        public Map<UUID, PlayerData> getRegisteredPlayers() {
            return registeredPlayers;
        }

        public boolean isGameRunning() {
            return isGameRunning;
        }

    }




}
