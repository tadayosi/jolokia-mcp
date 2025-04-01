///usr/bin/env jbang "$0" "$@" ; exit $?

//JAVA 21
//DEPS io.modelcontextprotocol.sdk:mcp-bom:0.8.1@pom
//DEPS io.modelcontextprotocol.sdk:mcp
//DEPS org.slf4j:slf4j-simple:2.0.17

import static java.lang.System.out;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.Content;
import io.modelcontextprotocol.spec.McpSchema.LoggingLevel;
import io.modelcontextprotocol.spec.McpSchema.LoggingMessageNotification;
import io.modelcontextprotocol.spec.McpSchema.ServerCapabilities;
import io.modelcontextprotocol.spec.McpSchema.TextContent;
import io.modelcontextprotocol.spec.McpSchema.Tool;

public class hello_server {

    static final String NAME = hello_server.class.getName();

    public static void main(String... args) {
        out.println("Start " + NAME);

        var transportProvider = new StdioServerTransportProvider(new ObjectMapper());
        var server = McpServer.sync(transportProvider)
                .serverInfo(hello_server.class.getName(), "1.0.0")
                .capabilities(ServerCapabilities.builder()
                        .tools(true)
                        .logging()
                        .build())
                .build();

        var inputSchema = """
                {
                  "type" : "object",
                  "properties" : {
                    "name" : {
                      "type" : "string"
                    }
                  },
                  "required" : ["name"]
                }
                """;
        var toolSpec = new McpServerFeatures.SyncToolSpecification(
                new Tool("hello", "Hello tool", inputSchema),
                hello_server::tool);
        server.addTool(toolSpec);

        server.loggingNotification(LoggingMessageNotification.builder()
                .level(LoggingLevel.INFO)
                .logger(NAME)
                .data("Server initialised")
                .build());

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            out.println("Close " + NAME);
            server.close();
        }));
    }

    static CallToolResult tool(McpSyncServerExchange exchange, Map<String, Object> arguments) {
        out.println("Exchange: " + exchange);
        out.println("Arguments: " + arguments);
        var result = List.<Content>of(
                new TextContent("Hello " + Optional.ofNullable(arguments.get("name")).orElse("unknown")));
        return new CallToolResult(result, false);
    }
}
