package com.extSort;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class BatchSorter {

    private final static Logger logger = Logger.getLogger(BatchSorter.class);
    private BufferedReader unsortedFileBufferedReader;
    private String sortedFileLocation;
    private Long maxListLength;


    public BatchSorter(String unsortedFileLocation, String sortedFileLocation, Double useMemory) {
        File unsortedFile = new File(unsortedFileLocation);

        try {
            unsortedFileBufferedReader = new BufferedReader(new FileReader(unsortedFile));
        } catch (FileNotFoundException e) {
            logger.error("Can't open input file!");
        }

        this.sortedFileLocation = sortedFileLocation;

        maxListLength = Double.valueOf(
                Math.floor((Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory()) * useMemory / 16)
        ).longValue();
        logger.info(String.format("com.extSort.BatchSorter instance created. maxListLength = %d", maxListLength));
    }

    public void sort() throws IOException {

        String st;
        List<Long> batch = new ArrayList<>();
        Integer outputFileCounter = 0;

        logger.info(String.format(
                "Start sorting batches. Memory used: %d Mb", Runtime.getRuntime().totalMemory()/1024/1024));
        while ((st = unsortedFileBufferedReader.readLine()) != null) {

            if (batch.size() < maxListLength) {
                batch.add(Long.valueOf(st));

            } else {
                batch.add(Long.valueOf(st));
                outputFileCounter++;
                saveSortedBatch(batch, outputFileCounter);
                batch = new ArrayList<>();
            }
        }

        outputFileCounter++;
        saveSortedBatch(batch, outputFileCounter);
    }

    private void saveSortedBatch(List<Long> batch, Integer outputFileCounter) {
        logger.debug(String.format(
                "Writing sorted batch. Memory used: %d Mb Batches processed: %d",
                Runtime.getRuntime().totalMemory()/1024/1024, outputFileCounter));
        // TODO: make decreasing sorting
        Collections.sort(batch);

        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(new FileWriter(String.format(sortedFileLocation, outputFileCounter)));
        } catch (IOException e) {
            logger.error("Can't create output file!");
        }

        for (Long i : batch){
            try {
                writer.write(i.toString());
                writer.newLine();
            } catch (IOException e) {
                logger.error("Can't access output file!");
            }
        }

        try {
            writer.flush();
        } catch (IOException e) {
            logger.error("Can't access output file!");
        }
    }
}
