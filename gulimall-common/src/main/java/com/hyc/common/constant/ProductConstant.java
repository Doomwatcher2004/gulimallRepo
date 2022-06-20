package com.hyc.common.constant;

/**
 * @projectName: gulimall
 * @package: com.hyc.common.constant
 * @className: ProductConstant
 * @author: 冷环渊 doomwatcher
 * @description: TODO
 * @date: 2022/6/14 15:07
 * @version: 1.0
 */
public class ProductConstant {
    public enum AttrEnum {
        ATTR_TYPE_BASE(1, "基本属性"), ATTR_TYPE_SALE(0, "销售属性");
        private int code;
        private String msg;

        AttrEnum(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }
}
