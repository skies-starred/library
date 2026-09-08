package foo.starred.snowbird.mixin.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import foo.starred.snowbird.internal.misc.DonatorTextReplacer;
import net.minecraft.client.gui.components.ComponentRenderUtils;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(ComponentRenderUtils.class)
public class ComponentRenderUtilsMixin {
    @ModifyReturnValue(method = "wrapComponents", at = @At("RETURN"))
    private static List<FormattedCharSequence> snowbird$wrapComponents(List<FormattedCharSequence> original) {
        if (!DonatorTextReplacer.enabled.getValue()) return original;

        original.replaceAll(DonatorTextReplacer.INSTANCE::fn);
        return original;
    }
}
