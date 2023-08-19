package be.machigan.dvdindexer.services.dvdinterface;

import org.jetbrains.annotations.Nullable;

import java.io.File;

public interface DvdChecker<T extends DvdData> {
    boolean isValidDvdFolder(File folder);

    @Nullable T getDvd(File folder);
}
