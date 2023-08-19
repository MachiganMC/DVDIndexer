package be.machigan.dvdindexer.services.dvd.movie;

import be.machigan.dvdindexer.services.AppData;
import be.machigan.dvdindexer.services.DVDFolder;
import be.machigan.dvdindexer.services.dvdinterface.MovieDvdData;
import be.machigan.dvdindexer.utils.Tools;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Résultats des dvd trouvés
 */
public class FoundDvd {
    private final MovieDvdData movieDvdData;
    private final DVDFolder folderOfDvd;

    public FoundDvd(MovieDvdData movieDvdData, DVDFolder folderOfDvd) {
        this.movieDvdData = movieDvdData;
        this.folderOfDvd = folderOfDvd;
    }

    public static List<FoundDvd> fromResearch(String searchWord) {
        List<FoundDvd> foundDvds = new ArrayList<>();
        String finalSearchWord = searchWord.toLowerCase();
        AppData.getInstance().getDvdDisks().forEach(folder -> foundDvds.addAll(folder.getMovies().stream()
                .filter(dvd -> dvd.getFolderName().toLowerCase().contains(finalSearchWord) || (
                        dvd.getDisplayName() != null
                        && dvd.getDisplayName().toLowerCase().contains(searchWord)))
                .map(dvd -> new FoundDvd(dvd, folder)).toList()
        ));
        return foundDvds;
    }

    public static TableView<FoundDvd> getTableView(String researchWord) {
        TableView<FoundDvd> tableView = new TableView<>();
        tableView.setPlaceholder(new Label("Aucun dvd n'a été trouvé avec le mot \"" + researchWord + "\""));
        TableColumn<FoundDvd, String> nameColumn = new TableColumn<>("Nom");
        nameColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().movieDvdData.toString()));

        TableColumn<FoundDvd, String> locationColumn = new TableColumn<>("Location");
        locationColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().folderOfDvd.getPath() + "\\" + cellData.getValue().movieDvdData.getFolderName()));

        TableColumn<FoundDvd, String> folderName = new TableColumn<>("Nom du dossier");
        folderName.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().folderOfDvd.getName()));

        TableColumn<FoundDvd, VBox> playColumn = getDvdDataButtonTableColumn();
        TableColumn<FoundDvd, VBox> folderColumn = getDataButtonTableColumn();

        tableView.getColumns().setAll(nameColumn, locationColumn, folderName, playColumn, folderColumn, getDvdDataVBoxTableColumn());
        tableView.getItems().setAll(FXCollections.observableList(fromResearch(researchWord)));
        return tableView;
    }

    @NotNull
    private static TableColumn<FoundDvd, VBox> getDataButtonTableColumn() {
        TableColumn<FoundDvd, VBox> folderColumn = new TableColumn<>("Ouvrir dans l'explorateur");
        folderColumn.setCellValueFactory(cellData -> {
            VBox vbox = new VBox();
            vbox.setAlignment(Pos.CENTER);
            Button folderButton = new Button();
            folderButton.setGraphic(Tools.getImageViewFromResources("folder.png", 25D));
            folderButton.setOnAction(event -> cellData.getValue().movieDvdData.openInFileExplorer());
            folderButton.setDisable(!cellData.getValue().movieDvdData.isAvailable());
            folderButton.setBackground(Background.EMPTY);
            vbox.getChildren().setAll(folderButton);
            return new SimpleObjectProperty<>(vbox);
        });
        return folderColumn;
    }

    @NotNull
    private static TableColumn<FoundDvd, VBox> getDvdDataButtonTableColumn() {
        TableColumn<FoundDvd, VBox> playColumn = new TableColumn<>("Lancer le film");
        playColumn.setCellValueFactory(cellData -> {
            VBox vbox = new VBox();
            vbox.setAlignment(Pos.CENTER);
            Button playButton = new Button();
            playButton.setGraphic(Tools.getImageViewFromResources("play.png", 25D));
            playButton.setOnAction(event -> cellData.getValue().movieDvdData.play());
            playButton.setDisable(!cellData.getValue().folderOfDvd.isAvailable());
            playButton.setBackground(Background.EMPTY);
            vbox.getChildren().add(playButton);
            return new SimpleObjectProperty<>(vbox);
        });
        playColumn.setPrefWidth(115);
        return playColumn;
    }

    @NotNull
    private static TableColumn<FoundDvd, VBox> getDvdDataVBoxTableColumn() {
        TableColumn<FoundDvd, VBox> seenColumn = new TableColumn<>("Visionné");
        seenColumn.setCellValueFactory(cellData -> {
            VBox vBox = new VBox();
            vBox.setAlignment(Pos.CENTER);
            CheckBox checkBox = new CheckBox();
            checkBox.setSelected(cellData.getValue().movieDvdData.isSeen());
            checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> cellData.getValue().movieDvdData.setSeen(newValue));
            vBox.getChildren().setAll(checkBox);
            return new SimpleObjectProperty<>(vBox);
        });
        return seenColumn;
    }
}
