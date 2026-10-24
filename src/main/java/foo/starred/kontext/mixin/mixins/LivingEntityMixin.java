package foo.starred.kontext.mixin.mixins;

import foo.starred.kontext.api.entity.duck.LivingEntityDuck;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements LivingEntityDuck {
    @Unique
    private float kontext$serverHealth = Float.NaN;

    @Override
    public float kontext$serverHealth() {
        return Float.isNaN(this.kontext$serverHealth) ? kontext$self().getHealth() : this.kontext$serverHealth;
    }

    @Override
    public void kontext$serverHealth(float v) {
        kontext$serverHealth = v;
    }

    @Unique
    private LivingEntity kontext$self() {
        return (LivingEntity) (Object) this;
    }
}
