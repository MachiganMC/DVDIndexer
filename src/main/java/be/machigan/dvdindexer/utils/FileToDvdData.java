package be.machigan.dvdindexer.utils;

import be.machigan.dvdindexer.services.dvdchecker.movie.IfoMovieDvdChecker;
import be.machigan.dvdindexer.services.dvdchecker.movie.SingleFileMovieDvdChecker;
import be.machigan.dvdindexer.services.dvdchecker.series.SingleFileSeriesChecker;
import be.machigan.dvdindexer.services.dvdinterface.DvdChecker;
import be.machigan.dvdindexer.services.dvdinterface.MovieDvdData;
import be.machigan.dvdindexer.services.dvdinterface.SeriesDvdData;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FileToDvdData {
    private static final List<DvdChecker<? extends MovieDvdData>> MOVIE_CHECKERS = Arrays.asList(
            new IfoMovieDvdChecker(),
            new SingleFileMovieDvdChecker()
    );
    private static final List<DvdChecker<? extends SeriesDvdData>> SERIES_CHECKERS = Arrays.asList(
        new SingleFileSeriesChecker()
    );
    @Nullable
    public static MovieDvdData convertToMovie(File folder) {
        return MOVIE_CHECKERS.stream()
                .filter(checker -> checker.isValidDvdFolder(folder))
                .map(checker -> checker.getDvd(folder))
                .filter(Objects::nonNull).findFirst().orElse(null);
    }

    public static SeriesDvdData convertToSeries(File folder) {
        return SERIES_CHECKERS.stream()
                .filter(checker -> checker.isValidDvdFolder(folder))
                .map(checker -> checker.getDvd(folder))
                .filter(Objects::nonNull).findFirst().orElse(null);
    }

    private FileToDvdData() {}
}
