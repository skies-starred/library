@file:Suppress("EmptyRange", "Unused")

package foo.starred.snowbird.api.text.replacer

import foo.starred.snowbird.api.EMPTY_OPTIONAL
import foo.starred.snowbird.utils.literal
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import net.minecraft.util.FormattedCharSequence

abstract class AbstractTextReplacer {
    private var root = Node()
    private var ia = emptyArray<IntArray>()
    private var r0 = emptyArray<String>()
    private var r1 = emptyArray<Component>()
    private var r2 = emptyArray<FormattedCharSequence>()

    val entries = Array(4096) { Entry() }
    var skips: String? = null
    var version: Int = 0

    var map0 = HashMap<String, String>()
        private set

    var map1 = HashMap<String, Component>()
        private set

    var map2 = HashMap<String, FormattedCharSequence>()
        private set

    fun put(key: String, str: String, cmp: Component, seq: FormattedCharSequence = cmp.visualOrderText) {
        val c = skips?.let { s -> cmp.copy().withStyle { it.withInsertion(s) } } ?: cmp

        map0[key] = str
        map1[key] = c
        map2[key] = if (c !== cmp) c.visualOrderText else seq
    }

    fun put(key: String, str: String) {
        put(key, str, str.literal())
    }

    fun remove(key: String) {
        map0.remove(key)
        map1.remove(key)
        map2.remove(key)
    }

    fun build() {
        version++

        val keys = map0.keys.sortedByDescending { it.length }.toTypedArray()
        val n = keys.size

        if (n == 0) {
            root = Node()
            ia = emptyArray()
            r0 = emptyArray()
            r1 = emptyArray()
            r2 = emptyArray()
            return
        }

        ia = Array(n) { keys[it].codePoints().toArray() }
        r0 = Array(n) { map0[keys[it]]!! }
        r1 = Array(n) { map1[keys[it]]!! }
        r2 = Array(n) { map2[keys[it]]!! }

        root = Node()
        val queue = ArrayDeque<Node>(n * 4)

        for (i in 0 until n) {
            val cps = ia[i]
            var cur = root

            for (j in cps.indices) {
                var child = cur.goto.get(cps[j])
                if (child == null) {
                    child = Node()
                    cur.goto.put(cps[j], child)
                }

                cur = child
            }

            cur.output = i
        }

        root.fail = root
        for (child in root.goto.values) {
            child.fail = root
            queue.addLast(child)
        }

        while (queue.isNotEmpty()) {
            val cur = queue.removeFirst()
            val fail = cur.fail!!

            for (entry in cur.goto.int2ObjectEntrySet()) {
                val child = entry.value

                child.fail = fail.goto.get(entry.intKey) ?: root
                if (child.output == -1) child.output = child.fail!!.output

                queue.addLast(child)
            }

            for (entry in fail.goto.int2ObjectEntrySet()) {
                cur.goto.putIfAbsent(entry.intKey, entry.value)
            }
        }
    }

    fun fn(input: String): String {
        if (ia.isEmpty()) return input

        val len = input.length
        if (len == 0) return input

        val sb = StringBuilder(len + 32)
        val b = IntArray(len)
        var bl = 0
        var i = 0
        var state = root

        while (i < len) {
            val cp = input.codePointAt(i)
            state = state.goto.get(cp) ?: root

            b[bl] = cp
            bl++

            if (state.output >= 0) {
                val idx = state.output
                bl -= ia[idx].size

                for (j in 0..<bl) sb.appendCodePoint(b[j])
                sb.append(r0[idx])
                bl = 0
                state = root
            }

            i += Character.charCount(cp)
        }

        for (j in 0..<bl) sb.appendCodePoint(b[j])
        return sb.toString()
    }

    fun fn(input: Component): Component {
        if (ia.isEmpty()) return input

        var chars = IntArray(128)
        val styles = ArrayList<Style>(128)
        var size = 0

        input.visit({ style, str ->
            for (cp in str.codePoints()) {
                if (size >= chars.size) chars = chars.copyOf(chars.size * 2)
                chars[size] = cp
                styles.add(style)
                size++
            }

            EMPTY_OPTIONAL
        }, Style.EMPTY)

        if (size == 0) {
            return input
        }

        val skip = skips
        val bool = skip != null
        val result = "".literal()

        val b = IntArray(size)
        val bs = arrayOfNulls<Style>(size)
        var bl = 0
        var i = 0
        var state = root

        fun flush() {
            var j = 0
            while (j < bl) {
                val style = bs[j]!!
                val sb = StringBuilder()

                while (j < bl && bs[j] === style) {
                    sb.appendCodePoint(b[j])
                    j++
                }

                result.append(sb.toString().literal().withStyle(style))
            }
        }

        var bool1 = false
        while (i < size) {
            if (bool && styles[i].insertion == skip) {
                flush()
                bl = 0
                state = root
                result.append(Character.toString(chars[i]).literal().withStyle(styles[i]))
                i++
                continue
            }

            state = state.goto.get(chars[i]) ?: root

            b[bl] = chars[i]
            bs[bl] = styles[i]
            bl++

            if (state.output >= 0) {
                bool1 = true
                bl -= ia[state.output].size
                flush()
                result.append(r1[state.output])
                bl = 0
                state = root
            }

            i++
        }

        if (!bool1) {
            return input
        }

        flush()
        return result
    }

    fun fn(input: FormattedCharSequence): FormattedCharSequence {
        if (ia.isEmpty()) return input

        var chars = IntArray(128)
        val styles = ArrayList<Style>(128)
        var size = 0

        input.accept { _, style, cp ->
            if (size >= chars.size) chars = chars.copyOf(chars.size * 2)
            chars[size] = cp
            styles.add(style)
            size++
            true
        }

        if (size == 0) return input

        val skip = skips
        val bool = skip != null

        return FormattedCharSequence { sink ->
            val s = IntArray(size)
            val bs = arrayOfNulls<Style>(size)
            var bl = 0
            var i = 0
            var state = root

            while (i < size) {
                if (bool && styles[i].insertion == skip) {
                    for (j in 0..<bl) sink.accept(0, bs[j]!!, s[j])

                    bl = 0
                    state = root

                    sink.accept(0, styles[i], chars[i])
                    i++
                    continue
                }

                state = state.goto.get(chars[i]) ?: root

                s[bl] = chars[i]
                bs[bl] = styles[i]
                bl++

                if (state.output >= 0) {
                    val io = state.output
                    val ml = ia[io].size
                    val ms = bl - ml

                    for (j in 0..<ms) sink.accept(0, bs[j]!!, s[j])

                    val bss = bs[ms]!!
                    r2[io].accept { _, repStyle, repCp ->
                        sink.accept(0, repStyle.applyTo(bss), repCp)
                        true
                    }

                    bl = 0
                    state = root
                }

                i++
            }

            for (j in 0..<bl) sink.accept(0, bs[j]!!, s[j])

            true
        }
    }

    companion object {
        class Entry {
            @JvmField
            var version: Int = -1
            @JvmField
            var string: String? = null
            @JvmField
            var style: Int = 0
            @JvmField
            var sequence: FormattedCharSequence? = null
        }

        private class Node {
            val goto = Int2ObjectOpenHashMap<Node>(4)
            var fail: Node? = null
            var output: Int = -1
        }
    }
}
