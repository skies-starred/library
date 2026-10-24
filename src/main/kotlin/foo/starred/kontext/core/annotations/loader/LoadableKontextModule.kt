    package foo.starred.kontext.core.annotations.loader

    @Target(AnnotationTarget.CLASS)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class LoadableKontextModule(val priority: Int = 0)
