package com.tradehousemedia.tradecore.java;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.tradehousemedia.tradecore.java.utils.Example;
import com.tradehousemedia.tradecore.java.utils.ExampleHolder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setExampleList();
    }

    private void setExampleList() {
        List<Example> examples = ExampleHolder.getExamples();

        ListView viewExamples = findViewById(R.id.viewExamples);
        viewExamples.setOnItemClickListener((adapterView, view, i, l) -> {
            Class classActivity = examples.get(i).classActivity;
            ExampleHolder.setLastExample(examples.get(i));
            startActivity(new Intent(this, classActivity));
        });
        ArrayAdapter<Example> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, examples);
        viewExamples.setAdapter(adapter);
    }

}