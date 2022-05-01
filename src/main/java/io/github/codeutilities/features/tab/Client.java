package io.github.codeutilities.features.tab;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.loader.Loadable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Session;
import org.apache.commons.lang3.RandomStringUtils;

import java.net.URI;

public class Client implements Loadable {

    public static CodeUtilitiesServer client;

    @Override
    public void load() {
        connect();
    }

    public static void connect() {
        try {
            MinecraftClient mc = CodeUtilities.MC;
            Session session = mc.getSession();

            String serverid = RandomStringUtils.randomAlphabetic(20);
            mc.getSessionService().joinServer(session.getProfile(), session.getAccessToken(), serverid);
            String url = "wss://codeutilities.vatten.dev/?username=" + session.getUsername() + "&serverid=" + serverid + "&version=" + CodeUtilities.MOD_VERSION;

            client = new CodeUtilitiesServer(new URI(url));
            client.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}