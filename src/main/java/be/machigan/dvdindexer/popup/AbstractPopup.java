package be.machigan.dvdindexer.popup;

import be.machigan.dvdindexer.DVDIndexer;
import be.machigan.dvdindexer.utils.Tools;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;

@Getter
public abstract class AbstractPopup extends Application {
    protected Stage popup;

    protected AbstractPopup() {
        this.popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.initOwner(DVDIndexer.getStage());
    }

    protected abstract void start() throws Exception;
    protected abstract String getSceneName();

    @Override
    public void start(Stage stage) throws Exception {
        this.popup.setScene(new Scene(Tools.loadSceneFromFXML(this.getSceneName())));
        this.popup.show();
    }
}
