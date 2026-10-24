package foo.starred.kontext.api.version.data

enum class KontextVersionType(val weight: Int, val prefix: Set<String>) {
    UNKNOWN(0, setOf()),
    ALPHA(1, setOf("a", "alpha")),
    BETA(2, setOf("b", "beta")),
    CANDIDATE(3, setOf("rc")),
    RELEASE(4, setOf("")),
    REVISION(5, setOf("r", "revision"));

    companion object {
        fun get(string: String): KontextVersionType {
            val string = string.lowercase()
            return entries.firstOrNull { string in it.prefix } ?: UNKNOWN
        }
    }
}
