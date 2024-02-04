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

--

sealed interface X {
data class A
}

--

ViewModel...

protected val _state = MutableStatEflow(this.defaultState())
val state = _state.asStateFlow()

private val _channel = Channel<UIEvent>()
val channel = _channel.receiveAsFlow()

fun sendEvent(e: UIEvent) {
viewModelScope.launch {
_channel.send(e)
}
}

fun f() {
try {
_state.update { it.copy(

        )}
    } catch (e: Exception) {
        sendEvent(UIEvent(...))
    }

}

--

what is LanuchedEffect used in @Composable

in UI viewModel.channel.collect{ event -> ...}

--

try to reduce coupling, even if there is duplication!

- Base classes are super strong coupling!
- hinders experimentation / flexibility
- make things easy to understand for someone new to codebase and easy to test

======================

// destructuring

val allUsers List<User> = ...
val (isOnline, isOffline) = allUsers.partition { it.isOnline }

isOnline.forEach { user ->
user.sendMessage(...)
}

val ma p mapOf<String, Int>
map.forEach { (key, value) ->
key...
value...
}


// property delegate == overider getter / setter == inhertit ReadWriteProperty class
: ReadWriteProperty<Any?, String>

======================

design suspend fun's to be able to work from main without a problem
= avoid having coroutinescope run within cocoroutinescope
= IDE tells use if suspend being used or not

return withContentext(Dispatcher.IO) {
// prevents code from blocking
}

======================

what is compose LaunchedEffect(...) ?

vararg val args: Any

use Channel

*/
