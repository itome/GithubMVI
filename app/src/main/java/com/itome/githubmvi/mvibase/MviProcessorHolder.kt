package com.itome.githubmvi.mvibase

import io.reactivex.Observable
import io.reactivex.ObservableTransformer

abstract class MviProcessorHolder<A : MviAction, R : MviResult> {

    abstract val actionProcessor: ObservableTransformer<A, R>

    protected fun <T : A, F : R> createProcessor(
        processorFunction: (T) -> Observable<F>
    ): ObservableTransformer<T, F> =
        ObservableTransformer { actions ->
            actions.flatMap(processorFunction)
        }
}