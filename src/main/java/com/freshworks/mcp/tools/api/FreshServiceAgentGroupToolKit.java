package com.freshworks.mcp.tools.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.freshworks.mcp.utils.SchemaLoader;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface for FreshService Agent Group operations.
 * Provides tools for managing agent groups including listing, creating, updating, and deleting groups.
 */
public interface FreshServiceAgentGroupToolKit {

    /**
     * Lists all agent groups in FreshService with optional pagination.
     */
    JsonNode listAgentGroups(String domain, String apiKey);

    /**
     * Retrieves a specific agent group by ID.
     */
    JsonNode getAgentGroup(String domain, String apiKey, Long agentGroupId);

    /**
     * Creates a new agent group in FreshService.
     */
    JsonNode createAgentGroup(String domain, String apiKey, String name, String description, 
                             Long businessHoursId, Long escalateTo, String unassignedFor,
                             List<Long> agentIds, List<Long> members, List<Long> observers, 
                             List<Long> leaders, Boolean restricted, Boolean approvalRequired,
                             Boolean autoTicketAssign, Long workspaceId);

    /**
     * Updates an existing agent group in FreshService.
     */
    JsonNode updateAgentGroup(String domain, String apiKey, Long agentGroupId, String name, 
                             String description, Long businessHoursId, Long escalateTo, 
                             String unassignedFor, List<Long> agentIds, List<Long> members, 
                             List<Long> observers, List<Long> leaders, Boolean restricted, 
                             Boolean approvalRequired, Boolean autoTicketAssign, Long workspaceId);

    /**
     * Deletes an agent group from FreshService.
     */
    JsonNode deleteAgentGroup(String domain, String apiKey, Long agentGroupId);

    /**
     * Returns the list of tool specifications for agent group operations.
     */
    static List<McpServerFeatures.SyncToolSpecification> getToolSpecs(FreshServiceAgentGroupToolKit tool) {
        List<McpServerFeatures.SyncToolSpecification> specs = new ArrayList<>();

        // List Agent Groups
        var listSchema = SchemaLoader.loadSchema("freshservice_input.json");
        specs.add(new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool("list_agent_groups", "List all agent groups in FreshService", listSchema),
                (exchange, arguments) -> {
                    String domain = (String) arguments.get("domain");
                    String key = (String) arguments.get("apikey");

                    JsonNode json = tool.listAgentGroups(domain, key);
                    return getCallToolResult(json != null ? json.toString() : "No agent groups found");
                }
        ));

        // Get Agent Group by ID
        var getSchema = SchemaLoader.loadSchema("agent_group_by_id_input.json");
        specs.add(new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool("get_agent_group", "Get a specific agent group by ID", getSchema),
                (exchange, arguments) -> {
                    String domain = (String) arguments.get("domain");
                    String key = (String) arguments.get("apikey");
                    Long groupId = Long.valueOf(arguments.get("agent_group_id").toString());

                    JsonNode json = tool.getAgentGroup(domain, key, groupId);
                    return getCallToolResult(json != null ? json.toString() : "Agent group not found");
                }
        ));

        // Create Agent Group
        var createSchema = SchemaLoader.loadSchema("create_agent_group_input.json");
        specs.add(new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool("create_agent_group", "Create a new agent group in FreshService", createSchema),
                (exchange, arguments) -> {
                    String domain = (String) arguments.get("domain");
                    String key = (String) arguments.get("apikey");
                    String name = (String) arguments.get("name");
                    String description = (String) arguments.get("description");
                    Long businessHoursId = arguments.get("business_hours_id") != null ? 
                        Long.valueOf(arguments.get("business_hours_id").toString()) : null;
                    Long escalateTo = arguments.get("escalate_to") != null ? 
                        Long.valueOf(arguments.get("escalate_to").toString()) : null;
                    String unassignedFor = (String) arguments.get("unassigned_for");
                    
                    @SuppressWarnings("unchecked")
                    List<Long> agentIds = (List<Long>) arguments.get("agent_ids");
                    @SuppressWarnings("unchecked")
                    List<Long> members = (List<Long>) arguments.get("members");
                    @SuppressWarnings("unchecked")
                    List<Long> observers = (List<Long>) arguments.get("observers");
                    @SuppressWarnings("unchecked")
                    List<Long> leaders = (List<Long>) arguments.get("leaders");
                    
                    Boolean restricted = arguments.get("restricted") != null ? (Boolean) arguments.get("restricted") : null;
                    Boolean approvalRequired = arguments.get("approval_required") != null ? (Boolean) arguments.get("approval_required") : null;
                    Boolean autoTicketAssign = arguments.get("auto_ticket_assign") != null ? (Boolean) arguments.get("auto_ticket_assign") : null;
                    Long workspaceId = arguments.get("workspace_id") != null ? 
                        Long.valueOf(arguments.get("workspace_id").toString()) : null;

                    JsonNode json = tool.createAgentGroup(domain, key, name, description, businessHoursId, 
                        escalateTo, unassignedFor, agentIds, members, observers, leaders, 
                        restricted, approvalRequired, autoTicketAssign, workspaceId);
                    return getCallToolResult(json != null ? json.toString() : "Agent group creation failed");
                }
        ));

        // Update Agent Group
        var updateSchema = SchemaLoader.loadSchema("update_agent_group_input.json");
        specs.add(new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool("update_agent_group", "Update an existing agent group in FreshService", updateSchema),
                (exchange, arguments) -> {
                    String domain = (String) arguments.get("domain");
                    String key = (String) arguments.get("apikey");
                    Long groupId = Long.valueOf(arguments.get("agent_group_id").toString());
                    String name = (String) arguments.get("name");
                    String description = (String) arguments.get("description");
                    Long businessHoursId = arguments.get("business_hours_id") != null ? 
                        Long.valueOf(arguments.get("business_hours_id").toString()) : null;
                    Long escalateTo = arguments.get("escalate_to") != null ? 
                        Long.valueOf(arguments.get("escalate_to").toString()) : null;
                    String unassignedFor = (String) arguments.get("unassigned_for");
                    
                    @SuppressWarnings("unchecked")
                    List<Long> agentIds = (List<Long>) arguments.get("agent_ids");
                    @SuppressWarnings("unchecked")
                    List<Long> members = (List<Long>) arguments.get("members");
                    @SuppressWarnings("unchecked")
                    List<Long> observers = (List<Long>) arguments.get("observers");
                    @SuppressWarnings("unchecked")
                    List<Long> leaders = (List<Long>) arguments.get("leaders");
                    
                    Boolean restricted = arguments.get("restricted") != null ? (Boolean) arguments.get("restricted") : null;
                    Boolean approvalRequired = arguments.get("approval_required") != null ? (Boolean) arguments.get("approval_required") : null;
                    Boolean autoTicketAssign = arguments.get("auto_ticket_assign") != null ? (Boolean) arguments.get("auto_ticket_assign") : null;
                    Long workspaceId = arguments.get("workspace_id") != null ? 
                        Long.valueOf(arguments.get("workspace_id").toString()) : null;

                    JsonNode json = tool.updateAgentGroup(domain, key, groupId, name, description, businessHoursId, 
                        escalateTo, unassignedFor, agentIds, members, observers, leaders, 
                        restricted, approvalRequired, autoTicketAssign, workspaceId);
                    return getCallToolResult(json != null ? json.toString() : "Agent group update failed");
                }
        ));

        // Delete Agent Group
        var deleteSchema = SchemaLoader.loadSchema("agent_group_by_id_input.json");
        specs.add(new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool("delete_agent_group", "Delete an agent group from FreshService", deleteSchema),
                (exchange, arguments) -> {
                    String domain = (String) arguments.get("domain");
                    String key = (String) arguments.get("apikey");
                    Long groupId = Long.valueOf(arguments.get("agent_group_id").toString());

                    JsonNode json = tool.deleteAgentGroup(domain, key, groupId);
                    return getCallToolResult(json != null ? json.toString() : "Agent group deletion completed");
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