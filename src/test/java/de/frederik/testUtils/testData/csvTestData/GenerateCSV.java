package de.frederik.testUtils.testData.csvTestData;

import com.opencsv.CSVWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class GenerateCSV {

    final static Logger logger = LogManager.getLogger(GenerateCSV.class);

    public static File generateCSV(List<String[]> list, String pathname) {

        File file = new File(pathname);
        try {

            CSVWriter writer = new CSVWriter(new FileWriter(file, StandardCharsets.UTF_8));
            writer.writeAll(list, false);
            writer.close();

            return file;
        } catch (Exception e) {
            logger.error("Can't create CSV data!", e);
        } return file;
    }
}
