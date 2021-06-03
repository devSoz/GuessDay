package com.example.guessday;
import java.time.DayOfWeek;

import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import java.util.Calendar;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity
{
    private int counter=30;
    private Integer intScore = 0;
    private static final String TAG = "GameActivity";
    private static String dayOfTheWeek;
    private CountDownTimer timer1;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_main);
        final TextView counttime = findViewById(R.id.txtTimer);

        timer1= new CountDownTimer (50000, 1000)
        {
            @Override
            public void onTick(long millisUntilFinished)
            {
                counttime.setText(String.valueOf(counter));
                counter--;
                if (counter<=0)
                {
                    cancel();
                    storeScore();
                }
            }

            @Override
            public void onFinish()
            {
                counttime.setText("Round over");
                storeScore();

            }
        }.start();
        initialiseNormal();
        listenerNormal();
    }

    private void initialiseNormal()
    {
        //LocalDate date = createRandomDate(1920,2020);
        //DayOfWeek day = date.getDayOfWeek();
        setScore(intScore);
        Calendar calenderNormal =  new GregorianCalendar(2021, 2, 20); // Calendar.getInstance();

        int day = createRandomIntBetween(1, 28);
        int month = createRandomIntBetween(1, 12);
        int year = createRandomIntBetween(2000, 2050);
        calenderNormal.set(year,month,day);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        dayOfTheWeek = sdf.format(calenderNormal.getTime());
        sdf = new SimpleDateFormat("dd-MMM-yyyy");
        String strDate = sdf.format(calenderNormal.getTime());

        TextView tvDate = (TextView) findViewById(R.id.date1);
        tvDate.setText(strDate);
        Log.d(TAG,"beforeList");
        List<String> list = new ArrayList<>();

        list.add("Monday");
        list.add("Tuesday");
        list.add("Wednesday");
        list.add("Thursday");
        list.add("Friday");
        list.add("Saturday");
        list.add("Sunday");
        list.remove(new String(dayOfTheWeek));
        Log.d(TAG,"beforeRb");
        RadioButton rb[]=new RadioButton[4];
        rb[0]=(RadioButton) findViewById(R.id.rbNormal1);
        rb[1]=(RadioButton) findViewById(R.id.rbNormal2);
        rb[2]=(RadioButton) findViewById(R.id.rbNormal3);
        rb[3]=(RadioButton) findViewById(R.id.rbNormal4);

        int temp = (int)(Math.random() * (4));
        Log.d(TAG, String.valueOf(temp));

        rb[temp].setText(dayOfTheWeek);

        Log.d(TAG, "ygvh");
        for (int j=0; j<4; j++)
        {
            if(j!=temp)
            {
                String option = getRandomElement(list);
                rb[j].setText(option);
                list.remove(new String(option));
            }
        }

         }

    private String getRandomElement(List<String> list)
    {
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }

    private void setScore(Integer i)
    {
        TextView tvScore = (TextView) findViewById(R.id.scoreNormal);
        tvScore.setText(String.valueOf(i));
    }
    private void listenerNormal()
    {

        Button b1 = (Button)findViewById(R.id.btnSubmitNormal1);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                RadioGroup Rg1 = (RadioGroup)findViewById(R.id.radioGroupNormal);

                int selectedOption = Rg1.getCheckedRadioButtonId();
                if(selectedOption==(-1))
                {
                    Toast.makeText(GameActivity.this, "Select an option", Toast.LENGTH_SHORT).show();
                }
                else {
                    RadioButton rbSelected = (RadioButton) findViewById(selectedOption);
                    String strSelected = rbSelected.getText().toString();

                    if (strSelected.equals(dayOfTheWeek))
                    {
                        intScore += 10;
                        setBackground(Color.GREEN);
                        Toast.makeText(GameActivity.this, "Your next challenge is here....", Toast.LENGTH_SHORT).show();
                    }
                    else
                        {
                            counter -= 5;
                            intScore -= 5;
                            if (counter<=0)
                            {
                                timer1.cancel();
                                storeScore();

                            }
                            else {
                                setBackground(Color.RED);
                                Toast.makeText(GameActivity.this, "Oops...You are losing 5 points..Your next challenge is here...", Toast.LENGTH_LONG).show();

                            }
                            rbSelected.setChecked(false);
                    }
                    initialiseNormal();
                }
        }
        });
    }

    private void storeScore()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        Integer highScore;
        highScore = Integer.parseInt(sharedPreferences.getString("highScore","0")) ;

        if (intScore>highScore)
        {
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putString("highScore", String.valueOf(intScore));
            myEdit.commit();
        }
        //add alert here
        setScore(intScore);
        AlertDialog.Builder aBuilder=new AlertDialog.Builder(GameActivity.this);
        aBuilder.setMessage(R.string.dialog_message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent2 = new Intent(GameActivity.this, MainActivity.class);
                        startActivity(intent2);
                    }
                });

        AlertDialog alert=aBuilder.create();
        alert.show();


    }

    @SuppressLint("ResourceAsColor")

    private void setBackground(Integer c)
    {
        RelativeLayout layout1 = (RelativeLayout) findViewById(R.id.layout1);
        layout1.setBackgroundColor(c);
    }

    private static int createRandomIntBetween(int start, int end) {
        return start + (int) Math.round(Math.random() * (end - start));
    }
/*
    public static LocalDate createRandomDate(int startYear, int endYear) {
        int day = createRandomIntBetween(1, 28);
        int month = createRandomIntBetween(1, 12);
        int year = createRandomIntBetween(startYear, endYear);
        return LocalDate.of(year, month, day);
    }*/

    }