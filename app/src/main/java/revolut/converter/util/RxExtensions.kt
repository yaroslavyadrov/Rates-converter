package revolut.converter.util

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.BiFunction


inline fun <T, U, R> Observable<T>.withLatestFrom(
    other: ObservableSource<U>,
    crossinline combiner: (T, U) -> R
): Observable<R> {
    return withLatestFrom(other, BiFunction<T, U, R> { t, u ->
        combiner(t, u)
    })
}