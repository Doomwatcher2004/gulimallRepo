package com.hyc.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hyc.common.utils.PageUtils;
import com.hyc.gulimall.product.entity.SpuInfoEntity;
import com.hyc.gulimall.product.vo.SpuSaveVo;

import java.util.Map;

/**
 * spu信息
 *
 * @author lenghuanyuan
 * @email 3132774018@qq.com
 * @date 2022-05-22 16:21:49
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);


    void saveSpuInfo(SpuSaveVo spuVo);

    void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity);


    PageUtils queryPageByCondition(Map<String, Object> params);

    void up(Long spuId);

}

