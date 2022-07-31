package home.bruce.RxJavaTest;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

public class RxJavaTest4Thread {
    @Test
    public void test() {
        Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                        System.out.println("subscribe=" + Thread.currentThread().getName());
                        TimeUnit.SECONDS.sleep(5);
                        emitter.onNext("aaa");
                        emitter.onNext("bbb");
                        emitter.onNext("ccc");
                        emitter.onComplete();
                    }
                })
                /* subscribeOn
                 * 使用執行緒來執行上面的發送訊息的方法
                 *
                 *  Schedulers.newThread() 創建新執行緒
                 *  Schedulers.io() 使用執行緒池創建執行緒，讀寫檔案、讀寫資料庫、網路用這個
                 *  Schedulers.computation() CPU 大量計算可用這個
                 *
                 * 只有最上層的 subscribeOn 才會生效
                 * observeOn 往下執行，如果下面還有 observeOn，可以產生新的執行緒
                 * 如 Schedulers.io()) 後可接 map，然後又 subscribeOn，但上層已有就不會生效
                 */
                .subscribeOn(Schedulers.io())

                /* observeOn
                 * 影響觀察者的執行緒，如果沒有 observeOn 就是 subscribeOn 指定的執行緒
                 * 再沒有 subscribeOn，那就是 main 執行緒
                 */
                .observeOn(Schedulers.newThread())
                // .compose(new MyObservableTransformer<>()) 將多個被觀察者組合在一起
                .subscribe(new Observer<String>() {
                    // 接收訊息
                    @Override
                    public void onSubscribe(Disposable d) {
                        // main 執行緒
                        System.out.println("onSubscribe=" + Thread.currentThread().getName());
                    }

                    @Override
                    public void onNext(String s) {
                        System.out.println("onNext=" + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError=" + Thread.currentThread().getName());
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete=" + Thread.currentThread().getName());
                    }
                });

        /*
         * 由於上面睡5秒，這裡要比上面的久才行，這樣才會執行 onNext onError/onComplete
         * 此時 main 執行緒不會阻塞
         */
        try {
            TimeUnit.SECONDS.sleep(8);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
