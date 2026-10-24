package foo.starred.kontext.mixin.mixins;

import foo.starred.kontext.core.duck.AvatarRenderStateDuck;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AvatarRenderState.class)
public class AvatarRenderStateMixin implements AvatarRenderStateDuck {
    @Unique
    private Entity kontext$entity;

    @Override
    public @Nullable Entity kontext$entity() {
        return kontext$entity;
    }

    @Override
    public void kontext$entity(@Nullable Entity entity) {
        kontext$entity = entity;
    }
}
