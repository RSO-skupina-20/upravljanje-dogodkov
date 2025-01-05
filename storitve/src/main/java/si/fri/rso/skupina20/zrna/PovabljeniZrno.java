package si.fri.rso.skupina20.zrna;

import si.fri.rso.skupina20.auth.PreverjanjeZetonov;
import si.fri.rso.skupina20.entitete.Povabljeni;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

public class PovabljeniZrno {
    @PersistenceContext(unitName = "upravljanje-dogodkov-jpa")
    private EntityManager em;
    private Logger log = Logger.getLogger(PovabljeniZrno.class.getName());

    @Inject
    private DogodekZrno dogodekZrno;

    /***
     * Pridobi vse povabljene
     * @return seznam vseh povabljenih, ki so na voljo v bazi
     */
    public List<Povabljeni> getVabila() {
        try {
            return em.createNamedQuery("Povabljeni.getAll", Povabljeni.class).getResultList();
        } catch (Exception e) {
            log.severe("Napaka pri pridobivanju povabljenih: " + e.getMessage());
            return null;
        }
    }

    /***
     * Pridobi povabljenega iz baze glede na id povabila
     * @param id id povabila, ki ga želimo pridobiti
     * @return povabljenega, ki ga želimo pridobiti
     */
    public Povabljeni getVabilo(int id) {
        try {
            return em.createNamedQuery("Povabljeni.getVabilo", Povabljeni.class).setParameter("id", id).getSingleResult();
        } catch (Exception e) {
            log.severe("Napaka pri pridobivanju povabljenega: " + e.getMessage());
            return null;
        }
    }

    /***
     * Pridobi vsa povabila glede na id dogodkađ
     * @param id id, za katerega želimo pridobiti povabila
     * @return seznam povabljenih, ki so na voljo v bazi
     */
    public List<Povabljeni> getVabilaDogodka(int id) {
        try {
            return em.createNamedQuery("Povabljeni.getVabilaDogodka", Povabljeni.class).setParameter("id", id).getResultList();
        } catch (Exception e) {
            log.severe("Napaka pri pridobivanju povabljenih za dogodek: " + e.getMessage());
            return null;
        }
    }

    /***
     * Pridobi vsa povabila glede na id dogodka, ki so sprejeta
     * @param id id, za katerega želimo pridobiti povabila
     * @return seznam povabljenih, ki so na voljo v bazi
     */
    public List<Povabljeni> getVabilaDogodkaSprejeta(int id) {
        try {
            List<Povabljeni> vsiPovabljeni = this.getVabilaDogodka(id);
            vsiPovabljeni.removeIf(povabljeni -> !povabljeni.getSprejeto());
            return vsiPovabljeni;
        } catch (Exception e) {
            log.severe("Napaka pri pridobivanju povabljenih za dogodek: " + e.getMessage());
            return null;
        }
    }

    /***
     * Pridobi vsa povabila glede na id dogodka, ki so nesprejeta
     * @param id id, za katerega želimo pridobiti povabila
     * @return seznam povabljenih, ki so na voljo v bazi
     */
    public List<Povabljeni> getVabilaDogodkaNesprejeta(int id) {
        try {
            List<Povabljeni> vsiPovabljeni = this.getVabilaDogodka(id);
            vsiPovabljeni.removeIf(povabljeni -> povabljeni.getSprejeto());
            return vsiPovabljeni;
        } catch (Exception e) {
            log.severe("Napaka pri pridobivanju povabljenih za dogodek: " + e.getMessage());
            return null;
        }
    }

    /***
     * Ustvari novo vabilo
     * @param povabljeni vabilo, ki ga želimo dodati v bazo
     * @return vabilo, ki smo ga dodali v bazo
     */
    @Transactional
    public Povabljeni createPovabljeni(Povabljeni povabljeni, String token) {
        /*
        Integer uporabnik_id = PreverjanjeZetonov.verifyToken(token);
        if (uporabnik_id == -1) {
            return null;
        }
         */
        try {
            // Preveri, če povabljeni vsebuje vse potrebne podatke
            if (povabljeni == null || povabljeni.getIme() == null || povabljeni.getPriimek() == null ||
                    povabljeni.getEmail() == null || povabljeni.getId_dogodek() == null || povabljeni.getSprejeto() == null) {
                log.info("Povabljeni ne vsebuje vseh potrebnih podatkov");
                return null;
            }
            // Preveri, če dogodek obstaja
            if (dogodekZrno.getDogodek(povabljeni.getId_dogodek()) == null) {
                log.info("Dogodek ne obstaja, zato povabila ni mogoče ustvariti");
                return null;
            }
            em.persist(povabljeni);
            return povabljeni;
        } catch (Exception e) {
            log.severe("Napaka pri dodajanju povabljenega: " + e.getMessage());
            return null;
        }
    }

    /***
     * Posodobi vabilo
     * @param id id vabila, ki ga želimo posodobiti
     * @param povabljeni novi podatki vabila
     * @return posodobljeno vabilo
     */
    @Transactional
    public Povabljeni updatePovabljeni(int id, Povabljeni povabljeni) {
        try {
            Povabljeni oldPovabljeni = this.getVabilo(id);

            // Če povabljeni ne obstaja, vrni izjemo
            if (oldPovabljeni == null) {
                log.info("Povabljeni s podanim id-jem ne obstaja");
                return null;
            }
            // Preveri, če dogodek obstaja, v kolikor je bila sprememba dogodka
            if (povabljeni.getId_dogodek() != null && dogodekZrno.getDogodek(povabljeni.getId_dogodek()) == null) {
                log.info("Dogodek ne obstaja, zato povabila ni mogoče posodobiti");
                return null;
            }
            // Če je vnesen nov podatek, ga uporabi, sicer uporabi obstoječega
            povabljeni.setId(id);
            povabljeni.setEmail(povabljeni.getEmail() != null ? povabljeni.getEmail() : oldPovabljeni.getEmail());
            povabljeni.setIme(povabljeni.getIme() != null ? povabljeni.getIme() : oldPovabljeni.getIme());
            povabljeni.setPriimek(povabljeni.getPriimek() != null ? povabljeni.getPriimek() : oldPovabljeni.getPriimek());
            povabljeni.setId_dogodek(povabljeni.getId_dogodek() != null ? povabljeni.getId_dogodek() : oldPovabljeni.getId_dogodek());
            povabljeni.setSprejeto(povabljeni.getSprejeto() != null ? povabljeni.getSprejeto() : oldPovabljeni.getSprejeto());

            em.merge(povabljeni);
            return povabljeni;
        } catch (Exception e) {
            log.severe("Napaka pri posodabljanju povabljenega: " + e.getMessage());
            return null;
        }
    }

    /***
     * Izbriši vabilo
     * @param id id vabila, ki ga želimo izbrisati
     * @return true, če je bilo vabilo uspešno izbrisano, false sicer
     */
    @Transactional
    public boolean deletePovabljeni(int id) {
        try {
            Povabljeni povabljeni = this.getVabilo(id);

            if (povabljeni != null) {
                em.createNamedQuery("Povabljeni.deletePovabljeni").setParameter("id", id).executeUpdate();
                return true;
            }
            return false;
        } catch (Exception e) {
            log.severe("Napaka pri brisanju povabljenega: " + e.getMessage());
            return false;
        }
    }
}
