package foo.starred.kontext.abstraction.gui

import foo.starred.kontext.abstraction.client.extension.client
import foo.starred.kontext.abstraction.client.extension.nextTick
import foo.starred.kontext.abstraction.client.impl.MinecraftClient
import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.world.inventory.ContainerInput

object MinecraftGui {
    val instance: Gui
        get() = MinecraftClient.instance.gui

    val instanceNullable: Gui?
        get() = MinecraftClient.instance.gui

    val screen: Screen?
        get() {
            //? if >= 26.2 {
            return instanceNullable?.screen()
            //? } else {
            /*return MinecraftClient.instance.screen
            *///? }
        }

    val container: AbstractContainerScreen<*>?
        get() = screen as? AbstractContainerScreen<*>

    fun screen(screen: Screen) {
        nextTick {
            //? if >= 26.2 {
            instanceNullable?.setScreen(screen)
            //? } else {
            /*MinecraftClient.instance.setScreen(screen)
            *///? }
        }
    }

    fun click(id: Int, index: Int, button: Int = 0, clickType: ContainerInput = ContainerInput.PICKUP) {
        val player = client.player ?: return
        client.gameMode?.handleContainerInput(id, index, button, clickType, player)
    }
}
