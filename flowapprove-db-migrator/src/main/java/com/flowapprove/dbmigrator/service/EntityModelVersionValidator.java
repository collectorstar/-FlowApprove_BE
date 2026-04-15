package com.flowapprove.dbmigrator.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class EntityModelVersionValidator {

    public void validate() {
        try (InputStream inputStream = getClass().getResourceAsStream("/db/model/current-model-version.txt")) {
            if (inputStream == null) {
                throw new IllegalStateException("Missing current-model-version.txt.");
            }
            String modelVersion = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8).trim();
            if (!Objects.equals(modelVersion, "1")) {
                throw new IllegalStateException("Unexpected persistence model version: " + modelVersion);
            }
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to validate persistence model version.", ex);
        }
    }
}
