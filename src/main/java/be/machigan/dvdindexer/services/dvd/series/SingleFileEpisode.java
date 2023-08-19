package be.machigan.dvdindexer.services.dvd.series;

import be.machigan.dvdindexer.services.AppData;
import be.machigan.dvdindexer.services.dvdinterface.Episode;
import be.machigan.dvdindexer.services.dvdinterface.Playable;
import be.machigan.dvdindexer.services.dvdinterface.SingleFileMedia;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Getter
public class SingleFileEpisode extends Episode implements SingleFileMedia, Playable {
    private final String mediaFile;

    public SingleFileEpisode(File file) {
        super(file);
        this.mediaFile = this.searchMediaFile(file).getName();
    }

    @Override
    public void setIfAvailable() {
        this.isAvailable = Files.exists(this.getPathToMediaFile());
    }

    @Override
    public void play() {
        try {
            new ProcessBuilder(AppData.getInstance().getSettings().getPlayerPath(), this.getPathToMediaFile().toString()).start();
            this.isSeen = true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Path getPathToMediaFile() {
        return Path.of(this.fullPath);
    }
}
