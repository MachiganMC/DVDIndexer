package be.machigan.dvdindexer.utils.gui;

import be.machigan.dvdindexer.services.DVDFolder;
import be.machigan.dvdindexer.services.dvdinterface.*;
import be.machigan.dvdindexer.utils.Tools;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;

public final class DvdDataGuiUtils {

    public static <T extends DvdData>TableColumn<T, String> getNameColumn() {
        TableColumn<T, String> nameColumn = new TableColumn<>("Nom");
        nameColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().toString()));
        return nameColumn;
    }

    public static <T extends DvdData>TableColumn<T, String> getFolderName() {
        TableColumn<T, String> nameColumn = new TableColumn<>("Nom");
        nameColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getFolderName()));
        return nameColumn;
    }

    public static <T extends DvdData> TableColumn<T, String> getLocationColumn() {
        TableColumn<T, String> locationColumn = new TableColumn<>("Location");
        locationColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getFullPath()));
        return locationColumn;
    }

    public static <T extends DvdData> TableColumn<T, String> getDvdFolderName(DVDFolder folder) {
        TableColumn<T, String> folderName = new TableColumn<>("Nom du dossier");
        folderName.setCellValueFactory(cellData -> new SimpleObjectProperty<>(folder.getName()));
        return folderName;
    }

    public static <T extends Watchable> TableColumn<T, VBox> getCheckboxHasSeen() {
        TableColumn<T, VBox> seenColumn = new TableColumn<>("Visionné");
        seenColumn.setCellValueFactory(cellData -> {
            VBox vBox = new VBox();
            vBox.setAlignment(Pos.CENTER);
            CheckBox checkBox = new CheckBox();
            checkBox.setSelected(cellData.getValue().isSeen());
            checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> cellData.getValue().setSeen(newValue));
            vBox.getChildren().setAll(checkBox);
            return new SimpleObjectProperty<>(vBox);
        });
        return seenColumn;
    }

    public static <T extends MovieDvdData> TableColumn<T, String> getDirectorColumn() {
        TableColumn<T, String> directorColumn = new TableColumn<>("Réalisateur");
        directorColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDirector()));
        directorColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        return directorColumn;
    }

    public static <T extends DvdData> TableColumn<T, String> getDisplayNameColumn() {
        TableColumn<T, String> displayNameColumn = new TableColumn<>("Nom d'affichage");
        displayNameColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDisplayName()));
        displayNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        return displayNameColumn;
    }

    public static <T extends Playable> TableColumn<T, VBox> getPlayButton() {
        TableColumn<T, VBox> playColumn = new TableColumn<>("Lancer");
        playColumn.setCellValueFactory(cellData -> {
            VBox vBox = new VBox();
            vBox.setAlignment(Pos.CENTER);
            Button playButton = new Button();
            playButton.setGraphic(Tools.getImageViewFromResources("play.png", 25D));
            playButton.setOnAction(event -> {
                cellData.getValue().play();
                cellData.getTableView().refresh();
            });
            playButton.setDisable(!cellData.getValue().isAvailable());
            playButton.setBackground(Background.EMPTY);
            vBox.getChildren().setAll(playButton);
            return new SimpleObjectProperty<>(vBox);
        });
        return playColumn;
    }

    public static <T extends DvdData> TableColumn<T, VBox> getOpenInExplorerButton() {
        TableColumn<T, VBox> folderColumn = new TableColumn<>("Ouvrir dans l'explorateur");
        folderColumn.setCellValueFactory(cellData -> {
            VBox vbox = new VBox();
            vbox.setAlignment(Pos.CENTER);
            Button folderButton = new Button();
            folderButton.setGraphic(Tools.getImageViewFromResources("folder.png", 25D));
            folderButton.setOnAction(event -> cellData.getValue().openInFileExplorer());
            folderButton.setDisable(!cellData.getValue().isAvailable());
            folderButton.setBackground(Background.EMPTY);
            vbox.getChildren().setAll(folderButton);
            return new SimpleObjectProperty<>(vbox);
        });
        return folderColumn;
    }

    public static <T extends SeriesDvdData> TableColumn<T, Accordion> getPlayableSeasonColumn() {
        return getSeasonColumn(true);
    }

    public static <T extends SeriesDvdData> TableColumn<T, Accordion> getNonPlayableSeasonColumn() {
        return getSeasonColumn(false);
    }

    private static <T extends SeriesDvdData> TableColumn<T, Accordion> getSeasonColumn(boolean hasToBePlayable) {
        TableColumn<T, Accordion> seasonColumn = new TableColumn<>("Saisons");
        seasonColumn.setCellValueFactory(cellData -> {
            Accordion accordion = new Accordion();
            accordion.getPanes().setAll(cellData.getValue().getSeasons().stream()
                    .map(season -> getSeasonPane(season, hasToBePlayable)).toList());
            return new SimpleObjectProperty<>(accordion);
        });
        return seasonColumn;
    }

    @SuppressWarnings("unchecked")
    private static TitledPane getSeasonPane(Season season, boolean hasToBePlayable) {
        TitledPane pane = new TitledPane();
        int size = season.getEpisodes().size();
        pane.setText(season + " - " + size + " épisode" + (size > 1 ? "s" : ""));
        pane.setAnimated(false);
        pane.setPadding(new Insets(0, 0, 0, 10));
        TableView<Episode> tableView = new TableView<>();
        TableColumn<Episode, String> nameColumn = new TableColumn<>("Nom");
        nameColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().toString()));
        tableView.getItems().setAll(FXCollections.observableList(season.getEpisodes()));
        GuiApplier.addTableInfo(tableView);
        tableView.getColumns().setAll(DvdDataGuiUtils.getNameColumn());
        pane.setContent(tableView);
        if (hasToBePlayable)
            makeSeasonPanePlayable(pane, season);
        return pane;
    }

    @SuppressWarnings("unchecked")
    public static void makeSeasonPanePlayable(TitledPane pane, Season season) {
        TableView<Episode> tableView = (TableView<Episode>) pane.getContent();
        tableView.getColumns().addAll(
                DvdDataGuiUtils.getPlayButton(),
                DvdDataGuiUtils.getOpenInExplorerButton(),
                DvdDataGuiUtils.getCheckboxHasSeen());
        pane.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton().equals(MouseButton.SECONDARY) && event.getSource() instanceof TitledPane eventPane) {
                eventPane.setExpanded(eventPane.isExpanded());
                season.play();
                tableView.refresh();
            }
        });
    }

    private DvdDataGuiUtils() {}
}
