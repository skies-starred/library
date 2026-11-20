package foo.starred.kontext.mixin.mixins;

import foo.starred.kontext.api.entity.duck.EntityRenderStateDuck;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityRenderState.class)
public class EntityRenderStateMixin implements EntityRenderStateDuck {
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
