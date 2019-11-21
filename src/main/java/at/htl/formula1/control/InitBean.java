package at.htl.formula1.control;

import at.htl.formula1.boundary.ResultsRestClient;
import at.htl.formula1.entity.Driver;
import at.htl.formula1.entity.Race;
import at.htl.formula1.entity.Result;
import at.htl.formula1.entity.Team;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

@ApplicationScoped
public class InitBean {

    private static final String TEAM_FILE_NAME = "teams.csv";
    private static final String RACES_FILE_NAME = "races.csv";

    @PersistenceContext
    EntityManager em;

    @Inject
    ResultsRestClient client;

    @Transactional
    public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {

        readTeamsAndDriversFromFile(TEAM_FILE_NAME);
        readRacesFromFile(RACES_FILE_NAME);
        client.readResultsFromEndpoint();
    }

    /**
     * Einlesen der Datei "races.csv" und Speichern der Objekte in der Tabelle F1_RACE
     *
     * @param racesFileName
     */

    private void readRacesFromFile(String racesFileName) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        URL url = Thread.currentThread().getContextClassLoader()
                .getResource(racesFileName);
        try (Stream<String> stream = Files.lines(Paths.get(url.getPath()))) {
            stream
                .skip(1)
                .map(s -> s.split(";"))
                .map(a -> new Race(Long.parseLong(a[0]), a[1], LocalDate.parse(a[2], formatter)))
                //.forEach(System.out::println);
                .forEach(em::persist);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Einlesen der Datei "teams.csv".
     * Das String-Array jeder einzelnen Zeile wird der Methode persistTeamAndDrivers(...)
     * übergeben
     *
     * @param teamFileName
     */
    private void readTeamsAndDriversFromFile(String teamFileName) {

        try {
            InputStreamReader streamReader = new InputStreamReader(this.getClass().getResourceAsStream("/" + teamFileName), "UTF-8");
            BufferedReader reader = new BufferedReader(streamReader);
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(";");
                persistTeamAndDrivers(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Es wird überprüft ob es das übergebene Team schon in der Tabelle F1_TEAM gibt.
     * Falls nicht, wird das Team in der Tabelle gespeichert.
     * Wenn es das Team schon gibt, dann liest man das Team aus der Tabelle und
     * erstellt ein Objekt (der Klasse Team).
     * Dieses Objekt wird verwendet, um die Fahrer mit Ihrem jeweiligen Team
     * in der Tabelle F!_DRIVER zu speichern.
     *
     * @param line String-Array mit den einzelnen Werten der csv-Datei
     */

    private void persistTeamAndDrivers(String[] line) {

        List<Team> teams = em.createNamedQuery("Team.findByName", Team.class).setParameter("NAME", line[0]).getResultList();

        Team team;
        if(teams.isEmpty()){
            team = new Team(line[0]);
            em.persist(team);
        } else {
            team = teams.get(0);
        }

        Driver driver1 = new Driver(line[1], team);
        em.persist(driver1);
        Driver driver2 = new Driver(line[2], team);
        em.persist(driver2);

    }


}
