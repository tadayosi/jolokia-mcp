RUNNER ?= $(shell pwd)/target/jolokia-mcp-0.1.0-SNAPSHOT-runner.jar

mcp-inspector:
	npx @modelcontextprotocol/inspector java -jar ${RUNNER}

mcp-cli:
	npx @wong2/mcp-cli java -jar ${RUNNER}
