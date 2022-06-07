package com.hyc.common.ecception;

/**
 * @projectName: gulimall
 * @package: com.hyc.common.ecception
 * @className: BizCodeEnume
 * @author: 冷环渊 doomwatcher
 * @description: TODO
 * @date: 2022/5/31 15:49
 * @version: 1.0
 */
public enum BizCodeEnume {
    UNKNOW_EXCEPTION(10000, "系统未知异常"),
    VAILD_EXCEPTION(10001, "参数格式校验失败");

    private int code;
    private String msg;

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

    BizCodeEnume(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
