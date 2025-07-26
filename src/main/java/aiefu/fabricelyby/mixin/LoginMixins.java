package aiefu.fabricelyby.mixin;

import aiefu.fabricelyby.FabricElyBy;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLoginPacketListenerImpl.class)
public class LoginMixins {
    @Shadow @Final private MinecraftServer server;
    @Shadow private @Nullable GameProfile authenticatedProfile;
    @Unique
    private boolean awaitingSkinData = true;

    @Unique
    private boolean madeRequest = false;
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void getSkinDataAndProceed(CallbackInfo ci){
        if(!madeRequest){
            FabricElyBy.applySkinDataIfAvailableAsync(authenticatedProfile, server, () -> awaitingSkinData = false);
            madeRequest = true;
        }
        if(awaitingSkinData) ci.cancel();
    }

}
