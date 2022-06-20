package com.hyc.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyc.common.constant.ProductConstant;
import com.hyc.common.utils.PageUtils;
import com.hyc.common.utils.Query;
import com.hyc.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.hyc.gulimall.product.dao.AttrDao;
import com.hyc.gulimall.product.dao.AttrGroupDao;
import com.hyc.gulimall.product.dao.CategoryDao;
import com.hyc.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.hyc.gulimall.product.entity.AttrEntity;
import com.hyc.gulimall.product.entity.AttrGroupEntity;
import com.hyc.gulimall.product.entity.CategoryEntity;
import com.hyc.gulimall.product.service.AttrService;
import com.hyc.gulimall.product.service.CategoryService;
import com.hyc.gulimall.product.vo.AttrGroupRelationVo;
import com.hyc.gulimall.product.vo.AttrRespVo;
import com.hyc.gulimall.product.vo.AttrVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("attrService")
@Slf4j
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {
    @Autowired
    AttrAttrgroupRelationDao relationDao;

    @Autowired
    AttrGroupDao attrGroupDao;

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    CategoryService categoryService;

    @Override

    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * @author 冷环渊 Doomwatcher
     * @context: 重写的 添加属性分析VO数据分别添加到数据库中
     * @date: 2022/6/9 19:29
     * @param attrVo 属性的veiw object 对象
     * @return: void
     */
    @Transactional
    @Override
    public void saveAttr(AttrVo attrVo) {
        //最终保存的还是 entity 对象 VO对象是负责和视图属性转换的
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrVo, attrEntity);
        //保存基本数据
        this.save(attrEntity);
        if (attrVo.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode() && attrVo.getAttrGroupId() != null) {
            // 保存关联关系
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrGroupId(attrVo.getAttrGroupId());
            relationEntity.setAttrId(attrEntity.getAttrId());
            relationDao.insert(relationEntity);
        }
    }

    /**
     * @author 冷环渊 Doomwatcher
     * @context: 查询属性 如果是销售属性就不绑定规格分组（attrgroup同属性分组），如果是规格参数再绑定规格分组
     * @date: 2022/6/14 15:04
     * @param params
     * @param categoryId
     * @param type
     * @return: com.hyc.common.utils.PageUtils
     */
    @Override
    public PageUtils queryBaseAttrPage(Map<String, Object> params, Long categoryId, String type) {
        QueryWrapper<AttrEntity> attrEntityQueryWrapper = new QueryWrapper<AttrEntity>().eq("attr_type", "base".equalsIgnoreCase(type) ? ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode() : ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode());
        if (categoryId != 0) {
            attrEntityQueryWrapper.eq("catelog_id", categoryId);
        }
        String key = (String) params.get("key");
        if (StringUtils.isEmpty(key)) {
            attrEntityQueryWrapper.and((wapper) -> {
                wapper.eq("attr_id", key).or().like("attr_name", key);
            });
        }
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                attrEntityQueryWrapper
        );
        PageUtils pageUtils = new PageUtils(page);
        //获得返回的分页列表
        List<AttrEntity> records = page.getRecords();
        List<AttrRespVo> attrRespVoList = records.stream().map((attrEntity) -> {
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(attrEntity, attrRespVo);
            //设置分类和分组名
            if ("base".equalsIgnoreCase(type)) {
                AttrAttrgroupRelationEntity attr_id = relationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrEntity.getAttrId()));
                if (attr_id != null && attr_id.getAttrGroupId() != null) {
                    AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attr_id.getAttrGroupId());
                    attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }
            }
            CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
            if (categoryEntity != null) {
                attrRespVo.setCatelogName(categoryEntity.getName());
            }
            return attrRespVo;
        }).collect(Collectors.toList());
        pageUtils.setList(attrRespVoList);
        return pageUtils;
    }

    /**
     * @author 冷环渊 Doomwatcher
     * @context: 获得信息 判断： 规格参数/销售属性 不同的情况来拼装不同的信息
     * @date: 2022/6/17 16:33
     * @param attrId
     * @return: com.hyc.gulimall.product.vo.AttrRespVo
     */
    @Override
    public AttrRespVo getAttrInfo(Long attrId) {
        AttrRespVo attrRespVo = new AttrRespVo();
        AttrEntity attrEntity = this.getById(attrId);
        BeanUtils.copyProperties(attrEntity, attrRespVo);
        //判断类型
        if (attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {
            //1. 设置分组信息
            AttrAttrgroupRelationEntity attrgrouprelation = relationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId));
            if (attrgrouprelation != null) {
                attrRespVo.setAttrGroupId(attrgrouprelation.getAttrGroupId());
                //设置分组名
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrgrouprelation.getAttrGroupId());
                if (attrGroupEntity != null) {
                    attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }
            }
        }
        //    2.设置分类信息
        Long catelogId = attrEntity.getCatelogId();
        Long[] catelogPath = categoryService.findCatelogPath(catelogId);
        if (catelogPath != null) {
            attrRespVo.setCatelogPath(catelogPath);
        }
        CategoryEntity categoryEntity = categoryDao.selectById(catelogId);
        attrRespVo.setCatelogName(categoryEntity.getName());
        return attrRespVo;
    }

    /**
     * @author 冷环渊 Doomwatcher
     * @context: 修改属性 同样是根绝判断 规格参数/销售书信 来分别处理
     * 1. 规格参数 才需要修改分组关联
     * 2. 销售参数不需要添加分组 所以只需要操作attr 表即可
     * @date: 2022/6/17 16:33
     * @param attrVo
     * @return: void
     */
    @Override
    public void updateAttr(AttrVo attrVo) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrVo, attrEntity);
        this.updateById(attrEntity);
        if (attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {
            //    1. 修改分组关联
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrGroupId(attrVo.getAttrGroupId());
            relationEntity.setAttrId(attrVo.getAttrId());
            Integer count = relationDao.selectCount(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrVo.getAttrId()));
            if (count > 0) {
                relationDao.update(relationEntity, new UpdateWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrVo.getAttrId()));
            } else {
                relationDao.insert(relationEntity);
            }
        }
    }

    /**
     * @author 冷环渊 Doomwatcher
     * @context: 根据分组id 查询关联的所有基本属性
     * @date: 2022/6/17 16:35
     * @param attrgroupId
     * @return: java.util.List<com.hyc.gulimall.product.entity.AttrEntity>
     */
    @Override
    public List<AttrEntity> getRelationAttr(Long attrgroupId) {
        //查询信息
        List<AttrAttrgroupRelationEntity> entities = relationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrgroupId));
        List<Long> attrIds = entities.stream().map((attr) -> {
            return attr.getAttrId();
        }).collect(Collectors.toList());
        //可能是空值 因为可以能没有关联数据 所以需要非空判断,否则会导致下面的查询方法异常，所以提前非空返回
        if (attrIds == null || attrIds.size() == 0) {
            return null;
        }
        List<AttrEntity> ids = this.listByIds(attrIds);
        return ids;
    }

    /**
     * @author 冷环渊 Doomwatcher
     * @context: 删除属性分组和属性的关联关系
     * @date: 2022/6/17 16:44
     * @param vos
     * @return: void
     */
    @Override
    public void deleteRelation(AttrGroupRelationVo[] vos) {
        //我们传入的是一整个数组 如果是多个删除可能会重复的调用方法，所以我们封装一个拼接条件的方法
        //例子： delete * from AttrAttrgroupRelation where (id=1,agid=2) or(...) or(...)
        //relationDao.delete(new QueryWrapper<AttrAttrgroupRelationEntity>().eq(....));
        List<AttrAttrgroupRelationEntity> collect = Arrays.stream(vos).map((item) -> {
            AttrAttrgroupRelationEntity entity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(item, entity);
            return entity;
        }).collect(Collectors.toList());
        relationDao.deleteBatchRelation(collect);
    }

    /**
     * @author 冷环渊 Doomwatcher
     * @context: 获取当前分组没有关联的所有属性
     * @date: 2022/6/20 15:21
     * @param attrgroupId
     * @param params
     * @return: com.hyc.common.utils.PageUtils
     */
    @Override
    public PageUtils getNoRelationAttr(Long attrgroupId, Map<String, Object> params) {
        //1. 当前分组只能关联自己所属的分类的属性
        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrgroupId);
        Long catelogId = attrGroupEntity.getCatelogId();

        //2. 当前分组只能关联别的分组没有引用的属性
        //1) 当前分类下的全部分组包括自己
        List<AttrGroupEntity> grouplist = attrGroupDao.selectList(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));
        List<Long> AttrGroupIdlist = grouplist.stream().map((item) -> {
            return item.getAttrGroupId();
        }).collect(Collectors.toList());
        //2）找到分组关联的属性
        List<AttrAttrgroupRelationEntity> relationList = relationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_group_id", AttrGroupIdlist));
        //这里拿到的是所有属性
        List<Long> allAttridList = relationList.stream().map((item) -> {
            return item.getAttrId();
        }).collect(Collectors.toList());
        //3） 从当前分类的所有属性中找到没有关联的属性
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<AttrEntity>().eq("catelog_id", catelogId).eq("attr_type", ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode());
        if (allAttridList != null && allAttridList.size() > 0) {
            queryWrapper.notIn("attr_id", allAttridList);
        }
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            queryWrapper.and((w) -> {
                w.eq("attr_id", key).or().like("attr_name", key);
            });
        }
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), queryWrapper);
        PageUtils pageUtils = new PageUtils(page);
        return pageUtils;
    }


}

