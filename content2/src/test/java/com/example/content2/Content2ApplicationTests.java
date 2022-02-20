package com.example.content2;

import com.example.content2.Mapper.RegionMapper;
import com.example.content2.POJO.Region;
import com.example.content2.Service.Impl.SuggestValueServiceImpl;
import com.example.content2.Util.Execel.GenerateSmartCard;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import com.example.content2.Service.Impl.SuggestValueServiceImpl.ItemProperties.items;

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

}
