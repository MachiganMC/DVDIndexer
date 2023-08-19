package be.machigan.dvdindexer.utils.gui;

import be.machigan.dvdindexer.popup.RenameFolderPopup;
import be.machigan.dvdindexer.services.DVDFolder;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import lombok.Getter;

public class DvdFolderContextMenu {
    private final DVDFolder folder;
    @Getter private final ContextMenu contextMenu;

    public DvdFolderContextMenu(DVDFolder folder) {
        this.folder = folder;
        this.contextMenu = new ContextMenu();
    }

    public void addRenameFolderMenuItem() {
        MenuItem menuItem = new MenuItem("Renommer le dossier \"" + this.folder.getName() + "\"");
        menuItem.setOnAction(event -> {
            try {
                new RenameFolderPopup(this.folder).start();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        this.contextMenu.getItems().add(menuItem);
    }

    public void addDeleteFolderMenuItem() {
        MenuItem menuItem = new MenuItem("Supprimer le dossier \"" + this.folder.getName() + "\"");
        menuItem.setOnAction(event -> this.folder.delete());
        this.contextMenu.getItems().add(menuItem);
    }
}
