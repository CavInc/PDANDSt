package cav.pdst.ui.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import cav.pdst.R;
import cav.pdst.data.models.ItemSportsmanModel;
import cav.pdst.ui.adapters.ItemGroupAdapter;

public class ItemGroupActivity extends AppCompatActivity {

    private EditText mNameGroup;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_group);

        mNameGroup = (EditText) findViewById(R.id.group_name_edit);

        mListView = (ListView) findViewById(R.id.item_group_list_view);

        ArrayList<ItemSportsmanModel> models = new ArrayList<>();
        models.add(new ItemSportsmanModel(false,"Иванов",0,"-"));


        ItemGroupAdapter adapter = new ItemGroupAdapter(this,R.layout.item_group_item,models);
        mListView.setAdapter(adapter);

        setupToolBar();
    }

    private void setupToolBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
