# FreshService MCP Server

A Model Context Protocol (MCP) server implementation in Java that provides access to FreshService APIs. This project demonstrates how to build MCP servers using the official Java SDK for service desk operations.

## What is MCP?

The Model Context Protocol (MCP) is an open protocol developed by Anthropic that enables seamless integration between LLM applications and external data sources. MCP allows AI assistants to securely connect to various tools, databases, and services through a standardized interface.

## Features

This MCP server provides:

- **FreshService API Tools**: Complete set of tools for FreshService operations
- **Agent Management**: List, search, get, delete, and forget agents
- **Requester Management**: Full CRUD operations for requesters with advanced search
- **Ticket Operations**: Create, update, list, and manage tickets
- **Schema Management**: Load JSON schemas from external files for clean code organization
- **Standard I/O Transport**: Communication via stdin/stdout for easy integration with MCP clients
- **Java 21+ Support**: Built with modern Java features

## Available Tools

### Agent Tools

#### `list_agents`
Lists all agents in FreshService with optional filtering.

**Parameters:**
- `domain` (string, required): FreshService domain (e.g., 'company.freshservice.com')
- `apikey` (string, required): FreshService API key
- `per_page` (integer, optional): Number of entries per page (default: 10, max: 100)
- `page` (integer, optional): Page number (default: 1)
- `email` (string, optional): Filter by email address
- `mobile_phone_number` (string, optional): Filter by mobile phone
- `work_phone_number` (string, optional): Filter by work phone
- `state` (string, optional): Filter by state ('fulltime' or 'occasional')
- `active` (boolean, optional): Filter by active status
- `query` (string, optional): Complex query string for advanced filtering

#### `get_agent`
Retrieves a specific agent by ID.

**Parameters:**
- `domain` (string, required): FreshService domain
- `apikey` (string, required): FreshService API key
- `agent_id` (integer, required): Agent ID

#### `search_agents`
Advanced search for agents with complex query support.

**Parameters:**
- Same as `list_agents` with enhanced query capabilities

#### `delete_agent`
Converts an agent to a requester (non-destructive operation).

**Parameters:**
- `domain` (string, required): FreshService domain
- `apikey` (string, required): FreshService API key
- `agent_id` (integer, required): Agent ID

#### `forget_agent`
⚠️ **PERMANENTLY** deletes an agent and all associated tickets (irreversible).

**Parameters:**
- `domain` (string, required): FreshService domain
- `apikey` (string, required): FreshService API key
- `agent_id` (integer, required): Agent ID

### Requester Tools

#### `list_requesters`
Lists and searches requesters in FreshService with advanced filtering options.

**Parameters:**
- `domain` (string, required): FreshService domain (e.g., 'company.freshservice.com')
- `apikey` (string, required): FreshService API key
- `per_page` (integer, optional): Number of entries per page (default: 10, max: 100)
- `page` (integer, optional): Page number (default: 1)
- `email` (string, optional): Filter by email address
- `mobile_phone_number` (string, optional): Filter by mobile phone number
- `work_phone_number` (string, optional): Filter by work phone number
- `active` (boolean, optional): Filter by active status (true/false, omit for both)
- `query` (string, optional): Complex query string for advanced filtering

**Query String Examples:**
- `"job_title:'HR Manager' AND created_at:>'2018-08-10'"`
- `"first_name:'John' AND department_id:123"`
- `"primary_email:'*@company.com' AND active:true"`

**Supported Query Fields:**
- `first_name`, `last_name`, `job_title`
- `primary_email`, `secondary_emails`
- `work_phone_number`, `mobile_phone_number`
- `department_id`, `reporting_manager_id`
- `time_zone`, `language`, `location_id`
- `created_at`, `updated_at`
- All custom fields

#### `search_requesters`
Advanced search for requesters with complex query support (alias for enhanced list_requesters).

**Parameters:**
- Same as `list_requesters`

#### `get_requester`
Retrieves a specific requester by ID.

**Parameters:**
- `domain` (string, required): FreshService domain
- `apikey` (string, required): FreshService API key
- `requester_id` (integer, required): Requester ID

#### `create_requester`
Creates a new requester in FreshService.

**Parameters:**
- `domain` (string, required): FreshService domain
- `apikey` (string, required): FreshService API key
- `primary_email` (string, required): Primary email address
- `first_name` (string, required): First name
- `last_name` (string, optional): Last name
- `job_title` (string, optional): Job title
- `language` (string, optional): Language preference
- `time_zone` (string, optional): Time zone
- `time_format` (string, optional): Time format ('12h' or '24h')
- `secondary_emails` (array, optional): Secondary email addresses
- `work_phone_number` (string, optional): Work phone number
- `mobile_phone_number` (string, optional): Mobile phone number
- `department_ids` (array, optional): Department IDs
- `reporting_manager_id` (integer, optional): Reporting manager ID
- `address` (string, optional): Address
- `location_id` (integer, optional): Location ID
- `background_information` (string, optional): Background information
- `custom_fields` (object, optional): Custom fields

#### `update_requester`
Updates an existing requester in FreshService.

**Parameters:**
- `domain` (string, required): FreshService domain
- `apikey` (string, required): FreshService API key
- `requester_id` (integer, required): Requester ID to update
- All optional fields from `create_requester`

#### `delete_requester`
Deletes/deactivates a requester account.

**Parameters:**
- `domain` (string, required): FreshService domain
- `apikey` (string, required): FreshService API key
- `requester_id` (integer, required): Requester ID

#### `forget_requester`
⚠️ **PERMANENTLY** deletes a requester and all their data (irreversible).

**Parameters:**
- `domain` (string, required): FreshService domain
- `apikey` (string, required): FreshService API key
- `requester_id` (integer, required): Requester ID

#### `convert_requester_to_agent`
Converts a requester to an agent (grants agent privileges).

**Parameters:**
- `domain` (string, required): FreshService domain
- `apikey` (string, required): FreshService API key
- `requester_id` (integer, required): Requester ID

### Ticket Tools

#### `list_tickets`
Lists all tickets in FreshService.

**Parameters:**
- `domain` (string, required): FreshService domain
- `apikey` (string, required): FreshService API key

#### `get_ticket`
Retrieves a specific ticket by ID.

**Parameters:**
- `domain` (string, required): FreshService domain
- `apikey` (string, required): FreshService API key
- `ticket_id` (integer, required): Ticket ID

#### `create_ticket`
Creates a new ticket in FreshService.

**Parameters:**
- `domain` (string, required): FreshService domain
- `apikey` (string, required): FreshService API key
- `subject` (string, required): Ticket subject
- `description` (string, required): Ticket description
- `requester_id` (integer, required): Requester ID
- `priority` (integer, required): Priority level (1-4)
- `status` (integer, required): Status ID (2=open, 3=pending, etc.)
- `group_id` (integer, optional): Group ID
- `type` (string, optional): Ticket type (e.g., Incident, Service Request)
- `source` (integer, optional): Source (1=Email, 2=Portal, etc.)
- `due_by` (string, optional): Due by date/time (ISO 8601)
- `tags` (array of strings, optional): Ticket tags
- `custom_fields` (object, optional): Custom fields

**Example:**
```json
{
  "domain": "company.freshservice.com",
  "apikey": "your-api-key",
  "subject": "Cannot access VPN",
  "description": "User cannot connect to VPN from home.",
  "requester_id": 12345,
  "priority": 2,
  "status": 2,
  "group_id": 1001,
  "type": "Incident",
  "source": 1,
  "due_by": "2024-07-01T17:00:00Z",
  "tags": ["vpn", "remote"],
  "custom_fields": {"location": "Home"}
}
```

#### `update_ticket`
Updates an existing ticket in FreshService.

**Parameters:**
- `domain` (string, required): FreshService domain
- `apikey` (string, required): FreshService API key
- `ticketId` (string, required): Ticket ID
- `subject` (string, required): Ticket subject
- `description` (string, required): Ticket description
- `requester_id` (integer, required): Requester ID
- `priority` (integer, required): Priority level (1-4)
- `status` (integer, required): Status ID (2=open, 3=pending, etc.)
- `group_id` (integer, optional): Group ID
- `type` (string, optional): Ticket type (e.g., Incident, Service Request)
- `source` (integer, optional): Source (1=Email, 2=Portal, etc.)
- `due_by` (string, optional): Due by date/time (ISO 8601)
- `tags` (array of strings, optional): Ticket tags
- `custom_fields` (object, optional): Custom fields

**Example:**
```json
{
  "domain": "company.freshservice.com",
  "apikey": "your-api-key",
  "ticketId": "56789",
  "subject": "Cannot access VPN (updated)",
  "description": "User still cannot connect to VPN after troubleshooting.",
  "requester_id": 12345,
  "priority": 2,
  "status": 3,
  "group_id": 1001,
  "type": "Incident",
  "source": 1,
  "due_by": "2024-07-02T17:00:00Z",
  "tags": ["vpn", "remote", "escalated"],
  "custom_fields": {"location": "Home"}
}
```

## Prerequisites

- **Java 21** or later
- **Maven 3.6+** for building the project
- **FreshService Account** with API access
- **FreshService API Key** for authentication

## Installation

1. **Clone the repository:**
```bash
git clone <repository-url>
cd freshservice-mcp-server
```

2. **Build the project:**
```bash
mvn clean package
```

This will create a shaded JAR file `freshservice-mcp-server-1.0.0.jar` in the `target/` directory with all dependencies included.

3. **Verify the build:**
```bash
ls -la target/freshservice-mcp-server-1.0.0.jar
```

## Usage

### Running the Server Standalone

Execute the server using the shaded JAR:

```bash
java -jar target/freshservice-mcp-server-1.0.0.jar
```

The server will start and listen for MCP messages on standard input/output.

### Integration with MCP Clients

This server uses the standard I/O transport, making it compatible with any MCP client that supports stdio communication.

#### Claude Desktop Integration

**Step 1: Locate Claude Desktop Configuration**

The configuration file location depends on your operating system:

- **macOS**: `~/Library/Application Support/Claude/claude_desktop_config.json`
- **Windows**: `%APPDATA%\Claude\claude_desktop_config.json`
- **Linux**: `~/.config/Claude/claude_desktop_config.json`

**Step 2: Create or Edit Configuration File**

If the file doesn't exist, create it. Add or update the configuration:

```json
{
  "mcpServers": {
    "freshservice": {
      "command": "java",
      "args": [
        "-jar", 
        "/absolute/path/to/freshservice-mcp-server/target/freshservice-mcp-server-1.0.0.jar"
      ],
      "env": {
        "JAVA_HOME": "/path/to/your/java/installation"
      }
    }
  }
}
```

**Important Notes:**
- Replace `/absolute/path/to/freshservice-mcp-server/` with the actual absolute path to your project directory
- Replace `/path/to/your/java/installation` with your Java installation path (optional if Java is in PATH)
- Use forward slashes `/` even on Windows in the JSON configuration

**Step 3: Find Your Java Installation Path**

To find your Java installation path:

```bash
# On macOS/Linux
which java
java -XshowSettings:properties -version 2>&1 | grep java.home

# On Windows (Command Prompt)
where java
java -XshowSettings:properties -version 2>&1 | findstr java.home
```

**Step 4: Example Configuration**

Here's a complete example for macOS:

```json
{
  "mcpServers": {
    "freshservice": {
      "command": "java",
      "args": [
        "-jar", 
        "/Users/username/Documents/freshservice-mcp-server/target/freshservice-mcp-server-1.0.0.jar"
      ],
      "env": {
        "JAVA_HOME": "/Library/Java/JavaVirtualMachines/openjdk-21.jdk/Contents/Home"
      }
    }
  }
}
```

**Step 5: Restart Claude Desktop**

After saving the configuration file, completely quit and restart Claude Desktop for the changes to take effect.

**Step 6: Verify Integration**

In Claude Desktop, you should now be able to use FreshService tools. Try asking:
- "List the available tools"
- "Show me the FreshService requesters"

#### VS Code with Continue Extension Integration

**Step 1: Install Continue Extension**

1. Open VS Code
2. Go to Extensions (Ctrl+Shift+X / Cmd+Shift+X)
3. Search for "Continue" and install it

**Step 2: Configure Continue for MCP**

1. Open VS Code settings (Ctrl+, / Cmd+,)
2. Search for "Continue"
3. Find "Continue: Config" and click "Edit in settings.json"

**Step 3: Add MCP Server Configuration**

Add the following to your Continue configuration:

```json
{
  "models": [
    {
      "title": "Claude 3.5 Sonnet with FreshService",
      "provider": "anthropic",
      "model": "claude-3-5-sonnet-20241022",
      "apiKey": "your-anthropic-api-key"
    }
  ],
  "mcpServers": [
    {
      "name": "freshservice",
      "command": "java",
      "args": [
        "-jar",
        "/absolute/path/to/freshservice-mcp-server/target/freshservice-mcp-server-1.0.0.jar"
      ],
      "env": {
        "JAVA_HOME": "/path/to/your/java/installation"
      }
    }
  ]
}
```

**Step 4: Alternative VS Code Integration (MCP Extension)**

If there's a dedicated MCP extension for VS Code:

1. Install the MCP extension from the VS Code marketplace
2. Configure it with the server details:

```json
{
  "mcp.servers": {
    "freshservice": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/target/freshservice-mcp-server-1.0.0.jar"],
      "cwd": "/absolute/path/to/freshservice-mcp-server"
    }
  }
}
```

#### Other MCP Clients

For other MCP-compatible clients, configure them to execute:

```bash
java -jar /absolute/path/to/target/freshservice-mcp-server-1.0.0.jar
```

### Testing the Integration

**Test with Claude Desktop:**

1. Open Claude Desktop
2. Ask: "What FreshService tools are available?"
3. Try: "List requesters from my FreshService account" (you'll need to provide domain and API key)

**Test with VS Code:**

1. Open the Continue chat panel
2. Ask: "Show me available MCP tools"
3. Test a FreshService operation

### Environment Variables (Optional)

For easier configuration, you can set environment variables:

```bash
# Add to your shell profile (.bashrc, .zshrc, etc.)
export FRESHSERVICE_DOMAIN="your-company.freshservice.com"
export FRESHSERVICE_API_KEY="your-api-key"
```

Then modify your MCP client configuration to pass these variables:

```json
{
  "mcpServers": {
    "freshservice": {
      "command": "java",
      "args": ["-jar", "/path/to/freshservice-mcp-server-1.0.0.jar"],
      "env": {
        "FRESHSERVICE_DOMAIN": "${FRESHSERVICE_DOMAIN}",
        "FRESHSERVICE_API_KEY": "${FRESHSERVICE_API_KEY}"
      }
    }
  }
}
```

### Troubleshooting Integration

**Common Configuration Issues:**

1. **Path Issues**: Always use absolute paths in configuration files
2. **Java Not Found**: Ensure Java 21+ is installed and JAVA_HOME is set correctly
3. **Permissions**: Ensure the JAR file has execute permissions:
   ```bash
   chmod +x target/freshservice-mcp-server-1.0.0.jar
   ```
4. **Configuration Syntax**: Validate your JSON configuration files
5. **Client Restart**: Always restart your MCP client after configuration changes

**Debug Configuration:**

Enable debug logging in your MCP client configuration:

```json
{
  "mcpServers": {
    "freshservice": {
      "command": "java",
      "args": [
        "-Dorg.slf4j.simpleLogger.defaultLogLevel=debug",
        "-jar", 
        "/path/to/freshservice-mcp-server-1.0.0.jar"
      ]
    }
  }
}
```

## Project Structure

```
src/main/java/com/freshworks/mcp/
├── Application.java              # Main MCP server application
├── constants/
│   └── PathConstants.java       # API path constants
├── tools/
│   ├── api/                     # Tool interfaces
│   │   ├── FreshServiceAgentToolKit.java
│   │   ├── FreshServiceRequesterToolKit.java
│   │   └── FreshServiceTicketToolKit.java
│   └── impl/                    # Tool implementations
│       ├── user/
│       │   ├── FreshServiceAgentAdapter.java
│       │   └── FreshServiceRequesterAdapter.java
│       └── ticket/
│           └── FreshServiceTicketAdapter.java
└── utils/
    ├── SchemaLoader.java        # JSON schema loading utility
    └── http/
        └── RestClient.java      # HTTP client for API calls

src/main/resources/
└── schemas/                     # JSON schema files for tools
    ├── freshservice_input.json
    ├── agent_by_id_input.json
    ├── agent_search_input.json
    ├── requester_by_id_input.json
    ├── requester_search_input.json
    ├── create_requester_input.json
    ├── update_requester_input.json
    └── ticket_input.json
```

## Configuration

### FreshService API Authentication

All tools require:
- **Domain**: Your FreshService domain (e.g., 'company.freshservice.com')
- **API Key**: Your FreshService API key for authentication

### Getting Your API Key

1. Log in to your FreshService account
2. Go to Admin → API Settings
3. Generate or copy your API key
4. Use this key in the `apikey` parameter for all tool calls

## Advanced Search Examples

### Requester Search Examples

**Find HR Managers created after a specific date:**
```json
{
  "domain": "company.freshservice.com",
  "apikey": "your-api-key",
  "query": "job_title:'HR Manager' AND created_at:>'2018-08-10'"
}
```

**Find requesters by department:**
```json
{
  "domain": "company.freshservice.com",
  "apikey": "your-api-key",
  "query": "department_id:123"
}
```

**Find active requesters with company email:**
```json
{
  "domain": "company.freshservice.com",
  "apikey": "your-api-key",
  "query": "primary_email:'*@company.com' AND active:true"
}
```

### Agent Search Examples

**Find full-time active agents:**
```json
{
  "domain": "company.freshservice.com",
  "apikey": "your-api-key",
  "state": "fulltime",
  "active": true
}
```

**Find agents by phone number:**
```json
{
  "domain": "company.freshservice.com",
  "apikey": "your-api-key",
  "mobile_phone_number": "+1-555-0123"
}
```

## Dependencies

The project uses:

- **MCP Java SDK**: Official Java implementation of the Model Context Protocol
- **Jackson**: JSON processing for API requests/responses
- **SLF4J**: Logging facade for Java applications
- **Apache HTTP Client**: HTTP client for FreshService API calls

## Development

### Adding New Tools

To add new FreshService tools:

1. Create or update the appropriate toolkit interface in `tools/api/`
2. Implement the method in the corresponding adapter in `tools/impl/`
3. Create a JSON schema file in `src/main/resources/schemas/`
4. Add the tool specification in the toolkit's `getToolSpecs()` method
5. Register the toolkit in `Application.java`

### Schema Management

JSON schemas are stored in separate files under `src/main/resources/schemas/` and loaded using the `SchemaLoader` utility. This keeps the code clean and makes schema maintenance easier.

### Adding Support for Other FreshService Modules

To add support for other FreshService modules (Assets, Changes, Releases, etc.):

1. Create a new toolkit interface and adapter
2. Add the appropriate API endpoints to `PathConstants`
3. Create corresponding schema files
4. Register the new toolkit in the main application

## Security Considerations

- **API Key Security**: Store FreshService API keys securely, not in code or configuration files
- **Rate Limiting**: FreshService APIs have rate limits; implement appropriate throttling
- **Permission Management**: Ensure API keys have only necessary permissions
- **Data Privacy**: Be mindful of sensitive data when logging or debugging

## Building from Source

```bash
# Clean and compile
mvn clean compile

# Run tests (if any)
mvn test

# Package with dependencies
mvn package

# The executable JAR will be created at:
# target/freshservice-mcp-server-1.0.0.jar
```

## Troubleshooting

### Common Issues

1. **Java Version**: Ensure you're using Java 21 or later
2. **API Key**: Verify your FreshService API key is valid and has necessary permissions
3. **Domain**: Ensure the domain format is correct (e.g., 'company.freshservice.com')
4. **Rate Limits**: If you encounter rate limiting, reduce the frequency of API calls
5. **Network**: Ensure network connectivity to FreshService APIs

### Debug Mode

To enable debug logging, set the system property:
```bash
java -Dorg.slf4j.simpleLogger.defaultLogLevel=debug -jar target/freshservice-mcp-server-1.0.0.jar
```

### API Testing

Test your FreshService API connection:
```bash
curl -u your-api-key:X -H "Content-Type: application/json" \
  https://your-domain.freshservice.com/api/v2/requesters
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Related Links

- [Model Context Protocol Specification](https://modelcontextprotocol.io/)
- [MCP Java SDK](https://github.com/modelcontextprotocol/java-sdk)
- [FreshService API Documentation](https://api.freshservice.com/)
- [FreshService Developer Portal](https://developers.freshservice.com/)

## Support

For questions or issues:
1. Check the [MCP Java SDK documentation](https://github.com/modelcontextprotocol/java-sdk)
2. Review FreshService API documentation
3. Verify API key permissions and rate limits
4. Enable debug logging for detailed error information 