package com.hyc.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyc.common.utils.PageUtils;
import com.hyc.common.utils.Query;
import com.hyc.gulimall.product.dao.AttrGroupDao;
import com.hyc.gulimall.product.entity.AttrEntity;
import com.hyc.gulimall.product.entity.AttrGroupEntity;
import com.hyc.gulimall.product.service.AttrGroupService;
import com.hyc.gulimall.product.service.AttrService;
import com.hyc.gulimall.product.vo.AttrGroupwithAttrsVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {
    @Autowired
    AttrService attrService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {
        String key = (String) params.get("key");
        QueryWrapper<AttrGroupEntity> wapper = new QueryWrapper<AttrGroupEntity>();
        if (catelogId == 0) {
            IPage<AttrGroupEntity> page = this.page(
                    new Query<AttrGroupEntity>().getPage(params),
                    wapper);
            return new PageUtils(page);
        } else {
            wapper.eq("catelog_id", catelogId);
            if (!StringUtils.isEmpty(key)) {
                wapper.and((obj) -> {
                    obj.eq("attr_group_id", key).or().like("attr_group_name", key);
                });
            }

            IPage<AttrGroupEntity> page = this.page(
                    new Query<AttrGroupEntity>().getPage(params),
                    wapper);
            return new PageUtils(page);
        }
    }

    /**
     * @author 冷环渊 Doomwatcher
     * @context: 根据分类id 查出所有的分组以及这些组里的属性
     * @date: 2022/7/4 18:17
     * @param catelogId
     * @return: java.util.List<com.hyc.gulimall.product.vo.AttrGroupwithAttrsVo>
     */
    @Override
    public List<AttrGroupwithAttrsVo> getAttrGroupWithAttrsByCatelogId(Long catelogId) {
        //获取分组信息
        List<AttrGroupEntity> attrGroupEntities = this.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));
        List<AttrGroupwithAttrsVo> collect = attrGroupEntities.stream().map((group) -> {
            AttrGroupwithAttrsVo attrsVo = new AttrGroupwithAttrsVo();
            BeanUtils.copyProperties(group, attrsVo);
            List<AttrEntity> attrs = attrService.getRelationAttr(attrsVo.getAttrGroupId());
            attrsVo.setAttrs(attrs);
            return attrsVo;
        }).collect(Collectors.toList());
        return collect;
    }

}