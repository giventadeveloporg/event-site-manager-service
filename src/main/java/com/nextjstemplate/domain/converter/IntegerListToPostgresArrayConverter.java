package com.nextjstemplate.domain.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Converter for converting List<Integer> to/from TEXT column storing JSON array.
 *
 * Stores the list as a JSON array string (e.g., "[1,2,3]") in a TEXT column.
 * This approach is consistent with how other TEXT fields store JSON data in the codebase.
 */
@Converter
public class IntegerListToPostgresArrayConverter implements AttributeConverter<List<Integer>, String> {

    private static final Logger log = LoggerFactory.getLogger(IntegerListToPostgresArrayConverter.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Integer> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            // Return null for empty lists - TEXT column can be null
            return null;
        }
        try {
            // Convert to JSON array format: [1,2,3]
            return objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            log.error("Failed to serialize List<Integer> to JSON string", e);
            throw new RuntimeException("Failed to serialize List<Integer> to JSON", e);
        }
    }

    @Override
    public List<Integer> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return new ArrayList<>();
        }

        try {
            // Parse JSON array format: [1,2,3]
            String trimmed = dbData.trim();

            // Handle legacy PostgreSQL array format {1,2,3} for backward compatibility
            if (trimmed.startsWith("{") && trimmed.endsWith("}")) {
                log.debug("Converting legacy PostgreSQL array format to JSON format");
                String cleaned = trimmed.replace("{", "").replace("}", "");
                if (cleaned.isEmpty()) {
                    return new ArrayList<>();
                }
                // Convert to JSON array format
                String[] parts = cleaned.split(",");
                List<Integer> result = new ArrayList<>();
                for (String part : parts) {
                    String trimmedPart = part.trim();
                    if (!trimmedPart.isEmpty()) {
                        result.add(Integer.parseInt(trimmedPart));
                    }
                }
                return result;
            }

            // Parse JSON array format
            return objectMapper.readValue(trimmed, new TypeReference<List<Integer>>() {});
        } catch (Exception e) {
            log.error("Failed to deserialize JSON string to List<Integer>: {}", dbData, e);
            // Return empty list on error to avoid breaking the application
            return new ArrayList<>();
        }
    }
}
