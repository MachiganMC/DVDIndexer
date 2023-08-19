package be.machigan.dvdindexer.controllers;

import be.machigan.dvdindexer.services.dvdinterface.DvdData;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import lombok.Getter;

@Getter
public class HomeLoadingController {
    @FXML private Label currentAction;
    @FXML private ProgressBar progressBar;
    @Getter private static HomeLoadingController instance;

    @FXML
    public void initialize() {
        instance = this;
    }

    public static void setCurrenAction(String action) {
        instance.currentAction.setText(action);
    }

    public static void setProgressBar(double progress) {
        instance.progressBar.setProgress(progress);
    }

    public static void setProgressAndAction(String action, double progress) {
        setCurrenAction(action);
        setProgressBar(progress);
    }

    public static void setProgressionOfCheck(DvdData data, int sizeOfMedia) {
        Platform.runLater(() -> HomeLoadingController.setProgressAndAction(
                "Vérification de  " + data.toString(),
                HomeLoadingController.getInstance().getProgressBar().getProgress() + 0.15 / sizeOfMedia
        ));
        data.setIfAvailable();
    }
}
