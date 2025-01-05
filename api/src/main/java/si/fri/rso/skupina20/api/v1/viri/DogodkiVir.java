package si.fri.rso.skupina20.api.v1.viri;

import com.kumuluz.ee.cors.annotations.CrossOrigin;
import com.kumuluz.ee.rest.beans.QueryParameters;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import si.fri.rso.skupina20.auth.PreverjanjeZetonov;
import si.fri.rso.skupina20.entitete.Dogodek;
import si.fri.rso.skupina20.zrna.DogodekZrno;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
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
            @APIResponse(responseCode = "200", description = "Seznam dogodkov", content = @Content(schema = @Schema(
                    implementation = Dogodek.class)),
                    headers = @Header(name = "X-Total-Count", description = "Število vrnjenih dogodkov", schema = @Schema(
                            type = SchemaType.INTEGER))),
            @APIResponse(responseCode = "404", description = "Dogodki ne obstajajo", content = @Content(
                    mediaType = "application/json", schema = @Schema(
                    implementation = String.class, example = "{\"napaka\": \"Dogodkov ni mogoče najti\"}"))),
    })
    public Response vrniDogodke() {
        QueryParameters query = QueryParameters.query(uriInfo.getRequestUri().getQuery()).build();
        List<Dogodek> dogodki = dogodekZrno.getDogodki(query);
        if (dogodki == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("{\"napaka\": \"Dogodkov ni mogoče najti\"}")
                    .build();
        }
        Long count = dogodekZrno.getDogodkiCount(query);
        return Response.ok(dogodki).header("X-Total-Count", count).build();
    }

    // Pridobi dogodek iz baze glede na id
    @GET
    @Operation(summary = "Pridobi dogodek glede na id", description = "Vrne dogodek glede na id")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Dogodek", content = @Content(
                    schema = @Schema(implementation = Dogodek.class))),
            @APIResponse(responseCode = "404", description = "Dogodek ne obstaja", content = @Content(
                    mediaType = "application/json", schema = @Schema(
                    implementation = String.class, example = "{\"napaka\": \"Dogodek ne obstaja\"}"))),
    })
    @Path("{id}")
    public Response vrniDogodek(@PathParam("id") Integer id) {
        Dogodek dogodek = dogodekZrno.getDogodek(id);
        if (dogodek == null) {
            return Response.status(Response.Status.NOT_FOUND).
                    entity("{\"napaka\": \"Dogodek z id " + id + " ne obstaja\"}").build();
        }
        return Response.ok(dogodek).build();
    }

    // Pridobi dogodke glede na id_uporabnik
    @GET
    @Path("/uporabnik/{id_uporabnik}")
    @Operation(summary = "Pridobi dogodke glede na id_uporabnik", description = "Vrne dogodke glede na id_uporabnik")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Seznam dogodkov", content = @Content(
                    schema = @Schema(implementation = Dogodek.class))),
            @APIResponse(responseCode = "404", description = "Dogodki ne obstajajo", content = @Content(
                    schema = @Schema(implementation = String.class, example = "{\"napaka\": \"Dogodkov ni mogoče najti\"}"))),
    })
    public Response vrniDogodkeUporabnik(@PathParam("id_uporabnik") Integer id_uporabnik) {
        List<Dogodek> dogodki = dogodekZrno.getDogodkiUporabnik(id_uporabnik);
        if (dogodki == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("{\"napaka\": \"Dogodkov ni mogoče najti\"}")
                    .build();
        }
        return Response.ok(dogodki).build();
    }

    // Ustvari nov dogodek
    @POST
    @Operation(summary = "Ustvari nov dogodek", description = "Ustvari nov dogodek")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Dogodek ustvarjen", content = @Content(
                    schema = @Schema(implementation = Dogodek.class))),
            @APIResponse(responseCode = "400", description = "Neveljavni podatki", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = String.class, example =
                    "{\"napaka\": \"Neveljavni podatki\"}"))),
            @APIResponse(responseCode = "401", description = "Neuspešna avtorizacija", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = String.class, example =
                    "{\"napaka\": \"Neveljaven avtorizacijski žeton\"}"))),
            @APIResponse(responseCode = "500", description = "Napaka na strežniku", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = String.class, example =
                    "{\"napaka\": \"Napaka na strežniku\"}")))
    })
    @SecurityRequirement(name = "bearerAuth")
    @Timed(name = "ustvariDogodekTimer")
    public Response ustvariDogodek(@RequestBody(description = "Entiteta dogodek", required = true, content = @Content(
            schema = @Schema(implementation = Dogodek.class, example = "{\"naziv\": \"Koncert\", \"zacetek\": \"2020-12-12 20:00:00\", \"konec\": \"2020-12-12 23:00:00\", \"opis\": \"Koncert skupine Siddharta\", \"cena\": 20, \"id_prostor\": 1, \"id_uporabnik\": 1}"))) Dogodek dogodek, @HeaderParam("Authorization") String authorization) {

        // Preveri, če so vhodni podatki veljavni
        if (dogodek.getNaziv() == null || dogodek.getZacetek() == null || dogodek.getKonec() == null ||
                dogodek.getOpis() == null || dogodek.getCena() == null || dogodek.getId_prostor() == null ||
                dogodek.getId_uporabnik() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"napaka\": \"Neveljavni podatki. Vsi obvezni parametri morajo biti vključeni.\"}")
                    .build();
        }

        try {
            Dogodek novDogodek = dogodekZrno.createDogodek(dogodek, authorization);

            if (novDogodek == null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("{\"napaka\": \"Neveljaven avtorizacijski žeton ali uporabnik ne obstaja.\"}")
                        .build();
            }

            return Response.status(Response.Status.CREATED).entity(novDogodek).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"napaka\": \"Napaka na strežniku\"}")
                    .build();
        }
    }


    // Izbriši dogodek iz baze glede na id
    @DELETE
    @Path("{id}")
    @Operation(summary = "Izbriši dogodek glede na id", description = "Izbriši dogodek glede na id")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Dogodek izbrisan", content = @Content(
                    schema = @Schema(implementation = String.class, example = "{\"sporocilo\": \"Dogodek uspešno izbrisan\"}"))),
            @APIResponse(responseCode = "404", description = "Dogodek ne obstaja", content = @Content(
                    schema = @Schema(implementation = String.class, example = "{\"napaka\": \"Dogodek ne obstaja\"}"))),
            @APIResponse(responseCode = "403", description = "Nimate pravic za brisanje dogodka", content = @Content(
                    schema = @Schema(implementation = String.class, example = "{\"napaka\": \"Nimate pravic za brisanje dogodka\"}")))
    })
    @SecurityRequirement(name = "bearerAuth")
    public Response izbrisiDogodek(@PathParam("id") Integer id, @HeaderParam("authorization") String authorization) {
        if (!PreverjanjeZetonov.verifyOwner(authorization, id)) {
            return Response.status(Response.Status.FORBIDDEN).entity("{\"napaka\": \"Nimate pravic za brisanje dogodka\"}")
                    .build();
        }
        if (dogodekZrno.deleteDogodek(id)) {
            return Response.ok("{\"sporocilo\": \"Dogodek uspešno izbrisan\"}").build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("{\"napaka\": \"Dogodek z id " + id + " ne obstaja\"}")
                .build();
    }

    // Posodobi dogodek
    @PUT
    @Path("{id}")
    @Operation(summary = "Posodobi dogodek glede na id", description = "Posodobi dogodek glede na id")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Dogodek posodobljen", content = @Content(
                    schema = @Schema(implementation = Dogodek.class))),
            @APIResponse(responseCode = "400", description = "Neveljavni podatki", content = @Content(
                    schema = @Schema(implementation = String.class, example = "{\"napaka\": \"Neveljavni podatki\"}"))),
            @APIResponse(responseCode = "404", description = "Dogodek ne obstaja", content = @Content(
                    schema = @Schema(implementation = String.class, example = "{\"napaka\": \"Dogodek ne obstaja\"}"))),
            @APIResponse(responseCode = "403", description = "Nimate pravic za posodabljanje dogodka", content = @Content(
                    schema = @Schema(implementation = String.class, example = "{\"napaka\": \"Nimate pravic za posodabljanje dogodka\"}")))
    })
    @SecurityRequirement(name = "bearerAuth")
    public Response posodobiDogodek(@PathParam("id") Integer id, @RequestBody(description = "Entiteta dogodek", required = true, content = @Content(
            schema = @Schema(implementation = Dogodek.class, example = "{\"naziv\": \"Koncert\", \"zacetek\": \"2020-12-12 20:00:00\", \"konec\": \"2020-12-12 23:00:00\", \"opis\": \"Koncert skupine Siddharta\", \"cena\": 20, \"id_prostor\": 1, \"id_uporabnik\": 1}"))) Dogodek dogodek, @HeaderParam("authorization") String authorization) {

        /*
        Integer uporabnik_id = PreverjanjeZetonov.verifyToken(authorization);
        if (uporabnik_id == -1) {
            return Response.status(Response.Status.FORBIDDEN).entity("{\"napaka\": \"Nimate pravic za posodabljanje dogodka\"}")
                    .build();
        }
         */
        // Pridobi dogodek iz baze
        Dogodek oldDogodek = dogodekZrno.getDogodek(id);
        if (oldDogodek == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("{\"napaka\": \"Dogodek z id " + id + " ne obstaja\"}")
                    .build();
        }

        // Posodobi polja dogodka
        oldDogodek.setNaziv((dogodek.getNaziv() != null) ? dogodek.getNaziv() : oldDogodek.getNaziv());
        oldDogodek.setZacetek((dogodek.getZacetek() != null) ? dogodek.getZacetek() : oldDogodek.getZacetek());
        oldDogodek.setKonec((dogodek.getKonec() != null) ? dogodek.getKonec() : oldDogodek.getKonec());
        oldDogodek.setOpis((dogodek.getOpis() != null) ? dogodek.getOpis() : oldDogodek.getOpis());
        oldDogodek.setCena((dogodek.getCena() != null) ? dogodek.getCena() : oldDogodek.getCena());
        oldDogodek.setId_prostor((dogodek.getId_prostor() != null) ? dogodek.getId_prostor() : oldDogodek.getId_prostor());
        oldDogodek.setId_uporabnik((dogodek.getId_uporabnik() != null) ? dogodek.getId_uporabnik() : oldDogodek.getId_uporabnik());

        // Shrani posodobljen dogodek v bazo
        dogodekZrno.updateDogodek(id, oldDogodek);

        // Vrni posodobljen dogodek
        return Response.ok(oldDogodek).build();
    }

    // Pridobi dogodke glede na id_prostor
    @GET
    @Path("/prostor/{id_prostor}")
    @Operation(summary = "Pridobi dogodke glede na id_prostor", description = "Vrne dogodke glede na id_prostor")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Seznam dogodkov", content = @Content(
                    schema = @Schema(implementation = Dogodek.class))),
            @APIResponse(responseCode = "404", description = "Dogodki ne obstajajo", content = @Content(
                    schema = @Schema(implementation = String.class, example = "{\"napaka\": \"Dogodkov ni mogoče najti\"}"))),
    })
    public Response vrniDogodkeProstor(@PathParam("id_prostor") Integer id_prostor) {
        List<Dogodek> dogodki = dogodekZrno.getDogodkiProstor(id_prostor);
        if (dogodki == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("{\"napaka\": \"Dogodkov ni mogoče najti\"}")
                    .build();
        }
        return Response.ok(dogodki).build();
    }
}
