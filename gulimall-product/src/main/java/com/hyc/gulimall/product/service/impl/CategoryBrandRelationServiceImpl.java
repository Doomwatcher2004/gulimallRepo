package com.hyc.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyc.common.utils.PageUtils;
import com.hyc.common.utils.Query;
import com.hyc.gulimall.product.dao.BrandDao;
import com.hyc.gulimall.product.dao.CategoryBrandRelationDao;
import com.hyc.gulimall.product.dao.CategoryDao;
import com.hyc.gulimall.product.entity.BrandEntity;
import com.hyc.gulimall.product.entity.CategoryBrandRelationEntity;
import com.hyc.gulimall.product.entity.CategoryEntity;
import com.hyc.gulimall.product.service.BrandService;
import com.hyc.gulimall.product.service.CategoryBrandRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Autowired
    BrandDao brandDao;
    @Autowired
    CategoryDao categoryDao;

    @Autowired
    BrandService brandService;

    @Override

    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveDetail(CategoryBrandRelationEntity categoryBrandRelation) {
        //品牌id
        Long brandId = categoryBrandRelation.getBrandId();
        //分类id
        Long catelogId = categoryBrandRelation.getCatelogId();
        BrandEntity brandEntity = brandDao.selectById(brandId);
        CategoryEntity categoryEntity = categoryDao.selectById(catelogId);

        categoryBrandRelation.setBrandName(brandEntity.getName());
        categoryBrandRelation.setCatelogName(categoryEntity.getName());

        this.save(categoryBrandRelation);
    }

    /**
     * @author 冷环渊 Doomwatcher
     * @context:从商品和分类关联表中 更新商品的信息
     * @date: 2022/6/9 18:55
     * @param brandId
     * @param name
     * @return: void
     */
    @Override
    public void updatebrand(Long brandId, String name) {
        CategoryBrandRelationEntity entity = new CategoryBrandRelationEntity();
        entity.setBrandId(brandId);
        entity.setBrandName(name);
        this.update(entity, new UpdateWrapper<CategoryBrandRelationEntity>().eq("brand_id", brandId));
    }

    @Override
    public void updateCategory(Long catId, String name) {
        this.baseMapper.updateCategory(catId, name);
    }

    /**
     * @author 冷环渊 Doomwatcher
     * @context: 根据分类id 获取到关联的品牌 关联表只能获取到名字和id 于是我们通过id继续获取全部信息 以备不时之需
     * @date: 2022/7/4 18:03
     * @param catId
     * @return: java.util.List<com.hyc.gulimall.product.entity.BrandEntity>
     */
    @Override
    public List<BrandEntity> getBrandsByCatId(Long catId) {

        List<CategoryBrandRelationEntity> relationList = this.baseMapper.selectList(new QueryWrapper<CategoryBrandRelationEntity>().eq("catelog_id", catId));
        List<BrandEntity> collect = relationList.stream().map(item -> {
            Long brandId = item.getBrandId();
            BrandEntity entity = brandService.getById(brandId);
            return entity;
        }).collect(Collectors.toList());
        return collect;
    }

}