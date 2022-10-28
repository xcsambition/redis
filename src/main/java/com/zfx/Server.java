package com.zfx;


import com.github.tonivade.resp.RespServer;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import java.io.IOException;

public class Server {
    public static void main(String[] args) throws IOException {
        OptionParser parser = new OptionParser();
        OptionSpec<Void> help = parser.accepts("help", "print help");
        OptionSpec<String> host = parser.accepts("h", "host")
                .withRequiredArg()
                .defaultsTo(DBServerContext.DEFAULT_HOST);
        OptionSpec<String> port = parser.accepts("p", "port")
                .withRequiredArg();
        OptionSet options = parser.parse(args);

        if (options.has(help)) {
            parser.printHelpOn(System.out);
        } else {
            String optionHost = options.valueOf(host);
            int optionPort = parsePort(options.valueOf(port));
            RespServer server = ClauDB.builder().host(optionHost).port(optionPort).build();
            server.start();
        }
    }


    private static int parsePort(String optionPort) {
        return optionPort != null ? Integer.parseInt(optionPort) : DBServerContext.DEFAULT_PORT;
    }
}
