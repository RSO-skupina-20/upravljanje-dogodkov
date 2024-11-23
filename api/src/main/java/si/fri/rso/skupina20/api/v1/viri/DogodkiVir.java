package si.fri.rso.skupina20.api.v1.viri;

import com.kumuluz.ee.cors.annotations.CrossOrigin;
import com.kumuluz.ee.rest.beans.QueryParameters;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import si.fri.rso.skupina20.entitete.Dogodek;
import si.fri.rso.skupina20.zrna.DogodekZrno;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.awt.*;
import java.util.List;

@ApplicationScoped
@Path("/dogodki")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@CrossOrigin(supportedMethods = "GET, POST, DELETE, PUT, HEAD, OPTIONS")
@Tag(name = "Dogodki", description = "Upravljanje z dogodki")
public class DogodkiVir {
    @Context
    protected UriInfo uriInfo;

    @Inject
    private DogodekZrno dogodekZrno;

    @GET
    @Operation(summary = "Pridobi seznam vseh dogodkov", description = "Vrne seznam vseh dogodkov")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Seznam dogodkov", content = @Content(schema = @Schema(implementation = Dogodek.class)),
                    headers = @Header(name = "X-Total-Count", description = "Število vrnjenih dogodkov", schema = @Schema(type = SchemaType.INTEGER))),
            @APIResponse(responseCode = "404", description = "Dogodki ne obstajajo", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class, example = "{\"napaka\": \"Dogodkov ni mogoče najti\"}"))),
    })
    public Response vrniDogodke() {
        QueryParameters query = QueryParameters.query(uriInfo.getRequestUri().getQuery()).build();
        List<Dogodek> dogodki = dogodekZrno.getDogodki(query);
        if (dogodki == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("{\"napaka\": \"Dogodkov ni mogoče najti\"}").build();
        }
        Long count = dogodekZrno.getDogodkiCount(query);
        return Response.ok(dogodki).header("X-Total-Count", count).build();
    }
}
