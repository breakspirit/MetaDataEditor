package com.breakspirit.metaDataEditor.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class FileToEdit {

    private File file;
    private SimpleStringProperty fileName;
    private ObjectProperty<LocalDateTime> dateCreated;
    private ObjectProperty<LocalDateTime> dateModified;

    public FileToEdit(File file) {
        this.file = file;
        this.fileName = new SimpleStringProperty(file.getName());

        // Populate metadata values
        Path filePath = file.toPath();
        try {
            BasicFileAttributes fileAttributes = Files.readAttributes(filePath, BasicFileAttributes.class);

            LocalDateTime dateCreatedLocalDate = LocalDateTime.ofInstant(fileAttributes.creationTime().toInstant(), ZoneId.systemDefault());
            this.dateCreated = new SimpleObjectProperty<>(dateCreatedLocalDate);

            LocalDateTime dateModifiedLocalDate = LocalDateTime.ofInstant(fileAttributes.lastModifiedTime().toInstant(), ZoneId.systemDefault());
            this.dateModified = new SimpleObjectProperty<>(dateModifiedLocalDate);

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

    public LocalDateTime getDateCreated() {
        return dateCreated.get();
    }

    public ObjectProperty dateCreatedProperty() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated.set(dateCreated);
    }

    public LocalDateTime getDateModified() {
        return dateModified.get();
    }

    public ObjectProperty dateModifiedProperty() {
        return dateModified;
    }

    public void setDateModified(LocalDateTime dateModified) {
        this.dateModified.set(dateModified);
    }
}
