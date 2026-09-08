package foo.starred.snowbird.mixin.mixins.compat.caxton;

import foo.starred.snowbird.internal.misc.DonatorTextReplacer;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import xyz.flirora.caxton.layout.Run;

@Mixin(value = Run.class, remap = false)
public class CaxtonRunMixin {
    @ModifyVariable(method = "splitIntoRuns", at = @At("HEAD"), argsOnly = true)
    private static FormattedCharSequence snowbird$splitIntoRuns(FormattedCharSequence text) {
        if (!DonatorTextReplacer.enabled.getValue()) return text;

        return DonatorTextReplacer.INSTANCE.fn(text);
    }
}
