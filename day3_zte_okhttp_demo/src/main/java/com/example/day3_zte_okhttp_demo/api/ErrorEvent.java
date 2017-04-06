package com.example.day3_zte_okhttp_demo.api;

/**
 * Created by lny on 2016/3/30.
 */
public class ErrorEvent {

    /**
     * 参数为空
     */
    static final public int PARAM_NULL = -2;

    /**
     * 参数不合法
     */
    static final public int PARAM_ILLEGAL = -3;

    /**
     * 请求失败
     */
    static final public int REQUEST_FAILURE = -4;

    /**
     * 数据为空
     */
    static final public int DATA_IS_EMPTY = -5;

    /**
     * 解析数据失败
     */
    static final public int PARSE_DATA_FAILURE = -6;

}
