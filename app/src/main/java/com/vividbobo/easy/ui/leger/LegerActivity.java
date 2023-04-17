package com.vividbobo.easy.ui.leger;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;

import com.vividbobo.easy.BaseActivity;
import com.vividbobo.easy.R;
import com.vividbobo.easy.databinding.ActivityLegerBinding;
import com.vividbobo.easy.database.model.LegerItem;
import com.vividbobo.easy.ui.others.OnItemClickListener;

public class LegerActivity extends BaseActivity {
    private ActivityLegerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLegerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.legerToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //add dialog

        binding.legerToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add_leger:
                        AddLegerDialog.newInstance(null).show(getSupportFragmentManager(), AddLegerDialog.TAG);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });

        //recycler view
        binding.legerRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        LegerAdapter legerAdapter = new LegerAdapter();
        legerAdapter.setOnEditBtnClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                AddLegerDialog.newInstance((LegerItem) item).show(getSupportFragmentManager(), AddLegerDialog.TAG);
            }
        });
        legerAdapter.setOnClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                //TODO getGlobalViewModel().setLegerId(((LegerItem) item).getId());
                int old_pos = legerAdapter.getSelectPosition(); //old
                legerAdapter.setSelectPosition(position);       //new

                legerAdapter.notifyItemChanged(old_pos);
                legerAdapter.notifyItemChanged(legerAdapter.getSelectPosition());
            }
        });


        binding.legerRecyclerView.setAdapter(legerAdapter);


    }
}