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

	@Override
	public void onInitializeServer() {
		LOGGER.info("Ely-by skin system loaded!");
	}

	@Nullable
	public static JsonElement getSkinData(String playerName) {
		HttpURLConnection connection;
		try {
			connection = (HttpURLConnection) new URL("http://skinsystem.ely.by/textures/signed/" + playerName).openConnection();
			return JsonParser.parseReader(new InputStreamReader(connection.getInputStream()));

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
