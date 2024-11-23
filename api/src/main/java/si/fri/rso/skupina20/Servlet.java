package si.fri.rso.skupina20;

import si.fri.rso.skupina20.entitete.Dogodek;
import si.fri.rso.skupina20.zrna.DogodekZrno;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

@WebServlet("/servlet")
public class Servlet extends HttpServlet {
    @Inject
    private DogodekZrno dogodekZrno;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Dogodek> dogodki = dogodekZrno.getDogodki();

        PrintWriter writer = resp.getWriter();

        for (Dogodek dogodek : dogodki) {
            writer.write("Dogodek: " + dogodek.getId() + " " + dogodek.getNaziv() + "\n");
        }

        // Search for a specific Dogodek with id 2
        Dogodek dogodek = dogodekZrno.getDogodek(2);
        writer.write("Dogodek pridobljen z getDogodek: " + dogodek.getId() + " " + dogodek.getNaziv() + "\n");

        // Update the Dogodek with id 2
        Dogodek dogodekToUpdate = dogodekZrno.getDogodek(2);
        dogodekToUpdate.setNaziv("Kolesarski izlet");
        dogodekZrno.updateDogodek(dogodekToUpdate.getId(), dogodekToUpdate);

        // Create a new Dogodek
        Dogodek dogodekToCreate = new Dogodek();
        dogodekToCreate.setNaziv("Tenis turnir");
        dogodekToCreate.setZacetek(new Date());
        dogodekToCreate.setKonec(new Date(dogodekToCreate.getZacetek().getTime() + 3600000));
        dogodekToCreate.setOpis("Tenis turnir za vse ljubitelje tenisa");
        dogodekToCreate.setCena(10.0);
        dogodekToCreate.setIdProstor(1);
        dogodekToCreate.setIdUporabnik(1);

        dogodekZrno.createDogodek(dogodekToCreate);
        writer.write("Dogodek ustvarjen z createDogodek: " + dogodekToCreate.getId() + " " + dogodekToCreate.getNaziv() + "\n");
        writer.write("Povabljeni na dogodek 1: " + dogodekZrno.getDogodek(1).getPovabljeni() + "\n");
    }
}
