///usr/bin/env jbang "$0" "$@" ; exit $?

//JAVA 17+
//DEPS org.jolokia.mcp:jolokia-mcp:0.1.0-SNAPSHOT

import org.jolokia.mcp.Main;

public class mcp {

    public static void main(String... args) {
        Main.main(args);
    }
}
