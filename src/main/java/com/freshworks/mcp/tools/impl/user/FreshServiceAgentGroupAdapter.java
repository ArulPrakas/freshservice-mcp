package com.freshworks.mcp.tools.impl.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.freshworks.mcp.constants.PathConstants;
import com.freshworks.mcp.tools.AbstractAdapter;
import com.freshworks.mcp.tools.api.FreshServiceAgentGroupToolKit;
import com.freshworks.mcp.utils.http.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * Implementation of FreshService Agent Group operations.
 * Provides methods to manage agent groups including CRUD operations.
 */
public class FreshServiceAgentGroupAdapter extends AbstractAdapter implements FreshServiceAgentGroupToolKit {

    private static final Logger logger = LoggerFactory.getLogger(FreshServiceAgentGroupAdapter.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public JsonNode listAgentGroups(String domain, String apiKey) {
        try {
            RestClient restClient = new RestClient(domain, apiKey);
            return parse(restClient.get(PathConstants.GROUPS));
        } catch (IOException e) {
            logger.error("Error listing agent groups: {}", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public JsonNode getAgentGroup(String domain, String apiKey, Long agentGroupId) {
        try {
            RestClient restClient = new RestClient(domain, apiKey);
            String path = PathConstants.GROUPS + "/" + agentGroupId;
            return parse(restClient.get(path));
        } catch (IOException e) {
            logger.error("Error getting agent group {}: {}", agentGroupId, e.getMessage(), e);
        }
        return null;
    }

    @Override
    public JsonNode createAgentGroup(String domain, String apiKey, String name, String description,
                                    Long businessHoursId, Long escalateTo, String unassignedFor,
                                    List<Long> agentIds, List<Long> members, List<Long> observers,
                                    List<Long> leaders, Boolean restricted, Boolean approvalRequired,
                                    Boolean autoTicketAssign, Long workspaceId) {
        try {
            RestClient restClient = new RestClient(domain, apiKey);
            
            // Build the request payload
            ObjectNode payload = objectMapper.createObjectNode();
            payload.put("name", name);
            
            if (description != null) payload.put("description", description);
            if (businessHoursId != null) payload.put("business_hours_id", businessHoursId);
            if (escalateTo != null) payload.put("escalate_to", escalateTo);
            if (unassignedFor != null) payload.put("unassigned_for", unassignedFor);
            if (restricted != null) payload.put("restricted", restricted);
            if (approvalRequired != null) payload.put("approval_required", approvalRequired);
            if (autoTicketAssign != null) payload.put("auto_ticket_assign", autoTicketAssign);
            if (workspaceId != null) payload.put("workspace_id", workspaceId);
            
            if (agentIds != null && !agentIds.isEmpty()) {
                payload.set("agent_ids", objectMapper.valueToTree(agentIds));
            }
            if (members != null && !members.isEmpty()) {
                payload.set("members", objectMapper.valueToTree(members));
            }
            if (observers != null && !observers.isEmpty()) {
                payload.set("observers", objectMapper.valueToTree(observers));
            }
            if (leaders != null && !leaders.isEmpty()) {
                payload.set("leaders", objectMapper.valueToTree(leaders));
            }
            
            logger.debug("Creating agent group with payload: {}", payload);
            
            return parse(restClient.post(PathConstants.GROUPS, payload.toString()));
        } catch (IOException e) {
            logger.error("Error creating agent group '{}': {}", name, e.getMessage(), e);
        }
        return null;
    }

    @Override
    public JsonNode updateAgentGroup(String domain, String apiKey, Long agentGroupId, String name,
                                    String description, Long businessHoursId, Long escalateTo,
                                    String unassignedFor, List<Long> agentIds, List<Long> members,
                                    List<Long> observers, List<Long> leaders, Boolean restricted,
                                    Boolean approvalRequired, Boolean autoTicketAssign, Long workspaceId) {
        try {
            RestClient restClient = new RestClient(domain, apiKey);
            String path = PathConstants.GROUPS + "/" + agentGroupId;
            
            // Build the request payload with only non-null values
            ObjectNode payload = objectMapper.createObjectNode();
            
            if (name != null) payload.put("name", name);
            if (description != null) payload.put("description", description);
            if (businessHoursId != null) payload.put("business_hours_id", businessHoursId);
            if (escalateTo != null) payload.put("escalate_to", escalateTo);
            if (unassignedFor != null) payload.put("unassigned_for", unassignedFor);
            if (restricted != null) payload.put("restricted", restricted);
            if (approvalRequired != null) payload.put("approval_required", approvalRequired);
            if (autoTicketAssign != null) payload.put("auto_ticket_assign", autoTicketAssign);
            if (workspaceId != null) payload.put("workspace_id", workspaceId);
            
            if (agentIds != null) {
                payload.set("agent_ids", objectMapper.valueToTree(agentIds));
            }
            if (members != null) {
                payload.set("members", objectMapper.valueToTree(members));
            }
            if (observers != null) {
                payload.set("observers", objectMapper.valueToTree(observers));
            }
            if (leaders != null) {
                payload.set("leaders", objectMapper.valueToTree(leaders));
            }
            
            logger.debug("Updating agent group {} with payload: {}", agentGroupId, payload);
            
            return parse(restClient.put(path, payload.toString()));
        } catch (IOException e) {
            logger.error("Error updating agent group {}: {}", agentGroupId, e.getMessage(), e);
        }
        return null;
    }

    @Override
    public JsonNode deleteAgentGroup(String domain, String apiKey, Long agentGroupId) {
        try {
            RestClient restClient = new RestClient(domain, apiKey);
            String path = PathConstants.GROUPS + "/" + agentGroupId;
            return parse(restClient.delete(path));
        } catch (IOException e) {
            logger.error("Error deleting agent group {}: {}", agentGroupId, e.getMessage(), e);
        }
        return null;
    }
} 