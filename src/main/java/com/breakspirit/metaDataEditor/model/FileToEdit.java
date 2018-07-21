package com.breakspirit.metaDataEditor.model;

import javafx.beans.property.SimpleStringProperty;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

public class FileToEdit {

    private File file;
    private SimpleStringProperty fileName;
    private SimpleStringProperty dateCreated;
    private SimpleStringProperty dateModified;

    public FileToEdit(File file) {
        this.file = file;
        this.fileName = new SimpleStringProperty(file.getName());

        // Populate metadata values
        Path filePath = file.toPath();
        try {
            BasicFileAttributes fileAttributes = Files.readAttributes(filePath, BasicFileAttributes.class);

            this.dateCreated = new SimpleStringProperty(fileAttributes.creationTime().toString());
            this.dateModified = new SimpleStringProperty(fileAttributes.lastModifiedTime().toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getFileName() {
        return fileName.get();
    }

    public void renameFile(String newFileName) throws IOException {
        if (!file.exists()) {
            throw new IOException("Trying to rename file '" + file.getName() + "' but it does not exist!");
        }
        Path source = Paths.get(file.getPath());
        Files.move(source, source.resolveSibling(newFileName));
        this.fileName.set(newFileName);
    }


    public File getFile() {
        return file;
    }

    public String getDateCreated() {
        return dateCreated.get();
    }

    public SimpleStringProperty dateCreatedProperty() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated.set(dateCreated);
    }

    public String getDateModified() {
        return dateModified.get();
    }

    public SimpleStringProperty dateModifiedProperty() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified.set(dateModified);
    }
}
