package be.machigan.dvdindexer.services;


import be.machigan.dvdindexer.controllers.HomeLoadingController;
import be.machigan.dvdindexer.services.dvdinterface.*;
import be.machigan.dvdindexer.utils.DvdDataAdapter;
import be.machigan.dvdindexer.utils.ExclusionStrategyWithAnnotation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import javafx.application.Platform;
import lombok.Getter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Getter
public class AppData {
    private static final String APP_FOLDER = System.getenv("APPDATA") + "\\DvdIndexer";
    private static final String DATA_FILE_NAME = APP_FOLDER + "\\save.json";
    private final List<DVDFolder> dvdDisks = new ArrayList<>();
    @Getter private final Settings settings = new Settings();
    @Getter private static AppData instance;
    private static final Gson GSON = new GsonBuilder()
            .addSerializationExclusionStrategy(ExclusionStrategyWithAnnotation.INSTANCE)
            .addDeserializationExclusionStrategy(ExclusionStrategyWithAnnotation.INSTANCE)
            .registerTypeAdapter(MovieDvdData.class, new DvdDataAdapter<MovieDvdData>())
            .registerTypeAdapter(SeriesDvdData.class, new DvdDataAdapter<SeriesDvdData>())
            .registerTypeAdapter(Season.class, new DvdDataAdapter<Season>())
            .registerTypeAdapter(Episode.class, new DvdDataAdapter<Episode>())
            .setPrettyPrinting()
            .create();

    public static void save() {
        try (BufferedWriter bf = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(DATA_FILE_NAME), StandardCharsets.UTF_8))) {
            bf.write(GSON.toJson(AppData.getInstance()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void initializeData() throws IOException {
        try {
            searchData();
        } catch (JsonSyntaxException | FileNotFoundException e) {
            createAppData();
            return;
        }
        processData();
    }

    private static void searchData() throws IOException, JsonSyntaxException {
        if (!new File(DATA_FILE_NAME).exists())
            throw new FileNotFoundException("File not exist");
        Platform.runLater(() -> HomeLoadingController.setCurrenAction("Récupération des données ..."));
        instance = GSON.fromJson(getDataFiletoString(), AppData.class);
        if (instance == null)
            throw new JsonSyntaxException("Instance null");
    }

    private static void makeDataFolder() throws IOException {
        Platform.runLater(() -> HomeLoadingController.setProgressAndAction("Initialization des dossiers", 0.5));
        if (!new File(APP_FOLDER).mkdir())
            throw new IOException("Unable to create folder");
    }

    private static String getDataFiletoString() throws IOException {
        StringBuilder file = new StringBuilder();
        try (BufferedReader reader = Files.newBufferedReader(Path.of(DATA_FILE_NAME), StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                file.append(line);
            }
        }
        return file.toString();
    }

    private static void createAppData() throws IOException {
        instance = new AppData();
        if (!new File(APP_FOLDER).exists())
            makeDataFolder();
    }

    private static void processData() {
        if (instance == null)return;
        Platform.runLater(() -> HomeLoadingController.setProgressAndAction("Vérification des médias ...", 0.4));
        instance.getDvdDisks().forEach(DVDFolder::setIfAvailable);
        Platform.runLater(() -> HomeLoadingController.setProgressAndAction("Tri des données ...", 0.7));
        instance.getDvdDisks().forEach(DVDFolder::sortMedia);
    }
}
