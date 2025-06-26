package com.freshworks.mcp.tools.impl.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshworks.mcp.constants.PathConstants;
import com.freshworks.mcp.tools.AbstractAdapter;
import com.freshworks.mcp.tools.api.FreshServiceTicketToolKit;
import com.freshworks.mcp.utils.http.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class FreshServiceTicketAdapter extends AbstractAdapter
        implements FreshServiceTicketToolKit {
    private static final Logger logger = LoggerFactory.getLogger(FreshServiceTicketAdapter.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public JsonNode listTickets(String domain, String apiKey) {
        try {
            RestClient restClient = new RestClient(domain, apiKey);
            return parse(restClient.get(PathConstants.TICKETS));
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public JsonNode createTicket(String domain, String apiKey, String subject, String description, long requesterId, int priority, int status, Integer groupId, String type, Integer source, String dueBy, List<String> tags, Map<String, Object> customFields) {
        try {
            RestClient restClient = new RestClient(domain, apiKey);
            var ticketNode = objectMapper.createObjectNode();
            ticketNode.put("subject", subject);
            ticketNode.put("description", description);
            ticketNode.put("requester_id", requesterId);
            ticketNode.put("priority", priority);
            ticketNode.put("status", status);
            if (groupId != null) ticketNode.put("group_id", groupId);
            if (type != null) ticketNode.put("type", type);
            if (source != null) ticketNode.put("source", source);
            if (dueBy != null) ticketNode.put("due_by", dueBy);
            if (tags != null) ticketNode.set("tags", objectMapper.valueToTree(tags));
            if (customFields != null) ticketNode.set("custom_fields", objectMapper.valueToTree(customFields));
            String jsonPayload = objectMapper.writeValueAsString(ticketNode);
            return parse(restClient.post(PathConstants.TICKETS, jsonPayload));
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public JsonNode getTicketById(String domain, String apiKey, String ticketId) {
        try {
            RestClient restClient = new RestClient(domain, apiKey);
            String path = PathConstants.TICKETS + "/" + ticketId;
            return parse(restClient.get(path));
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public JsonNode updateTicket(String domain, String apiKey, String ticketId, String subject, String description, long requesterId, int priority, int status, Integer groupId, String type, Integer source, String dueBy, List<String> tags, Map<String, Object> customFields) {
        try {
            RestClient restClient = new RestClient(domain, apiKey);
            String path = PathConstants.TICKETS + "/" + ticketId;
            var ticketNode = objectMapper.createObjectNode();
            ticketNode.put("subject", subject);
            ticketNode.put("description", description);
            ticketNode.put("requester_id", requesterId);
            ticketNode.put("priority", priority);
            ticketNode.put("status", status);
            if (groupId != null) ticketNode.put("group_id", groupId);
            if (type != null) ticketNode.put("type", type);
            if (source != null) ticketNode.put("source", source);
            if (dueBy != null) ticketNode.put("due_by", dueBy);
            if (tags != null) ticketNode.set("tags", objectMapper.valueToTree(tags));
            if (customFields != null) ticketNode.set("custom_fields", objectMapper.valueToTree(customFields));
            String jsonPayload = objectMapper.writeValueAsString(ticketNode);
            return parse(restClient.put(path, jsonPayload));
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public JsonNode deleteTicket(String domain, String apiKey, String ticketId) {
        try {
            RestClient restClient = new RestClient(domain, apiKey);
            String path = PathConstants.TICKETS + "/" + ticketId;
            return parse(restClient.delete(path));
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}
