package com.example.content2.Service.Impl;

import com.example.content2.Mapper.Primary.PeasantMapper;
import com.example.content2.POJO.SoilAnalyse.Peasant;
import com.example.content2.Service.PeasantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("PeasantService")
public class PeasantServiceImpl implements PeasantService {

    private PeasantMapper peasantMapper;

    @Autowired
    public PeasantServiceImpl(PeasantMapper peasantMapper) {
        this.peasantMapper = peasantMapper;
    }

    public int isExistsId_card(String id_card){
        return peasantMapper.isExistsId_card(id_card);
    }

    @Override
    public Integer getIdByIdCard(String id_card) {
        return peasantMapper.getIdByIdCard(id_card);
    }

    @Override
    public int registerPeasant(Peasant p) {
        return peasantMapper.registerPeasant(p);
    }
}
