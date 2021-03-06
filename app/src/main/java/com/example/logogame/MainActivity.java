package com.example.logogame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.logogame.adapter.GridViewAnswerAdapter;
import com.example.logogame.adapter.GridViewSuggestAdapter;
import com.example.logogame.model.Logo;
import com.example.logogame.pool.Common;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public List<String> suggestSource = new ArrayList<>();
    public GridViewAnswerAdapter answerAdapter;
    public GridViewSuggestAdapter suggestAdapter;
    public Button btnSubmit;
    public GridView gridViewAnswer,gridViewSuggest;
    private MainActivityViewModel mViewModel;

    public ImageView imgViewQuestion;
    Logo[] logos;

    int[] image_list={
            R.drawable.blogger,
    };

    public char[] answer;
    String correct_answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //we can move all list setup part to view Model -- not enough time
//        MainActivityViewModel.Factory factory = new MainActivityViewModel.Factory();
//        mViewModel = new ViewModelProvider(this, factory).get(MainActivityViewModel.class);
        try {
            initView();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initView() throws FileNotFoundException {
        gridViewAnswer = findViewById(R.id.gridViewAnswer);
        gridViewSuggest = findViewById(R.id.gridViewSuggest);
        imgViewQuestion = findViewById(R.id.imgLogo);
        setupList();
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result="";
                for (int i = 0; i< Common.user_submit_answer.length; i++)
                    result+=String.valueOf(Common.user_submit_answer[i]);
                if (result.equals(correct_answer))
                {
                    Toast.makeText(MainActivity.this, "Finish ! This is"+result, Toast.LENGTH_SHORT).show();
                    Common.count = 0;
                    Common.user_submit_answer = new char[correct_answer.length()];

                    GridViewAnswerAdapter answerAdapter = new GridViewAnswerAdapter(setupNullList(),getApplicationContext());
                    gridViewAnswer.setAdapter(answerAdapter);
                    answerAdapter.notifyDataSetChanged();

                    GridViewSuggestAdapter suggestAdapter = new GridViewSuggestAdapter(suggestSource,getApplicationContext(),MainActivity.this);
                    gridViewSuggest.setAdapter(suggestAdapter);
                    suggestAdapter.notifyDataSetChanged();

                    try {
                        setupList();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Incorrect!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupList() throws FileNotFoundException {
        parseJson();
        Random random = new Random();
        Logo imageSelected = logos[random.nextInt(logos.length)];

        Picasso.get()
                .load(imageSelected.getLogoURL())
                .into(imgViewQuestion);

        correct_answer = imageSelected.getName();
        correct_answer = correct_answer.substring(correct_answer.lastIndexOf("/")+1);
        answer = correct_answer.toCharArray();
        Common.user_submit_answer = new char[answer.length];
        suggestSource.clear();

        for (char item:answer)
        {
            suggestSource.add(String.valueOf(item));
        }

        for (int i = answer.length;i<answer.length*2;i++)
            suggestSource.add(Common.alphabest_character[random.nextInt(Common.alphabest_character.length)]);

        Collections.shuffle(suggestSource);

        answerAdapter = new GridViewAnswerAdapter(setupNullList(),this);
        suggestAdapter = new GridViewSuggestAdapter(suggestSource,this,this);
        answerAdapter.notifyDataSetChanged();
        suggestAdapter.notifyDataSetChanged();

        gridViewSuggest.setAdapter(suggestAdapter);
        gridViewAnswer.setAdapter(answerAdapter);
    }

    private char[] setupNullList() {
        char result[] = new char[answer.length];
        for (int i=0; i<answer.length;i++)
            result[i]=' ';
        return result;
    }

    private void parseJson() throws FileNotFoundException {
        Gson gson = new Gson();
        System.out.println(new File(".").getAbsoluteFile());
        JsonReader reader = new JsonReader(new FileReader("assets/logo.json"));

        logos = gson.fromJson(String.valueOf(reader), Logo[].class);

    }
}