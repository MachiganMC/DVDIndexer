package be.machigan.dvdindexer.utils.gui;

import be.machigan.dvdindexer.services.DVDFolder;
import be.machigan.dvdindexer.utils.Tools;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;

public class DvdFolderGuiUtils {

    public static TableColumn<DVDFolder, String> getNameColumn() {
        TableColumn<DVDFolder, String> nameColumn = new TableColumn<>("Nom");
        nameColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getName()));
        return nameColumn;
    }

    public static TableColumn<DVDFolder, String> getPathColumn() {
        TableColumn<DVDFolder, String> pathColumn = new TableColumn<>("Location");
        pathColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPath()));
        return pathColumn;
    }

    public static TableColumn<DVDFolder, Integer> getDvdNumberColumn() {
        TableColumn<DVDFolder, Integer> numberDvdColumn = new TableColumn<>("DVD");
        numberDvdColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(
                cellData.getValue().getMovies().size() + cellData.getValue().getSeries().size()));
        return numberDvdColumn;
    }

    public static TableColumn<DVDFolder, String> getStatusColumn() {
        TableColumn<DVDFolder, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().isAvailable() ? "Disponible" : "Indisponible"));
        return statusColumn;
    }

    public static TableColumn<DVDFolder, VBox> getSettingsButton() {
        TableColumn<DVDFolder, VBox> column = new TableColumn<>();
        column.setCellValueFactory(cellData -> {
            VBox vBox = new VBox();
            Button button = new Button();
            button.setGraphic(Tools.getImageViewFromResources("settings.png", 25D));
            button.setOnAction(event -> {
                (getSettingsContextMenu(cellData.getValue())).show(button, Side.BOTTOM, 0, 0);
            });
            vBox.setAlignment(Pos.CENTER);
            button.setBackground(Background.EMPTY);
            vBox.getChildren().setAll(button);
            return new SimpleObjectProperty<>(vBox);
        });
        return column;
    }

    private static ContextMenu getSettingsContextMenu(DVDFolder folder) {
        DvdFolderContextMenu dvdFolderContextMenu = new DvdFolderContextMenu(folder);
        dvdFolderContextMenu.addRenameFolderMenuItem();
        dvdFolderContextMenu.addDeleteFolderMenuItem();
        return dvdFolderContextMenu.getContextMenu();
    }

    private DvdFolderGuiUtils() {}
}
