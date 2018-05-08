package com.supdo.sb.demo.controller;


import java.util.HashMap;
import java.util.Map;

public class BaseController {
    protected String tpl = "default";

    protected Result result = new Result(false, "初始化");

    public BaseController() {

    }

    protected String render(String filename) {
        return String.format("%s/%s", this.tpl, filename);
    }

    public void clearResult(){
        result = new Result(false, "", "", "", 0);
        //result.clearItems();
    }

    /**
     * Result类
     *
     * @author quke
     */
    public class Result {

        private boolean flag;
        private String msg;
        private String html;
        private String url;
        private int sleep;
        private Map<String, Object> items = new HashMap<String, Object>();

        public Result() {
        }

        public Result(boolean flag, String msg) {
            super();
            this.flag = flag;
            this.msg = msg;

        }

        public Result(boolean flag, String msg, String html, String url, int sleep) {
            this.flag = flag;
            this.msg = msg;
            this.html = html;
            this.url = url;
            this.sleep = sleep;
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

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getHtml() {
            return html;
        }

        public void setHtml(String html) {
            this.html = html;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getSleep() {
            return sleep;
        }

        public void setSleep(int sleep) {
            this.sleep = sleep;
        }

        public Map<String, Object> getItems() {
            return items;
        }

        public void setItems(Map<String, Object> items) {
            this.items = items;
        }

        public void putItems(String key, Object val) {
            this.items.put(key, val);
        }
        public void removeItem(String key){
            this.items.remove(key);
        }

        public void clearItems(){this.items.clear();}
    }
}
