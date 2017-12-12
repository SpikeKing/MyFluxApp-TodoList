package www.wangchenlong.me.myfluxapp.actions;

import www.wangchenlong.me.myfluxapp.dispatcher.Dispatcher;
import www.wangchenlong.me.myfluxapp.model.Todo;

/**
 * 行为创建器, 向Dispatcher发送事件类型, 和数据字典.
 * <p/>
 * Created by wangchenlong on 15/8/17.
 */
public class ActionsCreator {
    private static ActionsCreator sInstance;  // 单例
    private final Dispatcher mDispatcher; // 调度器

    private ActionsCreator(Dispatcher dispatcher) {
        mDispatcher = dispatcher;
    }

    // 行为创建器单例
    public static ActionsCreator getInstance(Dispatcher dispatcher) {
        if (sInstance == null) {
            sInstance = new ActionsCreator(dispatcher);
        }
        return sInstance;
    }

    // 创建Item
    public void create(String text) {
        mDispatcher.dispatch(TodoActions.TODO_CREATE, TodoActions.KEY_TEXT, text);
    }

    // 销毁Item
    public void destroy(long id) {
        mDispatcher.dispatch(TodoActions.TODO_DESTROY, TodoActions.KEY_ID, id);
    }

    // 撤销销毁Item
    public void undoDestroy() {
        mDispatcher.dispatch(TodoActions.TODO_UNDO_DESTROY);
    }

    // 转换状态
    public void toggleComplete(Todo todo) {
        long id = todo.getId();
        String actionType = todo.isComplete() ? TodoActions.TODO_UNDO_COMPLETE : TodoActions.TODO_COMPLETE;
        mDispatcher.dispatch(actionType, TodoActions.KEY_ID, id);
    }

    // 全部转换状态
    public void toggleCompleteAll() {
        mDispatcher.dispatch(TodoActions.TODO_TOGGLE_COMPLETE_ALL);
    }

    // 销毁全部选中
    public void destroyCompleted() {
        mDispatcher.dispatch(TodoActions.TODO_DESTROY_COMPLETED);
    }
}
