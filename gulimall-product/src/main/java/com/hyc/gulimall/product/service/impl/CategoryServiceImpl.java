package com.hyc.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyc.common.utils.PageUtils;
import com.hyc.common.utils.Query;
import com.hyc.gulimall.product.dao.CategoryDao;
import com.hyc.gulimall.product.entity.CategoryEntity;
import com.hyc.gulimall.product.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        //查出所有的分类
        List<CategoryEntity> entities = baseMapper.selectList(null);
        //组装父子类的树结构
        //1。找到所有的一级分类所有的一级分类 父分类都是0
        List<CategoryEntity> level1Menu =
                entities.stream()
                        .filter(categoryEntity -> categoryEntity.getParentCid() == 0)
                        .map((menu) -> {
                            menu.setChildren(getChildrens(menu, entities));
                            return menu;
                        })
                        .sorted((menu1, menu2) -> {
                            return menu1.getSort() - menu2.getSort();
                        })
                        .collect(Collectors.toList());

        return level1Menu;
    }

    @Override
    public void removeMenuByIds(List<Long> aslist) {
        //TODO  1 检查当前删除的菜单 是否被别的引用
        baseMapper.deleteBatchIds(aslist);
    }

    /**
     * @author 冷环渊 Doomwatcher
     * @context: 找到完整的category路径 父/子/孙/
     * @date: 2022/6/3 16:49
     * @param catelogId
     * @return: java.lang.Long[]
     */
    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId, paths);
        Collections.reverse(parentPath);
        return parentPath.toArray(new Long[parentPath.size()]);
    }

    private List<Long> findParentPath(Long catelogId, List<Long> paths) {
        //    收集当前节点的id
        paths.add(catelogId);
        CategoryEntity byId = this.getById(catelogId);
        if (byId.getParentCid() != 0) {
            findParentPath(byId.getParentCid(), paths);
        }
        return paths;
    }

    //递归查找所有菜单的子菜单
    private List<CategoryEntity> getChildrens(CategoryEntity root, List<CategoryEntity> all) {
        List<CategoryEntity> children = all.stream()
                .filter((categoryEntity) -> {
                    return categoryEntity.getParentCid().equals(root.getCatId());
                })
                .map((categoryEntity) -> {
                    categoryEntity.setChildren(getChildrens(categoryEntity, all));
                    return categoryEntity;
                })
                .sorted((menu1, menu2) -> {
                    return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
                })
                .collect(Collectors.toList());

        return children;
    }

}