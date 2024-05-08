package com.example.router.test;

import com.example.router_api.RouterLoadGroup;
import com.example.router_api.RouterLoadPath;

import java.util.HashMap;
import java.util.Map;

public class Router$$Group$$order implements RouterLoadGroup {
    @Override
    public Map<String, Class<? extends RouterLoadPath>> loadGroup() {
        Map<String, Class<? extends RouterLoadPath>> groupMap = new HashMap<>();
        groupMap.put("order",Router$$Path$$order.class);
        return groupMap;
    }
}
