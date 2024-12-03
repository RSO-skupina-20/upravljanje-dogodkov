package si.fri.rso.skupina20.zrna;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import com.sun.xml.bind.v2.TODO;
import si.fri.rso.skupina20.entitete.Dogodek;
import si.fri.rso.skupina20.entitete.Uporabnik;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
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
     * Pridobi dogodke iz baze glede na query
     * @param query
     * @return
     */
    public List<Dogodek> getDogodki(QueryParameters query) {
        return JPAUtils.queryEntities(em, Dogodek.class, query);
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
    @Transactional
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

    /***
     * Dodaj dogodek v bazo
     * @param dogodek dogodek, ki ga želimo dodati v bazo
     * @return dogodek, ki smo ga dodali v bazo
     */
    @Transactional
    public Dogodek createDogodek(Dogodek dogodek) {
        // Preveri, če dogodek vsebuje vse potrebne podatke
        if (dogodek == null || dogodek.getNaziv() == null || dogodek.getZacetek() == null ||
                dogodek.getKonec() == null || dogodek.getOpis() == null || dogodek.getCena() == null ||
                dogodek.getId_prostor() == null || dogodek.getId_uporabnik() == null) {
            log.severe("Dogodek ni pravilno definiran");
            return null;
        }
        // Preveri, uporabnik s tem id-jem obstaja
        if (em.createNamedQuery("Uporabnik.getUporabnik", Uporabnik.class).setParameter("id", dogodek.getId_uporabnik()).getResultList().isEmpty()) {
            log.severe("Uporabnik z id-jem " + dogodek.getId_uporabnik() + " ne obstaja");
            return null;
        }

        TODO:
        //Preveri, če prostor s tem id-jem obstaja in pa če je prostor na voljo v času dogodka

        try {
            em.persist(dogodek);
            return dogodek;
        } catch (Exception e) {
            log.severe("Napaka pri dodajanju dogodka: " + e.getMessage());
            return null;
        }
    }

    /***
     * Posodobi dogodek v bazi
     * @param id id dogodka, ki ga želimo posodobiti
     * @param dogodek novi podatki dogodka, ki ga želimo posodobiti
     * @return posodobljen dogodek
     */
    @Transactional
    public Dogodek updateDogodek(int id, Dogodek dogodek) {
        try {
            Dogodek oldDogodek = this.getDogodek(id);

            oldDogodek.setNaziv((dogodek.getNaziv() != null) ? dogodek.getNaziv() : oldDogodek.getNaziv());
            oldDogodek.setZacetek((dogodek.getZacetek() != null) ? dogodek.getZacetek() : oldDogodek.getZacetek());
            oldDogodek.setKonec((dogodek.getKonec() != null) ? dogodek.getKonec() : oldDogodek.getKonec());
            oldDogodek.setOpis((dogodek.getOpis() != null) ? dogodek.getOpis() : oldDogodek.getOpis());
            oldDogodek.setCena((dogodek.getCena() != null) ? dogodek.getCena() : oldDogodek.getCena());
            oldDogodek.setId_prostor((dogodek.getId_prostor() != null) ? dogodek.getId_prostor() : oldDogodek.getId_prostor());
            oldDogodek.setId_uporabnik((dogodek.getId_uporabnik() != null) ? dogodek.getId_uporabnik() : oldDogodek.getId_uporabnik());

            em.merge(oldDogodek);
        } catch (Exception e) {
            log.severe("Napaka pri posodabljanju dogodka: " + e.getMessage());
            return null;
        }

        return dogodek;
    }

    public Long getDogodkiCount(QueryParameters query) {
        return JPAUtils.queryEntitiesCount(em, Dogodek.class, query);
    }
}
