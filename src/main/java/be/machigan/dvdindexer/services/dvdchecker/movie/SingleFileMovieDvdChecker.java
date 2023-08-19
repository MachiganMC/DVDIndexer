package be.machigan.dvdindexer.services.dvdchecker.movie;

import be.machigan.dvdindexer.services.dvd.movie.SingleFileMovieDvd;
import be.machigan.dvdindexer.services.dvdinterface.DvdChecker;
import be.machigan.dvdindexer.utils.DvdInformations;
import be.machigan.dvdindexer.utils.Tools;

import java.io.File;

public class SingleFileMovieDvdChecker implements DvdChecker<SingleFileMovieDvd> {
    @Override
    public boolean isValidDvdFolder(File folder) {
        return Tools.containsFileWithExtensions(folder, DvdInformations.VALID_EXTENSION);
    }

    @Override
    public SingleFileMovieDvd getDvd(File folder) throws IllegalArgumentException {
        return new SingleFileMovieDvd(folder);
    }
}
