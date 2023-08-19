package be.machigan.dvdindexer.services.dvd.series;

import be.machigan.dvdindexer.services.dvdinterface.Season;
import be.machigan.dvdindexer.services.dvdinterface.SeriesDvdData;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SingleFileSeries extends SeriesDvdData {

    public SingleFileSeries(File file) {
        super(file);
    }

    @Override
    public void setIfAvailable() {
        this.isAvailable = this.toFile().exists();
        this.seasons.forEach(Season::setIfAvailable);
    }

    @Override
    protected List<Season> searchSeasons(File folder) {
        File[] files = folder.listFiles();
        if (files == null)return Collections.emptyList();
        return Arrays.stream(files)
                .map(SingleFileSeason::new)
                .map(Season.class::cast)
                .filter(season -> !season.getEpisodes().isEmpty())
                .toList();
    }
}
