package foo.starred.snowbird.mixin.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import foo.starred.snowbird.internal.misc.DonatorWords;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;

@Mixin(EditBox.class)
public class EditBoxMixin {
    @Unique
    private FormattedCharSequence snowbird$cached;

    @Unique
    private String snowbird$last;

    @Unique
    private int snowbird$position = -1;

    @Unique
    private int snowbird$version = -1;

    @ModifyReturnValue(method = "applyFormat", at = @At("RETURN"))
    private FormattedCharSequence snowbird$applyFormat(FormattedCharSequence original, @Local(argsOnly = true) String text, @Local(argsOnly = true) int displayPos) {
        final int version = DonatorWords.INSTANCE.getVersion();
        if (displayPos == this.snowbird$position && this.snowbird$version == version && Objects.equals(text, this.snowbird$last) && this.snowbird$cached != null) return this.snowbird$cached;

        this.snowbird$last = text;
        this.snowbird$position = displayPos;
        this.snowbird$version = version;
        return this.snowbird$cached = DonatorWords.INSTANCE.fn(original);
    }
}