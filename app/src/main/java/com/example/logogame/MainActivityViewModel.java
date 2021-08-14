package com.example.logogame;

import android.util.JsonReader;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.logogame.model.Logo;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

public class MainActivityViewModel extends ViewModel {

    public static class Factory implements ViewModelProvider.Factory{

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return null;
        }
    }

    MutableLiveData<List<String>> suggestionLiveData;

    public void init() throws FileNotFoundException {
        //parse json
        parseJson();

    }

    private void parseJson() throws FileNotFoundException {
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader("logo.json"));

        Logo[] logos = gson.fromJson(String.valueOf(reader), Logo[].class);

    }
}
