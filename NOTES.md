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

*/
