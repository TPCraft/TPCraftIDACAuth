package net.tpcraft.minecraft.server.http;

import net.tpcraft.minecraft.TPCraftIDACAuth;
import net.tpcraft.minecraft.server.http.controller.Oauth2Controller;
import spark.Spark;

public class WebServer {
    public static void start() {
        Spark.port(TPCraftIDACAuth.config.getWebPort());

        new Oauth2Controller();

        Spark.get("/", (req, res) -> "TPCraftIDAC Auth Plugin WebServer, TPCraftIDAC: https://auth.tpcraft.net/");
    }

    public static void stop() {
        Spark.stop();
    }
}
