package com.hyc.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyc.common.utils.PageUtils;
import com.hyc.common.utils.Query;
import com.hyc.gulimall.ware.dao.PurchaseDetailDao;
import com.hyc.gulimall.ware.entity.PurchaseDetailEntity;
import com.hyc.gulimall.ware.service.PurchaseDetailService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;


@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailDao, PurchaseDetailEntity> implements PurchaseDetailService {

    /**
     * @author 冷环渊 Doomwatcher
     * @context: 按照条件查询
     *    status: 0,//状态
     *    wareId: 1,//仓库id
     * @date: 2022/8/27 20:22
     * @param params
     * @return: com.hyc.common.utils.PageUtils
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<PurchaseDetailEntity> queryWrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        //模糊id purchase_id  sku_id  可以是仓库id 也可以是商品型号id
        if (!StringUtils.isEmpty(key)) {
            queryWrapper.and((w) -> {
                w.eq("purchase_id", key).or().eq("status", key);
            });
        }
        String status = (String) params.get("status");
        if (!StringUtils.isEmpty(status)) {
            queryWrapper.eq("status", status);
        }
        String wareId = (String) params.get("wareId");
        if (!StringUtils.isEmpty(wareId)) {
            queryWrapper.eq("ware_id", wareId);
        }
        IPage<PurchaseDetailEntity> page = this.page(
                new Query<PurchaseDetailEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    /**
     * @author 冷环渊 Doomwatcher
     * @context: 根据id 找到采购单下的所有采购需求
     * @date: 2022/8/30 0:13
     * @param id
     * @return: java.util.List<com.hyc.gulimall.ware.entity.PurchaseDetailEntity>
     */
    @Override
    public List<PurchaseDetailEntity> ListDetailByPurchaseId(Long id) {
        return null;
    }

}