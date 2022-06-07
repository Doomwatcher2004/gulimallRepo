package com.hyc.common.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;

/**
 * @projectName: gulimall
 * @package: com.hyc.common.valid
 * @className: ListValueConstraintValidator
 * @author: 冷环渊 doomwatcher
 * @description: TODO
 * @date: 2022/5/31 16:12
 * @version: 1.0
 */
public class ListValueConstraintValidator implements ConstraintValidator<ListValue, Integer> {
    HashSet<Integer> set = new HashSet<>();

    //初始化
    @Override
    public void initialize(ListValue constraintAnnotation) {

        int[] vals = constraintAnnotation.vals();
        for (int val : vals) {
            set.add(val);
        }
    }

    /**
     * @author 冷环渊 Doomwatcher
     * @context:
     * @date: 2022/5/31 16:15
     * @param value 需要校验的值
     * @param constraintValidatorContext
     * @return: boolean
     */
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
        if (set.contains(value)) {
            return true;
        }
        return false;
    }
}
