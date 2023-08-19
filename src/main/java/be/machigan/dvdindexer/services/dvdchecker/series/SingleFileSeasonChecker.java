package be.machigan.dvdindexer.services.dvdchecker.series;

import be.machigan.dvdindexer.services.dvd.series.SingleFileSeason;
import be.machigan.dvdindexer.services.dvdinterface.DvdChecker;
import be.machigan.dvdindexer.utils.DvdInformations;
import be.machigan.dvdindexer.utils.Tools;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class SingleFileSeasonChecker implements DvdChecker<SingleFileSeason> {
    @Override
    public boolean isValidDvdFolder(File folder) {
        return Tools.containsFileWithExtensions(folder, DvdInformations.VALID_EXTENSION);
    }

    @Override
    public @Nullable SingleFileSeason getDvd(File folder) {
        return new SingleFileSeason(folder);
    }
}
