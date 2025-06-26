package com.freshworks.mcp.tools.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.freshworks.mcp.utils.SchemaLoader;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface FreshServiceTicketToolKit {

    JsonNode listTickets(String domain, String apiKey);
    JsonNode createTicket(String domain, String apiKey, String subject, String description, long requesterId, int priority, int status, Integer groupId, String type, Integer source, String dueBy, List<String> tags, Map<String, Object> customFields);
    JsonNode getTicketById(String domain, String apiKey, String ticketId);
    JsonNode updateTicket(String domain, String apiKey, String ticketId, String subject, String description, long requesterId, int priority, int status, Integer groupId, String type, Integer source, String dueBy, List<String> tags, Map<String, Object> customFields);
    JsonNode deleteTicket(String domain, String apiKey, String ticketId);

    static List<McpServerFeatures.SyncToolSpecification> getToolSpecs(FreshServiceTicketToolKit tool) {
        List<McpServerFeatures.SyncToolSpecification> specs = new ArrayList<>();

        // List Tickets
        var schema = SchemaLoader.loadSchema("freshservice_input.json");
        specs.add(new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool("list_tickets", "List tickets", schema),
                (exchange, arguments) -> {
                    String domain = (String) arguments.get("domain");
                    String key = (String) arguments.get("apikey");

                    JsonNode json = tool.listTickets(domain, key);
                    return getCallToolResult(json.toString());
                }
        ));

        // Create Ticket
        var createSchema = SchemaLoader.loadSchema("freshservice_create_ticket_input.json");
        specs.add(new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool("create_ticket", "Create a new ticket", createSchema),
                (exchange, arguments) -> {
                    String domain = (String) arguments.get("domain");
                    String key = (String) arguments.get("apikey");
                    String subject = (String) arguments.get("subject");
                    String description = (String) arguments.get("description");
                    long requesterId = (long) arguments.get("requester_id");
                    int priority = (int) arguments.get("priority");
                    int status = (int) arguments.get("status");
                    Integer groupId = arguments.get("group_id") != null ? (Integer) arguments.get("group_id") : null;
                    String type = (String) arguments.get("type");
                    Integer source = arguments.get("source") != null ? (Integer) arguments.get("source") : null;
                    String dueBy = (String) arguments.get("due_by");
                    List<String> tags = (List<String>) arguments.get("tags");
                    Map<String, Object> customFields = (Map<String, Object>) arguments.get("custom_fields");

                    JsonNode json = tool.createTicket(domain, key, subject, description, requesterId, priority, status, groupId, type, source, dueBy, tags, customFields);
                    return getCallToolResult(json.toString());
                }
        ));

        // Get Ticket by ID
        var getSchema = SchemaLoader.loadSchema("freshservice_get_ticket_input.json");
        specs.add(new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool("get_ticket", "Get ticket by ID", getSchema),
                (exchange, arguments) -> {
                    String domain = (String) arguments.get("domain");
                    String key = (String) arguments.get("apikey");
                    String ticketId = (String) arguments.get("ticketId");

                    JsonNode json = tool.getTicketById(domain, key, ticketId);
                    return getCallToolResult(json.toString());
                }
        ));

        // Update Ticket
        var updateSchema = SchemaLoader.loadSchema("freshservice_update_ticket_input.json");
        specs.add(new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool("update_ticket", "Update ticket", updateSchema),
                (exchange, arguments) -> {
                    String domain = (String) arguments.get("domain");
                    String key = (String) arguments.get("apikey");
                    String ticketId = (String) arguments.get("ticketId");
                    String subject = (String) arguments.get("subject");
                    String description = (String) arguments.get("description");
                    int requesterId = (int) arguments.get("requester_id");
                    int priority = (int) arguments.get("priority");
                    int status = (int) arguments.get("status");
                    Integer groupId = arguments.get("group_id") != null ? (Integer) arguments.get("group_id") : null;
                    String type = (String) arguments.get("type");
                    Integer source = arguments.get("source") != null ? (Integer) arguments.get("source") : null;
                    String dueBy = (String) arguments.get("due_by");
                    List<String> tags = (List<String>) arguments.get("tags");
                    Map<String, Object> customFields = (Map<String, Object>) arguments.get("custom_fields");

                    JsonNode json = tool.updateTicket(domain, key, ticketId, subject, description, requesterId, priority, status, groupId, type, source, dueBy, tags, customFields);
                    return getCallToolResult(json.toString());
                }
        ));

        // Delete Ticket
        var deleteSchema = SchemaLoader.loadSchema("freshservice_delete_ticket_input.json");
        specs.add(new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool("delete_ticket", "Delete ticket", deleteSchema),
                (exchange, arguments) -> {
                    String domain = (String) arguments.get("domain");
                    String key = (String) arguments.get("apikey");
                    String ticketId = (String) arguments.get("ticketId");

                    JsonNode json = tool.deleteTicket(domain, key, ticketId);
                    return getCallToolResult(json.toString());
                }
        ));

        return specs;
    }

    private static McpSchema.CallToolResult getCallToolResult(String result) {
        List<McpSchema.Content> contents = new ArrayList<>();
        contents.add(new McpSchema.TextContent(result));
        return new McpSchema.CallToolResult(contents, false);
    }
}
