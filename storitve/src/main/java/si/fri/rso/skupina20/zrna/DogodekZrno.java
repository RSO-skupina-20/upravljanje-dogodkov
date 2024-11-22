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

    /***
     * Pridobi vse dogodke iz baze
     * @return seznam vseh dogodkov, ki so na voljo v bazi
     */
    public List<Dogodek> getDogodki() {
        try {
            return em.createNamedQuery("Dogodek.getAll", Dogodek.class).getResultList();
        } catch (Exception e) {
            log.severe("Napaka pri pridobivanju dogodkov: " + e.getMessage());
            return null;
        }
    }

    /***
     * Pridobi dogodek iz baze glede na id
     * @param id id dogodka, ki ga želimo pridobiti
     * @return dogodek, ki ga želimo pridobiti
     */
    public Dogodek getDogodek(int id) {
        try {
            return em.createNamedQuery("Dogodek.getDogodek", Dogodek.class).setParameter("id", id).getSingleResult();
        } catch (Exception e) {
            log.severe("Napaka pri pridobivanju dogodka: " + e.getMessage());
            return null;
        }
    }

    /***
     * Izbriši dogodek iz baze glede na id
     * @param id id dogodka, ki ga želimo izbrisati
     * @return true, če je bil dogodek uspešno izbrisan, false sicer
     */
    public boolean deleteDogodek(int id) {
        try {
            Dogodek dogodek = this.getDogodek(id);

            if (dogodek != null) {
                em.createNamedQuery("Dogodek.deleteDogodek").setParameter("id", id).executeUpdate();
                return true;
            }
            return false;
        } catch (Exception e) {
            log.severe("Napaka pri brisanju dogodka: " + e.getMessage());
            return false;
        }
    }
}
