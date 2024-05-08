package com.example.router;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.annotation.Router;
import com.example.annotation.RouterBean;
import com.example.router.databinding.ActivityMainBinding;
import com.example.router.test.Router$$Group$$order;
import com.example.router_api.RouterLoadGroup;
import com.example.router_api.RouterLoadPath;

import java.util.Map;

@Router(path = "/app/HomeActivity")
public class HomeActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        jumpOrder();
    }

    /**
     *  模块跟模块之间的核心跳转诉求； 所以APT+javapoet核心目标时生成 Router$$Group$$xxx和Router$$Path$xxx
     *
     */
    public void jumpOrder(){
        RouterLoadGroup loadGroup = new Router$$Group$$order();
        Map<String, Class<? extends RouterLoadPath>> groupMap = loadGroup.loadGroup();
        Class<? extends RouterLoadPath> clazz = groupMap.get("order");

        try {
            RouterLoadPath path = clazz.newInstance();
            Map<String, RouterBean> pathMap = path.loadPath();
            RouterBean routerBean = pathMap.get("/order/OrderActivity");
            if(routerBean !=null){
                Intent intent = new Intent(this,routerBean.getClass());
                startActivity(intent);
            }
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }

    }
}
