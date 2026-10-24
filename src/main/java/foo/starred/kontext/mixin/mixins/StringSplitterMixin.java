package foo.starred.kontext.mixin.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import foo.starred.kontext.api.text.replacer.AbstractTextReplacer;
import foo.starred.kontext.core.misc.DonatorTextReplacer;
import net.minecraft.client.StringSplitter;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.Optional;

@Mixin(value = StringSplitter.class, priority = 2000)
public abstract class StringSplitterMixin {
    @Unique
    private static final long[] kontext$widths = new long[4096];

    @Unique
    private static final StringBuilder kontext$sb = new StringBuilder();

    @Unique
    private static int kontext$hash0;

    @Shadow
    public abstract float stringWidth(FormattedCharSequence text);

    @ModifyReturnValue(method = "stringWidth(Lnet/minecraft/network/chat/FormattedText;)F", at = @At("RETURN"))
    private float kontext$stringWidth(float original, FormattedText text) {
        if (text == null) return original;
        if (!DonatorTextReplacer.enabled.getValue()) return original;

        final String string = kontext$extract(text);
        final int hash0 = kontext$hash0;
        final int hash1 = (string.hashCode() ^ hash0) & 4095;

        final int version = DonatorTextReplacer.INSTANCE.getVersion();
        final AbstractTextReplacer.Companion.Entry entry = DonatorTextReplacer.INSTANCE.getEntries()[hash1];

        if (entry.version != version || entry.style != hash0 || !string.equals(entry.string)) {
            return original;
        }

        final int version0 = version ^ string.hashCode() ^ hash0;
        final long packed = kontext$widths[hash1];
        if ((int) (packed >>> 32) == version0 && packed != 0L) {
            return Float.intBitsToFloat((int) packed);
        }

        final float width = this.stringWidth(entry.sequence);
        kontext$widths[hash1] = ((long) version0 << 32) | (Float.floatToIntBits(width) & 0xFFFFFFFFL);
        return width;
    }

    @Unique
    private static String kontext$extract(FormattedText text) {
        if (text instanceof Component component) {
            kontext$hash0 = kontext$hash(component);
            return component.getString();
        }

        kontext$sb.setLength(0);
        kontext$hash0 = 0;

        text.visit((style, str) -> {
            kontext$sb.append(str);
            kontext$hash0 = 31 * kontext$hash0 + kontext$hash(style);
            return Optional.empty();
        }, Style.EMPTY);

        return kontext$sb.toString();
    }

    @Unique
    private static int kontext$hash(Component component) {
        int hash = kontext$hash(component.getStyle());
        final List<Component> siblings = component.getSiblings();

        for (Component sibling : siblings) {
            hash = 31 * hash + kontext$hash(sibling);
        }

        return hash;
    }

    @Unique
    private static int kontext$hash(Style style) {
        if (style.isEmpty()) return 0;
        int flags = (style.isBold() ? 1 : 0) | (style.isItalic() ? 2 : 0) | (style.isUnderlined() ? 4 : 0) | (style.isStrikethrough() ? 8 : 0) | (style.isObfuscated() ? 16 : 0);
        int color = style.getColor() != null ? style.getColor().getValue() : -1;
        return flags ^ (color * 31);
    }
}
