package com.example.content2;

import com.example.content2.Mapper.RegionMapper;
import com.example.content2.POJO.Region;
import com.example.content2.Service.Impl.SuggestValueServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.example.content2.Service.Impl.SuggestValueServiceImpl.ItemProperties.items;

import java.util.ArrayList;

@SpringBootTest
class Content2ApplicationTests {

    @Autowired
    SuggestValueServiceImpl suggestValueService;
    @Autowired
    RegionMapper regionMapper;
    @Test
    void contextLoads() {
        ArrayList<Region> r = regionMapper.selectByName_county("肇州县");
        System.out.println(r);
    }

}
