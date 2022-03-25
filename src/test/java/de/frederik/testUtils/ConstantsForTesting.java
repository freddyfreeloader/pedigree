package de.frederik.testUtils;

import java.time.Year;
import java.util.Locale;
import java.util.ResourceBundle;

public class ConstantsForTesting {

    public final static String DEFAULT_PEDIGREE = ResourceBundle.getBundle("pedigree").getString("default.pedigree.title");

    public final static String NAME_IS_BLANK_ALERT = ResourceBundle.getBundle("alerts", Locale.getDefault()).getString("name.is.blank");
    public final static String INVALID_YEAR_ALERT = ResourceBundle.getBundle("alerts", Locale.getDefault()).getString("invalid.year");
    public final static String NAME_ALREADY_EXISTS_ALERT = ResourceBundle.getBundle("alerts", Locale.getDefault()).getString("name.already.exists");
    public final static String DELETE_ALERT = ResourceBundle.getBundle("alerts").getString("delete.pedigree");
    public final static String TITLE_IS_BLANK_ALERT = ResourceBundle.getBundle("alerts", Locale.getDefault()).getString("title.is.blank");
    public final static String TITLE_ALREADY_EXISTS_ALERT = ResourceBundle.getBundle("alerts", Locale.getDefault()).getString("name.already.exists");

    public final static String FIRST_TEST_PEDIGREE = "first test pedigree";
    public final static String SECOND_TEST_PEDIGREE = "second test pedigree";
    public final static String CHANGED_TITLE = "New Title Of Pedigree";
    public final static String EMPTY_TITLE = "";
    public final static String CHANGED_DESCRIPTION = "New Description";
    public final static String EXISTING_PEDIGREE1 = "Already existing Pedigree 1";
    public final static String EXISTING_PEDIGREE2 = "Already existing Pedigree 2";

    public final static String GIVEN_NAME = "GivenName";
    public final static String FAMILY_NAME = "FamilyName";
    public final static Year YEAR_OF_BIRTH = Year.of(2000);

    public final static String GIVEN_NAME1 = "GivenName1";
    public final static String FAMILY_NAME1 = "FamilyName1";
    public final static Year YEAR_OF_BIRTH1 = Year.of(2001);

    public final static String GIVEN_NAME2 = "GivenName2";
    public final static String FAMILY_NAME2 = "FamilyName2";
    public final static Year YEAR_OF_BIRTH2 = Year.of(2002);
}
