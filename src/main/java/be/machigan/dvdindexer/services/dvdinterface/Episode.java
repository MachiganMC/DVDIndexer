package be.machigan.dvdindexer.services.dvdinterface;

import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Getter
public abstract class Episode extends DvdData implements Playable, Watchable {
    @Setter protected boolean isSeen;
    protected Episode(File file) {
        super(file);
    }
}
