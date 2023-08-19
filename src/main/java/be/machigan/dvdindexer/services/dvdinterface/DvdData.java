package be.machigan.dvdindexer.services.dvdinterface;

import be.machigan.dvdindexer.utils.ExcludeFromGson;
import be.machigan.dvdindexer.utils.Tools;
import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Getter
public abstract class DvdData {
    @Setter
    protected String displayName;
    @Getter
    protected final String fullPath;
    @Getter
    protected final String folderName;
    @ExcludeFromGson protected boolean isAvailable;

    public abstract void setIfAvailable();

    protected DvdData(File file) {
        this.fullPath = file.getAbsolutePath();
        this.folderName = file.getName();
        this.isAvailable = true;
    }

    public void openInFileExplorer() {
        Tools.openAndSelectInExplorer(new File(this.fullPath));
    }

    public File toFile() {
        return new File(this.fullPath);
    }

    @Override
    public String toString() {
        return this.displayName == null || this.displayName.isBlank() ? this.folderName : this.displayName;
    }
}
