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
            val i0 = ia[i]
            var i1 = root

            for (j in i0.indices) {
                var child = i1.goto.get(i0[j])
                if (child == null) {
                    child = Node()
                    i1.goto.put(i0[j], child)
                }

                i1 = child
            }

            i1.output = i
        }

        root.fail = root
        for (child in root.goto.values) {
            child.fail = root
            queue.addLast(child)
        }

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            val fail = current.fail!!

            for (entry in current.goto.int2ObjectEntrySet()) {
                val child = entry.value

                child.fail = fail.goto.get(entry.intKey) ?: root
                if (child.output == -1) child.output = child.fail!!.output

                queue.addLast(child)
            }

            for (entry in fail.goto.int2ObjectEntrySet()) {
                current.goto.putIfAbsent(entry.intKey, entry.value)
            }
        }
    }

    fun fn(input: String): String {
        if (ia.isEmpty()) return input

        val len = input.length
        if (len == 0) return input

        val builder = StringBuilder(len + 32)
        val array = IntArray(len)
        var i0 = 0
        var i1 = 0

        var state = root
        while (i1 < len) {
            val cp = input.codePointAt(i1)
            state = state.goto.get(cp) ?: root

            array[i0] = cp
            i0++

            if (state.output >= 0) {
                val idx = state.output
                i0 -= ia[idx].size

                for (i2 in 0..<i0) {
                    builder.appendCodePoint(array[i2])
                }

                builder.append(r0[idx])
                i0 = 0
                state = root
            }

            i1 += Character.charCount(cp)
        }

        for (j in 0..<i0) {
            builder.appendCodePoint(array[j])
        }

        return builder.toString()
    }

    fun fn(input: Component): Component {
        if (ia.isEmpty()) return input

        var chars = IntArray(128)
        val styles = ArrayList<Style>(128)
        var size = 0

        input.visit({ style, str ->
            for (i in str.codePoints()) {
                if (size >= chars.size) chars = chars.copyOf(chars.size * 2)

                chars[size] = i
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

        val array0 = IntArray(size)
        val array1 = arrayOfNulls<Style>(size)
        var i0 = 0
        var i1 = 0

        fun flush() {
            var j = 0
            while (j < i0) {
                val style = array1[j]!!
                val sb = StringBuilder()

                while (j < i0 && array1[j] === style) {
                    sb.appendCodePoint(array0[j])
                    j++
                }

                result.append(sb.toString().literal().withStyle(style))
            }
        }

        var state = root
        var bool1 = false
        while (i1 < size) {
            if (bool && styles[i1].insertion == skip) {
                flush()
                i0 = 0
                state = root
                result.append(Character.toString(chars[i1]).literal().withStyle(styles[i1]))
                i1++
                continue
            }

            state = state.goto.get(chars[i1]) ?: root

            array0[i0] = chars[i1]
            array1[i0] = styles[i1]
            i0++

            if (state.output >= 0) {
                bool1 = true
                i0 -= ia[state.output].size
                flush()
                result.append(r1[state.output])
                i0 = 0
                state = root
            }

            i1++
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
            val array0 = IntArray(size)
            val array1 = arrayOfNulls<Style>(size)
            var i0 = 0
            var i1 = 0
            var i2 = 0

            var state = root
            while (i1 < size) {
                if (bool && styles[i1].insertion == skip) {
                    for (j in 0..<i0) {
                        sink.accept(i2++, array1[j]!!, array0[j])
                    }

                    i0 = 0
                    state = root

                    sink.accept(i2++, styles[i1], chars[i1])
                    i1++
                    continue
                }

                state = state.goto.get(chars[i1]) ?: root
                array0[i0] = chars[i1]
                array1[i0] = styles[i1]
                i0++

                if (state.output >= 0) {
                    val i3 = state.output
                    val i4 = ia[i3].size
                    val i5 = i0 - i4

                    for (i6 in 0..<i5) {
                        sink.accept(i2++, array1[i6]!!, array0[i6])
                    }

                    val style1 = array1[i5]!!
                    r2[i3].accept { _, style, codepoint ->
                        sink.accept(i2++, style.applyTo(style1), codepoint)
                    }

                    i0 = 0
                    state = root
                }

                i1++
            }

            for (i4 in 0..<i0) {
                sink.accept(i2++, array1[i4]!!, array0[i4])
            }

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
