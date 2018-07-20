package com.breakspirit.metaDataEditor.model;

import javafx.beans.property.SimpleStringProperty;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileToEdit {

    private SimpleStringProperty actualFileName;
    private SimpleStringProperty updatedPreviewFileName;
    private File file;

    public FileToEdit(File file, String updatedFileName) {
        this.actualFileName = new SimpleStringProperty(file.getName());
        this.file = file;
        this.updatedPreviewFileName = new SimpleStringProperty(updatedFileName);
    }

    public String getActualFileName() {
        return actualFileName.get();
    }

    public void setActualFileName(String newFileName) throws IOException {
        if(!file.exists()) {
            throw new IOException("Trying to rename file '" + file.getName() + "' but it does not exist!");
        }
        Path source = Paths.get(file.getPath());
        Files.move(source, source.resolveSibling(newFileName));
        this.actualFileName.set(newFileName);

//        this.file.renameTo(newFileName);
    }

    public String getUpdatedPreviewFileName() {
        return updatedPreviewFileName.get();
    }

    public void setUpdatedPreviewFileName(String updatedPreviewFileName) {
        this.updatedPreviewFileName.set(updatedPreviewFileName);
    }

    public File getFile() {
        return file;
    }
}
