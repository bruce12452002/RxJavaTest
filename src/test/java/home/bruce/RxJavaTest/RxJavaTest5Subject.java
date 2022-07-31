package home.bruce.RxJavaTest;

import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.*;
import org.junit.jupiter.api.Test;

public class RxJavaTest5Subject {
    /**
     * Subject 是觀察者也是被觀察者
     */
    @Test
    public void testAsyncSubject() {
        handle(AsyncSubject.create()); // f 無論是訂閱前或後，只會收到最後一條消息
    }

    @Test
    public void testBehaviorSubject() {
        handle(BehaviorSubject.create()); // c d e f 能收到訂閱前的一條消息和訂閱後的所有消息
    }

    @Test
    public void testPublishSubject() {
        handle(PublishSubject.create()); // d e f 只會收到訂閱後的消息
    }

    @Test
    public void testReplaySubject() {
        handle(ReplaySubject.create()); // a b c d e f 無論是訂閱前或後，都會收到，可以有多個觀察者
    }

    @Test
    public void testUnicastSubject() {
        handle(UnicastSubject.create()); // a b c d e f 無論是訂閱前或後，都會收到，但只能一個觀察者
    }

    private void handle(Subject<String> subject) {
        subject.onNext("a");
        subject.onNext("b");
        subject.onNext("c");
        Disposable disposable = subject.subscribe(System.out::print);
//        Disposable disposable2 = subject.subscribe(System.out::print);
        subject.onNext("d");
        subject.onNext("e");
        subject.onNext("f");
        subject.onComplete();
        if (!disposable.isDisposed()) disposable.dispose(); // 取消訂閱
//      if (!disposable2.isDisposed()) disposable2.dispose();
    }

}
