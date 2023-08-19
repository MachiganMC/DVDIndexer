package be.machigan.dvdindexer.controllers;

import be.machigan.dvdindexer.popup.AddFolderPopup;
import be.machigan.dvdindexer.popup.NewDvdFolderSelectionDvdPopup;
import be.machigan.dvdindexer.services.AppData;
import be.machigan.dvdindexer.services.DVDFolder;
import be.machigan.dvdindexer.utils.Tools;
import be.machigan.dvdindexer.utils.gui.GuiApplier;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.File;

public class AddFolderController {
    @FXML private Label pathLabel;
    @FXML private TextField nameTextField;
    @FXML private Button validateButton;
    @FXML private Button cancelButton;
    @FXML private VBox resultZone;
    private final Label currentDvd = new Label();
    private final ProgressBar stateOfAnalyseBar = new ProgressBar(0);
    @Getter private static AddFolderController instance;

    @FXML
    public void initialize() {
        instance = this;
        this.pathLabel.setText(AddFolderPopup.getInstance().getFolder().getAbsolutePath());
        GuiApplier.setPlaceHolderActiveOnFocus(this.nameTextField);
    }

    public void onValidate() {
        if (this.nameTextField.getText().length() >= DVDFolder.MAX_NAME_LENGTH) {
            this.nameToLong();
            return;
        }
        if (DVDFolder.existADvdFolderWithThisName(this.nameTextField.getText())) {
            this.nameAlreadyTaken();
            return;
        }
        if (AppData.getInstance().getDvdDisks().stream().anyMatch(dvdFolder -> dvdFolder.getPath()
                .equalsIgnoreCase(AddFolderPopup.getInstance().getFolder().getAbsolutePath()))) {
            this.folderAlreadyInuse();
            return;
        }
        if (this.nameTextField.getText().isBlank())return;
        this.checkDvd();
    }

    private void checkDvd() {
        this.validateButton.setDisable(true);
        this.cancelButton.setDisable(true);
        this.nameTextField.setDisable(true);
        this.setOnLoad();
        new Thread(() -> {
            DVDFolder dvdFolder = new DVDFolder(AddFolderPopup.getInstance().getFolder().getAbsolutePath(), this.nameTextField.getText());
            if (dvdFolder.getMovies().isEmpty() && dvdFolder.getSeries().isEmpty()) {
                Platform.runLater(this::noValidDvd);
                return;
            }
            Platform.runLater(() -> displayValidDvd(dvdFolder));
        }).start();
    }

    private static void displayValidDvd(DVDFolder dvdFolder) {
        try {
            new NewDvdFolderSelectionDvdPopup(dvdFolder).start(new Stage());
            AddFolderPopup.getInstance().getPopup().close();
        } catch (Exception e) {
            AddFolderPopup.getInstance().getPopup().close();
        }
    }

    private void setOnLoad() {
        this.stateOfAnalyseBar.setMinWidth(300);
        this.resultZone.getChildren().setAll(
                Tools.getImageViewFromResources("loading.gif", 25D),
                new Label("Indexation en cours ..."),
                this.currentDvd,
                this.stateOfAnalyseBar
        );
    }

    private void nameToLong() {
        Label label = new Label("Le nom du dossier doit faire moins de " + DVDFolder.MAX_NAME_LENGTH + " caractères");
        label.setTextFill(Color.RED);
        this.resultZone.getChildren().setAll(label);
    }

    private void nameAlreadyTaken() {
        Label label = new Label("Le nom \"" + this.nameTextField.getText() + "\" est déjà utilisé");
        label.setTextFill(Color.RED);
        this.resultZone.getChildren().setAll(label);
    }

    private void folderAlreadyInuse() {
        Label label = new Label("Ce dossier est déjà en utilisé");
        label.setTextFill(Color.RED);
        this.resultZone.getChildren().setAll(label);
    }

    private void noValidDvd() {
        this.nameTextField.setEditable(true);
        Label label = new Label("Aucun dvd valide n'a été trouvé");
        label.setTextFill(Color.RED);
        this.resultZone.getChildren().setAll(label);
    }

    public void updateAnalyse(File folderOfDvd, File currentDvd) {
        if (!folderOfDvd.isDirectory())return;
        File[] folderInDvdDisk = folderOfDvd.listFiles();
        if (folderInDvdDisk == null)return;
        this.currentDvd.setText(currentDvd.getName());
        this.stateOfAnalyseBar.setProgress(this.stateOfAnalyseBar.getProgress() + 1D / folderInDvdDisk.length);
    }

    public void onCloseButton() {
        AddFolderPopup.getInstance().getPopup().close();
    }

    public void onSetName() {
        this.validateButton.setDisable(this.nameTextField.getText().isBlank());
    }
}
