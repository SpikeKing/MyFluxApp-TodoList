package www.wangchenlong.me.myfluxapp.stores;

import www.wangchenlong.me.myfluxapp.actions.Action;
import www.wangchenlong.me.myfluxapp.dispatcher.Dispatcher;

/**
 * 数据状态
 * <p/>
 * Created by wangchenlong on 15/8/17.
 */
public abstract class Store {
    private final Dispatcher mDispatcher; // 调度器

    protected Store(Dispatcher dispatcher) {
        mDispatcher = dispatcher;
    }

    // 通知改变
    public void emitStoreChange() {
        mDispatcher.emitChange(changeEvent());
    }

    // 改变事件, 由子类重写
    public abstract StoreChangeEvent changeEvent();

    @SuppressWarnings("unused")
    public abstract void onAction(Action action);

    // 存储改变事件
    public interface StoreChangeEvent {
    }
}
