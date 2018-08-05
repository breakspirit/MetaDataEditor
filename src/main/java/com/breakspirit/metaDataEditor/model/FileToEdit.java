package com.breakspirit.metaDataEditor.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
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

    public void updateFileOnDrive() throws IOException {
        if (!file.exists()) {
            throw new IOException("Trying to update file '" + file.getName() + "' but it does not exist!");
        }

        // Set the basic Windows file system dates
        FileTime dateCreatedFileTime = FileTime.from(dateCreated.get().atZone(ZoneId.systemDefault()).toInstant());
        FileTime dateModifiedFileTime = FileTime.from(dateCreated.get().atZone(ZoneId.systemDefault()).toInstant());
        Files.setAttribute(file.toPath(), "creationTime", dateCreatedFileTime);
        Files.setAttribute(file.toPath(), "lastModifiedTime", dateModifiedFileTime);

        // Now try to set media-specific dates
        //todo none of the libraries tested have actually been able to modify the xmp meta data correctly.  The Windows file system dates work fine.

    }

    public String getFileName() {
        return fileName.get();
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
