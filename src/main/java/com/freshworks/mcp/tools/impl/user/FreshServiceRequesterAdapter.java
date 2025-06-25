package com.freshworks.mcp.tools.impl.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshworks.mcp.constants.PathConstants;
import com.freshworks.mcp.tools.AbstractAdapter;
import com.freshworks.mcp.tools.api.FreshServiceRequesterToolKit;
import com.freshworks.mcp.utils.http.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.StringJoiner;

public class FreshServiceRequesterAdapter extends AbstractAdapter
        implements FreshServiceRequesterToolKit {
    private static final Logger logger = LoggerFactory.getLogger(FreshServiceRequesterAdapter.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public JsonNode listRequesters(String domain, String apiKey, Map<String, Object> queryParams) {
        try {
            RestClient restClient = new RestClient(domain, apiKey);
            String path = PathConstants.REQUESTERS;
            
            // Build query string from parameters
            String queryString = buildQueryString(queryParams);
            if (!queryString.isEmpty()) {
                path += "?" + queryString;
            }
            
            logger.debug("Listing requesters with path: {}", path);
            return parse(restClient.get(path));
        } catch (IOException e) {
            logger.error("Error listing requesters: {}", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public JsonNode searchRequesters(String domain, String apiKey, Map<String, Object> searchParams) {
        // Search requesters is essentially the same as list with parameters
        return listRequesters(domain, apiKey, searchParams);
    }

    @Override
    public JsonNode getRequester(String domain, String apiKey, Long requesterId) {
        try {
            RestClient restClient = new RestClient(domain, apiKey);
            String path = PathConstants.REQUESTERS + "/" + requesterId;
            return parse(restClient.get(path));
        } catch (IOException e) {
            logger.error("Error getting requester {}: {}", requesterId, e.getMessage(), e);
        }
        return null;
    }

    @Override
    public JsonNode createRequester(String domain, String apiKey, Map<String, Object> requesterData) {
        try {
            RestClient restClient = new RestClient(domain, apiKey);
            String jsonPayload = objectMapper.writeValueAsString(requesterData);
            logger.debug("Creating requester with payload: {}", jsonPayload);
            return parse(restClient.post(PathConstants.REQUESTERS, jsonPayload));
        } catch (IOException e) {
            logger.error("Error creating requester: {}", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public JsonNode updateRequester(String domain, String apiKey, Long requesterId, Map<String, Object> requesterData) {
        try {
            RestClient restClient = new RestClient(domain, apiKey);
            String path = PathConstants.REQUESTERS + "/" + requesterId;
            String jsonPayload = objectMapper.writeValueAsString(requesterData);
            logger.debug("Updating requester {} with payload: {}", requesterId, jsonPayload);
            return parse(restClient.put(path, jsonPayload));
        } catch (IOException e) {
            logger.error("Error updating requester {}: {}", requesterId, e.getMessage(), e);
        }
        return null;
    }

    @Override
    public JsonNode deleteRequester(String domain, String apiKey, Long requesterId) {
        try {
            RestClient restClient = new RestClient(domain, apiKey);
            String path = PathConstants.REQUESTERS + "/" + requesterId;
            return parse(restClient.delete(path));
        } catch (IOException e) {
            logger.error("Error deleting requester {}: {}", requesterId, e.getMessage(), e);
        }
        return null;
    }

    @Override
    public JsonNode forgetRequester(String domain, String apiKey, Long requesterId) {
        try {
            RestClient restClient = new RestClient(domain, apiKey);
            String path = PathConstants.REQUESTERS + "/" + requesterId + "/forget";
            return parse(restClient.delete(path));
        } catch (IOException e) {
            logger.error("Error permanently deleting requester {}: {}", requesterId, e.getMessage(), e);
        }
        return null;
    }

    @Override
    public JsonNode convertRequesterToAgent(String domain, String apiKey, Long requesterId) {
        try {
            RestClient restClient = new RestClient(domain, apiKey);
            String path = PathConstants.REQUESTERS + "/" + requesterId + "/convert_to_agent";
            return parse(restClient.put(path, "{}"));
        } catch (IOException e) {
            logger.error("Error converting requester {} to agent: {}", requesterId, e.getMessage(), e);
        }
        return null;
    }

    /**
     * Builds a query string from the provided parameters
     */
    private String buildQueryString(Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return "";
        }

        StringJoiner queryString = new StringJoiner("&");
        
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            
            if (value != null) {
                try {
                    String encodedKey = URLEncoder.encode(key, "UTF-8");
                    String encodedValue = URLEncoder.encode(value.toString(), "UTF-8");
                    queryString.add(encodedKey + "=" + encodedValue);
                } catch (UnsupportedEncodingException e) {
                    logger.warn("Error encoding query parameter {}={}: {}", key, value, e.getMessage());
                }
            }
        }
        
        return queryString.toString();
    }
} 