package com.example.content2.Config;

import com.example.content2.Service.Impl.SuggestValueServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.content2.Service.Impl.SuggestValueServiceImpl.ItemProperties.*;

@Configuration
public class Fun1SuggestItemsConfig {
    /**
     * SuggestValueServiceImpl.ItemProperties.items  元素配置类（枚举类型）
     *
     * ph 酸碱度
     * organic_matter 有机质
     * total_nitrogen 全氮
     * Olsen_P  有效磷
     * Olsen_K 速效钾
     * slowly_K 缓效钾
     * Effective_Cu 有效铜
     * Effective_Zn 有效锌
     * Effective_Fe 有效铁
     * Effective_Mn 有效猛
     * Effective_N 碱解氮
     *
     * @return
     */
    @Bean
    public SuggestValueServiceImpl.ItemProperties getPropertiesConfig(){

        SuggestValueServiceImpl.ItemProperties bean = new SuggestValueServiceImpl.ItemProperties();

        //在此配置查询的元素
        bean.addItem(items.Effective_N).addItem(items.Olsen_P).
                addItem(items.Olsen_K).addItem(items.ph).addItem(items.organic_matter);


        return bean;
    }
}
