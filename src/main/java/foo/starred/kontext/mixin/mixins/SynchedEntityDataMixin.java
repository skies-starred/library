package foo.starred.kontext.mixin.mixins;

import foo.starred.kontext.api.entity.duck.LivingEntityDuck;
import foo.starred.kontext.mixin.accessors.LivingEntityAccessor;
import net.minecraft.network.syncher.SyncedDataHolder;
import net.minecraft.network.syncher.SynchedEntityData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SynchedEntityData.class)
public class SynchedEntityDataMixin {
    @Final
    @Shadow
    private SyncedDataHolder entity;

    @Inject(method = "assignValue", at = @At("TAIL"))
    private <T> void kontext$onAssign(SynchedEntityData.DataItem<T> dataItem, SynchedEntityData.DataValue<?> item, CallbackInfo ci) {
        if (dataItem.getAccessor() != LivingEntityAccessor.kontext$dataHealthId()) return;
        if (!(this.entity instanceof LivingEntityDuck duck)) return;
        if (!(item.value() instanceof Float f)) return;

        duck.kontext$serverHealth(f);
    }
}
