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
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller {

    private final ObservableList<FileToEdit> filesToRename = FXCollections.observableArrayList();

    // UI elements
    public GridPane rootGrid;
    public Button chooseFilesButton;
    public Button refreshButton;
    public Button clearButton;
    public TableView fileListTable;
    public TableColumn fileNameColumn;
    public TableColumn dateCreatedColumn;
    public TableColumn dateModifiedColumn;
    public Label alertLabel;
    public Button applyButton;

    private Logger logger = Logger.getLogger("Controller");

    @FXML
    public void initialize() {
        logger.log(Level.INFO, "Initializing the Controller");

        fileNameColumn.setCellValueFactory(new PropertyValueFactory<FileToEdit, String>("fileName"));
        dateCreatedColumn.setCellValueFactory(new PropertyValueFactory<FileToEdit, String>("dateCreated"));
        dateModifiedColumn.setCellValueFactory(new PropertyValueFactory<FileToEdit, String>("dateModified"));

        showPositiveAlertMessage("");

        applyButton.setDisable(true);
    }

    public void openFileChooserDialog(ActionEvent actionEvent) {
        logger.log(Level.INFO, "Should be showing file select window");

        final FileChooser fileChooser = new FileChooser();
//        fileChooser.setInitialDirectory(new File("C:\\Users\\break\\Desktop\\file rename test folder"));
        fileChooser.setTitle("Choose files to be renamed");

        List<File> filesSelected = fileChooser.showOpenMultipleDialog(rootGrid.getScene().getWindow());

        if (filesSelected != null) {
            logger.log(Level.INFO, "Number of files chosen: " + filesSelected.size());
            filesToRename.clear();

            for (File file : filesSelected) {
                FileToEdit fileToEdit = new FileToEdit(file);
                filesToRename.add(fileToEdit);
            }

            applyButton.setDisable(false);
            fileListTable.setItems(filesToRename);

        } else {
            logger.log(Level.INFO, "No files were chosen");
        }
        showPositiveAlertMessage("");
    }

    public void clearButtonAction(ActionEvent actionEvent) {
        logger.log(Level.INFO, "User is clearing the table");

        filesToRename.clear();
        showPositiveAlertMessage("Table cleared");

        fileListTable.refresh();
    }

    public void refreshButtonAction(ActionEvent actionEvent) {
        logger.log(Level.INFO, "User is refreshing the table");

        ArrayList<File> refreshedFiles = new ArrayList<>();

        for (FileToEdit fileToEdit : filesToRename) {
            if (fileToEdit.getFile().exists()) {
                refreshedFiles.add(new File(fileToEdit.getFile().getPath()));
            }
        }
        filesToRename.clear();

        for (File file : refreshedFiles) {
            FileToEdit fileToEdit = new FileToEdit(file);
            filesToRename.add(fileToEdit);
        }

        showPositiveAlertMessage("Table refreshed");

        fileListTable.refresh();
    }

    public void applyRenameOperations(ActionEvent actionEvent) {
        logger.log(Level.INFO, "User chose to apply the changes");

        if (filesToRename.isEmpty()) {
            logger.log(Level.SEVERE, "User tried to apply changes to an empty list of files, which should not happen");
            return;
        }

        for (FileToEdit fileToEdit : filesToRename) {
            String newFileName = applyAllSelectedTransformations(fileToEdit.getFile());

            try {
                fileToEdit.renameFile(newFileName);
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Failed to apply the rename operation to file '" + fileToEdit.getFileName() + "' so we are aborting the operation");
                showNegativeAlertMessage("Rename operation failed at file '" + fileToEdit.getFileName() + "'");
                fileListTable.refresh();
                return;
            }
        }
        showPositiveAlertMessage("All rename operations were applied successfully!");
        fileListTable.refresh();
    }

    private String applyAllSelectedTransformations(File inputFile) {
        String transformedName = inputFile.getName();

        return transformedName;
    }

    private void showPositiveAlertMessage(String message) {
        alertLabel.setText(message);
        alertLabel.setTextFill(Color.web("#056d13"));
    }

    private void showNegativeAlertMessage(String message) {
        alertLabel.setText(message);
        alertLabel.setTextFill(Color.web("#930a21"));
    }
}
