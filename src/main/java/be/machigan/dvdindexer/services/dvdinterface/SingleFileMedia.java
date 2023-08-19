package be.machigan.dvdindexer.services.dvdinterface;

import java.io.File;
import java.nio.file.Path;

public interface SingleFileMedia {

    default File searchMediaFile(File folder) {
        return folder;
    }


    Path getPathToMediaFile();
}
