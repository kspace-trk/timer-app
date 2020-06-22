package com.example.MyRepo04A;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.media.MediaPlayer;
import android.media.AudioManager;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    boolean inputType_m = false;                    //minute押したかどうか初期値
    boolean inputType_s = false;                    //second押したかどうか処理
    long minute1 = 0;                              //分の値初期値
    long second1 = 0;                              //秒の値初期値
    boolean nowStatus = false;                      //タイマー稼働状況
    long mm = 0;
    long ss = 0;
    private MediaPlayer mediaPlayer;
    boolean nowPlayStatus = false;
    boolean changeAlarm = false;





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }

    //////////////////ここからMediaPlayer////////////////////////////////////////////////////

        boolean audioSetup(){
        boolean fileCheck = false;

        // rawにファイルがある場合
            if (changeAlarm) {
                mediaPlayer = MediaPlayer.create(this, R.raw.finish_alarm);
            }else {
                mediaPlayer = MediaPlayer.create(this, R.raw.minute_alarm);
            }
        // 音量調整を端末のボタンに任せる
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        fileCheck = true;

        return fileCheck;

    }






    private void audioPlay() {

        if (mediaPlayer == null) {
            // audio ファイルを読出し
            if (!audioSetup()) {
                Toast.makeText(getApplication(), "Error: read audio file", Toast.LENGTH_SHORT).show();
                return;

            }
        }


        // 再生する
        mediaPlayer.start();





        nowPlayStatus = true;

        // 終了を検知するリスナー
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                audioStop();

                if (changeAlarm) {

                    TextView timer_buttonView = findViewById(R.id.timer_button);
                    timer_buttonView.setText("開始");



                    nowPlayStatus = false;

                }


            }
        });
    }




    private void audioStop() {
        // 再生終了
        mediaPlayer.stop();
        // リセット
        mediaPlayer.reset();
        // リソースの解放
        mediaPlayer.release();

        mediaPlayer = null;

        nowPlayStatus = false;


    }



    //////////////////ここまでMediaPlayer////////////////////////////////////////////////////



    //////////////////ここからCountDownTimer/////////////////////////////////////////////////




    public class CountDown extends CountDownTimer{

        CountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);

        }

        @Override
        public void onFinish() {
            // カウントダウン完了後に呼ばれる
            nowStatus = false;
            changeAlarm = true;
            audioPlay();

            TextView timer_buttonView = findViewById(R.id.timer_button);
            timer_buttonView.setText("タイマー停止");

            TextView triangleUp_mView = findViewById(R.id.triangleUp_m);          ///矢印非表示
            triangleUp_mView.setVisibility(View.VISIBLE);

            TextView triangleDown_mView = findViewById(R.id.triangleDown_m);          ///矢印非表示
            triangleDown_mView.setVisibility(View.VISIBLE);

            TextView triangleUp_sView = findViewById(R.id.triangleUp_s);          ///矢印非表示
            triangleUp_sView.setVisibility(View.VISIBLE);

            TextView triangleDown_sView = findViewById(R.id.triangleDown_s);          ///矢印非表示
            triangleDown_sView.setVisibility(View.VISIBLE);

            minute1 = 0;
            second1 = 0;

        }

        @Override
        public void onTick(long millisUntilFinished) {
            // インターバル(countDownInterval)毎に呼ばれる
            mm = millisUntilFinished / 1000 / 60;
            ss = millisUntilFinished / 1000 % 60;

            if (mm == 1 && ss == 0){

                changeAlarm = false;

                audioPlay();

                TextView minuteView = findViewById(R.id.minute);
                String mm_s = String.valueOf(mm);

                minuteView.setText("0" + mm_s);

                TextView secondView = findViewById(R.id.second);
                String ss_s = String.valueOf(ss);

                secondView.setText("0" + ss_s);


            }else {

                TextView minuteView = findViewById(R.id.minute);

                if (mm < 10) {

                    String mm_s = String.valueOf(mm);

                    minuteView.setText("0" + mm_s);

                } else {

                    minuteView.setText(String.valueOf(mm));

                }


                TextView secondView = findViewById(R.id.second);

                if (ss < 10) {

                    String ss_s = String.valueOf(ss);

                    secondView.setText("0" + ss_s);

                } else {

                    secondView.setText(String.valueOf(ss));

                }

            }


        }
    }





    /////////////ここからボタン処理//////////////////////////



    CountDown timer;


    public void timer_button(View v) {


            if (nowPlayStatus) {

                audioStop();

                TextView timer_buttonView = findViewById(R.id.timer_button);
                timer_buttonView.setText("開始");

                nowPlayStatus = false;



            } else {

                if (nowStatus) {




                    // 中止
                    timer.cancel();

                    timer = null;

                    nowStatus = false;

                    minute1 = mm;
                    second1 = ss;

                    TextView timer_buttonView = findViewById(R.id.timer_button);
                    timer_buttonView.setText("開始");

                    TextView triangleUp_mView = findViewById(R.id.triangleUp_m);          ///矢印表示
                    triangleUp_mView.setVisibility(View.VISIBLE);

                    TextView triangleDown_mView = findViewById(R.id.triangleDown_m);          ///矢印表示
                    triangleDown_mView.setVisibility(View.VISIBLE);

                    TextView triangleUp_sView = findViewById(R.id.triangleUp_s);          ///矢印表示
                    triangleUp_sView.setVisibility(View.VISIBLE);

                    TextView triangleDown_sView = findViewById(R.id.triangleDown_s);          ///矢印表示
                    triangleDown_sView.setVisibility(View.VISIBLE);


                } else {

                    // 3分= 3x60x1000 = 180000 msec
                    long countNumber = minute1 * 60000 + second1 * 1000;  ////カウントする時間代入

                    if (countNumber == 0){

                    }else {


                        timer = new CountDown(countNumber, 1000);

                        // 開始
                        timer.start();

                        TextView triangleUp_mView = findViewById(R.id.triangleUp_m);          ///矢印非表示
                        triangleUp_mView.setVisibility(View.INVISIBLE);

                        TextView triangleDown_mView = findViewById(R.id.triangleDown_m);          ///矢印非表示
                        triangleDown_mView.setVisibility(View.INVISIBLE);

                        TextView triangleUp_sView = findViewById(R.id.triangleUp_s);          ///矢印非表示
                        triangleUp_sView.setVisibility(View.INVISIBLE);

                        TextView triangleDown_sView = findViewById(R.id.triangleDown_s);          ///矢印非表示
                        triangleDown_sView.setVisibility(View.INVISIBLE);

                        nowStatus = true;

                        TextView timer_buttonView = findViewById(R.id.timer_button);
                        timer_buttonView.setText("停止");
                    }}

                }
            }




    public void reset_button(View v){

        if (nowStatus){
            timer.cancel();

            timer = null;

            minute1 = 0;
            second1 = 0;

            TextView timer_buttonView = findViewById(R.id.timer_button);
            timer_buttonView.setText("開始");

            TextView minuteView = findViewById(R.id.minute);
            minuteView.setText("0" + minute1);

            TextView secondView = findViewById(R.id.second);
            secondView.setText("0" + second1);

            TextView triangleUp_mView = findViewById(R.id.triangleUp_m);          ///矢印表示
            triangleUp_mView.setVisibility(View.VISIBLE);

            TextView triangleDown_mView = findViewById(R.id.triangleDown_m);          ///矢印表示
            triangleDown_mView.setVisibility(View.VISIBLE);

            TextView triangleUp_sView = findViewById(R.id.triangleUp_s);          ///矢印表示
            triangleUp_sView.setVisibility(View.VISIBLE);

            TextView triangleDown_sView = findViewById(R.id.triangleDown_s);          ///矢印表示
            triangleDown_sView.setVisibility(View.VISIBLE);

            nowStatus = false;

        }else {

            minute1 = 0;
            second1 = 0;

            TextView minuteView = findViewById(R.id.minute);
            minuteView.setText("0" + minute1);

            TextView secondView = findViewById(R.id.second);
            secondView.setText("0" + second1);

        }

    }


    /////////////ここまでボタン処理//////////////////////////





    //////////////ここまでCountDownTimer///////////////////////////////////////////////////////







    //////////////ここから分の処理//////////////////////////


    public void minUp(View v) {

        if (nowStatus){



        }else {

            if (inputType_m) {


                TextView minuteView = findViewById(R.id.minute);

                minute1 += 10;                                //数値カウントアップ

                String minute1_s = String.valueOf(minute1);           //int(minute1)をString(minute1_s)に変換

                minuteView.setText(minute1_s);                  //minute1出力


            } else {

                TextView minuteView = findViewById(R.id.minute);

                minute1++;                                           //数値カウントアップ

                String minute1_s = String.valueOf(minute1);           //int(minute1)をString(minute1_s)に変換

                if (minute1 <= 9) {

                    minuteView.setText("0" + minute1_s);              //分の数値が1桁の時のminute出力

                } else {

                    minuteView.setText(minute1_s);                  //分の数値が2桁の時のminute出力

                }


            }
        }
    }


    public void minDown(View v) {

        if (nowStatus){

        }else {

            if (inputType_m) {

                if (minute1 == 0) {


                    TextView minuteView = findViewById(R.id.minute);

                    String minute1_s = String.valueOf(minute1);           //int(minute1)をString(minute1_s)に変換

                    minuteView.setText("0" + minute1_s);                  //minute1出力


                } else {

                    if (minute1 >= 10 && minute1 < 20) {

                        minute1 -= 10;

                        TextView minuteView = findViewById(R.id.minute);

                        String minute1_s = String.valueOf(minute1);           //int(minute1)をString(minute1_s)に変換

                        minuteView.setText("0" + minute1_s);                  //minute1出力

                    } else if (minute1 <= 9) {
                        minute1 = 0;
                        TextView minuteView = findViewById(R.id.minute);

                        String minute1_s = String.valueOf(minute1);           //int(minute1)をString(minute1_s)に変換

                        minuteView.setText("0" + minute1_s);                  //minute1出力

                    } else {

                        TextView minuteView = findViewById(R.id.minute);

                        minute1 -= 10;                                //数値ダウン

                        String minute1_s = String.valueOf(minute1);           //int(minute1)をString(minute1_s)に変換

                        minuteView.setText(minute1_s);                  //minute1出力


                    }


                }


            } else {

                if (minute1 == 0) {
                    TextView minuteView = findViewById(R.id.minute);

                    String minute1_s = String.valueOf(minute1);           //int(minute1)をString(minute1_s)に変換

                    minuteView.setText("0" + minute1_s);                  //minute1出力
                } else {

                    TextView minuteView = findViewById(R.id.minute);

                    minute1--;                                           //数値カウントアップ

                    String minute1_s = String.valueOf(minute1);           //int(minute1)をString(minute1_s)に変換

                    if (minute1 <= 9) {

                        minuteView.setText("0" + minute1_s);              //分の数値が1桁の時のminute出力

                    } else {

                        minuteView.setText(minute1_s);                  //分の数値が2桁の時のminute出力

                    }

                }


            }
        }
    }


    public void minute(View v) {

        if (inputType_m) {

            inputType_m = false;

            TextView minuteViewWhite = findViewById(R.id.minute);
            minuteViewWhite.setBackgroundColor(Color.rgb(249, 249, 249));          //minuteView背景色249


        } else {

            inputType_m = true;

            TextView minuteViewGray = findViewById(R.id.minute);
            minuteViewGray.setBackgroundColor(Color.rgb(220, 220, 220));          //minuteView背景色グレー


        }
    }


    //////////////////////ここまで分の処理//////////////////


    /////////////////////////////ここから秒の処理///////////


    public void secUp(View v) {

        if (nowStatus){

        }else {

            if (inputType_s) {

                TextView secondView = findViewById(R.id.second);

                second1 += 10;                                //second1を60に設定

                if (second1 >= 59) {

                    second1 = 59;                                //second1を60に設定

                    String second1_s = String.valueOf(second1);           //int(second1)をString(second1_s)に変換

                    secondView.setText(second1_s);                  //second1出力

                } else {

                    String second1_s = String.valueOf(second1);           //int(second1)をString(second1_s)に変換

                    secondView.setText(second1_s);                  //second1出力
                }


            } else {

                TextView secondView = findViewById(R.id.second);

                second1++;                                           //数値カウントアップ

                if (second1 <= 9) {

                    String second1_s = String.valueOf(second1);           //int(second1)をString(second1_s)に変換

                    secondView.setText("0" + second1_s);              //分の数値が1桁の時のsecond出力

                } else if (second1 >= 59) {

                    second1 = 59;

                    String second1_s = String.valueOf(second1);           //int(second1)をString(second1_s)に変換

                    secondView.setText(second1_s);                  //分の数値が2桁の時のsecond出力

                } else {

                    String second1_s = String.valueOf(second1);           //int(second1)をString(second1_s)に変換

                    secondView.setText(second1_s);                  //分の数値が2桁の時のsecond出力
                }


            }

        }


    }


    public void secDown(View v) {

        if (nowStatus){

        }else {

            if (inputType_s) {

                second1 -= 10;

                TextView secondView = findViewById(R.id.second);

                if (second1 <= 9 && second1 >= 0) {

                    String second1_s = String.valueOf(second1);           //int(minute1)をString(minute1_s)に変換

                    secondView.setText("0" + second1_s);                  //minute1出力

                } else if (second1 < 0) {

                    second1 = 0;

                    String second1_s = String.valueOf(second1);           //int(minute1)をString(minute1_s)に変換

                    secondView.setText("0" + second1_s);                  //minute1出力

                } else {

                    String second1_s = String.valueOf(second1);           //int(minute1)をString(minute1_s)に変換

                    secondView.setText(second1_s);                  //minute1出力

                }


            } else {

                second1--;                                              //数値カウントダウン

                TextView secondView = findViewById(R.id.second);

                if (second1 <= 9 && second1 >= 0) {

                    String second1_s = String.valueOf(second1);           //int(minute1)をString(minute1_s)に変換

                    secondView.setText("0" + second1_s);              //分の数値が1桁の時のminute出力

                } else if (second1 < 0) {

                    second1 = 0;

                    String second1_s = String.valueOf(second1);           //int(minute1)をString(minute1_s)に変換

                    secondView.setText("0" + second1_s);                  //分の数値が2桁の時のminute出力

                } else {

                    String second1_s = String.valueOf(second1);           //int(minute1)をString(minute1_s)に変換

                    secondView.setText(second1_s);                  //分の数値が2桁の時のminute出力

                }

            }

        }



    }


    public void second(View v) {

        if (inputType_s) {

            inputType_s = false;

            TextView secondViewWhite = findViewById(R.id.second);
            secondViewWhite.setBackgroundColor(Color.rgb(249, 249, 249));          //minuteView背景色249


        } else {

            inputType_s = true;

            TextView secondViewGray = findViewById(R.id.second);
            secondViewGray.setBackgroundColor(Color.rgb(220, 220, 220));          //minuteView背景色グレー


        }
    }

    //////////////////////////////////ここまで秒の処理//////


}