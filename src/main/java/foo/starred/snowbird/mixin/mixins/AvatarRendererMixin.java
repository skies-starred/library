package foo.starred.snowbird.mixin.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import foo.starred.snowbird.internal.ducks.AvatarRenderStateDuck;
import foo.starred.snowbird.internal.ducks.PlayerDuck;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public class AvatarRendererMixin {
    @Inject(method = "scale(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V"))
    private void snowbird$scale(AvatarRenderState state, PoseStack poseStack, CallbackInfo ci) {
        final Entity a = ((AvatarRenderStateDuck) state).snowbird$entity();
        if (a == null) return;
        if (!(a instanceof Player b)) return;

        final PlayerDuck c = (PlayerDuck) b;
        if (c.snowbird$size() == 0) return;

        float x = c.snowbird$size$x() * 0.9375f;
        float y = c.snowbird$size$y() * 0.9375f;
        float z = c.snowbird$size$z() * 0.9375f;
        poseStack.scale(x, y, z);

        if (state.nameTagAttachment != null) state.nameTagAttachment = state.nameTagAttachment.scale(y);
        state.shadowRadius *= Math.max(x, z) / 1.5f;
    }
}
