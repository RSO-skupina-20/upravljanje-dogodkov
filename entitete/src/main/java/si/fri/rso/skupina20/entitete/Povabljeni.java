package si.fri.rso.skupina20.entitete;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;

@Entity(name = "povabljeni")
@NamedQueries(value = {
        @NamedQuery(name = "Povabljeni.getAll", query = "SELECT p FROM povabljeni p"),
        @NamedQuery(name = "Povabljeni.getVabilo", query = "SELECT p FROM povabljeni p WHERE p.id = :id"),
        @NamedQuery(name = "Povabljeni.getVabilaDogodka", query = "SELECT p FROM povabljeni p WHERE p.id_dogodek = :id"),
        @NamedQuery(name = "Povabljeni.updatePovabljeni", query = "UPDATE povabljeni p SET p.ime = :ime, p.priimek = :priimek, p.email = :email, p.id_dogodek = :id_dogodek, p.sprejeto = :sprejeto WHERE p.id = :id"),
        @NamedQuery(name = "Povabljeni.deletePovabljeni", query = "DELETE FROM povabljeni p WHERE p.id = :id")
})
public class Povabljeni {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ime", nullable = false)
    private String ime;

    @Column(name = "priimek", nullable = false)
    private String priimek;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "id_dogodek", nullable = false)
    private Integer id_dogodek;

    @Column(name = "sprejeto", nullable = false)
    private Boolean sprejeto;

    @ManyToOne
    @JoinColumn(name = "id_dogodek", insertable = false, updatable = false)
    @JsonbTransient
    private Dogodek dogodek;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPriimek() {
        return priimek;
    }

    public void setPriimek(String priimek) {
        this.priimek = priimek;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getId_dogodek() {
        return id_dogodek;
    }

    public void setId_dogodek(Integer id_dogodek) {
        this.id_dogodek = id_dogodek;
    }

    public Boolean getSprejeto() {
        return sprejeto;
    }

    public void setSprejeto(Boolean sprejeto) {
        this.sprejeto = sprejeto;
    }

    public Dogodek getDogodek() {
        return dogodek;
    }

    public void setDogodek(Dogodek dogodek) {
        this.dogodek = dogodek;
    }
}
