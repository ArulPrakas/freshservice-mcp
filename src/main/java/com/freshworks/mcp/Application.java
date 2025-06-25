package com.freshworks.mcp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshworks.mcp.tools.api.FreshServiceAgentGroupToolKit;
import com.freshworks.mcp.tools.api.FreshServiceAgentToolKit;
import com.freshworks.mcp.tools.api.FreshServiceRequesterToolKit;
import com.freshworks.mcp.tools.api.FreshServiceTicketToolKit;
import com.freshworks.mcp.tools.impl.core.FreshServiceTicketAdapter;
import com.freshworks.mcp.tools.impl.user.FreshServiceAgentAdapter;
import com.freshworks.mcp.tools.impl.user.FreshServiceAgentGroupAdapter;
import com.freshworks.mcp.tools.impl.user.FreshServiceRequesterAdapter;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);
    private static final FreshServiceAgentToolKit agentTool = new FreshServiceAgentAdapter();
    private static final FreshServiceAgentGroupToolKit agentGroupTool = new FreshServiceAgentGroupAdapter();
    private static final FreshServiceTicketToolKit ticketTool = new FreshServiceTicketAdapter();
    private static final FreshServiceRequesterToolKit requesterTool = new FreshServiceRequesterAdapter();

    public static void main(String[] args) {

        // Stdio Server Transport (Support for SSE also available)
        var transportProvider = new StdioServerTransportProvider(new ObjectMapper());

        // Get tool specifications
        List<McpServerFeatures.SyncToolSpecification> specs = new ArrayList<>();
        specs.addAll(FreshServiceAgentToolKit.getToolSpecs(agentTool));
        specs.addAll(FreshServiceAgentGroupToolKit.getToolSpecs(agentGroupTool));
        specs.addAll(FreshServiceTicketToolKit.getToolSpecs(ticketTool));
        specs.addAll(FreshServiceRequesterToolKit.getToolSpecs(requesterTool));

        // Create a server with custom configuration
        McpServer.sync(transportProvider)
                .serverInfo("freshservice-mcp-server", "1.0.0")
                .capabilities(McpSchema.ServerCapabilities.builder().tools(true).logging().build())
                // Register
                .tools(specs.toArray(new McpServerFeatures.SyncToolSpecification[0]))
                .build();

        log.info("Starting FreshService MCP Server...");
    }

}
