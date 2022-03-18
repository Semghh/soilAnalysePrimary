package com.example.content2.Config;

import com.example.content2.Service.Impl.Fun1ResultMapHandle;
import com.example.content2.Util.DoubleFormat;
import com.example.content2.Util.getFieldFromMap;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Configuration
public class Fun1ResultMapChainConfig {


    /**
     *
     * fun1 结果Map将封装如下的基础Fields.
     * 	"data": {
     * 		"mea_organic_matter": "26.2",
     * 		"mea_Olsen_K": "243.0",
     * 		"mea_Olsen_P": "8.5",
     * 		"min_Longitude": 125.255912,
     * 		"sug_ph": "无参考值",
     * 		"sug_Olsen_K": "4.2",
     * 		"name_countryside": "兴城镇",
     * 		"sug_organic_matter": "无参考值",
     * 		"sug_Effective_N": "12.5",
     * 		"min_Latitude": 46.023009,
     * 		"mea_ph": "8.2",
     * 		"isDirectMeasured": "false",
     * 		"sug_Olsen_P": "5.2",
     * 		"name_village": "红星村",
     * 		"mea_Effective_N": "108.5",
     * }
     *
     *    通过向Spring容器注入Fun1HandleChain类型的Bean ,修改Fun1结果处理链。
     *    这个处理链将增加 额外字段。
     *    处理链将自动修改map,因此使用时需要小心修改里面的基础数据
     */
    @Bean("fun1HandleChain1")
    public Fun1ResultMapHandle getChain(){
        return new Fun1ResultMapHandle() {
            @Override
            public void resultMapHandle(HashMap<String, Object> resultMap) {
                String[] fieldNames = new String[]{
                        "sug_Olsen_K","sug_Olsen_P","sug_Effective_N"};
                Class[] fieldClz = new Class[]{Double.class,Double.class,Double.class};
                Object[] field = new Object[0];
                try {
                    field = getFieldFromMap.get(resultMap, fieldNames, fieldClz);
                } catch (getFieldFromMap.notFoundSuchField notFoundSuchField) {
                    notFoundSuchField.printStackTrace();
                }

                Double K2SO4 = (Double)field[0] / 0.6;

                Double KCL = (Double)field[0]/0.5;

                Double erAn = (Double) field[1]/0.46;

                Double niaoSu = ((Double) field[2]-erAn*0.16)/0.6;

                resultMap.put("sug_K2SO4", DoubleFormat.format(2,K2SO4));
                resultMap.put("sug_KCL", DoubleFormat.format(2,KCL));
                resultMap.put("sug_erAn", DoubleFormat.format(2,erAn));
                resultMap.put("sug_niaoSu", DoubleFormat.format(2,niaoSu));
            }
        };
    }



    @Bean("defaultHandleChain")
    @Order(Integer.MIN_VALUE)
    public List<Fun1ResultMapHandle> getChainList(ApplicationContext context){
        LinkedList<Fun1ResultMapHandle> res = new LinkedList<>();
        context.getBeanProvider(Fun1ResultMapHandle.class)
                .stream()
                .forEach(res::add);
        return res;
    }
}
