package be.machigan.dvdindexer.services.dvd.movie;

import be.machigan.dvdindexer.services.dvdchecker.movie.IfoMovieDvdChecker;
import be.machigan.dvdindexer.services.dvdinterface.MovieDvdData;
import be.machigan.dvdindexer.utils.Tools;

import java.io.File;

public class IfoMovieDvd extends MovieDvdData {
    public static final String AUDIO_TS = "AUDIO_TS";
    public static final String VIDEO_TS = "VIDEO_TS";
    public static final String DVD_PLAY_FILE_NAME = "VIDEO_TS.IFO";

    public IfoMovieDvd(File file) {
        super(file);
    }

    @Override
    public String getPathToPlayFile() {
        return this.fullPath + "\\" + VIDEO_TS + "\\" + DVD_PLAY_FILE_NAME;
    }

    @Override
    public void setIfAvailable() {
        File file = this.toFile();
        this.isAvailable = file.exists() && new IfoMovieDvdChecker().isValidDvdFolder(file);
    }

    @Override
    public void openInFileExplorer() {
        Tools.openAndSelectInExplorer(new File(this.fullPath + "\\" + VIDEO_TS + "\\" + DVD_PLAY_FILE_NAME));
    }
}
