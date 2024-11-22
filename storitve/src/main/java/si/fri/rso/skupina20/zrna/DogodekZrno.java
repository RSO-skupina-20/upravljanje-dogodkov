package si.fri.rso.skupina20.zrna;

import si.fri.rso.skupina20.entitete.Dogodek;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class DogodekZrno {
    @PersistenceContext(unitName = "upravljanje-dogodkov-jpa")
    private EntityManager em;
    private Logger log = Logger.getLogger(DogodekZrno.class.getName());

    public List<Dogodek> getDogodki() {
        try {
            return em.createNamedQuery("Dogodek.getAll", Dogodek.class).getResultList();
        } catch (Exception e) {
            log.severe("Napaka pri pridobivanju dogodkov: " + e.getMessage());
            return null;
        }
    }
}
