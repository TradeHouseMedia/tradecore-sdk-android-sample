package com.tradehousemedia.tradecore.java.utils;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.tradehousemedia.tradecore.java.R;

public class BaseExampleActivity extends AppCompatActivity {

    public void setTitle() {
        TextView title = findViewById(R.id.viewTitle);
        title.setText(ExampleHolder.getLastExample().title);
    }

    public LinearLayout getAdContainer() {
        return findViewById(R.id.viewAdHolder);
    }

    public Button getRefreshButton() {
        return findViewById(R.id.viewRefresh);
    }

}
