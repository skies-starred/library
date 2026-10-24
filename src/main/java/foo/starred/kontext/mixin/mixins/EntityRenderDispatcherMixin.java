package foo.starred.kontext.mixin.mixins;

import foo.starred.kontext.core.duck.AvatarRenderStateDuck;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
    @Inject(method = "extractEntity", at = @At("RETURN"))
    private void kontext$extractEntity(Entity entity, float partialTicks, CallbackInfoReturnable<EntityRenderState> cir) {
        EntityRenderState a = cir.getReturnValue();
        if (!(a instanceof AvatarRenderState)) return;
        ((AvatarRenderStateDuck) a).kontext$entity(entity);
    }
}
