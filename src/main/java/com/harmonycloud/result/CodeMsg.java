package com.harmonycloud.result;

/**
 * @author qidong
 * @date 2019/2/15
 */
public class CodeMsg {
    private String msg;

    public CodeMsg( String msg) {
        this.msg = msg;
    }


    public static CodeMsg QUERY_DATA_IS_NOT_EXIST = new CodeMsg("query data is not exist");
    public static CodeMsg PARAM_ERROR = new CodeMsg("param error");
    public static CodeMsg SAVE_DATA_FAIL = new CodeMsg("save data fail");
    public static CodeMsg QUERY_DATA_ERROR = new CodeMsg("query data error");
    public static CodeMsg DELETE_DATA_ERROR = new CodeMsg("delete data error");


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


}
