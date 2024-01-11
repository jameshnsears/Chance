/*

Remember that when using a singleton repository, it's crucial to ensure that the repository doesn't
hold onto any resources that need to be released when the application shuts down. This could include
database connections, file handles, or network connections.

---

https://plugins.jetbrains.com/plugin/7380-adb-idea

---

    TabBagViewModel = settingsRepository, bagRepository
        DialogBagAndroidViewModel = settingsRepository, bagRepository
        ZoomBagViewModel = settingsRepository, bagRepository

    TabRollViewModel = settingsRepository, rollRepository
        ZoomRollViewModel = settingsRepository, rollRepository

*/
