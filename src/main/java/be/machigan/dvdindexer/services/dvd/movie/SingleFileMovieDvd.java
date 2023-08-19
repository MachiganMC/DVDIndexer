package be.machigan.dvdindexer.services.dvd.movie;

import be.machigan.dvdindexer.services.dvdinterface.MovieDvdData;
import be.machigan.dvdindexer.services.dvdinterface.SingleFileMedia;
import be.machigan.dvdindexer.utils.Tools;

import java.io.File;
import java.nio.file.Path;

public class SingleFileMovieDvd extends MovieDvdData implements SingleFileMedia {
    private final String mediaFile;

    public SingleFileMovieDvd(File folder) {
        super(folder);
        this.mediaFile = this.searchMediaFile(folder).getName();
    }

    @Override
    public void setIfAvailable() {
        this.isAvailable = new File(this.getPathToPlayFile()).exists();
    }

    @Override
    public Path getPathToMediaFile() {
        return Path.of(this.fullPath);
    }

    @Override
    public String getPathToPlayFile() {
        return this.getPathToMediaFile().toString();
    }

    @Override
    public void openInFileExplorer() {
        Tools.openInExplorer(new File(this.getPathToPlayFile()));
    }
}
