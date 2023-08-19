package be.machigan.dvdindexer.services.dvdinterface;

import be.machigan.dvdindexer.services.AppData;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;

@Getter
public abstract class MovieDvdData extends DvdData implements Playable, Watchable {
    @Setter protected String director;
    @Setter protected boolean seen;
    public abstract String getPathToPlayFile();

    protected MovieDvdData(File file) {
        super(file);
        this.isAvailable = true;
    }

    @Override
    public void play() {
        try {
            new ProcessBuilder(AppData.getInstance().getSettings().getPlayerPath(), this.getPathToPlayFile()).start();
            this.seen = true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
