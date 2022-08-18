package com.hyc.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyc.common.utils.PageUtils;
import com.hyc.common.utils.Query;
import com.hyc.gulimall.product.dao.SpuInfoDescDao;
import com.hyc.gulimall.product.entity.SpuInfoDescEntity;
import com.hyc.gulimall.product.service.SpuInfoDescService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("spuInfoDescService")
public class SpuInfoDescServiceImpl extends ServiceImpl<SpuInfoDescDao, SpuInfoDescEntity> implements SpuInfoDescService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoDescEntity> page = this.page(
                new Query<SpuInfoDescEntity>().getPage(params),
                new QueryWrapper<SpuInfoDescEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * @author 冷环渊 Doomwatcher
     * @context: 保存Spu的介绍图片
     * @date: 2022/7/28 19:53
     * @param descEntity
     * @return: void
     */
    @Override
    public void saveSpuInfDesc(SpuInfoDescEntity descEntity) {
        this.baseMapper.insert(descEntity);
    }

}