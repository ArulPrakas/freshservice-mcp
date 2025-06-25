package com.freshworks.mcp.tools.impl.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.freshworks.mcp.constants.PathConstants;
import com.freshworks.mcp.tools.AbstractAdapter;
import com.freshworks.mcp.tools.api.FreshServiceAgentToolKit;
import com.freshworks.mcp.utils.http.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FreshServiceAgentAdapter extends AbstractAdapter
        implements FreshServiceAgentToolKit {
    private static final Logger logger = LoggerFactory.getLogger(FreshServiceAgentAdapter.class);

    @Override
    public JsonNode listAgents(String domain, String apiKey) {
        try {
            RestClient restClient = new RestClient(domain, apiKey);
            return parse(restClient.get(PathConstants.AGENTS));
        } catch (IOException e) {
            logger.error("Error listing agents: {}", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public JsonNode getAgent(String domain, String apiKey, Long agentId) {
        try {
            RestClient restClient = new RestClient(domain, apiKey);
            String path = PathConstants.AGENTS + "/" + agentId;
            return parse(restClient.get(path));
        } catch (IOException e) {
            logger.error("Error getting agent with ID {}: {}", agentId, e.getMessage(), e);
        }
        return null;
    }

    @Override
    public JsonNode searchAgents(String domain, String apiKey, String query, String email, String mobilePhone, String workPhone, String state, Boolean active) {
        try {
            RestClient restClient = new RestClient(domain, apiKey);
            String path = PathConstants.AGENTS + buildQueryString(query, email, mobilePhone, workPhone, state, active);
            return parse(restClient.get(path));
        } catch (IOException e) {
            logger.error("Error searching agents: {}", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public JsonNode deleteAgent(String domain, String apiKey, Long agentId) {
        try {
            RestClient restClient = new RestClient(domain, apiKey);
            String path = PathConstants.AGENTS + "/" + agentId;
            return parse(restClient.delete(path));
        } catch (IOException e) {
            logger.error("Error deleting agent with ID {}: {}", agentId, e.getMessage(), e);
        }
        return null;
    }

    @Override
    public JsonNode forgetAgent(String domain, String apiKey, Long agentId) {
        try {
            RestClient restClient = new RestClient(domain, apiKey);
            String path = PathConstants.AGENTS + "/" + agentId + "/forget";
            logger.warn("Permanently deleting agent with ID {} and all their tickets", agentId);
            return parse(restClient.delete(path));
        } catch (IOException e) {
            logger.error("Error permanently deleting agent with ID {}: {}", agentId, e.getMessage(), e);
        }
        return null;
    }

    /**
     * Builds query string for agent search with various filter parameters
     */
    private String buildQueryString(String query, String email, String mobilePhone, String workPhone, String state, Boolean active) {
        List<String> params = new ArrayList<>();
        
        if (query != null && !query.trim().isEmpty()) {
            params.add("query=" + URLEncoder.encode(query, StandardCharsets.UTF_8));
        }
        
        if (email != null && !email.trim().isEmpty()) {
            params.add("email=" + URLEncoder.encode(email, StandardCharsets.UTF_8));
        }
        
        if (mobilePhone != null && !mobilePhone.trim().isEmpty()) {
            params.add("mobile_phone_number=" + URLEncoder.encode(mobilePhone, StandardCharsets.UTF_8));
        }
        
        if (workPhone != null && !workPhone.trim().isEmpty()) {
            params.add("work_phone_number=" + URLEncoder.encode(workPhone, StandardCharsets.UTF_8));
        }
        
        if (state != null && !state.trim().isEmpty()) {
            params.add("state=" + URLEncoder.encode(state, StandardCharsets.UTF_8));
        }
        
        if (active != null) {
            params.add("active=" + active);
        }
        
        if (params.isEmpty()) {
            return "";
        }
        
        return "?" + String.join("&", params);
    }
}
