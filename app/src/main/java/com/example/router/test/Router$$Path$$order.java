package com.example.router.test;

import com.example.annotation.RouterBean;
//import com.example.order.ListActivity;
//import com.example.order.OrderActivity;
import com.example.router_api.RouterLoadPath;

import java.util.HashMap;
import java.util.Map;

public class Router$$Path$$order implements RouterLoadPath {
    @Override
    public Map<String, RouterBean> loadPath() {
        Map<String, RouterBean> pathMap = new HashMap<>();
//        pathMap.put("/order/OrderActivity", RouterBean.create(RouterBean.Type.ACTIVITY, OrderActivity.class, "/order/OrderActivity", "order"));
//        pathMap.put("/order/ListActivity", RouterBean.create(RouterBean.Type.ACTIVITY, ListActivity.class, "/order/ListActivity", "order"));
        return pathMap;
    }
}
