package be.machigan.dvdindexer.services;

import be.machigan.dvdindexer.controllers.AddFolderController;
import be.machigan.dvdindexer.controllers.HomeController;
import be.machigan.dvdindexer.controllers.HomeLoadingController;
import be.machigan.dvdindexer.popup.DefaultLoadingPopup;
import be.machigan.dvdindexer.services.dvdinterface.MovieDvdData;
import be.machigan.dvdindexer.services.dvdinterface.SeriesDvdData;
import be.machigan.dvdindexer.utils.DvdComparator;
import be.machigan.dvdindexer.utils.ExcludeFromGson;
import be.machigan.dvdindexer.utils.FileToDvdData;
import be.machigan.dvdindexer.utils.Tools;
import javafx.application.Platform;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Getter
public class DVDFolder {
    private final String path;
    @Setter private String name;
    private final List<MovieDvdData> movies;
    private final List<SeriesDvdData> series;
    @ExcludeFromGson private long size;
    @ExcludeFromGson @Getter
    private boolean isAvailable;

    public static final int MAX_NAME_LENGTH = 20;

    public DVDFolder(String path, String name, List<MovieDvdData> movies, List<SeriesDvdData> series) {
        this.path = path;
        this.name = name;
        this.movies = movies;
        this.series = series;
        this.isAvailable = true;
    }

    public DVDFolder(String path, String name) {
        this(path, name,
                searchValidMoviesInFolder(new File(path)),
                searchValidSeriesInFolder(new File(path)));
    }

    public void reloadDVDList() {
        try {
            new DefaultLoadingPopup().start(new Stage());
        } catch (Exception e) {
            return;
        }
        new Thread(() -> {
            AppData.getInstance().getDvdDisks().forEach(DVDFolder::setIfAvailable);
            AppData.getInstance().getDvdDisks().forEach(DVDFolder::sortMedia);
            Platform.runLater(() -> HomeController.getInstance().initialize());
            DefaultLoadingPopup.getInstance().setTaskFinished(true);
            Platform.runLater(() -> DefaultLoadingPopup.getInstance().getPopup().close());
        }).start();

    }

    private static List<MovieDvdData> searchValidMoviesInFolder(File folder) {
        File[] files = folder.listFiles();
        if (files == null)return Collections.emptyList();
        return Arrays.stream(files).map(file -> {
            Platform.runLater(() -> AddFolderController.getInstance().updateAnalyse(folder,file));
            return FileToDvdData.convertToMovie(file);
        }).filter(Objects::nonNull).toList();
    }

    private static List<SeriesDvdData> searchValidSeriesInFolder(File folder) {
        File[] files = folder.listFiles();
        if (files == null)return Collections.emptyList();
        return Arrays.stream(files).map(file -> {
            Platform.runLater(() -> AddFolderController.getInstance().updateAnalyse(folder,file));
            return FileToDvdData.convertToSeries(file);
        }).filter(Objects::nonNull).toList();
    }

    public String getWeightStr() {
        final DecimalFormat format = new DecimalFormat("##.##");
        if (this.size == 0)
            this.size = Tools.getFolderSize(new File(this.path));
        if (this.size < 500)
            return this.size + " o";
        if (this.size < 500_000)
            return format.format(this.size / 1_000) + " Ko";
        if (size < 500_000_000)
            return format.format(this.size / 1_000_000) + " Mo";
        return format.format(this.size / 1_000_000_000) + " Go";
    }

    public void setIfAvailable() {
        File file = new File(this.path);
        this.movies.forEach(data -> HomeLoadingController.setProgressionOfCheck(data, this.movies.size()));
        this.series.forEach(data -> HomeLoadingController.setProgressionOfCheck(data, this.series.size()));
        this.isAvailable = file.exists() && file.isDirectory();
    }

    public static boolean existADvdFolderWithThisName(String name) {
        return AppData.getInstance().getDvdDisks().stream().anyMatch(dvdFolder -> dvdFolder.getName().equalsIgnoreCase(name));
    }
    public void sortMedia() {
        this.movies.sort(DvdComparator.INSTANCE);
        this.series.sort(DvdComparator.INSTANCE);
        this.series.forEach(seriesDvdData -> seriesDvdData.getSeasons().sort(DvdComparator.INSTANCE));
        this.series.forEach(seriesDvdData -> seriesDvdData.getSeasons().forEach(season -> season.getEpisodes().sort(DvdComparator.INSTANCE)));
    }

    public void delete() {
        AppData.getInstance().getDvdDisks().remove(this);
        HomeController.getInstance().getDiskListZone().getItems().remove(this);
        if (AppData.getInstance().getDvdDisks().isEmpty()) {
            HomeController.getInstance().getDiskListZone().getItems().clear();
            HomeController.refreshTables();
            HomeController.getInstance().getMovieListZone().getChildren().clear();
            HomeController.getInstance().getSeriesListZone().getChildren().clear();
        }
        AppData.save();
    }
}
