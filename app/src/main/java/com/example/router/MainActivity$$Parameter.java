package com.example.router;

import com.example.router_api.ParameterLoad;

public class MainActivity$$Parameter implements ParameterLoad {
    @Override
    public void loadParameter(Object target) {
        MainActivity t = (MainActivity) target;

       t.setName(t.getIntent().getStringExtra("name"));
       t.setAge(t.getIntent().getIntExtra("age", t.getAge()));
    }
}
