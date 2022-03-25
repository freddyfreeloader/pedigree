package de.frederik.testUtils.testData;

import java.time.Year;

public enum BuddenbrooksData {

    JOHAN("Johan", "Buddenbrook", null),
    JOHANN("Johann", "Buddenbrook", Year.of(1765)),
    BERNHARD("Bernhard", "Buddenbrook", null),
    JOSEPHINE("Josephine", "Buddenbrook", null),
    ANTOINETTE("Antoinette", "Duchamps", null),
    GOTTHOLD("Gotthold", "Buddenbrook", Year.of(1796)),
    ROSALIE_STUEWING("Rosalie", "Stuewing", Year.of(1798)),
    FREDERIKE("Frederike", "Buddenbrook", Year.of(1822)),
    HENRIETTE("Henriette", "Buddenbrook", Year.of(1823)),
    PFIFFI("Pfiffi", "Buddenbrook", Year.of(1824)),
    OLLY("Olly", "Buddenbrook", null),
    JOHANN_JEAN("Johann (Jean)", "Buddenbrook", Year.of(1800)),
    KLOTHILDE("Klothilde", "Buddenbrook", Year.of(1827)),
    THOMAS("Thomas", "Buddenbrook", Year.of(1826)),
    ANTONIE("Antonie (Tony)", "Buddenbrook", Year.of(1827)),
    CHRISTIAN("Christian", "Buddenbrook", Year.of(1828)),
    CLARA("Clara", "Buddenbrook", Year.of(1838)),
    HANNO("Hanno", "Buddenbrook", Year.of(1861)),
    ERIKA("Erika", "Gruenlich", Year.of(1846)),
    ELISABETH_WEINSCHENK("Elisabeth", "Weinschenk", null),
    HUGO("Hugo", "Weinschenk", null),
    ALOIS("Alois", "Permaneder", null),
    BENDIX("Bendix", "Gruenlich", null),
    GISELA("Gisela", "Buddenbrook", Year.of(1860)),
    ALINE("Aline", "Puvogel", null),
    GERDA("Gerda", "Arnoldsen", Year.of(1829)),
    SIEVERT("Sievert", "Tiburtius", Year.of(1829)),
    LEBERECHT("Leberecht", "Kroeger", Year.of(1848)),
    ELISABETH_KROEGER("Elisabeth (Bethsy)", "Kroeger", Year.of(1805)),
    JUSTUS("Justus", "Kroeger", Year.of(1800)),
    JAKOB("Jakob", "Kroeger", Year.of(1828)),
    JUERGEN("Juergen", "Kroeger", Year.of(1826)),
    ROSALIE_OVERDIEK("Rosalie", "Overdiek", null),
    CATHARINA("Catharina", "Kroeger", null),

    ALIEN("Alien", "Alien", null);

    public String givenName;
    public String familyName;
    public Year year;

    @Override
    public String toString() {
        return givenName + " " + familyName;
    }

    BuddenbrooksData(String givenName, String familyName, Year year) {
        this.givenName = givenName;
        this.familyName = familyName;
        this.year = year;
    }
}
