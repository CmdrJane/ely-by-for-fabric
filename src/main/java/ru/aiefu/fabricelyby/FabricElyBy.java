package ru.aiefu.fabricelyby;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.fabricmc.api.DedicatedServerModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FabricElyBy implements DedicatedServerModInitializer {

	public static final Logger LOGGER = LogManager.getLogger("fabricelyby");

	public static String serverToken;

	@Override
	public void onInitializeServer() {
		LOGGER.info("Ely-by skin system loaded!");
	}

	@Nullable
	public static JsonElement getSkinData(String playerName) {
		HttpURLConnection connection;
		try {
			connection = (HttpURLConnection) new URL(String.format("http://skinsystem.ely.by/textures/signed/%s?token=%s", playerName, serverToken)).openConnection();
			if(connection.getResponseCode() == 204){
				FabricElyBy.LOGGER.warn(String.format("Unable to retrieve skin textures for %s! Textures not found on server!", playerName));
				return null;
			}
			return JsonParser.parseReader(new InputStreamReader(connection.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
