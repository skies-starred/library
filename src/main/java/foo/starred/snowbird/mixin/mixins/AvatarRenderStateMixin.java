package foo.starred.snowbird.mixin.mixins;

import foo.starred.snowbird.internal.ducks.AvatarRenderStateDuck;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

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
