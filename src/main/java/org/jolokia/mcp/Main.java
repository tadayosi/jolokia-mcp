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

import jakarta.inject.Inject;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import java.util.concurrent.Callable;
import org.jboss.logging.Logger;
import picocli.CommandLine;

@QuarkusMain
@CommandLine.Command(
    name = "jolokia-mcp",
    description = "Jolokia MCP Server",
    versionProvider = Main.VersionProvider.class)
public class Main implements Callable<Integer>, QuarkusApplication {

    private static final Logger LOG = Logger.getLogger(Main.class);

    @SuppressWarnings("unused")
    @CommandLine.Option(names = { "--help", "-h" }, usageHelp = true, description = "Show help")
    boolean help;

    @SuppressWarnings("unused")
    @CommandLine.Option(names = { "--version", "-V" }, versionHelp = true, description = "Print version")
    boolean version;

    @CommandLine.Parameters
    String endpoint;

    @Inject
    CommandLine.IFactory factory;

    public static void main(String... args) {
        Quarkus.run(Main.class, args);
    }

    @Override
    public int run(String... args) {
        return new CommandLine(this, factory).execute(args);
    }

    @Override
    public Integer call() {
        LOG.info("Jolokia MCP Server CLI");
        LOG.info(">>> " + endpoint);
        return 0;
    }

    static class VersionProvider implements CommandLine.IVersionProvider {
        @Override
        public String[] getVersion() {
            String version = Main.class.getPackage().getImplementationVersion();
            return new String[] { version };
        }
    }
}
