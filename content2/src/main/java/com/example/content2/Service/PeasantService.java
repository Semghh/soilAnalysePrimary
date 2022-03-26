package com.example.content2.Service;

import com.example.content2.POJO.SoilAnalyse.Peasant;

public interface PeasantService {
    int isExistsId_card(String id_card);

    Integer getIdByIdCard(String id_card);

    int registerPeasant(Peasant p);
}
