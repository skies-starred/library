package xyz.aerii.library.mixin.mixins;

import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import xyz.aerii.library.internal.misc.DonatorWords;

@Mixin(Font.class)
public class FontMixin {
    @ModifyVariable(method = "prepareText(Ljava/lang/String;FFIZI)Lnet/minecraft/client/gui/Font$PreparedText;", at = @At("HEAD"), argsOnly = true)
    private String library$prepareText$string(String text) {
        return DonatorWords.INSTANCE.fn(text);
    }

    //~ if >= 1.21.11 'FFIZI' -> 'FFIZZI'
    @ModifyVariable(method = "prepareText(Lnet/minecraft/util/FormattedCharSequence;FFIZZI)Lnet/minecraft/client/gui/Font$PreparedText;", at = @At("HEAD"), argsOnly = true)
    private FormattedCharSequence library$prepareText$sequence(FormattedCharSequence text) {
        return DonatorWords.INSTANCE.fn(text);
    }

    @ModifyVariable(method = "width(Lnet/minecraft/util/FormattedCharSequence;)I", at = @At("HEAD"), argsOnly = true)
    private FormattedCharSequence library$width$sequence(FormattedCharSequence text) {
        return DonatorWords.INSTANCE.fn(text);
    }

    @ModifyVariable(method = "width(Lnet/minecraft/network/chat/FormattedText;)I", at = @At("HEAD"), argsOnly = true)
    private FormattedText library$width$text(FormattedText text) {
        return text instanceof Component ? DonatorWords.INSTANCE.fn((Component) text) : text;
    }

    @ModifyVariable(method = "width(Ljava/lang/String;)I", at = @At("HEAD"), argsOnly = true)
    private String library$width$string(String str) {
        return DonatorWords.INSTANCE.fn(str);
    }
}