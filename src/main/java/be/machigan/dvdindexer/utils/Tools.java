package be.machigan.dvdindexer.utils;

import be.machigan.dvdindexer.DVDIndexer;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Tools {
    private static final Map<String, Image> IMAGE_CACHE = new HashMap<>();
    public static Pane loadSceneFromFXML(String fileName) throws IOException {
        return new FXMLLoader(DVDIndexer.class.getResource("scenes/" + fileName)).load();
    }

    public static ImageView getImageViewFromResources(String urlInResources, Double height, Double width) {
        ImageView view;
        if (IMAGE_CACHE.containsKey(urlInResources)) {
            view = new ImageView(IMAGE_CACHE.get(urlInResources));
        } else {
            try (InputStream stream = DVDIndexer.class.getResourceAsStream("/be/machigan/dvdindexer/img/" + urlInResources)) {
                if (stream == null)throw new IOException("Image not found");
                Image image = new Image(stream);
                view = new ImageView(image);
                IMAGE_CACHE.put(urlInResources, image);
            } catch (IOException ignored) {
                return null;
            }
        }
        if (height != null)
            view.setFitHeight(height);
        if (width != null)
            view.setFitWidth(width);
        return view;
    }

    public static ImageView getImageViewFromResources(String urlInResources, Double dimension) {
        return getImageViewFromResources(urlInResources, dimension, dimension);
    }

    public static ImageView getImageViewFromResources(String urlInResources) {
        return getImageViewFromResources(urlInResources, null, null);
    }

    public static long getFolderSize(File folder) {
        long size = 0;
        File[] filesInFolder = folder.listFiles();
        if (filesInFolder == null)return 0;
        for (File file : filesInFolder) {
            if (file.isFile())
                size += file.length();
            else
                size += getFolderSize(file);
        }
        return size;
    }

    public static boolean containsFileWithExtension(File folder, String fileExtension) {
        File[] files = folder.listFiles();
        if (files == null)return false;
        if (!fileExtension.startsWith("."))
            fileExtension = "." + fileExtension;
        String finalFileExtension = fileExtension;
        return Arrays.stream(files).anyMatch(file -> file.getName().endsWith(finalFileExtension));
    }

    public static boolean containsFileWithExtensions(File folder, String[] fileExtensions) {
        return Arrays.stream(fileExtensions).anyMatch(extension -> containsFileWithExtension(folder, extension));
    }

    public static void openInExplorer(File file) {
        try {
            Runtime.getRuntime().exec("explorer.exe " + file.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void openAndSelectInExplorer(File file) {
        try {
            Runtime.getRuntime().exec("explorer.exe /select," + file.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private Tools() {}
}
