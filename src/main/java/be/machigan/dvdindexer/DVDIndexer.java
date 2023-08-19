package be.machigan.dvdindexer;

import be.machigan.dvdindexer.services.AppData;
import be.machigan.dvdindexer.utils.Tools;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class DVDIndexer extends Application {
    private static Stage primaryStage;
    public static void runApp(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        DVDIndexer.initiateStage(stage);
        DVDIndexer.waitToNewScreen();
    }

    private static void initiateStage(Stage stage) throws IOException {
        stage.setTitle("DVDIndexer - Chargement");
        stage.setMinWidth(1000);
        stage.setMinHeight(600);
        stage.setResizable(false);
        stage.setScene(new Scene(Tools.loadSceneFromFXML("homeLoading.fxml")));
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX(screenBounds.getMinX() + (screenBounds.getWidth() - primaryStage.getWidth()) / 2);
        stage.setY(screenBounds.getMinY() + (screenBounds.getHeight() - primaryStage.getHeight()) / 2);
        stage.getIcons().add(Tools.getImageViewFromResources("icon.png").getImage());
        stage.show();
    }

    private static void waitToNewScreen() {
        new Thread(() -> {
            try {
                AppData.initializeData();
                Scene scene = new Scene(Tools.loadSceneFromFXML("home.fxml"));
                Platform.runLater(() -> DVDIndexer.setCurrentStageToHome(scene));
            } catch (IOException e) {
                e.printStackTrace();
                DVDIndexer.setCurrentStageToError();
            }
        }).start();
    }

    private static void setCurrentStageToHome(Scene scene) {
        primaryStage.close();
        primaryStage.setScene(scene);
        primaryStage.setTitle("DVD Indexer - Accueil");
        primaryStage.setMaximized(true);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        AppData.save();
    }

    private static void setCurrentStageToError() {
        // rien
    }

    public static Stage getStage() {
        return primaryStage;
    }
}