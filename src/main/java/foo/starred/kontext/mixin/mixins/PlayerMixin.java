package foo.starred.kontext.mixin.mixins;

import foo.starred.kontext.core.duck.PlayerDuck;
import foo.starred.kontext.core.misc.DonatorEmblem;
import foo.starred.kontext.core.misc.DonatorSize;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Player.class)
public class PlayerMixin implements PlayerDuck {
    @Unique
    int kontext$name$emblem$int = -1;

    @Unique
    Component kontext$name$emblem$tag = null;

    @Unique
    int kontext$size = -1;

    @Unique
    float kontext$size$x = 1F;

    @Unique
    float kontext$size$y = 1F;

    @Unique
    float kontext$size$z = 1F;

    @Override
    public int kontext$name$emblem$int() {
        if (kontext$name$emblem$int == -1) kontext$name$emblem$int = DonatorEmblem.INSTANCE.fn(kontext$self().getGameProfile().name()) ? 1 : 0;
        return kontext$name$emblem$int;
    }

    @Override
    public @NotNull Component kontext$name$emblem$tag(@NonNull Component tag) {
        if (kontext$name$emblem$tag == null) kontext$name$emblem$tag = DonatorEmblem.INSTANCE.fn(kontext$self().getGameProfile().name(), tag);
        return kontext$name$emblem$tag;
    }

    @Override
    public int kontext$size() {
        if (kontext$size == -1) kontext$size = DonatorSize.fn(kontext$self()) ? 1 : 0;
        return kontext$size;
    }

    @Override
    public float kontext$size$x() {
        return kontext$size$x;
    }

    @Override
    public float kontext$size$y() {
        return kontext$size$y;
    }

    @Override
    public float kontext$size$z() {
        return kontext$size$z;
    }

    @Override
    public void kontext$size(float i0, float i1, float i2) {
        kontext$size$x = i0;
        kontext$size$y = i1;
        kontext$size$z = i2;
    }

    @Unique
    private Player kontext$self() {
        return (Player) (Object) this;
    }
}
