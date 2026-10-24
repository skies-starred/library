package foo.starred.kontext.mixin.mixins;

import foo.starred.kontext.api.item.duck.ItemStackDuck;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;

@Mixin(ItemStack.class)
public class ItemStackMixin implements ItemStackDuck {
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

    @Inject(method = "copy", at = @At("RETURN"))
    private void kontext$copy(CallbackInfoReturnable<ItemStack> cir) {
        Object[] data = this.kontext$data;
        if (data == null) return;

        ((ItemStackDuck) (Object) cir.getReturnValue()).kontext$data(data.clone());
    }

    @Inject(method = {"set*", "remove"}, at = @At("RETURN"))
    private void kontext$onComponentSet(CallbackInfoReturnable<?> cir) {
        this.kontext$data = null;
    }

    @Inject(method = {"applyComponents*", "applyComponentsAndValidate"}, at = @At("RETURN"))
    private void kontext$update$1(CallbackInfo ci) {
        this.kontext$data = null;
    }
}
