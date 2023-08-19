package be.machigan.dvdindexer.popup;

import javafx.application.Platform;
import lombok.Getter;
import lombok.Setter;

public class DefaultLoadingPopup extends AbstractPopup {
    @Setter private boolean taskFinished = false;
    @Getter
    private static DefaultLoadingPopup instance;
    private final String sceneName;

    public DefaultLoadingPopup(String sceneName) {
        super();
        instance = this;
        if (sceneName == null)
            sceneName = "defaultLoading.fxml";
        this.sceneName = sceneName;
    }

    public DefaultLoadingPopup() {
        this(null);
    }

    @Override
    protected void start() throws Exception {
        popup.setTitle("Chargement en cours...");
        popup.setResizable(false);
        Platform.setImplicitExit(false);
        popup.setOnCloseRequest(event -> {
            if (!this.taskFinished)
                event.consume();
        });
        this.start(this.popup);
    }

    @Override
    protected String getSceneName() {
        return this.sceneName;
    }
}
