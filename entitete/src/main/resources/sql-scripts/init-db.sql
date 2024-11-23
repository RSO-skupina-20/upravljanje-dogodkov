INSERT INTO dogodek (naziv, zacetek, konec, opis, cena, id_prostor, id_uporabnik) VALUES ('Koncert', '2020-12-12 20:00:00', '2020-12-12 23:00:00', 'Koncert skupine Siddharta', 20, 1, 1);
INSERT INTO dogodek (naziv, zacetek, konec, opis, cena, id_prostor, id_uporabnik) VALUES ('Predavanje', '2020-12-15 18:00:00', '2020-12-15 20:00:00', 'Predavanje o programiranju', 0, 2, 2);

INSERT INTO uporabnik (email, geslo, ime, priimek, telefon) VALUES ('janez.novak@gmail.com', 'janez', 'Janez', 'Novak', '040123456');
INSERT INTO uporabnik (email, geslo, ime, priimek, telefon) VALUES ('marija.cesen@gmail.com', 'marija', 'Marija', 'Cesen', '041654321');
INSERT INTO uporabnik (email, geslo, ime, priimek, telefon) VALUES ('tajda.vrhovec@gmail.com', 'tajda', 'Tajda', 'Vrhovec', '031987654');

INSERT INTO povabljeni (email, id_dogodek, ime, priimek, sprejeto) VALUES ('janez.novak@gmail.com"', 1, 'Janez', 'Novak', TRUE);
INSERT INTO povabljeni (email, id_dogodek, ime, priimek, sprejeto) VALUES ('matija.nabija@gmail.com', 1, 'Matija', 'Nabija', FALSE);
INSERT INTO povabljeni (email, id_dogodek, ime, priimek, sprejeto) VALUES ('benjamin.sesko@gmail.com', 2, 'Benjamin', 'Šeško', TRUE);