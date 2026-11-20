package foo.starred.kontext.mixin.mixins;

import foo.starred.kontext.api.entity.duck.EntityDuck;
import net.minecraft.world.entity.Entity;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Arrays;

@Mixin(Entity.class)
public class EntityMixin implements EntityDuck {
    @Unique
    private Object[] kontext$data = null;

    @Override
    public Object kontext$data(int id) {
        Object[] data = this.kontext$data;
        if (data == null) return null;
        if (id >= data.length) return null;

        return data[id];
    }

    @Override
    public void kontext$data(int id, Object value) {
        Object[] data = this.kontext$data;

        if (data == null) {
            data = new Object[Math.max(id + 1, 8)];
            data[id] = value;
            this.kontext$data = data;
            return;
        }

        if (id >= data.length) {
            int capacity = Math.max(id + 1, data.length * 2);
            data = Arrays.copyOf(data, capacity);
        }

        data[id] = value;
        this.kontext$data = data;
    }

    @Override
    public Object @NonNull [] kontext$data() {
        return this.kontext$data;
    }

    @Override
    public void kontext$data(Object @NonNull [] data) {
        this.kontext$data = data;
    }
}
