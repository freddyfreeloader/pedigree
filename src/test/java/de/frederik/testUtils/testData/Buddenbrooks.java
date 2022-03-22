package de.frederik.testUtils.testData;

import de.pedigreeProject.model.Person;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides a test pedigree of the buddenbrook family.
 * Every person is correctly related to each other.
 * @see <a href="http://buddenbrookhaus.de/file/stb_fam_buddenbrook.pdf">buddenbrookhaus.de</a>
 */
public class Buddenbrooks implements TestPersons {

    /**
     * Pedigree of Buddenbrooks:
     * @see <a href="http://buddenbrookhaus.de/file/stb_fam_buddenbrook.pdf">buddenbrookhaus.de</a>
     */
    @Override
    public List<Person> getPersons() {
        final List<Person> persons;
        persons = createPersonsList();
        return persons;
    }

    private static List<Person> createPersonsList() {

        List<Person> persons = new ArrayList<>();
        int i = 0;

        Person johan = new Person(++i, 1, "Johan", "Buddenbrook", null);
        persons.add(johan);

        Person alien = new Person(++i, 1, "Alien", "Alien", null);
        persons.add(alien);

        Person johann = new Person(++i, 1, "Johann", "Buddenbrook", Year.of(1765));
        persons.add(johann);
        Person bernhard = new Person(++i, 1, "Bernhard", "Buddenbrook", null);
        persons.add(bernhard);
        Person josephine = new Person(++i, 1, "Josephine", "Buddenbrook", null);
        persons.add(josephine);
        Person antoinetteDuchamps = new Person(++i, 1, "Antoinette", "Duchamps", null);
        persons.add(antoinetteDuchamps);
        Person gotthold = new Person(++i, 1, "Gotthold", "Buddenbrook", Year.of(1796));
        persons.add(gotthold);
        Person rosalieStuewing = new Person(++i, 1, "Rosalie", "Stuewing", Year.of(1798));
        persons.add(rosalieStuewing);
        Person frederike = new Person(++i, 1, "Frederike", "Buddenbrook", Year.of(1822));
        persons.add(frederike);
        Person henriette = new Person(++i, 1, "Henriette", "Buddenbrook", Year.of(1823));
        persons.add(henriette);
        Person pfiffi = new Person(++i, 1, "Pfiffi", "Buddenbrook", Year.of(1824));
        persons.add(pfiffi);
        Person olly = new Person(++i, 1, "Olly", "Buddenbrook", null);
        persons.add(olly);
        Person johannJean = new Person(++i, 1, "Johann (Jean)", "Buddenbrook", Year.of(1800));
        persons.add(johannJean);
        Person klothilde = new Person(++i, 1, "Klothilde", "Buddenbrook", Year.of(1827));
        persons.add(klothilde);
        Person thomas = new Person(++i, 1, "Thomas", "Buddenbrook", Year.of(1826));
        persons.add(thomas);
        Person antonie = new Person(++i, 1, "Antonie (Tony)", "Buddenbrook", Year.of(1827));
        persons.add(antonie);
        Person christian = new Person(++i, 1, "Christian", "Buddenbrook", Year.of(1828));
        persons.add(christian);
        Person clara = new Person(++i, 1, "Clara", "Buddenbrook", Year.of(1838));
        persons.add(clara);
        Person hanno = new Person(++i, 1, "Hanno", "Buddenbrook", Year.of(1861));
        persons.add(hanno);
        Person erikaGruenlich = new Person(++i, 1, "Erika", "Gruenlich", Year.of(1846));
        persons.add(erikaGruenlich);
        Person elisabethWeinschenk = new Person(++i, 1, "Elisabeth", "Weinschenk", null);
        persons.add(elisabethWeinschenk);
        Person hugoWeinschenk = new Person(++i, 1, "Hugo", "Weinschenk", null);
        persons.add(hugoWeinschenk);
        Person aloisPermaneder = new Person(++i, 1, "Alois", "Permaneder", null);
        persons.add(aloisPermaneder);
        Person bendixGruenlich = new Person(++i, 1, "Bendix", "Gruenlich", null);
        persons.add(bendixGruenlich);
        Person gisela = new Person(++i, 1, "Gisela", "Buddenbrook", Year.of(1860));
        persons.add(gisela);
        Person alinePuvogel = new Person(++i, 1, "Aline", "Puvogel", null);
        persons.add(alinePuvogel);
        Person gerdaArnoldsen = new Person(++i, 1, "Gerda", "Arnoldsen", Year.of(1829));
        persons.add(gerdaArnoldsen);
        Person sievertTiburtius = new Person(++i, 1, "Sievert", "Tiburtius", Year.of(1829));
        persons.add(sievertTiburtius);
        Person leberechtKroeger = new Person(++i, 1, "Leberecht", "Kroeger", Year.of(1848));
        persons.add(leberechtKroeger);
        Person elisabethBethsyKroeger = new Person(++i, 1, "Elisabeth (Bethsy)", "Kroeger", Year.of(1805));
        persons.add(elisabethBethsyKroeger);
        Person justusKroeger = new Person(++i, 1, "Justus", "Kroeger", Year.of(1800));
        persons.add(justusKroeger);
        Person jakobKroeger = new Person(++i, 1, "Jakob", "Kroeger", Year.of(1828));
        persons.add(jakobKroeger);
        Person juergenKroeger = new Person(++i, 1, "Juergen", "Kroeger", Year.of(1826));
        persons.add(juergenKroeger);
        Person rosalieOverdiek = new Person(++i, 1, "Rosalie", "Overdiek", null);
        persons.add(rosalieOverdiek);
        Person catharinaKroeger = new Person(++i, 1, "Catharina", "Kroeger", null);
        persons.add(catharinaKroeger);

        johan.addChild(johann);
        johan.addChild(bernhard);
        johann.addParent(johan);
        johann.addSpouse(josephine);
        johann.addSpouse(antoinetteDuchamps);
        johann.addChild(gotthold);
        johann.addChild(olly);
        johann.addChild(johannJean);
        johann.addSibling(bernhard);
        bernhard.addParent(johan);
        bernhard.addSibling(johann);
        bernhard.addChild(klothilde);
        josephine.addSpouse(johann);
        josephine.addChild(gotthold);
        antoinetteDuchamps.addSpouse(johann);
        antoinetteDuchamps.addChild(olly);
        antoinetteDuchamps.addChild(johannJean);
        klothilde.addParent(bernhard);
        gotthold.addParent(johann);
        gotthold.addParent(josephine);
        gotthold.addSibling(olly);
        gotthold.addSibling(johannJean);
        gotthold.addSpouse(rosalieStuewing);
        gotthold.addChild(frederike);
        gotthold.addChild(henriette);
        gotthold.addChild(pfiffi);
        rosalieStuewing.addSpouse(gotthold);
        rosalieStuewing.addChild(frederike);
        rosalieStuewing.addChild(henriette);
        rosalieStuewing.addChild(pfiffi);
        olly.addParent(johann);
        olly.addParent(antoinetteDuchamps);
        olly.addSibling(gotthold);
        olly.addSibling(johannJean);
        johannJean.addParent(johann);
        johannJean.addParent(antoinetteDuchamps);
        johannJean.addSibling(olly);
        johannJean.addSibling(gotthold);
        johannJean.addSpouse(elisabethBethsyKroeger);
        johannJean.addChild(thomas);
        johannJean.addChild(antonie);
        johannJean.addChild(christian);
        johannJean.addChild(clara);
        frederike.addParent(gotthold);
        frederike.addParent(rosalieStuewing);
        frederike.addSibling(henriette);
        frederike.addSibling(pfiffi);
        henriette.addParent(gotthold);
        henriette.addParent(rosalieStuewing);
        henriette.addSibling(frederike);
        henriette.addSibling(pfiffi);

        pfiffi.addParent(gotthold);
        pfiffi.addParent(rosalieStuewing);
        pfiffi.addSibling(frederike);
        pfiffi.addSibling(henriette);
        thomas.addParent(johannJean);
        thomas.addParent(elisabethBethsyKroeger);
        thomas.addSibling(antonie);
        thomas.addSibling(christian);
        thomas.addSibling(clara);
        thomas.addSpouse(gerdaArnoldsen);
        thomas.addChild(hanno);
        gerdaArnoldsen.addSpouse(thomas);
        gerdaArnoldsen.addChild(hanno);
        hanno.addParent(thomas);
        hanno.addParent(gerdaArnoldsen);
        antonie.addParent(johannJean);
        antonie.addParent(elisabethBethsyKroeger);
        antonie.addSibling(thomas);
        antonie.addSibling(christian);
        antonie.addSibling(clara);
        antonie.addSpouse(bendixGruenlich);
        antonie.addChild(erikaGruenlich);
        antonie.addSpouse(aloisPermaneder);
        bendixGruenlich.addSpouse(antonie);
        bendixGruenlich.addChild(erikaGruenlich);
        erikaGruenlich.addParent(antonie);
        erikaGruenlich.addParent(bendixGruenlich);
        erikaGruenlich.addSpouse(hugoWeinschenk);
        erikaGruenlich.addChild(elisabethWeinschenk);
        hugoWeinschenk.addSpouse(erikaGruenlich);
        hugoWeinschenk.addChild(elisabethWeinschenk);
        aloisPermaneder.addSpouse(antonie);
        elisabethWeinschenk.addParent(erikaGruenlich);
        elisabethWeinschenk.addParent(hugoWeinschenk);
        christian.addParent(johannJean);
        christian.addParent(elisabethBethsyKroeger);
        christian.addSibling(thomas);
        christian.addSibling(antonie);
        christian.addSibling(clara);
        christian.addSpouse(alinePuvogel);
        christian.addChild(gisela);
        alinePuvogel.addSpouse(christian);
        alinePuvogel.addChild(gisela);
        gisela.addParent(christian);
        gisela.addParent(alinePuvogel);
        clara.addParent(johannJean);
        clara.addParent(elisabethBethsyKroeger);
        clara.addSibling(thomas);
        clara.addSibling(antonie);
        clara.addSibling(christian);
        clara.addSpouse(sievertTiburtius);
        sievertTiburtius.addSpouse(clara);

        leberechtKroeger.addSpouse(catharinaKroeger);
        leberechtKroeger.addChild(elisabethBethsyKroeger);
        leberechtKroeger.addChild(justusKroeger);
        catharinaKroeger.addSpouse(leberechtKroeger);
        catharinaKroeger.addChild(elisabethBethsyKroeger);
        catharinaKroeger.addChild(justusKroeger);
        elisabethBethsyKroeger.addParent(leberechtKroeger);
        elisabethBethsyKroeger.addParent(catharinaKroeger);
        elisabethBethsyKroeger.addSibling(justusKroeger);
        justusKroeger.addParent(leberechtKroeger);
        justusKroeger.addParent(catharinaKroeger);
        justusKroeger.addSibling(elisabethBethsyKroeger);
        justusKroeger.addSpouse(rosalieOverdiek);
        justusKroeger.addChild(jakobKroeger);
        justusKroeger.addChild(juergenKroeger);
        rosalieOverdiek.addSpouse(justusKroeger);
        rosalieOverdiek.addChild(jakobKroeger);
        rosalieOverdiek.addChild(juergenKroeger);
        jakobKroeger.addParent(justusKroeger);
        jakobKroeger.addParent(rosalieOverdiek);
        jakobKroeger.addSibling(juergenKroeger);
        juergenKroeger.addParent(rosalieOverdiek);
        juergenKroeger.addParent(justusKroeger);
        juergenKroeger.addSibling(jakobKroeger);

        return persons;
    }
}
