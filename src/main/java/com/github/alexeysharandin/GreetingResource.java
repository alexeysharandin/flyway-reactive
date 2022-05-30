package com.github.alexeysharandin;

import io.quarkus.reactive.datasource.ReactiveDataSource;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowIterator;
import io.vertx.mutiny.sqlclient.RowSet;
import org.flywaydb.core.Flyway;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hello")
public class GreetingResource {
    @Inject
    @ReactiveDataSource("reactpg")
    io.vertx.mutiny.pgclient.PgPool client;

    @Inject
    Flyway flyway;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from RESTEasy Reactive";
    }

    @GET
    @Path("flyway")
    @Produces(MediaType.TEXT_PLAIN)
    public String flyway() {
        return flyway.info().current().getVersion().toString();
    }

    @GET
    @Path("pg")
    public String pg() {
        RowSet<Row> rows = client.query("SELECT 1").executeAndAwait();
        RowIterator<Row> iterator = rows.iterator();

        if (iterator.hasNext()) {
            return String.valueOf(iterator.next().getInteger(1));
        }
        return "Ups";
    }

}