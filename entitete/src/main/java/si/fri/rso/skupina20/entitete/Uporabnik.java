package si.fri.rso.skupina20.entitete;

import javax.persistence.*;

@Entity(name = "uporabnik")
@NamedQueries(value = {
        @NamedQuery(name = "Uporabnik.getAll", query = "SELECT u FROM uporabnik u"),
        @NamedQuery(name = "Uporabnik.getUporabnik", query = "SELECT u FROM uporabnik u WHERE u.id = :id"),
        @NamedQuery(name = "Uporabnik.updateUporabnik", query = "UPDATE uporabnik u SET u.ime = :ime, u.priimek = :priimek, u.email = :email, u.telefon = :telefon, u.geslo = :geslo WHERE u.id = :id"),
        @NamedQuery(name = "Uporabnik.deleteUporabnik", query = "DELETE FROM uporabnik u WHERE u.id = :id")
})
public class Uporabnik {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "ime", nullable = false)
    private String ime;
    @Column(name = "priimek", nullable = false)
    private String priimek;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "telefon", nullable = false)
    private String telefon;
    @Column(name = "geslo", nullable = false)
    private String geslo;

}
