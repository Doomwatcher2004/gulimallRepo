package com.hyc.gulimall.product;


import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hyc.gulimall.product.entity.BrandEntity;
import com.hyc.gulimall.product.service.AttrService;
import com.hyc.gulimall.product.service.BrandService;
import com.hyc.gulimall.product.service.CategoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GulimallProductApplicationTests {
    @Autowired
    @Qualifier("brandService")
    BrandService brandService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    OSSClient ossClient;

    @Autowired
    AttrService attrService;

    @Test
    public void findParentPath() {
        Long[] catelogPath = categoryService.findCatelogPath(225L);
        System.out.println(Arrays.asList(catelogPath));
    }

    @Test
    public void contextLoads() {
        //BrandEntity brandEntity = new BrandEntity();
        //brandEntity.setBrandId(1L);
        //brandEntity.setName("华为");
        //brandEntity.setDescript("华为手机国货之光");
        //brandService.updateById(brandEntity);
        //System.out.println("保存成功");
        List<BrandEntity> list = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", 1L));
        list.forEach((item) -> System.out.println(item));
    }

    @Test
    public void testUpload() {
        //// Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        //String endpoint = "xxxxx";
        //// 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        //String accessKeyId = "xxxxx";
        //String accessKeySecret = "xxxxx";
        // 填写Bucket名称，例如examplebucket。
        String bucketName = "xxxxx";
        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
        String objectName = "xxxxx";
        // 填写本地文件的完整路径，例如D:\\localpath\\examplefile.txt。
        // 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
        String filePath = "C:\\Users\\doomwstcher.000\\Pictures\\QQ截图20220513000450.png";

        //// 创建OSSClient实例。
        //OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            InputStream inputStream = new FileInputStream(filePath);
            // 创建PutObject请求。
            ossClient.putObject(bucketName, objectName, inputStream);
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
                System.out.println("上传完成");
            }
        }
    }

    @Test
    public void selectSearchAttrs() {
        Long[] ids = {8L, 13L, 14L, 7L, 15L, 16L};
        List<Long> longs = attrService.selectSearchAttrs(Arrays.asList(ids));
        System.out.println(longs);
    }
}
