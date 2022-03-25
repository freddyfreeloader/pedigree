package de.frederik.testUtils.testData;

import de.pedigreeProject.model.Person;

import java.util.ArrayList;
import java.util.List;

import static de.frederik.testUtils.testData.BuddenbrooksData.*;

/**
 * Provides a test pedigree of the buddenbrook family.
 * Every person is correctly related to each other.
 *
 * @see <a href="http://buddenbrookhaus.de/file/stb_fam_buddenbrook.pdf">buddenbrookhaus.de</a>
 */
public class Buddenbrooks implements TestPersons {

    /**
     * Pedigree of Buddenbrooks:
     *
     * @see <a href="http://buddenbrookhaus.de/file/stb_fam_buddenbrook.pdf">buddenbrookhaus.de</a>
     * @see BuddenbrooksData
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

        Person johan = new Person(++i, 1, JOHAN.givenName, JOHAN.familyName, JOHAN.year);
        persons.add(johan);

        Person alien = new Person(++i, 1, ALIEN.givenName, ALIEN.familyName, ALIEN.year);
        persons.add(alien);

        Person johann = new Person(++i, 1, JOHANN.givenName, JOHANN.familyName, JOHANN.year);
        persons.add(johann);
        Person bernhard = new Person(++i, 1, BERNHARD.givenName, BERNHARD.familyName, BERNHARD.year);
        persons.add(bernhard);
        Person josephine = new Person(++i, 1, JOSEPHINE.givenName, JOSEPHINE.familyName, JOSEPHINE.year);
        persons.add(josephine);
        Person antoinetteDuchamps = new Person(++i, 1, ANTOINETTE.givenName, ANTOINETTE.familyName, ANTOINETTE.year);
        persons.add(antoinetteDuchamps);
        Person gotthold = new Person(++i, 1, GOTTHOLD.givenName, GOTTHOLD.familyName, GOTTHOLD.year);
        persons.add(gotthold);
        Person rosalieStuewing = new Person(++i, 1, ROSALIE_STUEWING.givenName, ROSALIE_STUEWING.familyName, ROSALIE_STUEWING.year);
        persons.add(rosalieStuewing);
        Person frederike = new Person(++i, 1, FREDERIKE.givenName, FREDERIKE.familyName, FREDERIKE.year);
        persons.add(frederike);
        Person henriette = new Person(++i, 1, HENRIETTE.givenName, HENRIETTE.familyName, HENRIETTE.year);
        persons.add(henriette);
        Person pfiffi = new Person(++i, 1, PFIFFI.givenName, PFIFFI.familyName, PFIFFI.year);
        persons.add(pfiffi);
        Person olly = new Person(++i, 1, OLLY.givenName, OLLY.familyName, OLLY.year);
        persons.add(olly);
        Person johannJean = new Person(++i, 1, JOHANN_JEAN.givenName, JOHANN_JEAN.familyName, JOHANN_JEAN.year);
        persons.add(johannJean);
        Person klothilde = new Person(++i, 1, KLOTHILDE.givenName, KLOTHILDE.familyName, KLOTHILDE.year);
        persons.add(klothilde);
        Person thomas = new Person(++i, 1, THOMAS.givenName, THOMAS.familyName, THOMAS.year);
        persons.add(thomas);
        Person antonie = new Person(++i, 1, ANTONIE.givenName, ANTONIE.familyName, ANTONIE.year);
        persons.add(antonie);
        Person christian = new Person(++i, 1, CHRISTIAN.givenName, CHRISTIAN.familyName, CHRISTIAN.year);
        persons.add(christian);
        Person clara = new Person(++i, 1, CLARA.givenName, CLARA.familyName, CLARA.year);
        persons.add(clara);
        Person hanno = new Person(++i, 1, HANNO.givenName, HANNO.familyName, HANNO.year);
        persons.add(hanno);
        Person erikaGruenlich = new Person(++i, 1, ERIKA.givenName, ERIKA.familyName, ERIKA.year);
        persons.add(erikaGruenlich);
        Person elisabethWeinschenk = new Person(++i, 1, ELISABETH_WEINSCHENK.givenName, ELISABETH_WEINSCHENK.familyName, ELISABETH_WEINSCHENK.year);
        persons.add(elisabethWeinschenk);
        Person hugoWeinschenk = new Person(++i, 1, HUGO.givenName, HUGO.familyName, HUGO.year);
        persons.add(hugoWeinschenk);
        Person aloisPermaneder = new Person(++i, 1, ALOIS.givenName, ALOIS.familyName, ALOIS.year);
        persons.add(aloisPermaneder);
        Person bendixGruenlich = new Person(++i, 1, BENDIX.givenName, BENDIX.familyName, BENDIX.year);
        persons.add(bendixGruenlich);
        Person gisela = new Person(++i, 1, GISELA.givenName, GISELA.familyName, GISELA.year);
        persons.add(gisela);
        Person alinePuvogel = new Person(++i, 1, ALINE.givenName, ALINE.familyName, ALINE.year);
        persons.add(alinePuvogel);
        Person gerdaArnoldsen = new Person(++i, 1, GERDA.givenName, GERDA.familyName, GERDA.year);
        persons.add(gerdaArnoldsen);
        Person sievertTiburtius = new Person(++i, 1, SIEVERT.givenName, SIEVERT.familyName, SIEVERT.year);
        persons.add(sievertTiburtius);
        Person leberechtKroeger = new Person(++i, 1, LEBERECHT.givenName, LEBERECHT.familyName, LEBERECHT.year);
        persons.add(leberechtKroeger);
        Person elisabethBethsyKroeger = new Person(++i, 1, ELISABETH_KROEGER.givenName, ELISABETH_KROEGER.familyName, ELISABETH_KROEGER.year);
        persons.add(elisabethBethsyKroeger);
        Person justusKroeger = new Person(++i, 1, JUSTUS.givenName, JUSTUS.familyName, JUSTUS.year);
        persons.add(justusKroeger);
        Person jakobKroeger = new Person(++i, 1, JAKOB.givenName, JAKOB.familyName, JAKOB.year);
        persons.add(jakobKroeger);
        Person juergenKroeger = new Person(++i, 1, JUERGEN.givenName, JUERGEN.familyName, JUERGEN.year);
        persons.add(juergenKroeger);
        Person rosalieOverdiek = new Person(++i, 1, ROSALIE_OVERDIEK.givenName, ROSALIE_OVERDIEK.familyName, ROSALIE_OVERDIEK.year);
        persons.add(rosalieOverdiek);
        Person catharinaKroeger = new Person(++i, 1, CATHARINA.givenName, CATHARINA.familyName, CATHARINA.year);
        persons.add(catharinaKroeger);

        johan.addChild(johann, bernhard);
        johann.addParent(johan);
        johann.addSpouse(josephine, antoinetteDuchamps);
        johann.addChild(gotthold, olly, johannJean);
        johann.addSibling(bernhard);

        bernhard.addParent(johan);
        bernhard.addSibling(johann);
        bernhard.addChild(klothilde);

        josephine.addSpouse(johann);
        josephine.addChild(gotthold);

        antoinetteDuchamps.addSpouse(johann);
        antoinetteDuchamps.addChild(olly, johannJean);

        klothilde.addParent(bernhard);

        gotthold.addParent(johann, josephine);
        gotthold.addSibling(olly, johannJean);
        gotthold.addSpouse(rosalieStuewing);
        gotthold.addChild(frederike, henriette, pfiffi);

        rosalieStuewing.addSpouse(gotthold);
        rosalieStuewing.addChild(frederike, henriette, pfiffi);

        olly.addParent(johann, antoinetteDuchamps);
        olly.addSibling(gotthold, johannJean);

        johannJean.addParent(johann, antoinetteDuchamps);
        johannJean.addSibling(olly, gotthold);
        johannJean.addSpouse(elisabethBethsyKroeger);
        johannJean.addChild(thomas, antonie, christian, clara);

        frederike.addParent(gotthold, rosalieStuewing);
        frederike.addSibling(henriette, pfiffi);

        henriette.addParent(gotthold, rosalieStuewing);
        henriette.addSibling(frederike, pfiffi);

        pfiffi.addParent(gotthold, rosalieStuewing);
        pfiffi.addSibling(frederike, henriette);

        thomas.addParent(johannJean, elisabethBethsyKroeger);
        thomas.addSibling(antonie, christian, clara);
        thomas.addSpouse(gerdaArnoldsen);
        thomas.addChild(hanno);

        gerdaArnoldsen.addSpouse(thomas);
        gerdaArnoldsen.addChild(hanno);

        hanno.addParent(thomas, gerdaArnoldsen);

        antonie.addParent(johannJean, elisabethBethsyKroeger);
        antonie.addSibling(thomas, christian, clara);
        antonie.addSpouse(bendixGruenlich, aloisPermaneder);
        antonie.addChild(erikaGruenlich);

        bendixGruenlich.addSpouse(antonie);
        bendixGruenlich.addChild(erikaGruenlich);

        erikaGruenlich.addParent(antonie, bendixGruenlich);
        erikaGruenlich.addSpouse(hugoWeinschenk);
        erikaGruenlich.addChild(elisabethWeinschenk);

        hugoWeinschenk.addSpouse(erikaGruenlich);
        hugoWeinschenk.addChild(elisabethWeinschenk);

        aloisPermaneder.addSpouse(antonie);

        elisabethWeinschenk.addParent(erikaGruenlich, hugoWeinschenk);

        christian.addParent(johannJean, elisabethBethsyKroeger);
        christian.addSibling(thomas, antonie, clara);
        christian.addSpouse(alinePuvogel);
        christian.addChild(gisela);

        alinePuvogel.addSpouse(christian);
        alinePuvogel.addChild(gisela);

        gisela.addParent(christian, alinePuvogel);

        clara.addParent(johannJean, elisabethBethsyKroeger);
        clara.addSibling(thomas, antonie, christian);
        clara.addSpouse(sievertTiburtius);

        sievertTiburtius.addSpouse(clara);

        leberechtKroeger.addSpouse(catharinaKroeger);
        leberechtKroeger.addChild(elisabethBethsyKroeger, justusKroeger);

        catharinaKroeger.addSpouse(leberechtKroeger);
        catharinaKroeger.addChild(elisabethBethsyKroeger, justusKroeger);

        elisabethBethsyKroeger.addParent(leberechtKroeger, catharinaKroeger);
        elisabethBethsyKroeger.addSibling(justusKroeger);

        justusKroeger.addParent(leberechtKroeger, catharinaKroeger);
        justusKroeger.addSibling(elisabethBethsyKroeger);
        justusKroeger.addSpouse(rosalieOverdiek);
        justusKroeger.addChild(jakobKroeger, juergenKroeger);

        rosalieOverdiek.addSpouse(justusKroeger);
        rosalieOverdiek.addChild(jakobKroeger, juergenKroeger);

        jakobKroeger.addParent(justusKroeger, rosalieOverdiek);
        jakobKroeger.addSibling(juergenKroeger);

        juergenKroeger.addParent(rosalieOverdiek, justusKroeger);
        juergenKroeger.addSibling(jakobKroeger);

        return persons;
    }
}
