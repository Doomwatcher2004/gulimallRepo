package com.hyc.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyc.common.constant.WareConstant;
import com.hyc.common.utils.PageUtils;
import com.hyc.common.utils.Query;
import com.hyc.gulimall.ware.VO.MergeVo;
import com.hyc.gulimall.ware.VO.PurchaseItemDoneVo;
import com.hyc.gulimall.ware.VO.PurchaseSDoneVo;
import com.hyc.gulimall.ware.dao.PurchaseDao;
import com.hyc.gulimall.ware.dao.PurchaseDetailDao;
import com.hyc.gulimall.ware.entity.PurchaseDetailEntity;
import com.hyc.gulimall.ware.entity.PurchaseEntity;
import com.hyc.gulimall.ware.service.PurchaseDetailService;
import com.hyc.gulimall.ware.service.PurchaseService;
import com.hyc.gulimall.ware.service.WareSkuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("purchaseService")
@Slf4j
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Autowired
    PurchaseDetailService purchaseDetailService;
    @Autowired
    PurchaseDetailDao purchaseDetailDao;

    @Autowired
    PurchaseService purchaseService;

    @Autowired
    WareSkuService wareSkuService;

    @Override

    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageUnreceivePurchase(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>().eq("status", 0).or().eq("status", 1)
        );

        return new PageUtils(page);
    }

    /**
     * @author ????????? Doomwatcher
     * @context: ???????????????????????????????????? purchaseId ?????????????????????????????????????????????????????????????????????id ?????????????????????
     * @date: 2022/8/27 20:47
     * @param mergeVo
     * @return: void
     */
    @Transactional
    @Override
    public boolean mergePurchase(MergeVo mergeVo) {
        Long purchaseId = mergeVo.getPurchaseId();
        if (purchaseId == null) {
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            //???????????????
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.CREATED.getCode());
            purchaseEntity.setPriority(1);
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            this.save(purchaseEntity);
            purchaseId = purchaseEntity.getId();
        }

        List<Long> items = mergeVo.getItems();
        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> collect = items.stream().map(i -> {
            PurchaseDetailEntity purchaseDetailEntity = purchaseDetailDao.selectById(i);
            if (purchaseDetailEntity.getStatus() == WareConstant.PurchaseStatusEnum.CREATED.getCode() || purchaseDetailEntity.getStatus() == WareConstant.PurchaseStatusEnum.ASSIGNED.getCode()) {
                PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
                detailEntity.setId(i);
                detailEntity.setPurchaseId(finalPurchaseId);
                detailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode());
                return detailEntity;
            } else {
                return null;
            }
        }).collect(Collectors.toList());
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(purchaseId);
        purchaseEntity.setUpdateTime(new Date());
        this.updateById(purchaseEntity);
        if (collect.size() > 0 && !CollectionUtils.isEmpty(collect)) {
            boolean b = purchaseDetailService.updateBatchById(collect);
            return b;
        } else {
            return false;
        }
    }

    @Transactional
    @Override
    public void received(List<Long> ids) {
        //1??? ????????????????????????????????? ?????????????????????????????????
        List<PurchaseEntity> newPurEntitiy = ids.stream().map(id -> {
            PurchaseEntity byId = this.getById(id);
            return byId;
        }).filter(item -> {
            if (item.getStatus() == WareConstant.PurchaseStatusEnum.CREATED.getCode() || item.getStatus() == WareConstant.PurchaseStatusEnum.ASSIGNED.getCode()) {
                return true;
            }
            return false;
        }).map(item -> {
            item.setStatus(WareConstant.PurchaseStatusEnum.RECEOVE.getCode());
            item.setUpdateTime(new Date());
            return item;
        }).collect(Collectors.toList());
        //2??? ????????????????????????
        this.updateBatchById(newPurEntitiy);
        //3??? ???????????????????????????
        newPurEntitiy.forEach(item -> {
            purchaseDetailDao.updatePurchaseDetailStatus(item.getId(), WareConstant.PurchaseDetailStatusEnum.BUYING.getCode());
        });
    }

    @Transactional
    @Override
    public void done(PurchaseSDoneVo DoneVo) {

        //    1???????????????????????????
        Boolean flag = true;
        List<PurchaseItemDoneVo> items = DoneVo.getItems();
        ArrayList<PurchaseDetailEntity> updateOK = new ArrayList<>();
        for (PurchaseItemDoneVo item : items) {
            PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
            if (item.getStatus() == WareConstant.PurchaseDetailStatusEnum.HASERROR.getCode()) {
                flag = false;
                detailEntity.setStatus(item.getStatus());
            } else {
                detailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.FINISH.getCode());
                //    3???????????????????????????
                PurchaseDetailEntity entity = purchaseDetailService.getById(item.getItemId());
                wareSkuService.addstock(entity.getSkuId(), entity.getWareId(), entity.getSkuNum());
            }

            detailEntity.setId(item.getItemId());
            updateOK.add(detailEntity);
        }
        purchaseDetailService.updateBatchById(updateOK);
        //    2????????????????????????
        Long id = DoneVo.getId();
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(id);
        purchaseEntity.setStatus(flag ? WareConstant.PurchaseDetailStatusEnum.FINISH.getCode() : WareConstant.PurchaseDetailStatusEnum.HASERROR.getCode());
        purchaseEntity.setUpdateTime(new Date());
        this.updateById(purchaseEntity);

    }

}