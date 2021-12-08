package ru.aiefu.fabricelyby.mixin;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.aiefu.fabricelyby.FabricElyBy;

@Mixin(PlayerList.class)
public class PlayerListMixins {

    @Inject(method = "placeNewPlayer", at =@At("HEAD"))
    private void attachSkinData(Connection connection, ServerPlayer serverPlayer, CallbackInfo ci){
        GameProfile gameProfile = serverPlayer.getGameProfile();
        JsonElement element = FabricElyBy.getSkinData(gameProfile.getName());
        if(element != null) {
            JsonObject j = element.getAsJsonObject().getAsJsonArray("properties").get(0).getAsJsonObject();
            Property property = new Property("textures", j.get("value").getAsString(), j.get("signature").getAsString());
            PropertyMap map = gameProfile.getProperties();
            map.removeAll("textures");
            map.put("textures", property);
        }
    }
}
