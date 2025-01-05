package si.fri.rso.skupina20.entitete;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity(name = "dogodek")
@NamedQueries(value = {
        @NamedQuery(name = "Dogodek.getAll", query = "SELECT d FROM dogodek d"),
        @NamedQuery(name = "Dogodek.getDogodek", query = "SELECT d FROM dogodek d WHERE d.id_dogodek = :id"),
        @NamedQuery(name = "Dogodek.updateDogodek", query = "UPDATE dogodek d SET d.naziv = :naziv, d.zacetek = :zacetek, d.konec = :konec, d.opis = :opis, d.cena = :cena, d.id_prostor = :id_prostor, d.id_uporabnik = :id_uporabnik WHERE d.id_dogodek = :id"),
        @NamedQuery(name = "Dogodek.deleteDogodek", query = "DELETE FROM dogodek d WHERE d.id_dogodek = :id"),
        @NamedQuery(name = "Dogodek.getAllByProstor", query = "SELECT d FROM dogodek d WHERE d.id_prostor = :id_prostor"),
        @NamedQuery(name = "Dogodek.getAllByUporabnik", query = "SELECT d FROM dogodek d WHERE d.id_uporabnik = :id_uporabnik")
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

    @Column(name = "id_uporabnik", nullable = false)
    private Integer id_uporabnik;

    @OneToMany(mappedBy = "dogodek", fetch = FetchType.EAGER)
    private List<Povabljeni> povabljeni;

    public Integer getId_dogodek() {
        return id_dogodek;
    }

    public void setId_dogodek(Integer id_dogodek) {
        this.id_dogodek = id_dogodek;
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

    public Integer getId_prostor() {
        return id_prostor;
    }

    public void setId_prostor(Integer id_prostor) {
        this.id_prostor = id_prostor;
    }

    public Integer getId_uporabnik() {
        return id_uporabnik;
    }

    public void setId_uporabnik(Integer id_uporabnik) {
        this.id_uporabnik = id_uporabnik;
    }

    public List<Povabljeni> getPovabljeni() {
        return povabljeni;
    }

    public void setPovabljeni(List<Povabljeni> povabljeni) {
        this.povabljeni = povabljeni;
    }
}
