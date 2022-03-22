package de.frederik.testUtils.testData.csvTestData;

import de.pedigreeProject.model.Person;
import de.frederik.testUtils.testData.TestDatabase;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class CreateTestData {

    public static void main(String[] args) {
        createData();
    }

    public static void createTestDataIfNotExist() {

        if (!csvDataExists()) {
            createData();
        }
    }

    private static void createData() {
        List<Person> buddenbrooks = TestDatabase.getBuddenbrooks();

        TestCSV_Factory couldBeChild = new CouldBeChild(buddenbrooks);
        GenerateCSV.generateCSV(couldBeChild.getStringArray(), "csvTestData/couldBeAChild.csv");

        TestCSV_Factory couldBeParent = new CouldBeParent(buddenbrooks);
        GenerateCSV.generateCSV(couldBeParent.getStringArray(), "csvTestData/couldBeAParent.csv");

        TestCSV_Factory couldBeSibling = new CouldBeSibling(buddenbrooks);
        GenerateCSV.generateCSV(couldBeSibling.getStringArray(), "csvTestData/couldBeASibling.csv");

        TestCSV_Factory couldBeSpouse = new CouldBeSpouse(buddenbrooks);
        GenerateCSV.generateCSV(couldBeSpouse.getStringArray(), "csvTestData/couldBeASpouse.csv");
    }

    private static boolean csvDataExists() {

        boolean childExists = Files.exists(Paths.get("csvTestData/couldBeAChild.csv"));
        boolean parentExists = Files.exists(Paths.get("csvTestData/couldBeAParent.csv"));
        boolean siblingExists = Files.exists(Paths.get("csvTestData/couldBeASibling.csv"));
        boolean spousesExists = Files.exists(Paths.get("csvTestData/couldBeASpouse.csv"));

        return childExists && parentExists && siblingExists && spousesExists;
    }
}
