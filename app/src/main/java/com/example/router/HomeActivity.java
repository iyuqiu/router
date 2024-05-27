package com.example.router;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.annotation.Router;
import com.example.router.databinding.ActivityMainBinding;

@Router(path = "/app/HomeActivity")
public class HomeActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }


}
