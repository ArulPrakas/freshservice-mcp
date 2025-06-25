package com.freshworks.mcp.tools.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.freshworks.mcp.utils.SchemaLoader;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;

import java.util.ArrayList;
import java.util.List;

public interface FreshServiceTicketToolKit {

    JsonNode listTickets(String domain, String apiKey);

    static List<McpServerFeatures.SyncToolSpecification> getToolSpecs(FreshServiceTicketToolKit tool) {
        List<McpServerFeatures.SyncToolSpecification> specs = new ArrayList<>();

        // List Agents
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

        return specs;
    }

    private static McpSchema.CallToolResult getCallToolResult(String result) {
        List<McpSchema.Content> contents = new ArrayList<>();
        contents.add(new McpSchema.TextContent(result));
        return new McpSchema.CallToolResult(contents, false);
    }
}
