package siu.example.com.headingout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import siu.example.com.headingout.util.Utilities;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());

        Utilities.hideKeyboard(this);
    }

    protected abstract int getLayoutResource();

}
