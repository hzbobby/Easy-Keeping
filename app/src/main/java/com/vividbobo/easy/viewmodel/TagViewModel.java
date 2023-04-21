package com.vividbobo.easy.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vividbobo.easy.database.model.Tag;
import com.vividbobo.easy.database.model.TagPresent;
import com.vividbobo.easy.repository.TagsRepo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TagViewModel extends AndroidViewModel {
    private final TagsRepo tagsRepo;
    private final LiveData<List<TagPresent>> allPresentTags;
    private final LiveData<List<Tag>> allTags;


    public TagViewModel(@NonNull Application application) {
        super(application);
        tagsRepo = new TagsRepo(application);
        allPresentTags = tagsRepo.getAllPresentTags();
        allTags = tagsRepo.getAllTags();

    }

    public LiveData<List<TagPresent>> getAllPresentTags() {
        return allPresentTags;
    }

    public void delete(Tag item) {
        tagsRepo.delete(item);
    }

    public void insert(Tag tag) {
        tagsRepo.insert(tag);
    }

    public void update(Tag tag) {
        tagsRepo.update(tag);
    }

    public LiveData<List<Tag>> getAllTags() {
        return allTags;
    }

}
