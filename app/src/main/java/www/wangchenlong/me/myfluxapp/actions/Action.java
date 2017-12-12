package www.wangchenlong.me.myfluxapp.actions;

import java.util.HashMap;

/**
 * Action(固定样式)事件, 主要由类型和数据, 两部分组成
 * 通过其中的静态类Builder逐个添加Key-Value进行构建.
 * <p/>
 * Created by wangchenlong on 15/8/17.
 */
public class Action {
    private final String mType; // 类型
    private final HashMap<String, Object> mData;

    public Action(String type, HashMap<String, Object> data) { // 数据
        mType = type;
        mData = data;
    }

    public String getType() {
        return mType;
    }

    public HashMap<String, Object> getData() {
        return mData;
    }

    // Builder的构造器模式
    public static class Builder {
        private String mType; // 类型
        private HashMap<String, Object> mData; // 数据

        // 通过类型创建Builder
        public Builder with(String type) {
            if (type == null) {
                throw new IllegalArgumentException("Type may not be null.");
            }
            mType = type;
            mData = new HashMap<>();
            return this;
        }

        // 绑定数据
        public Builder bundle(String key, Object value) {
            if (key == null || value == null) {
                throw new IllegalArgumentException("Key or value may not be null.");
            }
            mData.put(key, value);
            return this;
        }

        // 通过Builder创建Action
        public Action build() {
            if (mType == null || mType.isEmpty()) {
                throw new IllegalArgumentException("At least one key is required.");
            }
            return new Action(mType, mData);
        }
    }
}
