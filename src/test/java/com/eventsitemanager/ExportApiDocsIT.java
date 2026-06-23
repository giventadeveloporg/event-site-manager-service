package com.eventsitemanager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

/**
 * Exports OpenAPI JSON into {@code src/main/resources/swagger/} (same output as {@code scripts/export-api-docs.ps1}).
 */
@IntegrationTest
@AutoConfigureMockMvc
class ExportApiDocsIT {

    private static final Path OUTPUT_DIR = Paths.get("src/main/resources/swagger");

    @Autowired
    private MockMvc mockMvc;

    @Test
    void exportOpenApiDocumentation() throws Exception {
        exportGroup("all-apis", "api-docs.json");
        exportGroup("authentication", "api-docs-authentication.json");
        exportGroup("admin", "api-docs-admin.json");
        exportGroup("webhooks", "api-docs-webhooks.json");
    }

    private void exportGroup(String group, String fileName) throws Exception {
        MvcResult result = mockMvc.perform(get("/v3/api-docs/" + group)).andExpect(status().isOk()).andReturn();
        Files.createDirectories(OUTPUT_DIR);
        Path outputFile = OUTPUT_DIR.resolve(fileName);
        Files.writeString(outputFile, result.getResponse().getContentAsString(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
    }
}
