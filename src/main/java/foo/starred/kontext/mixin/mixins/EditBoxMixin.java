package foo.starred.kontext.mixin.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import foo.starred.kontext.core.misc.DonatorTextReplacer;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;

@Mixin(EditBox.class)
public class EditBoxMixin {
    @Unique
    private FormattedCharSequence kontext$cached;

    @Unique
    private String kontext$last;

    @Unique
    private int kontext$position = -1;

    @Unique
    private int kontext$version = -1;

    @ModifyReturnValue(method = "applyFormat", at = @At("RETURN"))
    private FormattedCharSequence kontext$applyFormat(FormattedCharSequence original, @Local(argsOnly = true) String text, @Local(argsOnly = true) int displayPos) {
        if (!DonatorTextReplacer.enabled.getValue()) return original;

        final int version = DonatorTextReplacer.INSTANCE.getVersion();
        if (displayPos == this.kontext$position && this.kontext$version == version && Objects.equals(text, this.kontext$last) && this.kontext$cached != null) return this.kontext$cached;

        this.kontext$last = text;
        this.kontext$position = displayPos;
        this.kontext$version = version;

        final FormattedCharSequence replaced = DonatorTextReplacer.INSTANCE.fn(original);
        if (replaced == original) return this.kontext$cached = original;

        return this.kontext$cached = replaced;
    }
}
