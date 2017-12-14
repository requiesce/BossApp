package com.example.x.bossbandsapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by x on 7/12/2017.
 */

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(500);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    Intent openStartingPoint = new Intent("android.intent.action.STARTINGPOINT");
                    startActivity(openStartingPoint);
                }
            }
        };
        timer.start();
    }

}
