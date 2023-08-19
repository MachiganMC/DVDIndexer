package be.machigan.dvdindexer.services.dvdchecker.series;

import be.machigan.dvdindexer.services.dvd.series.SingleFileSeries;
import be.machigan.dvdindexer.services.dvdinterface.SeriesDvdChecker;
import be.machigan.dvdindexer.services.dvdinterface.SeriesDvdData;

import java.io.File;
import java.util.Arrays;

public class SingleFileSeriesChecker implements SeriesDvdChecker<SeriesDvdData> {
    @Override
    public boolean isValidDvdFolder(File folder) {
        File[] files = folder.listFiles();
        if (files == null)return false;
        SingleFileSeasonChecker checker = new SingleFileSeasonChecker();
        return Arrays.stream(files).anyMatch(checker::isValidDvdFolder);
    }

    @Override
    public SeriesDvdData getDvd(File file) {
        return new SingleFileSeries(file);
    }
}
