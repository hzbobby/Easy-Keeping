package com.vividbobo.easy.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.common.util.concurrent.ListenableFuture;
import com.vividbobo.easy.database.model.Leger;
import com.vividbobo.easy.repository.LegersRepo;

import java.util.List;

public class LegerViewModel extends AndroidViewModel {

    private final LegersRepo legersRepo;
    private final LiveData<List<Leger>> allLegersLD;

    public LegerViewModel(@NonNull Application application) {
        super(application);
        legersRepo = new LegersRepo(application);
        allLegersLD = legersRepo.getAllLegersLD();
    }


    public LiveData<List<Leger>> getAllLegersLD() {
        return allLegersLD;
    }

    public void insert(Leger leger) {
        legersRepo.insert(leger);
    }

    public void update(Leger editLeger) {
        legersRepo.update(editLeger);
    }

    public void delete(Leger item) {
        legersRepo.delete(item);
    }

    public ListenableFuture<Leger> getLegerById(Integer defaultId) {
        return legersRepo.getLegerById(defaultId);
    }
}
