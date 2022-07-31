package home.bruce.RxJavaTest;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import org.junit.jupiter.api.Test;

public class RxJavaTest3Map {
    @Test
    public void testMap() {
        Disposable disposable = Observable.fromArray("java", "456", "c++", "python", "123", "go")
                .map(String::length)
                .subscribe(i -> System.out.println("map=" + i));
    }

    @Test
    public void testFlatMap() {
        Disposable disposable = Observable.fromArray("java", "456", "c++", "python", "123", "go")
                .flatMap(new Function<String, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(String s) throws Exception {
                        return Observable.just(s);
                    }
                })
                .subscribe(s -> System.out.println("flatMap=" + s));
    }

    /**
     * flatMap無序 concatMap有序，等有時間研究再驗證
     */
    @Test
    public void testConcatMap() {
        Disposable disposable = Observable.fromArray("java", "456", "c++", "python", "123", "go")
                .concatMap(new Function<String, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(String s) throws Exception {
                        return Observable.just(s);
                    }
                })
                .subscribe(s -> System.out.println("concatMap=" + s));
    }

    @Test
    public void testBuffer() {
        Disposable disposable = Observable.fromArray("java", "456", "c++", "python", "123", "go")
                .buffer(4)// 將訊息幾個為一組發送，最後一次將剩餘的發送
                .subscribe(s -> System.out.println("buffer=" + s));
    }

    @Test
    public void testConcat() {
        // concat 可以用2-4個參數
        Disposable disposable = Observable.concat(Observable.just("a"), Observable.just("b"), Observable.just("c"))
                .subscribe(s -> System.out.println("concat=" + s));
    }

    @Test
    public void testConcatArray() {
        // concatArray 可以用多個參數
        Disposable disposable = Observable.concatArray(
                        Observable.just("a"), Observable.just("b"), Observable.just("c"))
                .subscribe(s -> System.out.println("concatArray=" + s));
    }

    @Test
    public void testMerge() {
        // concatArray串行 merge併行
        Disposable disposable = Observable.merge(
                        Observable.just("a"), Observable.just("b"), Observable.just("c"))
                .subscribe(s -> System.out.println("merge=" + s));
    }
}
