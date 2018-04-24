package com.supdo.sb.demo.service;

import java.util.HashMap;
import java.util.Map;

public class BaseService {

    protected Result result = new Result(false, "初始化");

    public class Result {
        private boolean flag;
        private int code = 0;
        private String msg;
        private Map<String, Object> items = new HashMap<String, Object>();

        public Result(boolean flag, String msg) {
            this.flag = flag;
            this.msg = msg;
        }

        public Result simple(boolean flag, String msg) {
            this.flag = flag;
            this.msg = msg;
            return this;
        }

        public boolean isFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public Map<String, Object> getItems() {
            return items;
        }

        public void setItems(Map<String, Object> items) {
            this.items = items;
        }

        public void putItem(String key, Object val) {
            this.items.put(key, val);
        }

        public Object getItem(String key) {
            return this.items.get(key);
        }

        public void removeItem(String key) {
            this.items.remove(key);
        }
    }
}
