package foo.starred.kontext.api.version.impl

import foo.starred.kontext.api.version.data.KontextVersionType

data class KontextVersion(val raw: String) : Comparable<KontextVersion> {
    val major: Int
    val minor: Int
    val patch: Int

    val type: KontextVersionType
    val stage: Int

    init {
        val version = raw.trim().removePrefix("v").removePrefix("V")
        val main = version.substringBefore('+')

        val index = main.indexOf('-')
        val core = if (index == -1) main else main.substring(0, index)
        val pre = if (index == -1) "" else main.substring(index + 1)

        val numbers = core.split('.').map { it.toIntOrNull() ?: 0 }
        major = numbers.getOrElse(0) { 0 }
        minor = numbers.getOrElse(1) { 0 }
        patch = numbers.getOrElse(2) { 0 }

        type = if (pre.isEmpty()) KontextVersionType.RELEASE else KontextVersionType.get(pre.takeWhile { !it.isDigit() }.lowercase().removeSuffix("."))
        stage = pre.dropWhile { !it.isDigit() }.takeWhile { it.isDigit() }.toIntOrNull() ?: 0
    }

    override fun compareTo(other: KontextVersion): Int {
        if (major != other.major) return major.compareTo(other.major)
        if (minor != other.minor) return minor.compareTo(other.minor)
        if (patch != other.patch) return patch.compareTo(other.patch)
        if (type.weight != other.type.weight) return type.weight.compareTo(other.type.weight)

        return stage.compareTo(other.stage)
    }

    override fun toString(): String {
        return raw
    }
}
