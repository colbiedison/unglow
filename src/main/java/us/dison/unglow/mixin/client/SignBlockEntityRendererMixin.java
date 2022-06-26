package us.dison.unglow.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.render.block.entity.SignBlockEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import us.dison.unglow.client.UnglowClient;

@Environment(EnvType.CLIENT)
@Mixin(SignBlockEntityRenderer.class)
public class SignBlockEntityRendererMixin {

    @Redirect(
            method = "render(Lnet/minecraft/block/entity/SignBlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V",
            at = @At(
                    value = "INVOKE", target = "Lnet/minecraft/block/entity/SignBlockEntity;isGlowingText()Z"
            ))
    private boolean glowingRender(SignBlockEntity instance) {
        return !UnglowClient.isModEnabled() && instance.isGlowingText();
    }

    @Redirect(
            method = "getColor(Lnet/minecraft/block/entity/SignBlockEntity;)I",
            at = @At(
                    value = "INVOKE", target = "Lnet/minecraft/block/entity/SignBlockEntity;isGlowingText()Z"
            ))
    private static boolean glowingColor(SignBlockEntity instance) {
        return !UnglowClient.isModEnabled() && instance.isGlowingText();
    }

}
