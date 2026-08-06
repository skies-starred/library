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
    private Entity snowbird$entity;

    @Override
    public @Nullable Entity snowbird$entity() {
        return snowbird$entity;
    }

    @Override
    public void snowbird$entity(@Nullable Entity entity) {
        snowbird$entity = entity;
    }
}
