package ru.nikitae57.common.domain

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber


open class StateController<S: Any, E: Any>(initialState: S) {
    @Suppress("PropertyName")
    private val _state: PublishSubject<S> = PublishSubject.create<S>().apply {
        this.onNext(initialState)
    }
    val state: Observable<S> = _state

    protected fun emit(newState: S) {
        Timber.d("Emitting state: $newState")
        _state.onNext(newState)
    }

    open fun addEvent(event: E) {
        Timber.d("Got event: $event")
    }
}