module DVDIndexer {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires static org.jetbrains.annotations;
    requires commons.lang;
    requires static lombok;

    opens be.machigan.dvdindexer to javafx.fxml, com.google.gson;
    opens be.machigan.dvdindexer.services to com.google.gson, javafx.base;
    exports be.machigan.dvdindexer;
    exports be.machigan.dvdindexer.controllers;
    exports be.machigan.dvdindexer.services;
    exports be.machigan.dvdindexer.services.dvdinterface;
    opens be.machigan.dvdindexer.controllers to javafx.fxml;
    opens be.machigan.dvdindexer.services.dvdinterface to com.google.gson, javafx.base;
    opens be.machigan.dvdindexer.services.dvd.series to com.google.gson, javafx.base;
    opens be.machigan.dvdindexer.services.dvd.movie to com.google.gson, javafx.base;

}