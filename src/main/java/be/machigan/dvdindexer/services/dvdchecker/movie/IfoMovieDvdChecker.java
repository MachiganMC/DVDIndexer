package be.machigan.dvdindexer.services.dvdchecker.movie;

import be.machigan.dvdindexer.services.dvd.movie.IfoMovieDvd;
import be.machigan.dvdindexer.services.dvdinterface.DvdChecker;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class IfoMovieDvdChecker implements DvdChecker<IfoMovieDvd> {

    @Override
    public boolean isValidDvdFolder(File folder) {
        return (
                Files.exists(Path.of(folder.getAbsolutePath(), IfoMovieDvd.VIDEO_TS, IfoMovieDvd.DVD_PLAY_FILE_NAME))
                && Files.exists(Path.of(folder.getAbsolutePath(), IfoMovieDvd.AUDIO_TS))
                );
    }

    @Override
    public IfoMovieDvd getDvd(File folder) {
        return new IfoMovieDvd(folder);
    }
}
