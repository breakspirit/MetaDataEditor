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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller {

    private final ObservableList<FileToEdit> filesToRename = FXCollections.observableArrayList();

    // UI elements
    public GridPane rootGrid;
    public Button chooseFilesButton;
//    public Button previewButton;
    public Button applyButton;
    public TableView fileListTable;
    public TableColumn fileNameColumn;
    public TableColumn createdTimeColumn;
//    public TextField prefixField;
//    public TextField suffixField;
//    public TextField changeExtensionField;
//    public CheckBox textReplaceCheckbox;
//    public TextField replaceThisField;
//    public TextField withThisField;
//    public CheckBox tokenizedRenameCheckbox;
//    public TextField tokenizedRenameField;
    public Label alertLabel;

    private Logger logger = Logger.getLogger("Controller");

    private int ascendingNumbersCounter = 1;

    @FXML
    public void initialize() {
        logger.log(Level.INFO, "Initializing the Controller");

        fileNameColumn.setCellValueFactory(new PropertyValueFactory<FileToEdit, String>("actualFileName"));
        createdTimeColumn.setCellValueFactory(new PropertyValueFactory<FileToEdit, String>("updatedPreviewFileName"));

        setCheckboxValues();
        showPositiveAlertMessage("");

        applyButton.setDisable(true);
    }

    public void openFileChooserDialog(ActionEvent actionEvent) {
        logger.log(Level.INFO, "Should be showing file select window");

        final FileChooser fileChooser = new FileChooser();
//        fileChooser.setInitialDirectory(new File("C:\\Users\\break\\Desktop\\file rename test folder"));
        fileChooser.setTitle("Choose files to be renamed");

        List<File> filesSelected = fileChooser.showOpenMultipleDialog(rootGrid.getScene().getWindow());

        if(filesSelected != null) {
            logger.log(Level.INFO, "Number of files chosen: " + filesSelected.size());
            filesToRename.clear();

            for(File file : filesSelected) {
                FileToEdit fileToEdit = new FileToEdit(file, file.getName());
                filesToRename.add(fileToEdit);
            }

            applyButton.setDisable(false);
            fileListTable.setItems(filesToRename);

        } else {
            logger.log(Level.INFO, "No files were chosen");
        }
        showPositiveAlertMessage("");
    }

    public void previewButtonAction(ActionEvent actionEvent) {
        logger.log(Level.INFO, "User is previewing their changes");

        ascendingNumbersCounter = 1;

        for(FileToEdit fileToEdit : filesToRename) {
            String updatedPreviewFileName = applyAllSelectedTransformations(fileToEdit.getFile());

            fileToEdit.setUpdatedPreviewFileName(updatedPreviewFileName);
        }
        showPositiveAlertMessage("Preview updated");

        fileListTable.refresh();
    }

    public void applyRenameOperations(ActionEvent actionEvent) {
        logger.log(Level.INFO, "User chose to apply the changes");

        if(filesToRename.isEmpty()) {
            logger.log(Level.SEVERE, "User tried to apply changes to an empty list of files, which should not happen");
            return;
        }

        ascendingNumbersCounter = 1;

        for(FileToEdit fileToEdit : filesToRename) {
            String newFileName = applyAllSelectedTransformations(fileToEdit.getFile());

            try {
                fileToEdit.setUpdatedPreviewFileName(newFileName);
                fileToEdit.setActualFileName(newFileName);
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Failed to apply the rename operation to file '" + fileToEdit.getActualFileName() + "' so we are aborting the operation");
                showNegativeAlertMessage("Rename operation failed at file '" + fileToEdit.getActualFileName() + "'");
                fileListTable.refresh();
                return;
            }
        }
        showPositiveAlertMessage("All rename operations were applied successfully!");
        fileListTable.refresh();
    }

    private String applyAllSelectedTransformations(File inputFile) {
        String transformedName = inputFile.getName();

//        if(tokenizedRenameCheckbox.isSelected()) {
//            transformedName = FileNameTransformation.applyTokenizedRename(inputFile, tokenizedRenameField.getText(), ascendingNumbersCounter++);
//        }
//        if(textReplaceCheckbox.isSelected()) {
//            transformedName = FileNameTransformation.applyTextReplace(transformedName, replaceThisField.getText(), withThisField.getText());
//        }
//        transformedName = FileNameTransformation.applyPrefix(transformedName, prefixField.getText());
//        transformedName = FileNameTransformation.applySuffix(transformedName, suffixField.getText());
//        transformedName = FileNameTransformation.applyNewExtension(transformedName, changeExtensionField.getText());

        return transformedName;
    }

//    public void textReplaceCheckboxAction(ActionEvent actionEvent) {
//        tokenizedRenameCheckbox.setSelected(false);
//
//        setCheckboxValues();
//    }

    private void setCheckboxValues() {
//        replaceThisField.setDisable(!textReplaceCheckbox.isSelected());
//        withThisField.setDisable(!textReplaceCheckbox.isSelected());
//        tokenizedRenameField.setDisable(!tokenizedRenameCheckbox.isSelected());
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
