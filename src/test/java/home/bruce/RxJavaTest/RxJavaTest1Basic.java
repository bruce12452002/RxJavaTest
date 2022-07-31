package home.bruce.RxJavaTest;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import org.junit.jupiter.api.Test;

//@SpringBootTest
public class RxJavaTest1Basic {

    @Test
    public void testBase() {
        Observable.create(new ObservableOnSubscribe<String>() {
            /**
             * Observer 觀察者
             * Observable 被觀察者，subscribe(Observer) 表示被誰訂閱；
             *                      subscribe(ObservableEmitter) 表示訂閱主體
             * 如果沒有觀察者，被觀察者不會執行
             */
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                // 發送訊息
                emitter.onNext("aaa");
                emitter.onNext("bbb");
                emitter.onNext("ccc");
                // onComplete onError 只執行其一，但如果 onError 寫在後面，接收時的 onError 會收不到
                emitter.onComplete();
                // emitter.onError(new Throwable("我錯了"));
            }
        }).subscribe(new Observer<String>() {
            // 接收訊息
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("Observer.onSubscribe=" + d);
            }

            @Override
            public void onNext(String s) {
                System.out.println("Observer.onNext=" + s);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("Observer.onError=" + e);
            }

            @Override
            public void onComplete() {
                System.out.println("Observer.onComplete");
            }
        });
    }

    @Test
    public void testConsumer1() {
        Disposable disposable = getObservable1().subscribe(new Consumer<String>() { // RxJava 的 Consumer
            @Override
            public void accept(String s) throws Exception {
                System.out.println("accept=" + s);
                // int i = 1 / 0; // 有錯時並不會被被觀察者的 onError 補獲
            }
        });
    }

    @Test
    public void testConsumer2() {
        Disposable disposable = getObservable1().subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println("accept=" + s);
                int i = 1 / 0; // 有錯時會被第二個參數的 Consumer 補獲
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                System.out.println("throwable=" + throwable.getMessage());
            }
        });
    }

    @Test
    public void testConsumer3() {
        Disposable disposable = getObservable2().subscribe(s -> {
            System.out.println("accept=" + s);
            int i = 1 / 0; // 有錯時會被第二個參數的 Consumer 補獲
        }, throwable -> System.out.println("throwable=" + throwable.getMessage()));
    }

    @Test
    public void testJust() {
        // just 只能接收1-10個參數
        Disposable disposable = Observable.just("a", "b", "c")
                .subscribe(s -> System.out.println("just=" + s));
    }

    private Observable<String> getObservable1() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("aaa");
                emitter.onNext("bbb");
                emitter.onNext("ccc");
//                emitter.onError(new Throwable("我錯了"));
                emitter.onComplete();
            }
        });
    }

    private Observable<String> getObservable2() {
        return Observable.create(emitter -> {
            emitter.onNext("aaa");
            emitter.onNext("bbb");
            emitter.onNext("ccc");
//                emitter.onError(new Throwable("我錯了"));
            emitter.onComplete();
        });
    }
}
