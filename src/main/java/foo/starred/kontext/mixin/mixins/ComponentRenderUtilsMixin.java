package foo.starred.kontext.mixin.mixins;

import foo.starred.kontext.core.misc.DonatorTextReplacer;
import net.minecraft.client.gui.components.ComponentRenderUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ComponentRenderUtils.class)
public class ComponentRenderUtilsMixin {
    @ModifyVariable(method = "wrapComponents", at = @At("HEAD"), argsOnly = true)
    private static FormattedText kontext$wrapComponents(FormattedText message) {
        if (!DonatorTextReplacer.enabled.getValue()) return message;
        if (message instanceof Component component) return DonatorTextReplacer.INSTANCE.fn(component);

        return message;
    }
}
