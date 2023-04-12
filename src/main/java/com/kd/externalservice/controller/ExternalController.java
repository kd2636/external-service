package com.kd.externalservice.controller;

import com.google.api.gax.rpc.ServerStream;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.models.Query;
import com.google.cloud.bigtable.data.v2.models.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/external")
public class ExternalController {

    Logger LOGGER = LoggerFactory.getLogger(ExternalController.class);

    @Autowired
    BigtableDataClient bigtableDataClient;

    @GetMapping("/hello")
    public Row success(@RequestParam String rowKey) {
        Row row = bigtableDataClient.readRow("attributes", rowKey);
        LOGGER.info("got row - " + row);
        return row;

    }

    @GetMapping("/store")
    public List<String> store(@RequestParam String store) {
        Query query = Query.create("attributes").prefix(store);
        ServerStream<Row> rows = bigtableDataClient.readRows(query);
        List<String> rowKeysFetched = new ArrayList<>();
        for (Row row : rows) {
            rowKeysFetched.add(row.getKey().toStringUtf8());
            LOGGER.info("got row - " + row);
        }
        return rowKeysFetched;

    }

    @GetMapping("/fail-75")
    public String fail75() {
        if (ThreadLocalRandom.current().nextInt(100) < 75) {
            LOGGER.info("simulating error");
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "error occurred in external-service");
        }
        LOGGER.info("simulating success");
        return "Success from external service";
    }
}
