package foo.starred.kontext.api.storage.impl.profile.data

data class ProfileContainer<T>(
    var active: String = "Default",
    val profiles: MutableMap<String, T> = mutableMapOf()
)

