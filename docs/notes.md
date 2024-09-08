/*

Remember that when using a singleton repository, it's crucial to ensure that the repository doesn't
hold onto any resources that need to be released when the application shuts down. This could include
database connections, file handles, or network connections.

---

https://plugins.jetbrains.com/plugin/7380-adb-idea

---

@Parcelize
data class A(
...
): Parcelable

@Composable
fun f(
state: A
) {}


----

what is LanuchedEffect used in @Composable

in UI viewModel.channel.collect{ event -> ...}

--

try to reduce coupling, even if there is duplication!

- Base classes are super strong coupling!
- hinders experimentation / flexibility
- make things easy to understand for someone new to codebase and easy to test

======================

design suspend fun's to be able to work from main without a problem
= avoid having coroutinescope run within cocoroutinescope

return withContentext(Dispatcher.IO) {
// prevents code from blocking
}

======================

do not usa android / 3rd party speific dependencies in your domain layer
= so that you can reuse classes in local unit tests
= so that you do not need to test in emulator

use cases should only contain business logic?

business logic delongs in domain layer

======================

what is compose LaunchedEffect(...) ?

vararg val args: Any

use Channel

*/

MVI I = Intentaion user has = event classes / function for each screen
= easy event propigation
= uses 1 screen state, per screen
= less thread safe

MVVM VM = business logic + domain layer (for reusable use cases)
= 1 state (pub and private pair) for each component
= setting state is norally thread safe

how can mvvm deal with process death / restore?

above 2 are most common

--

MVC

MVP

Model = data source
View = ui

--

focus on SOLID priciples as these principles mean you can be more flexible
with any architecture implementation
= all about a testable app

======================

cortcoroutine tips:

fun p() {
val scope = coroutineScope(Dispatchers.Default) {
val x = mutableList<Deferred<String>>()
val f = async {
f2()    // a suspend function with a long call
}
x.add(f)

        x.awaitAll()
    }

    scope.cancel()

}

always cancel the scope

/////////////////

how to check a suspend function, using a CoroutineScope, is cancelled?
= use ensureActive()    // throws a cancellationException if the coroutine is cancelled

/////////////////

suspend functions need to be "main thread safe"

= you need to specifid dispatcher on suspend

return withContext(Dispatches.IO) {
true
}

/////////////////

suspend fun f() {
try ...
catch (e: Exception) {
if (e is CancellationExceptipon) {
throw e
}
}
}

///////////////////////////////////////////////////

how to check if compose ui performing OK
= tracking recompositions (redrawn on screen)

Layout Inspector
= select process
= expand tree
= red flash when a recompisition
= expand tree in inspector and see recompisition count & what skipped

///////////////////////////////////////////////////

you can flowN.collect / .collectLatest in a viewmodel (as well as ui stateflow)

///////////////////////////////////////////////////

lazyColumns improvements:

always test release build to see if lag applicable

how to check if coil is caching an image?

use layout inspector to see LazyColumn and scroll in the emulator to see red flashes
= you do not want to see these red flashes (unnecessary compositions)
= mark data class as @stable / @Immutable

///////////////////////////////////////////////////

design functions for "main safety"
= you can even call your function from the main thread!

= only use 1 context in a suspend fun?

= use withContext(Dispatchers.IO) { ... } // prevent blocking and "main safe"

@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

*/
