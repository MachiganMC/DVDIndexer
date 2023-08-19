package be.machigan.dvdindexer.controllers;

import be.machigan.dvdindexer.popup.DefaultLoadingPopup;
import be.machigan.dvdindexer.popup.NewDvdFolderSelectionDvdPopup;
import be.machigan.dvdindexer.services.AppData;
import be.machigan.dvdindexer.services.DVDFolder;
import be.machigan.dvdindexer.services.dvdinterface.DvdData;
import be.machigan.dvdindexer.services.dvdinterface.MovieDvdData;
import be.machigan.dvdindexer.services.dvdinterface.SeriesDvdData;
import be.machigan.dvdindexer.utils.gui.DvdDataGuiUtils;
import be.machigan.dvdindexer.utils.gui.GuiApplier;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewDvdFolderSelectionDvdController {
    @FXML TabPane folderTab;
    TableView<MovieDvdData> movieTableView;
    TableView<SeriesDvdData> seriesTableView;
    @FXML TextField regexName;
    @FXML TextField regexDirector;
    @FXML Button regexNameButton;
    @FXML Button regexDirectorButton;
    @FXML Button clearDirectors;
    @FXML Button suggestDirectorRegex;
    DVDFolder dvdFolder;

    @FXML
    public void initialize() {
        this.dvdFolder = NewDvdFolderSelectionDvdPopup.getInstance().getDvdFolder();
        GuiApplier.setPlaceHolderActiveOnFocus(this.regexName);
        GuiApplier.setPlaceHolderActiveOnFocus(this.regexDirector);
        this.createTabs();
    }

    private void createTabs() {
        final String tabStyleClass = "dvd-type-tab";
        int movieSize = this.dvdFolder.getMovies().size();
        if (movieSize > 0) {
            this.createMovieTableView();
            Tab movieTab = new Tab("Film" + (movieSize > 1 ? "s" : "") + " (" + movieSize + ")");
            movieTab.setClosable(false);
            movieTab.setContent(this.movieTableView);
            movieTab.getStyleClass().add(tabStyleClass);
            this.folderTab.getTabs().add(movieTab);
        }

        int seriesSize = this.dvdFolder.getSeries().size();
        if (seriesSize > 0) {
            this.createSeriesTableView();
            Tab seriesTab = new Tab("Série" + (seriesSize > 1 ? "s" : "") + " (" + seriesSize + ")");
            seriesTab.setClosable(false);
            seriesTab.setContent(this.seriesTableView);
            seriesTab.getStyleClass().add(tabStyleClass);
            this.folderTab.getTabs().add(seriesTab);
        }
        this.addEventOnTabPane();
    }

    private void addEventOnTabPane() {
        this.folderTab.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            boolean seriesTab = newTab.getText().contains("Série");
            this.regexDirector.setDisable(seriesTab);
            this.regexDirectorButton.setDisable(seriesTab);
            this.suggestDirectorRegex.setDisable(seriesTab);
            this.clearDirectors.setDisable(seriesTab);
        });
    }

    @SuppressWarnings("unchecked")
    private void createMovieTableView() {
        this.movieTableView = new TableView<>();
        GuiApplier.addTableInfo(movieTableView);

        TableColumn<MovieDvdData, String> directorColumn = DvdDataGuiUtils.getDirectorColumn();
        directorColumn.setEditable(true);
        directorColumn.setOnEditCommit(event -> dvdFolder.getMovies().get(event.getTablePosition().getRow()).setDirector(event.getNewValue()));

        this.movieTableView.setEditable(true);
        this.movieTableView.getColumns().setAll(DvdDataGuiUtils.getFolderName(), directorColumn, getEditableDisplayName());
        this.movieTableView.getItems().setAll(FXCollections.observableList(this.dvdFolder.getMovies()));
    }

    @SuppressWarnings("unchecked")
    private void createSeriesTableView() {
        this.seriesTableView = new TableView<>();
        GuiApplier.addTableInfo(movieTableView);
        this.seriesTableView.setEditable(true);
        this.seriesTableView.getColumns().setAll(DvdDataGuiUtils.getFolderName(), getEditableDisplayName(), DvdDataGuiUtils.getNonPlayableSeasonColumn());
        this.seriesTableView.getItems().setAll(FXCollections.observableList(this.dvdFolder.getSeries()));
    }

    private static <T extends DvdData> TableColumn<T, String> getEditableDisplayName() {
        TableColumn<T, String> displayNameColumn = DvdDataGuiUtils.getDisplayNameColumn();
        displayNameColumn.setEditable(true);
        displayNameColumn.setOnEditCommit(event -> event.getRowValue().setDisplayName(event.getNewValue()));
        return displayNameColumn;
    }

    public void onValidateButton() {
        try {
            new DefaultLoadingPopup().start(new Stage());
            NewDvdFolderSelectionDvdPopup.getInstance().getPopup().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        new Thread(() -> {
            AppData.getInstance().getDvdDisks().add(this.dvdFolder);
            AppData.save();
            Platform.runLater(() -> {
                if (AppData.getInstance().getDvdDisks().size() == 1) {
                    HomeController.getInstance().showDVDFolder();
                } else {
                    TableView<DVDFolder> tableView = HomeController.getInstance().getDiskListZone();
                    tableView.getItems().add(this.dvdFolder);
                    tableView.getSelectionModel().select(this.dvdFolder);
                    HomeController.getInstance().changeCurrentSelectedFolder(this.dvdFolder);
                }
            });
            DefaultLoadingPopup.getInstance().setTaskFinished(true);
            Platform.runLater(() -> DefaultLoadingPopup.getInstance().getPopup().close());
        }).start();

    }

    public void onRegexNameButton() {
        Pattern pattern = Pattern.compile(this.regexName.getText(), Pattern.CASE_INSENSITIVE);
        TableView<? extends DvdData> currentTableView = this.getCurrentTableViewDisplayed();
        currentTableView.getItems().forEach(data -> {
            if (data.getDisplayName() != null && !data.getDisplayName().isBlank())return;
            Matcher matcher = pattern.matcher(data.getFolderName());
            if (!matcher.find() || matcher.groupCount() < 1)return;
            Platform.runLater(() -> data.setDisplayName(matcher.group(1)));
        });
        currentTableView.refresh();
    }

    public void onRegexDirectorButton() {
        Pattern pattern = Pattern.compile(this.regexDirector.getText(), Pattern.CASE_INSENSITIVE);
        this.movieTableView.getItems().forEach(movie -> {
            if (movie.getDirector() != null && !movie.getDirector().isBlank())return;
            Matcher matcher = pattern.matcher(movie.getFolderName());
            if (!matcher.find() || matcher.groupCount() < 1)return;
            Platform.runLater(() -> movie.setDirector(matcher.group(1)));
        });
        this.movieTableView.refresh();
    }

    public void onClearNameButton() {
        TableView<? extends DvdData> currentTableView = this.getCurrentTableViewDisplayed();
        currentTableView.getItems().forEach(movie -> movie.setDisplayName(null));
        currentTableView.refresh();
    }

    public void onClearDirectorButton() {
        this.movieTableView.getItems().forEach(movie -> movie.setDirector(null));
        this.movieTableView.refresh();
    }

    public void onSuggestName() {
        this.regexName.setText("^(.+)\\s-\\s.*$");
    }

    public void onSuggestDirector() {
        this.regexDirector.setText("- (.+)$");
    }

    public void onCancelButton() {
        NewDvdFolderSelectionDvdPopup.getInstance().getPopup().close();
    }

    public TableView<? extends DvdData> getCurrentTableViewDisplayed() {

        return this.folderTab.getSelectionModel().getSelectedItem().getText().contains("Film") ?
                this.movieTableView
                : this.seriesTableView;
    }
}
