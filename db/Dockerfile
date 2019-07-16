FROM postgres:10
ENV POSTGRES_USER postgres
ENV POSTGRES_PASSWORD postgres
ADD schema.sql /docker-entrypoint-initdb.d
EXPOSE 5432