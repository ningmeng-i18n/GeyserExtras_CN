package dev.letsgoaway.geyserextras;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class PluginVersion {
    public static final String GE_VERSION = "1.21.0-v1.1.4";

    public static String latestVersion = "";

    public static boolean checkForUpdate() {
        try {
            URL url = new URL("https://api.github.com/repos/ningmeng-i18n/GeyserExtras/tags");
            URLConnection request = url.openConnection();
            request.setConnectTimeout(5000);
            request.connect();
            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
            JsonArray rootobj = root.getAsJsonArray();
            latestVersion = rootobj.get(0).getAsJsonObject().get("name").getAsString();
            ReleaseVersion latestVer = new ReleaseVersion(latestVersion);
            return !latestVersion.equals(GE_VERSION) && !latestVer.prerelease && latestVer.isNewer(new ReleaseVersion(GE_VERSION));
        } catch (IOException e) {
            return false;
        }
    }

    public static void checkForUpdatesAndPrintToLog(Consumer<String> logger) {
        new Thread(()->{
            if (checkForUpdate()){
                logger.accept("该插件中文版有更新，请及时更新");
                logger.accept("你的版本" + GE_VERSION +" 最新版 " + latestVersion + ".");
                logger.accept("Github: https://github.com/ningmeng-i18n/GeyserExtras/releases/latest");
            }
        }).start();
    }
}
