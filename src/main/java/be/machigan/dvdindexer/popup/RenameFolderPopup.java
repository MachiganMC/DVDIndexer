package be.machigan.dvdindexer.popup;

import be.machigan.dvdindexer.services.DVDFolder;
import lombok.Getter;

public class RenameFolderPopup extends AbstractPopup {
    @Getter private static RenameFolderPopup instance;
    @Getter private DVDFolder folder;

    public RenameFolderPopup(DVDFolder folder) {
        super();
        instance = this;
        this.folder = folder;
    }

    @Override
    public void start() throws Exception {
        this.popup.setTitle("Renommer - " + this.folder.getName());
        this.popup.setResizable(false);
        popup.setWidth(500);
        popup.setHeight(250);
        this.start(this.popup);
    }

    @Override
    protected String getSceneName() {
        return "renameFolder.fxml";
    }
}
