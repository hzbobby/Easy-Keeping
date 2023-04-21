package com.vividbobo.easy.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.vividbobo.easy.database.EasyDatabase;
import com.vividbobo.easy.database.dao.TagDao;
import com.vividbobo.easy.database.model.Tag;
import com.vividbobo.easy.database.model.TagPresent;
import com.vividbobo.easy.utils.AsyncProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class TagsRepo {
    //livedata

    private final LiveData<List<Tag>> allTags;

    //dao
    private TagDao tagDao;

    public TagsRepo(Application application) {
        EasyDatabase db = EasyDatabase.getDatabase(application);
        tagDao = db.tagDao();
        allTags = tagDao.getALlTags();
    }

    public LiveData<List<Tag>> getAllTags() {
        return allTags;
    }

    public LiveData<List<TagPresent>> getAllPresentTags() {
        return Transformations.map(allTags, this::convertToTagPresent);
    }

    public List<TagPresent> convertToTagPresent(List<Tag> tags) {
        List<TagPresent> tpList = new ArrayList<>();
        for (int i = 0; i < tags.size(); i++) {
            TagPresent tp = new TagPresent(tags.get(i));
            tp.setChecked(false);
            tpList.add(tp);
        }
        return tpList;
    }

    public void delete(Tag item) {
        AsyncProcessor.getInstance().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                tagDao.delete(item);
                return null;
            }
        });
    }

    public void insert(Tag tag) {
        AsyncProcessor.getInstance().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                tagDao.insert(tag);
                return null;
            }
        });
    }

    public void update(Tag tag) {
        AsyncProcessor.getInstance().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                tagDao.update(tag);
                return null;
            }
        });
    }
}
