package home.bruce.RxJavaTest;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RxJavaTest2From {
    @Test
    public void testFromArray() {
        // fromArray 接收 T...，所以可以傳多個，just 也是調用 fromArray
        Disposable disposable = Observable.fromArray("a", "b", "c")
                .subscribe(s -> System.out.println("fromArray=" + s));
    }

    @Test
    public void testFromIterable() {
        // 傳入集合
        List<String> list = Arrays.asList("a", "b", "c");
        Disposable disposable = Observable.fromIterable(list)
                .subscribe(s -> System.out.println("fromIterable=" + s));
    }

    @Test
    public void testFromFuture() {
        Disposable disposable = Observable.fromFuture(new Future<String>() {
                    @Override
                    public boolean cancel(boolean mayInterruptIfRunning) {
                        return false;
                    }

                    @Override
                    public boolean isCancelled() {
                        return false;
                    }

                    @Override
                    public boolean isDone() {
                        return false;
                    }

                    @Override
                    public String get() throws InterruptedException, ExecutionException {
                        return "xxxxxxxxxxxxx";
                    }

                    @Override
                    public String get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                        return null;
                    }
                })
                .subscribe(s -> System.out.println("fromFuture=" + s));
    }

    @Test
    public void testFromCallable() {
        Disposable disposable = Observable.fromCallable(() -> "ooooooo")
                .subscribe(s -> System.out.println("fromCallable=" + s));
    }

    @Test
    public void testFromPublisher() {
        Disposable disposable = Observable.fromPublisher(
                        (Subscriber<? super String> subscriber) -> {
                            subscriber.onNext("111");
                            subscriber.onNext("222");
                            subscriber.onNext("333");
                        })
                .subscribe(s -> System.out.println("fromPublisher=" + s));
    }
}
