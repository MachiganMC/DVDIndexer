package be.machigan.dvdindexer.services.dvd.series;

import be.machigan.dvdindexer.services.dvdchecker.series.SingleEpisodeChecker;
import be.machigan.dvdindexer.services.dvdinterface.Episode;
import be.machigan.dvdindexer.services.dvdinterface.Season;
import lombok.Getter;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Getter
public class SingleFileSeason extends Season {
    public SingleFileSeason(File folder) {
        super(folder);
    }

    @Override
    protected List<Episode> searchForEpisodes(File folder) {
        File[] files = folder.listFiles();
        Objects.requireNonNull(files);
        SingleEpisodeChecker checker = new SingleEpisodeChecker();
        return Arrays.stream(files).filter(checker::isValidDvdFolder).map(checker::getDvd).toList();
    }

    @Override
    public void setIfAvailable() {
        this.isAvailable = this.toFile().exists();
        this.episodes.forEach(Episode::setIfAvailable);
    }
}
