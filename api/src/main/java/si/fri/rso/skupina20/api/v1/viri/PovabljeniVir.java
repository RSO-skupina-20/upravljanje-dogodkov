package si.fri.rso.skupina20.api.v1.viri;

import com.kumuluz.ee.cors.annotations.CrossOrigin;
import com.kumuluz.ee.rest.beans.QueryParameters;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import si.fri.rso.skupina20.auth.PreverjanjeZetonov;
import si.fri.rso.skupina20.entitete.Dogodek;
import si.fri.rso.skupina20.entitete.Povabljeni;
import si.fri.rso.skupina20.utils.EmailSender;
import si.fri.rso.skupina20.zrna.DogodekZrno;
import si.fri.rso.skupina20.zrna.PovabljeniZrno;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@ApplicationScoped
@Path("/povabljeni")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@CrossOrigin(supportedMethods = "GET, POST, DELETE, PUT, HEAD, OPTIONS")
@Tag(name = "Povabljeni", description = "Upravljanje z povabljenimi na dogodke")
public class PovabljeniVir {
    @Context
    protected UriInfo uriInfo;

    @Inject
    private PovabljeniZrno povabljeniZrno;

    @Inject
    private DogodekZrno dogodekZrno;

    @Inject
    private EmailSender emailSender;

    private static String okolje = System.getenv("ENVIRONMENT");

    @GET
    @Operation(summary = "Pridobi seznam vseh povabljenih", description = "Vrne seznam vseh povabljenih")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Seznam povabljenih", content = @Content(schema = @Schema(
                    implementation = Povabljeni.class))),
            @APIResponse(responseCode = "404", description = "Povabljenih ne obstajajo", content = @Content(
                    mediaType = "application/json", schema = @Schema(
                    implementation = String.class, example = "{\"napaka\": \"Povabljenih ni mogoče najti\"}"))),
    })
    // Pridobi seznam vseh povabljenih v bazi
    public Response vrniPovabljene() {
        QueryParameters query = QueryParameters.query(uriInfo.getRequestUri().getQuery()).build();
        List<Povabljeni> povabljeni = povabljeniZrno.getVabila();
        if (povabljeni == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("{\"napaka\": \"Povabljenih ni mogoče najti\"}")
                    .build();
        }
        return Response.ok(povabljeni).build();
    }

    // Pridobi povabljenega glede na id povabila
    @GET
    @Path("/{id}")
    @Operation(summary = "Pridobi vabilo glede na id", description = "Vrne vabilo glede na id")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Vabilo", content = @Content(
                    schema = @Schema(implementation = Povabljeni.class))),
            @APIResponse(responseCode = "404", description = "Vabilo ne obstaja", content = @Content(
                    mediaType = "application/json", schema = @Schema(
                    implementation = String.class, example = "{\"napaka\": \"Vabila ni mogoče najti\"}"))),
    })
    // Pridobi vabilo glede na id
    public Response vrniVabilo(@PathParam("id") Integer id) {
        Povabljeni povabljeni = povabljeniZrno.getVabilo(id);
        if (povabljeni == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("{\"napaka\": \"Vabila ni mogoče najti\"}")
                    .build();
        }
        return Response.ok(povabljeni).build();
    }

    // Pridobi seznam povabljenih glede na id dogodka
    @GET
    @Path("/dogodek/{id}")
    @Operation(summary = "Pridobi seznam povabljenih glede na id dogodka", description = "Vrne seznam povabljenih glede na id dogodka")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Seznam povabljenih", content = @Content(
                    schema = @Schema(implementation = Povabljeni.class))),
            @APIResponse(responseCode = "404", description = "Povabljenih ni mogoče najti", content = @Content(
                    mediaType = "application/json", schema = @Schema(
                    implementation = String.class, example = "{\"napaka\": \"Povabljenih ni mogoče najti\"}"))),
    })
    // Pridobi seznam povabljenih glede na id dogodka
    public Response vrniPovabilaDogodka(@PathParam("id") Integer id) {
        List<Povabljeni> povabljeni = povabljeniZrno.getVabilaDogodka(id);
        if (povabljeni == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("{\"napaka\": \"Povabljenih ni mogoče najti\"}")
                    .build();
        }
        return Response.ok(povabljeni).build();
    }

    // Pridobi seznam povabljenih glede na id dogodka, ki so sprejeta
    @GET
    @Path("/dogodek/{id}/sprejeta")
    @Operation(summary = "Pridobi seznam povabljenih glede na id dogodka, ki so sprejeta",
            description = "Vrne seznam povabljenih glede na id dogodka, ki so sprejeta")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Seznam povabljenih", content = @Content(
                    schema = @Schema(implementation = Povabljeni.class))),
            @APIResponse(responseCode = "404", description = "Povabljenih ni mogoče najti", content = @Content(
                    mediaType = "application/json", schema = @Schema(
                    implementation = String.class, example = "{\"napaka\": \"Povabljenih ni mogoče najti\"}"))),
    })
    // Pridobi seznam povabljenih glede na id dogodka, ki so sprejeta
    public Response vrniPovabilaDogodkaSprejeta(@PathParam("id") Integer id) {
        List<Povabljeni> povabljeni = povabljeniZrno.getVabilaDogodkaSprejeta(id);
        if (povabljeni == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("{\"napaka\": \"Povabljenih ni mogoče najti\"}")
                    .build();
        }
        return Response.ok(povabljeni).build();
    }

    // Pridobi seznam povabljenih glede na id dogodka, ki so nesprejeta
    @GET
    @Path("/dogodek/{id}/nesprejeta")
    @Operation(summary = "Pridobi seznam povabljenih glede na id dogodka, ki so nesprejeta",
            description = "Vrne seznam povabljenih glede na id dogodka, ki so nesprejeta")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Seznam povabljenih", content = @Content(
                    schema = @Schema(implementation = Povabljeni.class))),
            @APIResponse(responseCode = "404", description = "Povabljenih ni mogoče najti", content = @Content(
                    mediaType = "application/json", schema = @Schema(
                    implementation = String.class, example = "{\"napaka\": \"Povabljenih ni mogoče najti\"}"))),
    })
    // Pridobi seznam povabljenih glede na id dogodka, ki so nesprejeta
    public Response vrniPovabilaDogodkaNesprejeta(@PathParam("id") Integer id) {
        List<Povabljeni> povabljeni = povabljeniZrno.getVabilaDogodkaNesprejeta(id);
        if (povabljeni == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("{\"napaka\": \"Povabljenih ni mogoče najti\"}")
                    .build();
        }
        return Response.ok(povabljeni).build();
    }

    // Ustvari novo povabilo
    @POST
    @Operation(summary = "Ustvari novo povabilo", description = "Ustvari novo povabilo")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Povabilo ustvarjeno", content = @Content(
                    schema = @Schema(implementation = Povabljeni.class))),
            @APIResponse(responseCode = "400", description = "Neveljavni podatki", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = String.class, example =
                    "{\"napaka\": \"Neveljavni podatki\"}")))
    })
    // Ustvari novo povabilo
    @SecurityRequirement(name = "bearerAuth")
    public Response ustvariPovabilo(Povabljeni povabljeni, @HeaderParam("authorization") String authorization) {
        Dogodek dogodek = dogodekZrno.getDogodek(povabljeni.getId_dogodek());
        if (dogodek == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"napaka\": \"Dogodek ne obstaja\"}").build();
        }

        Povabljeni povabljeniNew = povabljeniZrno.createPovabljeni(povabljeni, authorization);

        // if created successfully, then send email
        if (povabljeniNew != null) {
            // send email to the invited person
            System.out.println("Sending email to " + povabljeni.getEmail());
            String to = povabljeni.getEmail();
            String subject = "Povabilo na dogodek";
            String body = "Spoštovani " + povabljeni.getIme() + ",\n\n" +
                    "Vabljeni ste na dogodek: " + dogodek.getNaziv() + ".\n" +
                    "Datum: " + dogodek.getZacetek() + "\n\n" +
                    "Lep pozdrav,\nOrganizator";

            String nameTo = povabljeni.getIme() + " " + povabljeni.getPriimek();

            EmailSender.sendEmail(nameTo, to, subject, body);
            if(okolje.equals("dev")){
                emailSender.sendEmailKafka(nameTo, to, subject, body);
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("{\"napaka\": \"Povabilo ni bilo uspešno ustvarjeno\"}").build();
        }
        return Response.status(Response.Status.CREATED).entity(povabljeni).build();
    }

    // Posodobi povabilo
    @PUT
    @Path("/{id}")
    @Operation(summary = "Posodobi povabilo", description = "Posodobi povabilo")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Povabilo posodobljeno", content = @Content(
                    schema = @Schema(implementation = Povabljeni.class))),
            @APIResponse(responseCode = "400", description = "Neveljavni podatki", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = String.class, example =
                    "{\"napaka\": \"Neveljavni podatki\"}"))),
            @APIResponse(responseCode = "404", description = "Povabilo ne obstaja", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = String.class, example =
                    "{\"napaka\": \"Povabilo ne obstaja\"}")))
    })
    // Posodobi povabilo
    @SecurityRequirement(name = "bearerAuth")
    public Response posodobiPovabilo(@PathParam("id") Integer id, @RequestBody(description = "Entiteta povabljeni", required = true, content = @Content(
            schema = @Schema(implementation = Povabljeni.class))) Povabljeni povabljeni, @HeaderParam("authorization") String authorization) {

        Integer uporabnik_id = PreverjanjeZetonov.verifyToken(authorization);
        if (uporabnik_id == -1) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("{\"napaka\": \"Nimate pravic za posodabljanje povabila\"}").build();
        }
        // Pridobi povabilo iz baze
        Povabljeni oldPovabljeni = povabljeniZrno.getVabilo(id);
        if (oldPovabljeni == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("{\"napaka\": \"Povabilo z id " + id + " ne obstaja\"}").build();
        }

        // Posodobi polja povabila
        oldPovabljeni.setDogodek((povabljeni.getDogodek() != null) ? povabljeni.getDogodek() : oldPovabljeni.getDogodek());
        oldPovabljeni.setIme((povabljeni.getIme() != null) ? povabljeni.getIme() : oldPovabljeni.getIme());
        oldPovabljeni.setPriimek((povabljeni.getPriimek() != null) ? povabljeni.getPriimek() : oldPovabljeni.getPriimek());
        oldPovabljeni.setEmail((povabljeni.getEmail() != null) ? povabljeni.getEmail() : oldPovabljeni.getEmail());
        oldPovabljeni.setId_dogodek((povabljeni.getId_dogodek() != null) ? povabljeni.getId_dogodek() : oldPovabljeni.getId_dogodek());
        oldPovabljeni.setSprejeto((povabljeni.getSprejeto() != null) ? povabljeni.getSprejeto() : oldPovabljeni.getSprejeto());

        // Shrani posodobljeno povabilo v bazo
        povabljeniZrno.updatePovabljeni(id, oldPovabljeni);

        // Vrni posodobljeno povabilo
        return Response.ok(oldPovabljeni).build();
    }

    // Izbriši povabilo
    @DELETE
    @Path("/{id}")
    @Operation(summary = "Izbriši povabilo", description = "Izbriši povabilo")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Povabilo izbrisano", content = @Content(
                    schema = @Schema(implementation = String.class, example = "{\"sporocilo\": \"Povabilo uspešno izbrisano\"}"))),
            @APIResponse(responseCode = "404", description = "Povabilo ne obstaja", content = @Content(
                    schema = @Schema(implementation = String.class, example = "{\"napaka\": \"Povabilo ne obstaja\"}")))
    })
    // Izbriši povabilo
    @SecurityRequirement(name = "bearerAuth")
    public Response izbrisiPovabilo(@PathParam("id") Integer id, @HeaderParam("authorization") String authorization) {
        if (!PreverjanjeZetonov.verifyOwner(authorization, id)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("{\"napaka\": \"Nimate pravic za brisanje povabila\"}").build();
        }
        if (povabljeniZrno.deletePovabljeni(id)) {
            return Response.ok("{\"sporocilo\": \"Povabilo uspešno izbrisano\"}").build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("{\"napaka\": \"Povabilo z id " + id + " ne obstaja\"}")
                .build();
    }

}
