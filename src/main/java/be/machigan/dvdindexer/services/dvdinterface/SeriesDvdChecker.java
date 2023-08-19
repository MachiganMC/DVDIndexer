package be.machigan.dvdindexer.services.dvdinterface;

import java.io.File;

public interface SeriesDvdChecker<T extends SeriesDvdData> extends DvdChecker {
    T getDvd(File file);
}
