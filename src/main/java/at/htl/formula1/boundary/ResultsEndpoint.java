package at.htl.formula1.boundary;

import at.htl.formula1.entity.Driver;

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


public class ResultsEndpoint {


    /**
     * @param name als QueryParam einzulesen
     * @return JsonObject
     */
    public JsonObject getPointsSumOfDriver(
            String name
    ) {
        return null;
    }


    public void read(){
        //FALSCH
        /*
        String url = "http://vm90.htl-leonding.ac.at/results";
        Client client = ClientBuilder.newClient();
        WebTarget traget = client.target(url);

        Response response = traget.request(MediaType.APPLICATION_JSON);
        JasonArray payload = response.readEntity(JsonArray.class);

         */
    }

    /**
     * @param id des Rennens
     * @return
     */
    public Response findWinnerOfRace(long id) {
        return null;
    }


    // Ergänzen Sie Ihre eigenen Methoden ...

}
