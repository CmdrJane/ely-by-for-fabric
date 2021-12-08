package ru.aiefu.fabricelyby.mixin;

import com.google.gson.JsonElement;
import com.mojang.authlib.GameProfile;
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
            FabricElyBy.applySkin(gameProfile, element);
        }
    }
}
