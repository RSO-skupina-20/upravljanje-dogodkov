# Get default image PostreSQL 13
FROM postgres:13

# Set the environment variable
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=postgres
ENV POSTGRES_DB=dogodki

# Run with:
#   1.) docker build -t dogodki-db .
#   2.) docker run -d --name upravljanje-dogodkov-db -p 5434:5432 dogodki-db