package si.fri.rso.skupina20.entitete;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity(name = "dogodek")
@NamedQueries(value = {
        @NamedQuery(name = "Dogodek.getAll", query = "SELECT d FROM dogodek d"),
        @NamedQuery(name = "Dogodek.getDogodek", query = "SELECT d FROM dogodek d WHERE d.id = :id"),
        @NamedQuery(name = "Dogodek.updateDogodek", query = "UPDATE dogodek d SET d.naziv = :naziv, d.zacetek = :zacetek, d.konec = :konec, d.opis = :opis, d.cena = :cena, d.id_prostor = :id_prostor, d.id_uporabnik = :id_uporabnik, d.obvescanje_na_mail = :obvescanje_na_mail WHERE d.id = :id"),
        @NamedQuery(name = "Dogodek.deleteDogodek", query = "DELETE FROM dogodek d WHERE d.id = :id")
})
public class Dogodek {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_dogodek;

    @Column(name = "naziv", nullable = false)
    private String naziv;

    @Column(name = "zacetek", nullable = false)
    private Date zacetek;

    @Column(name = "konec", nullable = false)
    private Date konec;

    @Column(name = "opis", nullable = false)
    private String opis;

    @Column(name = "cena", nullable = false)
    private Double cena;

    @Column(name = "id_prostor", nullable = false)
    private Integer id_prostor;

    @OneToOne
    @Column(name = "id_uporabnik", nullable = false)
    private Uporabnik id_uporabnik;

    @OneToMany
    @Column(name = "obvescanje_na_mail", nullable = false)
    private List<Uporabnik> obvescanje_na_mail;

    public Integer getId() {
        return id_dogodek;
    }

    public void setId(Integer id) {
        this.id_dogodek = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public Date getZacetek() {
        return zacetek;
    }

    public void setZacetek(Date zacetek) {
        this.zacetek = zacetek;
    }

    public Date getKonec() {
        return konec;
    }

    public void setKonec(Date konec) {
        this.konec = konec;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public Double getCena() {
        return cena;
    }

    public void setCena(Double cena) {
        this.cena = cena;
    }

    public Integer getIdProstor() {
        return id_prostor;
    }

    public void setIdProstor(Integer id_prostor) {
        this.id_prostor = id_prostor;
    }

    public Uporabnik getIdUporabnik() {
        return id_uporabnik;
    }

    public void setIdUporabnik(Uporabnik id_uporabnik) {
        this.id_uporabnik = id_uporabnik;
    }

    public List<Uporabnik> getObvescanjeNaMail() {
        return obvescanje_na_mail;
    }

    public void setObvescanjeNaMail(List<Uporabnik> obvescanje_na_mail) {
        this.obvescanje_na_mail = obvescanje_na_mail;
    }


}
