package com.bcs.andy.strayanimalsshelter.ui.register;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.bcs.andy.strayanimalsshelter.R;

public class MyRegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_register);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.register_fragment_container, new RegisterAskEmailFragment())
                .commit();


    }
}
