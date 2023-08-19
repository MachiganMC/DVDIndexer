package be.machigan.dvdindexer.popup;

import be.machigan.dvdindexer.services.DVDFolder;
import lombok.Getter;

@Getter
public class NewDvdFolderSelectionDvdPopup extends AbstractPopup {
    @Getter private static NewDvdFolderSelectionDvdPopup instance;
    private DVDFolder dvdFolder;

    public NewDvdFolderSelectionDvdPopup(DVDFolder dvdFolder) {
        super();
        instance = this;
        this.dvdFolder = dvdFolder;
    }

    @Override
    public void start() throws Exception {
        popup.setTitle("Ajouter - " + dvdFolder.getName());
        popup.setMaximized(true);
        this.start(this.popup);
    }

    @Override
    protected String getSceneName() {
        return "newDvdFolderSelectionDvd.fxml";
    }
}
