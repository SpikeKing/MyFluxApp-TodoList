package www.wangchenlong.me.myfluxapp.actions;

import www.wangchenlong.me.myfluxapp.dispatcher.Dispatcher;
import www.wangchenlong.me.myfluxapp.model.Todo;

/**
 * 事件发生器, 向Dispatcher发送事件类型, 和数据字典.
 * <p/>
 * Created by wangchenlong on 15/8/17.
 */
public class ActionsCreator {
    private static ActionsCreator sInstance;
    private final Dispatcher mDispatcher;

    private ActionsCreator(Dispatcher dispatcher) {
        mDispatcher = dispatcher;
    }

    public static ActionsCreator getInstance(Dispatcher dispatcher) {
        if (sInstance == null) {
            sInstance = new ActionsCreator(dispatcher);
        }
        return sInstance;
    }

    public void create(String text) {
        mDispatcher.dispatch(TodoActions.TODO_CREATE, TodoActions.KEY_TEXT, text);
    }

    public void destroy(long id) {
        mDispatcher.dispatch(TodoActions.TODO_DESTROY, TodoActions.KEY_ID, id);
    }

    public void undoDestroy() {
        mDispatcher.dispatch(TodoActions.TODO_UNDO_DESTROY);
    }

    public void toggleComplete(Todo todo) {
        long id = todo.getId();
        String actionType = todo.isComplete() ? TodoActions.TODO_UNDO_COMPLETE : TodoActions.TODO_COMPLETE;
        mDispatcher.dispatch(actionType, TodoActions.KEY_ID, id);
    }

    public void toggleCompleteAll() {
        mDispatcher.dispatch(TodoActions.TODO_TOGGLE_COMPLETE_ALL);
    }

    public void destroyCompleted() {
        mDispatcher.dispatch(TodoActions.TODO_DESTROY_COMPLETED);
    }
}
