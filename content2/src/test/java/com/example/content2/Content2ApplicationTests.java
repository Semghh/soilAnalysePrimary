package com.example.content2;

import com.example.content2.Service.Impl.Fun1ResultMapHandle;
import com.example.content2.Util.Execel.GenerateSmartCard;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

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


    @Test
    public void test1(){

        ObjectProvider<Fun1ResultMapHandle> beanProvider = applicationContext.getBeanProvider(Fun1ResultMapHandle.class);
        ArrayList<Fun1ResultMapHandle> objects = new ArrayList<>();
        beanProvider.stream().forEach(objects::add);
    }

}
