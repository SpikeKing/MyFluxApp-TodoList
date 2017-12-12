package www.wangchenlong.me.myfluxapp.stores;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import www.wangchenlong.me.myfluxapp.dispatcher.Dispatcher;
import www.wangchenlong.me.myfluxapp.actions.Action;
import www.wangchenlong.me.myfluxapp.actions.TodoActions;
import www.wangchenlong.me.myfluxapp.model.Todo;

/**
 * 状态类, 主要处理所有Todo列表事件的状态, 提供各种操作.
 * <p/>
 * Created by wangchenlong on 15/8/17.
 */
public class TodoStore extends Store {

    private static TodoStore sInstance; // 单例
    private final List<Todo> mTodos; // 数据列表
    private Todo lastDeleted; // 最近一次删除数据

    private TodoStore(Dispatcher dispatcher) {
        super(dispatcher);
        mTodos = new ArrayList<>();
    }

    public static TodoStore getInstance(Dispatcher dispatcher) {
        if (sInstance == null) {
            sInstance = new TodoStore(dispatcher);
        }
        return sInstance;
    }

    // 获取数据
    public List<Todo> getTodos() {
        return mTodos;
    }

    // 恢复
    public boolean canUndo() {
        return lastDeleted != null;
    }

    @Override @Subscribe
    public void onAction(Action action) {
        long id;  // 待处理ID
        switch (action.getType()) {
            case TodoActions.TODO_CREATE:
                String text = ((String) action.getData().get(TodoActions.KEY_TEXT));
                create(text);
                emitStoreChange(); // 发生改变事件
                break;

            case TodoActions.TODO_DESTROY:
                id = ((long) action.getData().get(TodoActions.KEY_ID));
                destroy(id);
                emitStoreChange();
                break;

            case TodoActions.TODO_UNDO_DESTROY:
                undoDestroy();
                emitStoreChange();
                break;

            case TodoActions.TODO_COMPLETE:
                id = ((long) action.getData().get(TodoActions.KEY_ID));
                updateComplete(id, true);
                emitStoreChange();
                break;

            case TodoActions.TODO_UNDO_COMPLETE:
                id = ((long) action.getData().get(TodoActions.KEY_ID));
                updateComplete(id, false);
                emitStoreChange();
                break;

            case TodoActions.TODO_DESTROY_COMPLETED:
                destroyCompleted();
                emitStoreChange();
                break;

            case TodoActions.TODO_TOGGLE_COMPLETE_ALL:
                updateCompleteAll();
                emitStoreChange();
                break;
        }
    }

    // 删除全部完成事项
    private void destroyCompleted() {
        Iterator<Todo> iter = mTodos.iterator();
        while (iter.hasNext()) {
            Todo todo = iter.next();
            if (todo.isComplete()) {
                iter.remove();
            }
        }
    }

    // 反转全部事项的状态
    private void updateCompleteAll() {
        if (areAllComplete()) {
            updateAllComplete(false);
        } else {
            updateAllComplete(true);
        }
    }

    // 判断是否全部选中完成
    private boolean areAllComplete() {
        for (Todo todo : mTodos) {
            if (!todo.isComplete()) {
                return false;
            }
        }
        return true;
    }

    // 更新全部状态
    private void updateAllComplete(boolean complete) {
        for (Todo todo : mTodos) {
            todo.setComplete(complete);
        }
    }

    // 更新ID的完成状态
    private void updateComplete(long id, boolean complete) {
        Todo todo = getById(id);
        if (todo != null) {
            todo.setComplete(complete);
        }
    }

    // 撤销删除的事项
    private void undoDestroy() {
        if (lastDeleted != null) {
            addElement(lastDeleted.clone());
            lastDeleted = null;
        }
    }

    // 根据Text创建事项，并且排序
    private void create(String text) {
        long id = System.currentTimeMillis();
        Todo todo = new Todo(id, text);
        addElement(todo);
        Collections.sort(mTodos);
    }

    // 根据ID删除事项，并且放入最后删除项
    private void destroy(long id) {
        Iterator<Todo> iter = mTodos.iterator();
        while (iter.hasNext()) {
            Todo todo = iter.next();
            if (todo.getId() == id) {
                lastDeleted = todo.clone();
                iter.remove();
                break;
            }
        }
    }

    // 获取根据ID获取事项
    private Todo getById(long id) {
        Iterator<Todo> iter = mTodos.iterator();
        while (iter.hasNext()) {
            Todo todo = iter.next();
            if (todo.getId() == id) {
                return todo;
            }
        }
        return null;
    }

    // 添加数据进入列表
    private void addElement(Todo clone) {
        mTodos.add(clone);
        Collections.sort(mTodos);
    }

    @Override
    public StoreChangeEvent changeEvent() {
        return new TodoStoreChangeEvent();
    }

    public class TodoStoreChangeEvent implements StoreChangeEvent {
    }
}
