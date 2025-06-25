package com.freshworks.mcp.tools.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.freshworks.mcp.utils.SchemaLoader;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface FreshServiceRequesterToolKit {

    JsonNode listRequesters(String domain, String apiKey, Map<String, Object> queryParams);
    JsonNode searchRequesters(String domain, String apiKey, Map<String, Object> searchParams);
    JsonNode getRequester(String domain, String apiKey, Long requesterId);
    JsonNode createRequester(String domain, String apiKey, Map<String, Object> requesterData);
    JsonNode updateRequester(String domain, String apiKey, Long requesterId, Map<String, Object> requesterData);
    JsonNode deleteRequester(String domain, String apiKey, Long requesterId);
    JsonNode forgetRequester(String domain, String apiKey, Long requesterId);
    JsonNode convertRequesterToAgent(String domain, String apiKey, Long requesterId);

    static List<McpServerFeatures.SyncToolSpecification> getToolSpecs(FreshServiceRequesterToolKit tool) {
        List<McpServerFeatures.SyncToolSpecification> specs = new ArrayList<>();

        // List/Search Requesters with enhanced parameters
        var searchSchema = SchemaLoader.loadSchema("requester_search_input.json");
        specs.add(new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool("list_requesters", "List and search requesters in Freshservice with filtering options", searchSchema),
                (exchange, arguments) -> {
                    String domain = (String) arguments.get("domain");
                    String key = (String) arguments.get("apikey");
                    
                    // Extract search parameters (excluding domain and apikey)
                    Map<String, Object> queryParams = new java.util.HashMap<>(arguments);
                    queryParams.remove("domain");
                    queryParams.remove("apikey");

                    JsonNode json = tool.listRequesters(domain, key, queryParams);
                    return getCallToolResult(json != null ? json.toString() : "No requesters found");
                }
        ));

        // Search Requesters (alias for enhanced search)
        specs.add(new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool("search_requesters", "Advanced search for requesters with complex query support", searchSchema),
                (exchange, arguments) -> {
                    String domain = (String) arguments.get("domain");
                    String key = (String) arguments.get("apikey");
                    
                    // Extract search parameters (excluding domain and apikey)
                    Map<String, Object> searchParams = new java.util.HashMap<>(arguments);
                    searchParams.remove("domain");
                    searchParams.remove("apikey");

                    JsonNode json = tool.searchRequesters(domain, key, searchParams);
                    return getCallToolResult(json != null ? json.toString() : "No requesters found matching criteria");
                }
        ));

        // Get Requester by ID
        var getRequesterSchema = SchemaLoader.loadSchema("requester_by_id_input.json");
        specs.add(new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool("get_requester", "Get a specific requester by ID", getRequesterSchema),
                (exchange, arguments) -> {
                    String domain = (String) arguments.get("domain");
                    String key = (String) arguments.get("apikey");
                    Long requesterId = Long.valueOf(arguments.get("requester_id").toString());

                    JsonNode json = tool.getRequester(domain, key, requesterId);
                    return getCallToolResult(json != null ? json.toString() : "Requester not found");
                }
        ));

        // Create Requester
        var createRequesterSchema = SchemaLoader.loadSchema("create_requester_input.json");
        specs.add(new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool("create_requester", "Create a new requester in Freshservice", createRequesterSchema),
                (exchange, arguments) -> {
                    String domain = (String) arguments.get("domain");
                    String key = (String) arguments.get("apikey");
                    
                    // Extract requester data (excluding domain and apikey)
                    Map<String, Object> requesterData = new java.util.HashMap<>(arguments);
                    requesterData.remove("domain");
                    requesterData.remove("apikey");

                    JsonNode json = tool.createRequester(domain, key, requesterData);
                    return getCallToolResult(json != null ? json.toString() : "Failed to create requester");
                }
        ));

        // Update Requester
        var updateRequesterSchema = SchemaLoader.loadSchema("update_requester_input.json");
        specs.add(new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool("update_requester", "Update an existing requester in Freshservice", updateRequesterSchema),
                (exchange, arguments) -> {
                    String domain = (String) arguments.get("domain");
                    String key = (String) arguments.get("apikey");
                    Long requesterId = Long.valueOf(arguments.get("requester_id").toString());
                    
                    // Extract requester data (excluding domain, apikey, and requester_id)
                    Map<String, Object> requesterData = new java.util.HashMap<>(arguments);
                    requesterData.remove("domain");
                    requesterData.remove("apikey");
                    requesterData.remove("requester_id");

                    JsonNode json = tool.updateRequester(domain, key, requesterId, requesterData);
                    return getCallToolResult(json != null ? json.toString() : "Failed to update requester");
                }
        ));

        // Delete Requester
        var deleteRequesterSchema = SchemaLoader.loadSchema("requester_by_id_input.json");
        specs.add(new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool("delete_requester", "Delete/deactivate a requester account", deleteRequesterSchema),
                (exchange, arguments) -> {
                    String domain = (String) arguments.get("domain");
                    String key = (String) arguments.get("apikey");
                    Long requesterId = Long.valueOf(arguments.get("requester_id").toString());

                    JsonNode json = tool.deleteRequester(domain, key, requesterId);
                    return getCallToolResult(json != null ? json.toString() : "Requester deleted successfully");
                }
        ));

        // Forget Requester (Permanent deletion)
        var forgetRequesterSchema = SchemaLoader.loadSchema("requester_by_id_input.json");
        specs.add(new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool("forget_requester", "⚠️ PERMANENTLY delete a requester and all their data (irreversible)", forgetRequesterSchema),
                (exchange, arguments) -> {
                    String domain = (String) arguments.get("domain");
                    String key = (String) arguments.get("apikey");
                    Long requesterId = Long.valueOf(arguments.get("requester_id").toString());

                    JsonNode json = tool.forgetRequester(domain, key, requesterId);
                    return getCallToolResult(json != null ? json.toString() : "Requester permanently deleted");
                }
        ));

        // Convert Requester to Agent
        var convertRequesterSchema = SchemaLoader.loadSchema("requester_by_id_input.json");
        specs.add(new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool("convert_requester_to_agent", "Convert a requester to an agent (grants agent privileges)", convertRequesterSchema),
                (exchange, arguments) -> {
                    String domain = (String) arguments.get("domain");
                    String key = (String) arguments.get("apikey");
                    Long requesterId = Long.valueOf(arguments.get("requester_id").toString());

                    JsonNode json = tool.convertRequesterToAgent(domain, key, requesterId);
                    return getCallToolResult(json != null ? json.toString() : "Requester converted to agent successfully");
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