package com.example.content2;

import com.example.content2.Service.Impl.Fun1ResultMapHandle;
import com.example.content2.Service.fun1Calculate.CalculateRecordService;
import com.example.content2.Util.Execel.GenerateSmartCard;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;

@SpringBootTest
class Content2ApplicationTests {

    @Autowired
    GenerateSmartCard generateSmartCard;

    @Value("${excelTemplate.absolutePath}")
    String path;

    @Test
    void contextLoads() throws GenerateSmartCard.notCorrectFile, IOException, GenerateSmartCard.TemplateFileNotFound {
    }

    @Resource
    ApplicationContext applicationContext;

    @Resource
    BeanFactory beanFactory;

    @Resource(name = "defaultRedisTemplate")
    RedisTemplate redisTemplate;

    @Resource
    CalculateRecordService calculateRecordService;

//    @Test
    public void testApplicationContext(){

        ObjectProvider<Fun1ResultMapHandle> beanProvider = applicationContext.getBeanProvider(Fun1ResultMapHandle.class);
        ArrayList<Fun1ResultMapHandle> objects = new ArrayList<>();
        beanProvider.stream().forEach(objects::add);
    }
//    @Test
    public void testRedisTemplate(){
        redisTemplate.opsForValue().set("databaseVersionDate",new Long(System.currentTimeMillis()));
    }
//    @Test
    public void testCalculate(){
        double offset = 0.010;
        for (int i = 0; i < 10; i++) {
            offset += 0.001;
            calculateRecordService.calculateOffsetMagnificationForData(offset,2,1000);
        }
    }


}
