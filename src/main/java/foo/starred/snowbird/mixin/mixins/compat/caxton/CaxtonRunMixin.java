package foo.starred.snowbird.mixin.mixins.compat.caxton;

import foo.starred.snowbird.internal.misc.DonatorWords;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import xyz.flirora.caxton.layout.Run;

@Mixin(value = Run.class, remap = false)
public class CaxtonRunMixin {
    @ModifyVariable(method = "splitIntoRuns", at = @At("HEAD"), argsOnly = true)
    private static FormattedCharSequence snowbird$splitIntoRuns(FormattedCharSequence text) {
        return DonatorWords.INSTANCE.fn(text);
    }
}
