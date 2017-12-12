package www.wangchenlong.me.myfluxapp;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import www.wangchenlong.me.myfluxapp.actions.ActionsCreator;
import www.wangchenlong.me.myfluxapp.dispatcher.Dispatcher;
import www.wangchenlong.me.myfluxapp.stores.TodoStore;

/**
 * 主UI控件: ToDoList, 使用Flux架构
 * Dispatcher调度器, Action事件, Store控制选择
 * View调用Action事件, Action发送给Dispatcher进行调度, Dispatcher发送到EventBus
 * EventBus进行事件分发, 传递给Store, Store处理完成之后, 发送事件StoreChangeEvent,
 * 由EventBus找到主页面View进行更新UI.
 */
@SuppressWarnings("unused")
public class MainActivity extends AppCompatActivity {

    private static Dispatcher sDispatcher; // 调度器
    private static ActionsCreator sActionsCreator; // 活动创建器
    private static TodoStore sTodoStore; // 数据存储器, 存储Todo数据的状态
    private RecyclerAdapter mListAdapter; // 展示数据的列表

    @Bind((R.id.main_layout)) ViewGroup mMainLayout; // 主控件
    @Bind(R.id.main_input) EditText mMainInput; // 编辑控件
    @Bind(R.id.main_list) RecyclerView mMainList; // ListView
    @Bind(R.id.main_checkbox) CheckBox mMainCheck; // 选中按钮

    // 添加Todo项按钮
    @OnClick(R.id.main_add) void addItem() {
        addTodo(); // 添加TodoItem
        resetMainInput(); // 重置输入框
    }

    // 选中Todo项按钮
    @OnClick(R.id.main_checkbox) void checkItem() {
        checkAll(); // 所有Item项改变选中状态
    }

    // 清理完成的Todo事项
    @OnClick(R.id.main_clear_completed) void clearCompletedItems() {
        clearCompleted(); // 清除选中的状态
        resetMainCheck();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // 设置Layout
        ButterKnife.bind(this); // 绑定ButterKnife

        initDependencies(); // 创建Flux的核心管理类

        // 设置RecyclerView
        mMainList.setLayoutManager(new LinearLayoutManager(this));
        mListAdapter = new RecyclerAdapter(sActionsCreator);
        mMainList.setAdapter(mListAdapter);
    }

    // 初始化: Dispatcher调度器, Action事件, Store状态
    private void initDependencies() {
        sDispatcher = Dispatcher.getInstance(new Bus());
        sActionsCreator = ActionsCreator.getInstance(sDispatcher);
        sTodoStore = TodoStore.getInstance(sDispatcher);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 把订阅接口注册到EventBus
        sDispatcher.register(this);
        sDispatcher.register(sTodoStore);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 解除订阅接口
        sDispatcher.unregister(this);
        sDispatcher.unregister(sTodoStore);
    }

    // 添加项, 向ActionsCreator传递输入文本.
    private void addTodo() {
        if (validateInput()) {
            sActionsCreator.create(getInputText());
        }
    }

    // 重置输入框
    private void resetMainInput() {
        mMainInput.setText("");
    }

    // 改变全部项的选中状态(ActionsCreator)
    private void checkAll() {
        sActionsCreator.toggleCompleteAll();
    }

    // 清除全选中的项(ActionsCreator)
    private void clearCompleted() {
        sActionsCreator.destroyCompleted();
    }

    // 重置全选中按钮状态
    private void resetMainCheck() {
        if (mMainCheck.isChecked()) {
            mMainCheck.setChecked(false);
        }
    }

    // 验证输入框是否是空
    private boolean validateInput() {
        return !TextUtils.isEmpty(getInputText());
    }

    // 获取输入数据
    private String getInputText() {
        return mMainInput.getText().toString();
    }

    // 接收事件的改变
    @Subscribe
    public void onTodoStoreChange(TodoStore.TodoStoreChangeEvent event) {
        updateUI();
    }

    // 更新UI, 核心方法
    private void updateUI() {
        // 设置适配器数据, 每次更新TodoStore的状态
        mListAdapter.setItems(sTodoStore.getTodos());

        if (sTodoStore.canUndo()) { // 判断是否可恢复

            // 下面的提示条, 恢复删除, 提示信息
            Snackbar snackbar = Snackbar.make(mMainLayout, "Element deleted", Snackbar.LENGTH_LONG);

            // 恢复按钮
            snackbar.setAction("Undo", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sActionsCreator.undoDestroy();
                }
            });
            snackbar.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
