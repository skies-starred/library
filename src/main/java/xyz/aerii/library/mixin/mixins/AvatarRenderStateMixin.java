package xyz.aerii.library.mixin.mixins;

import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import xyz.aerii.library.internal.ducks.AvatarRenderStateDuck;

@Mixin(AvatarRenderState.class)
public class AvatarRenderStateMixin implements AvatarRenderStateDuck {
    @Unique
    private Entity aerii$library$entity;

    @Override
    public @Nullable Entity aerii$library$entity() {
        return aerii$library$entity;
    }

    @Override
    public void aerii$library$entity(@Nullable Entity entity) {
        aerii$library$entity = entity;
    }
}
