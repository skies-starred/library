package xyz.aerii.library.mixin.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.aerii.library.internal.ducks.AvatarRenderStateDuck;
import xyz.aerii.library.internal.ducks.PlayerDuck;

@Mixin(AvatarRenderer.class)
public class AvatarRendererMixin {
    @Inject(method = "scale(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V"))
    private void aerii$library$scale(AvatarRenderState state, PoseStack poseStack, CallbackInfo ci) {
        final Entity a = ((AvatarRenderStateDuck) state).aerii$library$entity();
        if (a == null) return;
        if (!(a instanceof Player b)) return;

        final PlayerDuck c = (PlayerDuck) b;
        if (c.aerii$library$size() == 0) return;

        float x = c.aerii$library$size$x() * 0.9375f;
        float y = c.aerii$library$size$y() * 0.9375f;
        float z = c.aerii$library$size$z() * 0.9375f;
        poseStack.scale(x, y, z);

        if (state.nameTagAttachment != null) state.nameTagAttachment = state.nameTagAttachment.scale(y);
        state.shadowRadius *= Math.max(x, z) / 1.5f;
    }
}
