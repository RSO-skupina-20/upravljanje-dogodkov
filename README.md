# upravljanje-dogodkov

# 1. Dodajanje .env datoteke
- Dodati je potrebno DB_URL, DB_USER, DB_PASSWORD in JWT_SECRET
- Prvi trije podatki morajo ustrezati glede na naslednji korak, kjer ustvarimo bazo

## 2. Zagon API-ja upravljanje-dogodkov v Docker okolju
- Ustvarjanje podatkovne baze (za vse mikrostorive enako) `docker run --name najem-prostorov-db -e POSTGRES_PASSWORD=postgres -e POSTGRES_USER=postgres -e POSTGRES_DB=najem-prostorov -p 5434:5432 -d postgres`
- Ustvarjanje jar datoteke: `mvn clean package`
- Ustvarjanje Docker slike: `docker build -t upravljanje-dogodkov-api .`
- Zagon Docker kontejnerja: `docker run --env-file .env -p 8082:8080 upravljanje-dogodkov-api `

## 3. Api dokumentacija
Api dokumentacija je dostopna na naslovu: `http://localhost:8080/api-specs/ui`
