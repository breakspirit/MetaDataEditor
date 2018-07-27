package com.breakspirit.metaDataEditor;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.google.common.base.Strings;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;

/**
 * @author Kevin Bernard
 */
class MetaDataTransformation {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String ORIGINAL_NAME_TOKEN = "%ON";
    private static final String ASCENDING_NUMBERS_TOKEN = "%AN";
    private static final String CREATION_DATE_YEAR_TOKEN = "%CY";
    private static final String CREATION_DATE_MONTH_TOKEN = "%CM";
    private static final String CREATION_DATE_DAY_TOKEN = "%CD";
    private static final String CREATION_DATE_TIME_TOKEN = "%CT";

    static String applyPrefix(String inputFileName, String prefixValue) {
        if(!Strings.isNullOrEmpty(prefixValue)) {

            return prefixValue + inputFileName;
        } else {
            return inputFileName;
        }
    }

    static String applySuffix(String inputFileName, String suffixValue) {
        if(!Strings.isNullOrEmpty(suffixValue)) {
            String fileNameWithoutExtension = FilenameUtils.removeExtension(inputFileName);
            String extension = FilenameUtils.getExtension(inputFileName);

            return fileNameWithoutExtension + suffixValue + "." + extension;
        } else {
            return inputFileName;
        }
    }

    static String applyNewExtension(String inputFileName, String newExtension) {
        if(!Strings.isNullOrEmpty(newExtension)) {
            String fileNameWithoutExtension = FilenameUtils.removeExtension(inputFileName);

            return fileNameWithoutExtension + "." + newExtension;
        } else {
            return inputFileName;
        }
    }

    static String applyTokenizedRename(File inputFile, String tokenizedRenameString, int ascendingNumbersCounter) {
        if(Strings.isNullOrEmpty(tokenizedRenameString)) {
            return inputFile.getName();
        }

        try {
            // Grab the oldest date, which could be the creation date, date last modified, or date taken(for photos)
            BasicFileAttributes fileAttributes = Files.readAttributes(inputFile.toPath(), BasicFileAttributes.class);
            Metadata metadata;
//            if(tokenizedRenameString.contains(SOMETHING)) {
                metadata = ImageMetadataReader.readMetadata(inputFile);
                ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
                Date creationDate = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);

            //todo the date stuff is not implemented yet

//            }



        } catch (ImageProcessingException e) {
//            if(tokenizedRenameString.contains(SOMETHING)) {
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Apply all the tokens
        tokenizedRenameString = tokenizedRenameString.replaceAll(ORIGINAL_NAME_TOKEN, FilenameUtils.removeExtension(inputFile.getName()));
        tokenizedRenameString = tokenizedRenameString.replaceAll(ASCENDING_NUMBERS_TOKEN, String.valueOf(ascendingNumbersCounter));
//        tokenizedRenameString = tokenizedRenameString.replaceAll(creationDateToken, fileAttributes.creationTime()..ye);

        if(inputFile.getName().contains(".")) {
            // File has an extension, so we need to add it back on
            return tokenizedRenameString + "." + FilenameUtils.getExtension(inputFile.getName());
        }
        return tokenizedRenameString;
    }

    static String applyTextReplace(String inputFileName, String replaceThisString, String withThisString) {
        String tokenizedNameWithoutExtension = FilenameUtils.removeExtension(inputFileName);
        if(inputFileName.contains(".")) {
            // File has an extension, so we need to add it back on
            return tokenizedNameWithoutExtension.replace(replaceThisString, withThisString) + "." + FilenameUtils.getExtension(inputFileName);
        }
        return tokenizedNameWithoutExtension.replace(replaceThisString, withThisString);
    }
}
