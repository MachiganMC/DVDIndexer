package be.machigan.dvdindexer.controllers;

import be.machigan.dvdindexer.popup.AddFolderPopup;
import be.machigan.dvdindexer.services.AppData;
import be.machigan.dvdindexer.services.DVDFolder;
import be.machigan.dvdindexer.services.dvd.movie.FoundDvd;
import be.machigan.dvdindexer.services.dvdinterface.MovieDvdData;
import be.machigan.dvdindexer.services.dvdinterface.SeriesDvdData;
import be.machigan.dvdindexer.utils.gui.DvdDataGuiUtils;
import be.machigan.dvdindexer.utils.gui.DvdFolderGuiUtils;
import be.machigan.dvdindexer.utils.gui.GuiApplier;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.File;

@Getter
public class HomeController {
    @FXML private TableView<DVDFolder> diskListZone;
    @FXML TabPane mediaTab;
    @FXML private VBox movieListZone;
    @FXML private VBox seriesListZone;
    @FXML private TextField searchBar;
    private DVDFolder selectedFolder;
    @Getter private static HomeController instance;

    @FXML
    public void initialize() {
        instance = this;
        if (AppData.getInstance() == null)return;
        showDVDFolder();
        GuiApplier.setPlaceHolderActiveOnFocus(this.searchBar);
    }

    @SuppressWarnings("unchecked")
    public void showDVDFolder() {
        final ObservableList<DVDFolder> data = FXCollections.observableList(AppData.getInstance().getDvdDisks());
        TableColumn<DVDFolder, String> nameColumn = DvdFolderGuiUtils.getNameColumn();
        nameColumn.setPrefWidth(125);

        TableColumn<DVDFolder, String> pathColumn = DvdFolderGuiUtils.getPathColumn();
        pathColumn.setPrefWidth(225);

        TableColumn<DVDFolder, Integer> numberDvdColumn = DvdFolderGuiUtils.getDvdNumberColumn();
        numberDvdColumn.setPrefWidth(50);

        TableColumn<DVDFolder, String> statusColumn = DvdFolderGuiUtils.getStatusColumn();
        statusColumn.setPrefWidth(100);

        TableColumn<DVDFolder, VBox> settingsColumn = DvdFolderGuiUtils.getSettingsButton();
        settingsColumn.setPrefWidth(50);

        this.diskListZone.getColumns().setAll(nameColumn, pathColumn, numberDvdColumn, statusColumn, settingsColumn);
        this.diskListZone.getItems().setAll(data);
        this.diskListZone.setOnMouseClicked(this::onClickFolderZone);
        this.diskListZone.getSelectionModel().select(0);
        this.movieListZone.setPrefWidth(550);
        if (!data.isEmpty()) {
            this.selectedFolder = AppData.getInstance().getDvdDisks().get(0);
            this.showDVDOfCurrentFolder();
        }
    }



    public void onClickAddFolderButton(ActionEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();
        File file = chooser.showDialog(((Node) event.getSource()).getScene().getWindow());
        if (file == null)return;
        try {
            new AddFolderPopup(file).start(new Stage());
        } catch (Exception ignored) {}
    }

    public void onClickReloadButton() {
        if (this.selectedFolder == null)return;
        this.selectedFolder.reloadDVDList();
        this.showDVDOfCurrentFolder();
    }

    private void onClickFolderZone(MouseEvent event) {
        if (
                event.getSource() instanceof TableView<?> table
                && table.getSelectionModel().getSelectedItem() instanceof DVDFolder folder
                && !folder.getPath().equalsIgnoreCase(this.selectedFolder.getPath())
                && event.getButton().equals(MouseButton.PRIMARY)) {
                this.changeCurrentSelectedFolder(folder);

        }
    }


    public void changeCurrentSelectedFolder(DVDFolder folderZone) {
        this.selectedFolder = folderZone;
        int indexTab = this.mediaTab.getSelectionModel().getSelectedIndex();
        if (indexTab == 0 && folderZone.getMovies().isEmpty()) {
            this.mediaTab.getSelectionModel().select(1);
        } else if (indexTab == 1 && folderZone.getSeries().isEmpty()) {
            this.mediaTab.getSelectionModel().select(0);
        }
        this.showDVDOfCurrentFolder();
    }



    private void showDVDOfCurrentFolder() {
        this.movieListZone.getChildren().setAll(getMoviesTableView(this.selectedFolder));
        this.seriesListZone.getChildren().setAll(getSeriesTableView(this.selectedFolder));
    }

    @SuppressWarnings("unchecked")
    private static TableView<MovieDvdData> getMoviesTableView(DVDFolder folder) {
        TableView<MovieDvdData> tableView = new TableView<>();
        tableView.setEditable(true);
        tableView.setPlaceholder(new Label("Aucun film valide dans le dossier"));
        GuiApplier.addTableInfo(tableView);
        tableView.getColumns().setAll(
                DvdDataGuiUtils.getNameColumn(),
                DvdDataGuiUtils.getLocationColumn(),
                DvdDataGuiUtils.getDvdFolderName(folder),
                DvdDataGuiUtils.getPlayButton(),
                DvdDataGuiUtils.getOpenInExplorerButton(),
                DvdDataGuiUtils.getCheckboxHasSeen());
        tableView.getItems().setAll(FXCollections.observableList(folder.getMovies()));
        return tableView;
    }

    @SuppressWarnings("unchecked")
    private static TableView<SeriesDvdData> getSeriesTableView(DVDFolder folder) {
        TableView<SeriesDvdData> tableView = new TableView<>();
        tableView.setEditable(true);
        tableView.setPrefWidth(1000);
        tableView.setPlaceholder(new Label("Aucune série valide dans le dossier"));
        GuiApplier.addTableInfo(tableView);

        TableColumn<SeriesDvdData, String> nameColumn = new TableColumn<>("Nom");
        nameColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().toString()));
        nameColumn.setMaxWidth(500);

        TableColumn<SeriesDvdData, Accordion> seasonColumn = DvdDataGuiUtils.getPlayableSeasonColumn();
        seasonColumn.setText("Saisons (clique droit pour lancer toute une saison)");

        tableView.getColumns().setAll(nameColumn, seasonColumn);
        tableView.getItems().setAll(FXCollections.observableList(folder.getSeries()));
        return tableView;
    }



    public void onSearchInAllFolder() {
        if (this.searchBar.getText().isBlank()) {
            this.onClickClearSearchButton();
            return;
        }
        this.movieListZone.getChildren().setAll(FoundDvd.getTableView(this.searchBar.getText()));
    }

    public void onClickClearSearchButton() {
        this.searchBar.clear();
        this.showDVDOfCurrentFolder();
    }

    public static void refreshTables() {
        instance.diskListZone.refresh();
        instance.mediaTab.getTabs().stream()
                .filter(tab -> tab.getContent() instanceof TableView<?>)
                .map(tab -> (TableView<?>) tab.getContent())
                .forEach(TableView::refresh);
    }
}
