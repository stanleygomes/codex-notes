package com.nazarethlabs.codex.helper

object DatabaseHelper {
    private const val SQLITE_DRIVER = "org.sqlite.JDBC"

    @Volatile
    private var driverLoaded = false

    @Synchronized
    fun ensureDriverLoaded() {
        if (!driverLoaded) {
            try {
                Class.forName(SQLITE_DRIVER)
                driverLoaded = true
            } catch (e: ClassNotFoundException) {
                throw RuntimeException("SQLite JDBC driver not found", e)
            }
        }
    }
}
