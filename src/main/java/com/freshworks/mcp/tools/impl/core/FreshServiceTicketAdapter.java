package com.freshworks.mcp.tools.impl.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.freshworks.mcp.constants.PathConstants;
import com.freshworks.mcp.tools.AbstractAdapter;
import com.freshworks.mcp.tools.api.FreshServiceTicketToolKit;
import com.freshworks.mcp.utils.http.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class FreshServiceTicketAdapter extends AbstractAdapter
        implements FreshServiceTicketToolKit {
    private static final Logger logger = LoggerFactory.getLogger(FreshServiceTicketAdapter.class);

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
}
