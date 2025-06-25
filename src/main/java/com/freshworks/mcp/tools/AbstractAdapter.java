package com.freshworks.mcp.tools;

import com.fasterxml.jackson.databind.JsonNode;
import com.freshworks.mcp.utils.http.RestClient;
import com.freshworks.mcp.utils.http.Serializer;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

public class AbstractAdapter {
    private static final Logger logger = LoggerFactory.getLogger(RestClient.class);
    private static final Serializer serializer = new Serializer();

    /**
     * Parses the HTTP response body into a JsonNode.
     *
     * @param response The HTTP response to parse.
     * @return A JsonNode representing the parsed JSON data, or null if the response body is empty.
     * @throws IOException If an error occurs while reading the response body.
     */
    protected JsonNode parse(Response response) throws IOException {
        Optional<ResponseBody> body = Optional.ofNullable(response.body());
        if (body.isPresent()) {
            String jsonStr = body.get().string();
            return serializer.parse(jsonStr);
        }
        return null;
    }
}
