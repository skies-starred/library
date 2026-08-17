package foo.starred.snowbird.mixin.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import foo.starred.snowbird.handlers.minecraft.AbstractWords;
import foo.starred.snowbird.internal.misc.DonatorWords;
import net.minecraft.client.resources.language.ClientLanguage;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;

@Mixin(ClientLanguage.class)
public abstract class ClientLanguageMixin {
    @Unique
    private static final int[] snowbird$unmodified = new int[512];

    @ModifyReturnValue(method = "getVisualOrder(Lnet/minecraft/network/chat/FormattedText;)Lnet/minecraft/util/FormattedCharSequence;", at = @At("RETURN"))
    private FormattedCharSequence snowbird$getVisualOrder(FormattedCharSequence original, FormattedText text) {
        if (original == null) return null;
        if (!(text instanceof Component component)) return original;

        final String string = component.getString();
        final Style style = component.getStyle();
        final int hash = (string.hashCode() ^ style.hashCode()) & 511;

        final int version = DonatorWords.INSTANCE.getVersion();
        if (snowbird$unmodified[hash] == (string.hashCode() ^ version)) return original;

        final AbstractWords.Companion.Entry entry = DonatorWords.INSTANCE.getEntries()[hash];
        if (entry.version == version && string.equals(entry.string) && Objects.equals(style, entry.style)) {
            return entry.sequence;
        }

        final Component replaced = DonatorWords.INSTANCE.fn(component);
        if (replaced.getString().equals(component.getString()) && replaced.getStyle().equals(component.getStyle())) {
            snowbird$unmodified[hash] = string.hashCode() ^ version;
            return original;
        }

        final FormattedCharSequence sequence = DonatorWords.INSTANCE.fn(original);
        entry.version = version;
        entry.string = string;
        entry.style = style;
        entry.sequence = sequence;
        return sequence;
    }
}