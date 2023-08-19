package be.machigan.dvdindexer.controllers;

import be.machigan.dvdindexer.popup.RenameFolderPopup;
import be.machigan.dvdindexer.services.DVDFolder;
import be.machigan.dvdindexer.utils.gui.GuiApplier;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class RenameFolderController {
    @FXML private TextField newName;
    @FXML private Label resultLabel;
    private final DVDFolder folder = RenameFolderPopup.getInstance().getFolder();

    @FXML
    public void initialize() {
        this.newName.setText(this.folder.getName());
        GuiApplier.setPlaceHolderActiveOnFocus(this.newName);
    }

    public void onClickValidateButton() {
        if (DVDFolder.existADvdFolderWithThisName(this.newName.getText())) {
            resultLabel.setText("Le nom " + this.newName.getText() + " est déjà pris");
            resultLabel.setTextFill(Color.RED);
            return;
        }
        this.folder.setName(this.newName.getText());
        HomeController.refreshTables();
        RenameFolderPopup.getInstance().getPopup().close();
    }
}
