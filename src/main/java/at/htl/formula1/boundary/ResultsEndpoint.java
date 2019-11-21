package at.htl.formula1.boundary;

import at.htl.formula1.entity.Driver;
import at.htl.formula1.entity.Race;
import at.htl.formula1.entity.Result;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("results")
public class ResultsEndpoint {

    @PersistenceContext
    EntityManager em;

    /**
     * @param name als QueryParam einzulesen
     * @return JsonObject
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    public JsonObject getPointsSumOfDriver(@QueryParam("name") String name) {
        long points = em.createNamedQuery("Result.getPointsSumOfDriver", Long.class).setParameter("NAME", name).getSingleResult();
        return Json.createObjectBuilder().add("driver", name).add("points", points).build();
    }


    /**
     * @param country des Rennens
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("winner/{country}")
    public Response findWinnerOfRace(@PathParam("country") String country) {
        Driver driver = em.createNamedQuery("Result.findWinnerOfRace", Driver.class).setParameter("COUNTRY", country).getSingleResult();
        return Response.ok(driver).build();
    }


    // Ergänzen Sie Ihre eigenen Methoden ...


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("raceswon")
    public Response findRacesWonByTeam (@QueryParam("team") String teamName){
        List<Race> races = em.createNamedQuery("Result.findRacesWonByTeam", Race.class).setParameter("NAME", teamName).getResultList();
        return Response.ok(races).build();
    }

}
