package foo.starred.kontext.core.annotations.impl

import foo.starred.klassgraph.KlassGraph
import foo.starred.kontext.core.annotations.loader.LoadableKontextModule
import foo.starred.kontext.util.safely

object KontextAnnotationLoader {
    private val comparator = compareBy<Class<*>>({ it.getAnnotation(LoadableKontextModule::class.java)?.priority ?: 0 }, { it.name })

    fun init(pkg: String = "foo.starred.kontext") {
        KlassGraph.scan(KontextAnnotationLoader::class.java, pkg).use { result ->
            val list = result.annotated<LoadableKontextModule>().sortedWith(comparator)

            for (klass in list) {
                safely {
                    Class.forName(klass.name)
                }
            }
        }
    }
}
