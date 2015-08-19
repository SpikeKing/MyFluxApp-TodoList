package www.wangchenlong.me.myfluxapp.actions;

/**
 * 事件明细
 * <p/>
 * Created by wangchenlong on 15/8/17.
 */
public interface TodoActions {
    // 事件类型
    String TODO_CREATE = "todo-create";
    String TODO_COMPLETE = "todo-complete";
    String TODO_DESTROY = "todo-destroy";
    String TODO_DESTROY_COMPLETED = "todo-destroy-completed";
    String TODO_TOGGLE_COMPLETE_ALL = "todo-toggle-complete-all";
    String TODO_UNDO_COMPLETE = "todo-undo-complete";
    String TODO_UNDO_DESTROY = "todo-undo-destroy";

    // 参数字典Key
    String KEY_TEXT = "key-text";
    String KEY_ID = "key-id";
}
