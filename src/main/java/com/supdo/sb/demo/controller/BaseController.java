package com.supdo.sb.demo.controller;


import com.supdo.sb.demo.plugin.Result;

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
        result = new Result(false, "", "", "", "", 0);
    }

}
