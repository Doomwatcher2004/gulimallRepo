package com.hyc.gulimallware;


import com.hyc.gulimall.ware.dao.PurchaseDetailDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class GulimallWareApplicationTests {
    @Autowired
    PurchaseDetailDao purchaseDetailDao;

    @Test
    public void contextLoads() {
     
    }

}
