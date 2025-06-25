package com.freshworks.mcp.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class SchemaLoader {

    private static final Logger log = LoggerFactory.getLogger(SchemaLoader.class);

    public static String loadSchema(String schemaFileName) {
        try (InputStream inputStream = SchemaLoader.class.getClassLoader()
                .getResourceAsStream("schemas/" + schemaFileName)) {

            if (inputStream == null) {
                throw new IllegalArgumentException("Schema file not found: " + schemaFileName);
            }

            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        } catch (IOException e) {
            log.error("Error loading schema file: {}", schemaFileName, e);
            throw new RuntimeException("Failed to load schema: " + schemaFileName, e);
        }
    }
} 