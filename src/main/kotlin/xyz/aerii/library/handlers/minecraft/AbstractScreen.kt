@file:Suppress("Unused")

package xyz.aerii.library.handlers.minecraft

//? < 1.21.11
//import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.input.CharacterEvent
import net.minecraft.client.input.KeyEvent
import net.minecraft.client.input.MouseButtonEvent
import xyz.aerii.library.api.nextTick
import xyz.aerii.library.utils.literal

abstract class AbstractScreen(
    title: String = "Scram [Solstice]"
) : Screen(title.literal()) {
    open fun onScramInit() {}

    open fun onScramClose() {}

    open fun onScramResize(width: Int, height: Int) {}

    open fun onScramRender(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, delta: Float) {}

    open fun onScramMouseClick(mouseX: Int, mouseY: Int, button: Int): Boolean = false

    open fun onScramMouseRelease(mouseX: Int, mouseY: Int, button: Int): Boolean = false

    open fun onScramMouseScroll(mouseX: Int, mouseY: Int, horizontal: Double, vertical: Double): Boolean = false

    open fun onScramKeyPress(keyCode: Int, scanCode: Int, modifiers: Int): Boolean = false

    open fun onScramKeyRelease(keyCode: Int, scanCode: Int, modifiers: Int): Boolean = false

    open fun onScramCharType(char: Char): Boolean = false

    override fun init() {
        onScramInit()
        super.init()
    }

    override fun onClose() {
        onScramClose()
        super.onClose()
    }

    override fun resize(/*? < 1.21.11 { *//*minecraft: Minecraft, *//*? }*/width: Int, height: Int) {
        onScramResize(width, height)
        super.resize(/*? < 1.21.11 { *//*minecraft, *//*? } */width, height)
    }

    //? >= 26.1 {
    override fun extractRenderState(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, delta: Float) {
        onScramRender(graphics, mouseX, mouseY, delta)
        super.extractRenderState(graphics, mouseX, mouseY, delta)
    }
    //? } else {
    /*override fun render(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, delta: Float) {
        onScramRender(graphics, mouseX, mouseY, delta)
        super.render(graphics, mouseX, mouseY, delta)
    }
    *///? }

    override fun mouseClicked(click: MouseButtonEvent, doubled: Boolean): Boolean {
        return if (onScramMouseClick(click.x.toInt(), click.y.toInt(), click.button())) true else super.mouseClicked(click, doubled)
    }

    override fun mouseReleased(click: MouseButtonEvent): Boolean {
        return if (onScramMouseRelease(click.x.toInt(), click.y.toInt(), click.button())) true else super.mouseReleased(click)
    }

    override fun keyPressed(keyEvent: KeyEvent): Boolean {
        return if (onScramKeyPress(keyEvent.key(), keyEvent.scancode(), keyEvent.modifiers())) true else super.keyPressed(keyEvent)
    }

    override fun keyReleased(keyEvent: KeyEvent): Boolean {
        return if (onScramKeyRelease(keyEvent.key(), keyEvent.scancode(), keyEvent.modifiers())) true else super.keyReleased(keyEvent)
    }

    override fun charTyped(characterEvent: CharacterEvent): Boolean {
        return if (onScramCharType(characterEvent.codepoint().toChar())) true else super.charTyped(characterEvent)
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, horizontal: Double, vertical: Double): Boolean {
        return if (onScramMouseScroll(mouseX.toInt(), mouseY.toInt(), horizontal, vertical)) true else super.mouseScrolled(mouseX, mouseY, horizontal, vertical)
    }

    fun open() {
        nextTick { setScreen(this@AbstractScreen) }
    }
}