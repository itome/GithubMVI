package com.itome.githubmvi.mvibase

import com.itome.githubmvi.extensions.ofType
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import kotlin.reflect.KClass

abstract class MviProcessorHolder<A : MviAction, R : MviResult> {

    abstract val actionProcessor: ObservableTransformer<A, R>

    protected fun <T : A, F : R> createProcessor(
        processorFunction: (T) -> Observable<F>
    ): ObservableTransformer<T, F> =
        ObservableTransformer { actions ->
            actions.flatMap(processorFunction)
        }

    @Suppress("UNCHECKED_CAST")
    protected fun mergeProcessor(
        vararg processors: Pair<ObservableTransformer<out A, out R>, KClass<out A>>
    ): ObservableTransformer<A, R> {
        return ObservableTransformer { actions ->
            actions.publish { shared ->
                Observable.merge<R>(
                    processors.map { (processor, clazz) ->
                        shared.ofType(clazz)
                            .compose(processor as ObservableTransformer<in Any, out R>)
                    }
                ).mergeWith(
                    shared.filter({ v ->
                        processors.all { (_, clazz) -> v::class != clazz }
                    }).flatMap { w ->
                        Observable.error<R>(
                            IllegalArgumentException("Unknown Action type: $w")
                        )
                    }
                )
            }
        }
    }
}