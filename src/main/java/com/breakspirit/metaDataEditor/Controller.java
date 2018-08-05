package com.breakspirit.metaDataEditor;

import com.breakspirit.metaDataEditor.model.FileToEdit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class Controller {

    private final ObservableList<FileToEdit> filesToEdit = FXCollections.observableArrayList();

    // UI elements
    public GridPane rootGrid;
    public Button chooseFilesButton;
    public Button refreshButton;
    public Button clearButton;
    public TableView fileListTable;
    public TableColumn<FileToEdit, String> fileNameColumn;
    public TableColumn<FileToEdit, LocalDateTime> dateCreatedColumn;
    public TableColumn<FileToEdit, LocalDateTime> dateModifiedColumn;
    public Label alertLabel;
    public Label filesSelectedLabel;
    public Button applyButton;

    private static final String DEFAULT_FILE_LOCATION = "C:\\Users\\break\\Desktop";

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @FXML
    public void initialize() {
        logger.info("Initializing the Controller");

        fileNameColumn.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        dateCreatedColumn.setCellValueFactory(new PropertyValueFactory<>("dateCreated"));
        dateModifiedColumn.setCellValueFactory(new PropertyValueFactory<>("dateModified"));

        showPositiveAlertMessage("");

        applyButton.setDisable(true);

        dateCreatedColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DateFieldConverter()));
        dateModifiedColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DateFieldConverter()));
    }

    public void openFileChooserDialog(ActionEvent actionEvent) {
        logger.info("Should be showing file select window");

        final FileChooser fileChooser = new FileChooser();
        if (new File(DEFAULT_FILE_LOCATION).isDirectory()) {
            fileChooser.setInitialDirectory(new File(DEFAULT_FILE_LOCATION));
        }
        fileChooser.setTitle("Choose files to be edited");

        List<File> filesSelected = fileChooser.showOpenMultipleDialog(rootGrid.getScene().getWindow());

        if (filesSelected != null) {
            logger.info("Number of files chosen: " + filesSelected.size());
            filesToEdit.clear();

            for (File file : filesSelected) {
                FileToEdit fileToEdit = new FileToEdit(file);
                filesToEdit.add(fileToEdit);
            }

            applyButton.setDisable(false);
            fileListTable.setItems(filesToEdit);

        } else {
            logger.info("No files were chosen");
        }
        updateFilesSelectedLabel();
        showPositiveAlertMessage("");
    }

    public void clearButtonAction(ActionEvent actionEvent) {
        logger.info("User is clearing the table");

        filesToEdit.clear();
        showPositiveAlertMessage("Table cleared");
        updateFilesSelectedLabel();

        fileListTable.refresh();
    }

    public void refreshButtonAction(ActionEvent actionEvent) {
        logger.info("User is refreshing the table");

        ArrayList<File> refreshedFiles = new ArrayList<>();

        for (FileToEdit fileToEdit : filesToEdit) {
            if (fileToEdit.getFile().exists()) {
                refreshedFiles.add(new File(fileToEdit.getFile().getPath()));
            }
        }
        filesToEdit.clear();

        for (File file : refreshedFiles) {
            FileToEdit fileToEdit = new FileToEdit(file);
            filesToEdit.add(fileToEdit);
        }

        showPositiveAlertMessage("Table refreshed");
        updateFilesSelectedLabel();

        fileListTable.refresh();
    }

    public void applyFileEdits(ActionEvent actionEvent) {
        logger.info("User chose to apply the changes");

        if (filesToEdit.isEmpty()) {
            logger.error("User tried to apply changes to an empty list of files, which should not happen");
            return;
        }

        for (FileToEdit fileToEdit : filesToEdit) {
            try {
                fileToEdit.updateFileOnDrive();

            } catch (IOException e) {
                logger.error("Failed to apply the edit operation to file '" + fileToEdit.getFileName() + "' so we are aborting the operation");
                showNegativeAlertMessage("Edit operation failed at file '" + fileToEdit.getFileName() + "'");
                fileListTable.refresh();
                return;
            }
        }
        showPositiveAlertMessage("All edit operations were applied successfully!");
        updateFilesSelectedLabel();
        fileListTable.refresh();
    }

    private void showPositiveAlertMessage(String message) {
        alertLabel.setText(message);
        alertLabel.setTextFill(Color.web("#056d13"));
    }

    private void showNegativeAlertMessage(String message) {
        alertLabel.setText(message);
        alertLabel.setTextFill(Color.web("#930a21"));
    }

    private void updateFilesSelectedLabel() {
        filesSelectedLabel.setText(fileListTable.getItems().size() + " Files Selected");
    }
}
