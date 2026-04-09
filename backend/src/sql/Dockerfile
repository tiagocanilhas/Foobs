FROM postgres:15-alpine

COPY create.sql /docker-entrypoint-initdb.d/01_create.sql
COPY insert.sql /docker-entrypoint-initdb.d/02_insert.sql

EXPOSE 5432