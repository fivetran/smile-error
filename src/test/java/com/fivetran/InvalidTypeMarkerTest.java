package com.fivetran;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class InvalidTypeMarkerTest {

    private static final ObjectMapper SMILE = createSmile();

    private static ObjectMapper createSmile() {
        ObjectMapper mapper = new ObjectMapper(new SmileFactory());

        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);

        return mapper;
    }

    @Test
    public void fails() throws IOException {
        Path path = Paths.get("small.smile");
        InputStream in = Files.newInputStream(path);
        MappingIterator<JsonNode> it = SMILE.readerFor(JsonNode.class).readValues(in);

        int count = 0;

        while (it.hasNextValue()) {
            it.nextValue();

            count++;
        }

        System.out.println("Read " + count + " values");
    }
}
