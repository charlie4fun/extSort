package com.extSort;

import java.io.*;
import java.util.Random;
import org.apache.log4j.Logger;


public class Producer {

    private final static Logger logger = Logger.getLogger(Producer.class);
    private String unsortedDataLocation;
    private BufferedWriter writer;


    public Producer(String unsortedDataLocation, String dataType) {
        this.unsortedDataLocation = unsortedDataLocation;

        try {
            writer = new BufferedWriter(new FileWriter(unsortedDataLocation));
        } catch (IOException e) {
            logger.error("Can't create output file!");
        }
    }

    public void generate(Long dataSize) {

        logger.info(String.format("Generating File. Location: %s Size: ~ %d Mb", unsortedDataLocation, dataSize/1024/1024));

        long longsCoung = dataSize/8;

        while (longsCoung > 0){
            Random rand = new Random();
            long n = rand.nextLong();
            try {
                writer.write(String.valueOf(n));
                writer.newLine();
            } catch (IOException e) {
                logger.error("Can't access output file!");
            }
            longsCoung--;
        }

        try {
            writer.flush();
        } catch (IOException e) {
            logger.error("Can't access output file!");
        }

        logger.info("Generating complete");
    }
}
