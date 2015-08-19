package www.wangchenlong.me.myfluxapp;

import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import www.wangchenlong.me.myfluxapp.actions.ActionsCreator;
import www.wangchenlong.me.myfluxapp.model.Todo;

/**
 * Recycler适配器
 * <p/>
 * Created by wangchenlong on 15/8/17.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private static ActionsCreator sActionsCreator; // 事件
    private List<Todo> mToDos; // 列表项

    public RecyclerAdapter(ActionsCreator actionsCreator) {
        mToDos = new ArrayList<>();
        sActionsCreator = actionsCreator;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        // 填充View
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_row_layout, parent, false);
        return new ViewHolder(v); // 返回绑定的View
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.bindView(mToDos.get(i)); // 绑定数据
    }

    @Override
    public int getItemCount() {
        return mToDos.size(); // 数量
    }

    public void setItems(List<Todo> todos) {
        mToDos = todos;
        notifyDataSetChanged(); // 通知数据集改变
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private Todo mTodo;

        @Bind(R.id.row_text)
        TextView todoText;

        @Bind(R.id.row_checkbox)
        CheckBox todoCheck;

        // Delete按钮的状态和监听(ActionsCreator)
        @OnClick(R.id.row_delete)
        void deleteItem() {
            sActionsCreator.destroy(mTodo.getId());
        }

        // CheckBox的状态和监听(ActionsCreator)
        @OnClick(R.id.row_checkbox)
        void toggleComplete() {
            sActionsCreator.toggleComplete(mTodo);
        }

        // 直接绑定View
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        // 绑定View
        public void bindView(final Todo todo) {

            mTodo = todo;

            if (todo.isComplete()) { // 完成字体进行划线
                SpannableString spannableString = new SpannableString(todo.getText());
                spannableString.setSpan(new StrikethroughSpan(), 0, spannableString.length(), 0);
                todoText.setText(spannableString);
            } else {
                todoText.setText(todo.getText());
            }

            todoCheck.setChecked(todo.isComplete());
        }
    }
}
