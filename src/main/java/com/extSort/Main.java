package com.extSort;

import java.io.*;
import java.util.*;

import org.apache.log4j.Logger;


public class Main {

    private final static Logger logger = Logger.getLogger(Main.class);


    public static void main(String[] args) {

        logger.info("Obtaining settings...");
        Properties props = getSettings();

        String unsortedFileLocation = props.getProperty("workingDirectory") + props.getProperty("unsortedFileName");
        String sortedFileLocation = props.getProperty("workingDirectory") + props.getProperty("sortedFilePrefix") + "-%d.txt";

        Producer gen = new Producer(unsortedFileLocation, props.getProperty("dataType"));
        gen.generate(Long.valueOf(props.getProperty("dataSize")));

        gen = null;

        BatchSorter sorter = new BatchSorter(unsortedFileLocation, sortedFileLocation, Double.valueOf(props.getProperty("useMemory")));
        try {
            sorter.sort();
        } catch (IOException e) {
            logger.error("com.extSort.BatchSorter can't access input file!");
        }

        sorter = null;

        BufferedWriter finalSortedFile = null;

        try {
            finalSortedFile = new BufferedWriter(new FileWriter(props.getProperty("workingDirectory") + props.getProperty("sortedFilePrefix") + ".txt"));
        } catch (IOException e) {
            logger.error("Can't create output file for final sorter!");
        }

        // TODO: Make accept Strings and Longs
        SortedFilesMerger<Long> sortedFilesMerger = new SortedLongsFilesMerger(props.getProperty("workingDirectory"), props.getProperty("sortedFilePrefix"));

        while (sortedFilesMerger.hasNext()) {
            try {
                finalSortedFile.write(sortedFilesMerger.next().toString());
                finalSortedFile.newLine();
            } catch (IOException e) {
                logger.error("Can't access output file for final sorter!");
            }
        }

        try {
            finalSortedFile.flush();
        } catch (IOException e) {
            logger.error("Can't access output file for final sorter!");
        }
    }

    public static Properties getSettings(){

        String resourceName = "config.properties";
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties props = new Properties();

        try(InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
            props.load(resourceStream);

        } catch (IOException e) {
            logger.error("Can't access config file!");
        }

        return props;
    }
}

