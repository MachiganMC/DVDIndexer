package be.machigan.dvdindexer.services.dvdinterface;

import lombok.Getter;

import java.io.File;
import java.util.List;

@Getter
public abstract class SeriesDvdData extends DvdData {
    protected final List<Season> seasons;

    protected abstract List<Season> searchSeasons(File folder);


    protected SeriesDvdData(File file) {
        super(file);
        this.seasons = this.searchSeasons(file);
    }
}
