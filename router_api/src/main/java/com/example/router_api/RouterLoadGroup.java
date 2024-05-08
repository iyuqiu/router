package com.example.router_api;

import java.util.Map;

public interface RouterLoadGroup {

    Map<String,Class<? extends RouterLoadPath>> loadGroup();
}
