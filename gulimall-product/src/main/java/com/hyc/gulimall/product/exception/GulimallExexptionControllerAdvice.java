package com.hyc.gulimall.product.exception;

import com.hyc.common.ecception.BizCodeEnume;
import com.hyc.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

/**
 * @projectName: gulimall
 * @package: com.hyc.gulimall.product.exception
 * @className: GulimallExexptionControllerAdvice
 * @author: 冷环渊 doomwatcher
 * @description: TODO
 * @date: 2022/5/31 15:38
 * @version: 1.0
 */
/*
 * 集中处理异常
 * */
@Slf4j
@RestControllerAdvice(basePackages = "com.hyc.gulimall.product.controller")
public class GulimallExexptionControllerAdvice {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleVaildException(MethodArgumentNotValidException e) {
        log.error("数据校验出现问题{}，异常类型：{}", e.getMessage(), e.getClass());
        BindingResult bindingResult = e.getBindingResult();
        HashMap<String, String> map = new HashMap<>();
        bindingResult.getFieldErrors().forEach((fieldError -> {
            map.put(fieldError.getField(), fieldError.getDefaultMessage());
        }));
        return R.error(BizCodeEnume.VAILD_EXCEPTION.getCode(), BizCodeEnume.VAILD_EXCEPTION.getMsg()).put("data", map);
    }

    @ExceptionHandler(value = Throwable.class)
    public R handleException(Throwable e) {
        System.out.println(e.getMessage());
        return R.error(BizCodeEnume.UNKNOW_EXCEPTION.getCode(), BizCodeEnume.UNKNOW_EXCEPTION.getMsg());
    }
}
