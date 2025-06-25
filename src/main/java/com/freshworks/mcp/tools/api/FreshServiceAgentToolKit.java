package com.freshworks.mcp.tools.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.freshworks.mcp.utils.SchemaLoader;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;

import java.util.ArrayList;
import java.util.List;

public interface FreshServiceAgentToolKit {

    JsonNode listAgents(String domain, String apiKey);
    JsonNode getAgent(String domain, String apiKey, Long agentId);
    JsonNode searchAgents(String domain, String apiKey, String query, String email, String mobilePhone, String workPhone, String state, Boolean active);
    JsonNode deleteAgent(String domain, String apiKey, Long agentId);
    JsonNode forgetAgent(String domain, String apiKey, Long agentId);

    static List<McpServerFeatures.SyncToolSpecification> getToolSpecs(FreshServiceAgentToolKit tool) {
        List<McpServerFeatures.SyncToolSpecification> specs = new ArrayList<>();

        // List Agents
        var listSchema = SchemaLoader.loadSchema("freshservice_input.json");
        specs.add(new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool("list_agents", "List all agents in Freshservice", listSchema),
                (exchange, arguments) -> {
                    String domain = (String) arguments.get("domain");
                    String key = (String) arguments.get("apikey");

                    JsonNode json = tool.listAgents(domain, key);
                    return getCallToolResult(json != null ? json.toString() : "No agents found");
                }
        ));

        // Get Agent by ID
        var getAgentSchema = SchemaLoader.loadSchema("agent_by_id_input.json");
        specs.add(new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool("get_agent", "Get a specific agent by ID", getAgentSchema),
                (exchange, arguments) -> {
                    String domain = (String) arguments.get("domain");
                    String key = (String) arguments.get("apikey");
                    Long agentId = Long.valueOf(arguments.get("agent_id").toString());

                    JsonNode json = tool.getAgent(domain, key, agentId);
                    return getCallToolResult(json != null ? json.toString() : "Agent not found");
                }
        ));

        // Search Agents
        var searchSchema = SchemaLoader.loadSchema("agent_search_input.json");
        specs.add(new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool("search_agents", "Search agents with various filters and query parameters", searchSchema),
                (exchange, arguments) -> {
                    String domain = (String) arguments.get("domain");
                    String key = (String) arguments.get("apikey");
                    String query = (String) arguments.get("query");
                    String email = (String) arguments.get("email");
                    String mobilePhone = (String) arguments.get("mobile_phone_number");
                    String workPhone = (String) arguments.get("work_phone_number");
                    String state = (String) arguments.get("state");
                    Boolean active = arguments.get("active") != null ? (Boolean) arguments.get("active") : null;

                    JsonNode json = tool.searchAgents(domain, key, query, email, mobilePhone, workPhone, state, active);
                    return getCallToolResult(json != null ? json.toString() : "No agents found matching criteria");
                }
        ));

        // Delete/Deactivate Agent (legacy - same as convert)
        var deleteAgentSchema = SchemaLoader.loadSchema("agent_by_id_input.json");
        specs.add(new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool("delete_agent", "Delete/deactivate an agent (converts agent to requester)", deleteAgentSchema),
                (exchange, arguments) -> {
                    String domain = (String) arguments.get("domain");
                    String key = (String) arguments.get("apikey");
                    Long agentId = Long.valueOf(arguments.get("agent_id").toString());

                    JsonNode json = tool.deleteAgent(domain, key, agentId);
                    return getCallToolResult(json != null ? json.toString() : "Agent deletion completed");
                }
        ));

        // Forget Agent (Permanent deletion)
        var forgetAgentSchema = SchemaLoader.loadSchema("agent_by_id_input.json");
        specs.add(new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool("forget_agent", "⚠️ PERMANENTLY delete an agent and all their tickets (irreversible)", forgetAgentSchema),
                (exchange, arguments) -> {
                    String domain = (String) arguments.get("domain");
                    String key = (String) arguments.get("apikey");
                    Long agentId = Long.valueOf(arguments.get("agent_id").toString());

                    JsonNode json = tool.forgetAgent(domain, key, agentId);
                    return getCallToolResult(json != null ? json.toString() : "Agent permanently deleted");
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
