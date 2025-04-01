/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jolokia.mcp;

import javax.management.MalformedObjectNameException;

import jakarta.inject.Inject;

import io.quarkiverse.mcp.server.ToolManager;
import io.quarkiverse.mcp.server.ToolResponse;
import io.quarkus.runtime.Startup;
import java.util.Optional;
import org.jolokia.client.J4pClient;
import org.jolokia.client.exception.J4pException;
import org.jolokia.client.request.J4pExecRequest;
import org.jolokia.client.request.J4pExecResponse;

public class JolokiaMcpServer {

    @Inject
    J4pClient jolokiaClient;

    @Inject
    ToolManager toolManager;

    @Startup
    void registerTools() {
        toolManager.newTool("gc")
            .setDescription("GC")
            .setHandler(args -> {
                try {
                    J4pExecRequest request = new J4pExecRequest("java.lang:type=Memory", "gc");
                    J4pExecResponse response = jolokiaClient.execute(request);
                    return ToolResponse.success(Optional.ofNullable(response.getValue()).orElse("null").toString());
                } catch (MalformedObjectNameException | J4pException e) {
                    return ToolResponse.error(e.getMessage());
                }
            })
            .register();

    }
}

