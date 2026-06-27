package xyz.aerii.library.mixin.mixins;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import xyz.aerii.library.internal.ducks.PlayerDuck;
import xyz.aerii.library.internal.misc.DonatorSize;

@Mixin(Player.class)
public class PlayerMixin implements PlayerDuck {
    @Unique
    int aerii$library$size = -1;

    @Unique
    float aerii$library$size$x = 1F;

    @Unique
    float aerii$library$size$y = 1F;

    @Unique
    float aerii$library$size$z = 1F;

    @Override
    public int aerii$library$size() {
        if (aerii$library$size == -1) aerii$library$size = DonatorSize.fn(aerii$library$self()) ? 1 : 0;
        return aerii$library$size;
    }

    @Override
    public float aerii$library$size$x() {
        return aerii$library$size$x;
    }

    @Override
    public float aerii$library$size$y() {
        return aerii$library$size$y;
    }

    @Override
    public float aerii$library$size$z() {
        return aerii$library$size$z;
    }

    @Override
    public void aerii$library$size(float i0, float i1, float i2) {
        aerii$library$size$x = i0;
        aerii$library$size$y = i1;
        aerii$library$size$z = i2;
    }

    @Unique
    private Player aerii$library$self() {
        return (Player) (Object) this;
    }
}
