package aiefu.fabricelyby;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class FabricElyBy implements ModInitializer {

	public static final Logger LOGGER = LogManager.getLogger("fabricelyby");

	public static Config cfg;

	@Override
	public void onInitialize(){
		try {
			IOManager.craftPaths();
			IOManager.genCfg();
			cfg = IOManager.readCfg();
			LOGGER.info("Ely-by skin system loaded!");
		} catch (IOException e) {
			LOGGER.fatal("Unable to read/write config.json in config/ely-by-for-fabric/");
			e.printStackTrace();
		}
	}

	public static void applySkinDataIfAvailableAsync(GameProfile profile, MinecraftServer server, Runnable r){
		CompletableFuture.supplyAsync(() -> {
			try {
				HttpURLConnection c = FabricElyBy.createUrlConnection(new URL(String.format("http://skinsystem.ely.by/textures/signed/%s?proxy=true?token=%s", profile.getName(), cfg.serverToken)));
				try(InputStreamReader reader = new InputStreamReader(c.getInputStream())){
					return JsonParser.parseReader(reader);
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				} finally {
					c.disconnect();
				}
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}).thenAcceptAsync(e -> {
			if(e != null){
				FabricElyBy.applySkin(profile, e);
			}
		}, server).whenCompleteAsync((unused, throwable) -> r.run(), server);
	}

	public static void applySkinDataIfAvailable(GameProfile profile) throws IOException {
		HttpURLConnection c = FabricElyBy.createUrlConnection(new URL(String.format("http://skinsystem.ely.by/textures/signed/%s?proxy=true", profile.getName())));
		try(InputStreamReader reader = new InputStreamReader(c.getInputStream())){
			applySkin(profile, JsonParser.parseReader(reader));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			c.disconnect();
		}

	}

	public static HttpURLConnection createUrlConnection(final URL url) throws IOException {
		Validate.notNull(url);
		LOGGER.debug("Opening connection to " + url);
		final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setConnectTimeout(15000);
		connection.setReadTimeout(15000);
		connection.setUseCaches(false);
		return connection;
	}

	public static void applySkin(GameProfile profile, JsonElement element){
		JsonObject j = element.getAsJsonObject().getAsJsonArray("properties").get(0).getAsJsonObject();
		Property property = new Property("textures", j.get("value").getAsString(), j.get("signature").getAsString());
		PropertyMap map = profile.getProperties();
		map.removeAll("textures");
		map.put("textures", property);
		System.out.println("TASK EXECUTED");
	}
}
