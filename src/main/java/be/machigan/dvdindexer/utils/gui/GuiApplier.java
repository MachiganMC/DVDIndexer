package be.machigan.dvdindexer.utils.gui;

import javafx.beans.binding.Bindings;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class GuiApplier {

    public static void addTableInfo(TableView<?> tableView) {
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(tableView, Priority.ALWAYS);
        tableView.setMinWidth(1000);
    }

    public static void setPlaceHolderActiveOnFocus(TextField textField) {
        textField.styleProperty().bind(Bindings
                .when(textField.focusedProperty())
                .then("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);")
                .otherwise("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);"));
    }
}
