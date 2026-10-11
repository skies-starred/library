package foo.starred.snowbird.mixin.mixins;

import foo.starred.snowbird.internal.ducks.PlayerDuck;
import foo.starred.snowbird.internal.misc.DonatorEmblem;
import foo.starred.snowbird.internal.misc.DonatorSize;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Player.class)
public class PlayerMixin implements PlayerDuck {
    @Unique
    Component snowbird$name$emblem;

    @Unique
    int snowbird$size = -1;

    @Unique
    float snowbird$size$x = 1F;

    @Unique
    float snowbird$size$y = 1F;

    @Unique
    float snowbird$size$z = 1F;

    @Override
    public @NotNull Component snowbird$name$emblem(@NonNull Component tag) {
        if (snowbird$name$emblem == null) snowbird$name$emblem = DonatorEmblem.INSTANCE.fn(snowbird$self().getGameProfile().name(), tag);
        return snowbird$name$emblem;
    }

    @Override
    public int snowbird$size() {
        if (snowbird$size == -1) snowbird$size = DonatorSize.fn(snowbird$self()) ? 1 : 0;
        return snowbird$size;
    }

    @Override
    public float snowbird$size$x() {
        return snowbird$size$x;
    }

    @Override
    public float snowbird$size$y() {
        return snowbird$size$y;
    }

    @Override
    public float snowbird$size$z() {
        return snowbird$size$z;
    }

    @Override
    public void snowbird$size(float i0, float i1, float i2) {
        snowbird$size$x = i0;
        snowbird$size$y = i1;
        snowbird$size$z = i2;
    }

    @Unique
    private Player snowbird$self() {
        return (Player) (Object) this;
    }
}
