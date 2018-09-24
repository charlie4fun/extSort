package com.extSort;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public abstract class SortedFilesMerger<T extends Comparable> implements Iterator<T> {

    private final static Logger logger = Logger.getLogger(SortedFilesMerger.class);
    HashMap<String, BufferedReader> sortedFiles;
    HashMap<String, T> nextItemsInSortedFiles;


    public SortedFilesMerger(String workingDirectoryLocation, String sortedFilePrefix) {

        // TODO: move selecting files for merging to separate method, not avoid proccesing them all at once
        File workingDirectory = new File(workingDirectoryLocation);
        File[] listOfFiles = workingDirectory.listFiles();
        String sortedFileNamePattern = String.format("^%s-\\d*\\.txt", sortedFilePrefix);

        logger.info(String.format("Count sorted files: %d", listOfFiles.length));

        sortedFiles = new HashMap<>();
        nextItemsInSortedFiles = new HashMap<>();

        for (final File fileEntry : listOfFiles) {

            if (fileEntry.getName().matches(sortedFileNamePattern)) {

                String sortedFileAbsPath = fileEntry.getAbsolutePath();

                try {
                    BufferedReader sortedFile = new BufferedReader(new FileReader(fileEntry.getAbsolutePath()));
                    sortedFiles.put(sortedFileAbsPath, sortedFile);

                    updateNextItemForSortedFile(sortedFileAbsPath);

                } catch (FileNotFoundException e) {
                    logger.error(String.format("Can't open sorted file: %s", sortedFileAbsPath));
                }
            }
        }

        logger.info(String.format("sortedFiles.size() = %d", sortedFiles.size()));
        logger.info(String.format("nextItemsInSortedFiles.size() = %d", nextItemsInSortedFiles.size()));
    }

    @Override
    public boolean hasNext() {
        return !(nextItemsInSortedFiles.isEmpty());
    }

    @Override
    public T next() {
        Entry<String, T> minEntry = null;

        for (Entry<String, T> entry : nextItemsInSortedFiles.entrySet()) {
            // TODO: make decreasing sorting
            if (minEntry == null || entry.getValue().compareTo(minEntry.getValue()) < 0) {
                minEntry = entry;
            }
        }

        T result = minEntry.getValue();
        updateNextItemForSortedFile(minEntry.getKey());

        return result;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported.");
    }

    abstract void updateNextItemForSortedFile(String sortedFileAbsPath);
}