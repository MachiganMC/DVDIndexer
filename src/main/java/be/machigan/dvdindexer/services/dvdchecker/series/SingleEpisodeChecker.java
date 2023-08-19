package be.machigan.dvdindexer.services.dvdchecker.series;

import be.machigan.dvdindexer.services.dvd.series.SingleFileEpisode;
import be.machigan.dvdindexer.services.dvdinterface.DvdChecker;
import be.machigan.dvdindexer.services.dvdinterface.Episode;
import be.machigan.dvdindexer.utils.DvdInformations;

import java.io.File;
import java.util.Arrays;

public class SingleEpisodeChecker implements DvdChecker<Episode> {
    @Override
    public boolean isValidDvdFolder(File folder) {
        return Arrays.stream(DvdInformations.VALID_EXTENSION).anyMatch(extension -> folder.getName().endsWith(extension));
    }

    @Override
    public Episode getDvd(File folder) {
        return new SingleFileEpisode(folder);
    }
}
