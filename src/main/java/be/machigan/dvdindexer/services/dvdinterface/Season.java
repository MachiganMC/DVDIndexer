package be.machigan.dvdindexer.services.dvdinterface;

import be.machigan.dvdindexer.services.AppData;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Getter
public abstract class Season extends DvdData implements Playable {
    protected final List<Episode> episodes;

    protected abstract List<Episode> searchForEpisodes(File folder);

    protected Season(File file) {
        super(file);
        this.episodes = searchForEpisodes(file);
    }

    @Override
    public void play() {
        try {
            new ProcessBuilder(AppData.getInstance().getSettings().getPlayerPath(), this.fullPath).start();
            this.episodes.forEach(episode -> episode.setSeen(true));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
