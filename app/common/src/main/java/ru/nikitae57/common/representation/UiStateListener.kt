package ru.nikitae57.common.representation

interface UiStateListener<T: Any> {
    fun listenState(state: T)
}