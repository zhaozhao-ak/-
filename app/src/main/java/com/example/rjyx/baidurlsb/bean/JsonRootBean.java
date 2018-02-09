package com.example.rjyx.baidurlsb.bean;

/**
 * Created by RJYX on 2018/2/9.
 */

/**
 * Copyright 2018 bejson.com
 */

import java.util.List;


/**
 * Auto-generated: 2018-02-09 14:53:18
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class JsonRootBean {

    private long log_id;
    private int result_num;
    private List<Result> result;
    public void setLog_id(long log_id) {
        this.log_id = log_id;
    }
    public long getLog_id() {
        return log_id;
    }

    public void setResult_num(int result_num) {
        this.result_num = result_num;
    }
    public int getResult_num() {
        return result_num;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }
    public List<Result> getResult() {
        return result;
    }

}