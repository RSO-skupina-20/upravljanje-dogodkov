# upravljanje-dogodkov
Mikrostoritev bo omogočala ustvarjanje, urejanje, brisanje ter pregled ustvarjenih dogodkov. Uporabnikom bo omogočala, 
da ustvarijo dogodek, pri čemer definirajo podrobnosti in izvedejo tudi rezervacijo ter plačilo prostora. Omogočeno bo 
tudi dodajanje gostov, ki bodo prejeli obvestilo na elektronsko pošto.

Funkcionalnosti aplikacije:
- Ustvarjanje dogodka: Uporabniki lahko ustvarijo nov dogodek. Po uspešni rezervaciji bodo prejeli obvestilo na elektronsko pošto
- Izbris dogodka: Uporabniki lahko izbrišejo dogodek, ki ga ne želijo več izvesti
- Urejanje dogodka: Uporabniki lahko urejajo podrobnosti dogodka
- Dodajanje gosta: Uporabniki lahko dodajo gosta na dogodek, ki bo prejel obvestilo na elektronsko pošto
- Izvedba plačila: Uporabniki lahko izvedejo plačilo za rezervacijo prostora


# 1. Dodajanje .env datoteke
- Dodati je potrebno DB_URL, DB_USER, DB_PASSWORD in JWT_SECRET
- Prvi trije podatki morajo ustrezati glede na naslednji korak, kjer ustvarimo bazo

## 2. Zagon API-ja upravljanje-dogodkov v Docker okolju
- Ustvarjanje podatkovne baze (za vse mikrostorive enako) `docker run --name najem-prostorov-db -e POSTGRES_PASSWORD=postgres -e POSTGRES_USER=postgres -e POSTGRES_DB=najem-prostorov -p 5434:5432 -d postgres`
- Ustvarjanje jar datoteke: `mvn clean package`
- Ustvarjanje Docker slike: `docker build -t upravljanje-dogodkov-api .`
- Zagon Docker kontejnerja: `docker run --env-file .env -p 8082:8080 upravljanje-dogodkov-api`

## 3. Api dokumentacija
Api dokumentacija je dostopna na naslovu: `http://localhost:8082/api-specs/ui`

## 4. Api metode
- GET v1/dogodki - Pridobitev vseh dogodkov
- POST v1/dogodki - Dodajanje novega dogodka (potreben jwt žeton)
- GET v1/dogodki/{id} - Pridobi dogodek glede na id
- PUT v1/dogodki/{id} - Posodobi dogodek glede na id (potreben jwt žeton)
- DELETE v1/dogodki/{id} - Izbriši dogodek (potreben jwt žeton)
- GET v1/povabljeni - Pridobi vse povabljene
- GET v1/povabljeni/{id} - Pridobi povabljenega glede na id
- GET v1/povabljeni/{id}/sprejeta - Pridobi vse povabljene, ki so sprejeli povabilo
- GET v1/povabljeni/{id}/nesprejeta - Pridobi vse povabljene, ki niso sprejeli povabila
- POST v1/povabljeni - Dodajanje novega povabljenega (potreben jwt žeton)
- PUT v1/povabljeni/{id} - Posodobi povabljenega glede na id (potreben jwt žeton)
- DELETE v1/povabljeni/{id} - Izbriši povabljenega (potreben jwt žeton)
