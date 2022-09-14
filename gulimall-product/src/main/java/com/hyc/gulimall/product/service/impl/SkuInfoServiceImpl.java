package com.hyc.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyc.common.utils.PageUtils;
import com.hyc.common.utils.Query;
import com.hyc.gulimall.product.dao.SkuInfoDao;
import com.hyc.gulimall.product.entity.SkuInfoEntity;
import com.hyc.gulimall.product.service.SkuInfoService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuInfo(SkuInfoEntity skuInfoEntity) {
        this.baseMapper.insert(skuInfoEntity);
    }

    /**
     * @author 冷环渊 Doomwatcher
     * @context: sku销售属性检索 按照条件参数动态凭借sql
     *  key: '华为',//检索关键字
     *  catelogId: 0,
     *  brandId: 0,
     *  min: 0,
     *  max: 0
     * @date: 2022/8/27 15:30
     * @param params
     * @return: com.hyc.common.utils.PageUtils
     */
    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SkuInfoEntity> QueryWrapper = new QueryWrapper<>();
        //模糊查询条件，关键字
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            QueryWrapper.and((w) -> {
                w.eq("sku_id", key).or().like("sku_name", key);
            });
        }
        String catelogId = (String) params.get("catelogId");
        if (!StringUtils.isEmpty(catelogId) && !"0".equalsIgnoreCase(catelogId)) {
            QueryWrapper.eq("catelog_id", catelogId);
        }
        String brandId = (String) params.get("brandId");
        if (!StringUtils.isEmpty(brandId) && !"0".equalsIgnoreCase(brandId)) {
            QueryWrapper.eq("brand_id", brandId);
        }
        String min = (String) params.get("min");
        if (!StringUtils.isEmpty(min)) {
            QueryWrapper.ge("price", min);
        }
        String max = (String) params.get("max");
        if (!StringUtils.isEmpty(max)) {
            BigDecimal bigDecimal = new BigDecimal(max);
            if (bigDecimal.compareTo(new BigDecimal("0")) == 1) {
                QueryWrapper.le("price", max);
            }
        }
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                QueryWrapper
        );

        return new PageUtils(page);
    }

    /**
     * @author 冷环渊 Doomwatcher
     * @context: 根据supid 查找到所有的sku
     * @date: 2022/9/14 23:54
     * @param spuId
     * @return: void
     */
    @Override
    public List<SkuInfoEntity> getSkusBySpuId(Long spuId) {
        List<SkuInfoEntity> list = this.list(new QueryWrapper<SkuInfoEntity>().eq("spu_id", spuId));
        return list;
    }

}