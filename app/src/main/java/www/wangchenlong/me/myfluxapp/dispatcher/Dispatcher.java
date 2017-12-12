package www.wangchenlong.me.myfluxapp.dispatcher;

import com.squareup.otto.Bus;

import www.wangchenlong.me.myfluxapp.actions.Action;
import www.wangchenlong.me.myfluxapp.stores.Store;

/**
 * 调度器
 * <p/>
 * Created by wangchenlong on 15/8/17.
 */
public class Dispatcher {

    private final Bus mBus;  // 事件总线
    private static Dispatcher sInstance;  // 单例

    private Dispatcher(Bus bus) {
        mBus = bus;
    }

    public static Dispatcher getInstance(Bus bus) {
        if (sInstance == null) {
            sInstance = new Dispatcher(bus);
        }
        return sInstance;
    }

    public void register(final Object cls) {
        mBus.register(cls);
    }

    public void unregister(final Object cls) {
        mBus.unregister(cls);
    }

    private void post(final Object event) {
        mBus.post(event);
    }

    // 每个状态改变都需要发送事件, 由视图相应地做出更改
    public void emitChange(Store.StoreChangeEvent o) {
        post(o);
    }

    /**
     * 调度核心函数
     *
     * @param type 调度类型
     * @param data 数据(Key, Value)
     */
    public void dispatch(String type, Object... data) {
        if (type == null || type.isEmpty()) { // 数据空
            throw new IllegalArgumentException("Type must not be empty");
        }
        if (data.length % 2 != 0) { // 非Key-Value
            throw new IllegalArgumentException("Data must be a valid list of key,value pairs");
        }

        Action.Builder actionBuilder = new Action.Builder().with(type);

        int i = 0;
        while (i < data.length) {
            String key = (String) data[i++];
            Object value = data[i++];
            actionBuilder.bundle(key, value); // 放置键值
        }

        // 发送到EventBus
        post(actionBuilder.build());
    }

}
