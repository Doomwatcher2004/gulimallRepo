package com.hyc.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyc.common.utils.PageUtils;
import com.hyc.common.utils.Query;
import com.hyc.common.utils.R;
import com.hyc.gulimall.ware.dao.WareSkuDao;
import com.hyc.gulimall.ware.entity.WareSkuEntity;
import com.hyc.gulimall.ware.feign.PreductFeignService;
import com.hyc.gulimall.ware.service.WareSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Autowired
    WareSkuDao wareSkuDao;

    @Autowired
    PreductFeignService preductFeignService;

    /**
     * @author 冷环渊 Doomwatcher
     * @context: 查询仓库的各种型号商品的库存
     * skuId: 某一个型号的id
     * wareId: 仓库id
     * @date: 2022/8/27 17:45
     * @param params
     * @return: com.hyc.common.utils.PageUtils
     */
    @Override

    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<WareSkuEntity> queryWrapper = new QueryWrapper<>();
        String skuId = (String) params.get("skuId");
        if (!StringUtils.isEmpty(skuId)) {
            queryWrapper.eq("sku_id", skuId);
        }
        String wareId = (String) params.get("wareId");
        if (!StringUtils.isEmpty(wareId)) {
            queryWrapper.eq("ware_id", wareId);
        }
        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void addstock(Long skuId, Long wareId, Integer skuNum) {
        //1、如果没有库存就是新增 如果有就是修改
        List<WareSkuEntity> wareSkuEntities = wareSkuDao.selectList(new QueryWrapper<WareSkuEntity>().eq("sku_id", skuId).eq("ware_id", wareId));
        if (wareSkuEntities == null || wareSkuEntities.size() == 0) {
            WareSkuEntity wareSkuEntity = new WareSkuEntity();
            wareSkuEntity.setSkuId(skuId);
            wareSkuEntity.setWareId(wareId);
            wareSkuEntity.setStock(skuNum);
            wareSkuEntity.setStockLocked(0);
            //远程调用查询
            //TODO 不需要应为一个字段出发事务回滚所以用try
            //TODO 如何出现异常 不回滚
            try {
                R info = preductFeignService.info(skuId);
                Map<String, Object> skuInfo = (Map<String, Object>) info.get("skuInfo");
                if (info.getCode() == 0) {
                    wareSkuEntity.setSkuName((String) skuInfo.get("skuName"));
                }
            } catch (Exception e) {
            }


            wareSkuDao.insert(wareSkuEntity);
        } else {
            wareSkuDao.addstock(skuId, wareId, skuNum);
        }
    }

}