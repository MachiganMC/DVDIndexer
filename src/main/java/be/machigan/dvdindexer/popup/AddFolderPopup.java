package be.machigan.dvdindexer.popup;

import lombok.Getter;

import java.io.File;

@Getter
public class AddFolderPopup extends AbstractPopup {
    private final File folder;
    @Getter private static AddFolderPopup instance;


    public AddFolderPopup(File selectedFolder) {
        super();
        instance = this;
        if (!selectedFolder.isDirectory())throw new IllegalArgumentException("Not a folder");
        this.folder = selectedFolder;
    }

    @Override
    protected void start() throws Exception {
        this.popup.setTitle("Ajouter - " + this.folder.getName());
        this.popup.setResizable(false);
        this.start(this.popup);
    }

    @Override
    protected String getSceneName() {
        return "addFolder.fxml";
    }
}
