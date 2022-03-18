package com.example.content2.Service.Impl;

import java.util.HashMap;

/**
 *
 * Fun1 功能，结果Map的 处理链接口的策略本体。
 *
 *
 *在fun1函数中,Handle将会有序的依次运行。
 * 在com.example.content2.Config.Fun1ResultMapChainConfig 中配置 执行链
 */
public interface Fun1ResultMapHandle {

    /**
     *
     * 这是一个没有返回值的方法。参数map是fun1的结果映射
     * 这个方法用于修改map容器里面的内容
     *
     * @param map 是fun1返回映射。所有的返回值都在里面
     *
     */
    public void resultMapHandle(HashMap<String,Object> map);
}
