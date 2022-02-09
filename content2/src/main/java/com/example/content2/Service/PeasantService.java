package com.example.content2.Service;

import com.example.content2.POJO.Peasant;

public interface PeasantService {
    public int isExistsId_card(String id_card);

    public Integer getIdByIdCard(String id_card);

    public int registerPeasant(Peasant p);
}
