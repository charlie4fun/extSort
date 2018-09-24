package com.extSort;

import org.apache.log4j.Logger;

import java.io.*;

public class SortedLongsFilesMerger extends SortedFilesMerger<Long> {

    private final static Logger logger = Logger.getLogger(SortedLongsFilesMerger.class);

    public SortedLongsFilesMerger(String workingDirectoryLocation, String sortedFilePrefix) {
        super(workingDirectoryLocation, sortedFilePrefix);
    }

    void updateNextItemForSortedFile(String sortedFileAbsPath) {
        Long newValue = null;

        try {
            newValue = Long.valueOf(super.sortedFiles.get(sortedFileAbsPath).readLine());

        } catch (IOException e) {
            logger.error(String.format("Can't access sorted file: %s", sortedFileAbsPath));
        } catch (NumberFormatException e) {
            nextItemsInSortedFiles.remove(sortedFileAbsPath);
            sortedFiles.remove(sortedFileAbsPath);
            File file = new File(sortedFileAbsPath);

            if(file.delete()) {
                logger.debug(String.format("File %s deleted successfully", sortedFileAbsPath));
            } else {
                logger.error(String.format("Failed to delete the file %s", sortedFileAbsPath));
            }

        }

        if (newValue != null) {
            nextItemsInSortedFiles.put(sortedFileAbsPath, newValue);
        }
//        else {
//            nextItemsInSortedFiles.remove(sortedFileAbsPath);
//        }
    }
}