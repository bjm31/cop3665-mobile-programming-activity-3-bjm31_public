package com.example.activity_3_bjm31;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

public class MainActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return CatGalleryFragment.newInstance();
    }
}
