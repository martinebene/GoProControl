package com.mebene.goprocontrol;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import apigopro.core.CameraMode;
import apigopro.core.GoProHelper;
import apigopro.core.model.BacPacStatus;
import apigopro.core.model.CamFields;
import apigopro.core.model.ENCameraBoss;
import apigopro.core.model.ENCameraPowerStatus;
import apigopro.core.model.ENCameraReady;


public class MainActivity extends ActionBarActivity {

    private static final String _10_5_5_9 = "10.5.5.9";
    public static final int _POLLINGTIME = 4000;
    public static final int _RETRY_OPERATION = 3;
    public GoProHelper gp_helper;
    public CamFields camfields;
    public EditText editText;

    Button b_data, b_pl, b_v_mod, b_ph_mod, b_bur_mod, b_Pht_mod, b_res960, b_upside, b_del_lst, b_dell_all, b_locate, b_on, b_off, b_st;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        gp_helper = new GoProHelper(_10_5_5_9, 80, "martin123456");


        try {
            //camfields = gp_helper.getCameraSettings();
        } catch (Exception e) {
            e.printStackTrace();
        }


        setGUI();
    }

    private void setGUI() {

        editText = (EditText) findViewById(R.id.editText);
        b_on= (Button) findViewById(R.id.b_on);
        b_off= (Button) findViewById(R.id.b_off);
        b_data= (Button) findViewById(R.id.b_data);
        b_pl= (Button) findViewById(R.id.b_pl);
        b_st= (Button) findViewById(R.id.b_st);
        b_v_mod= (Button) findViewById(R.id.b_v_mod);
        b_ph_mod= (Button) findViewById(R.id.b_ph_mod);
        b_bur_mod= (Button) findViewById(R.id.b_bur_mod);
        b_Pht_mod= (Button) findViewById(R.id.b_Pht_mod);
        b_res960= (Button) findViewById(R.id.b_res960);
        b_upside= (Button) findViewById(R.id.b_upside);
        b_del_lst= (Button) findViewById(R.id.b_del_lst);
        b_dell_all= (Button) findViewById(R.id.b_dell_all);
        b_locate= (Button) findViewById(R.id.b_locate);



        b_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                try {
                    Log.i("tag", "aprete boton\n");Thread.sleep(1000);
                    camfields = gp_helper.getCameraSettings();
                    editText.setText(
                            "CamFields:\n" +
                                    "Cname: " + camfields.getCamname() + "\n" +
                                    "Version: " + camfields.getVersion() + "\n" +
                                    "Batery: " + camfields.getBattery() + "\n" +
                                    "BeepSound: " + camfields.getBeepSound() + "\n" +
                                    "BurstMode: " + camfields.getBurstMode() + "\n" +
                                    "Continuous shot: " + camfields.getContinuousShot() + "\n" +
                                    "Frames per second: " + camfields.getFramesPerSecond() + "\n" +
                                    "Locate: " + camfields.getLocate() + "\n" +
                                    "Mode: " + camfields.getMode() + "\n" +
                                    "Model: " + camfields.getModel() + "\n" +
                                    "Photo Resolution: " + camfields.getPhotoResolution() + "\n" +
                                    "UpDown: " + camfields.getUpdown() + "\n" +
                                    "Video On Card: " + camfields.getVideoOncard() + "\n" +
                                    "Photos On Card: " + camfields.getPhotosOncard() + "\n"

                    );

                    Log.i("tag", "aprete boton despues de traer dato\n");Thread.sleep(1000);
                    Log.i("tag", "" +


                                    "Cname: " + camfields.getCamname() + "\n" +
                                    "Version: " + camfields.getVersion() + "\n" +
                                    "Batery: " + camfields.getBattery() + "\n" +
                                    "BeepSound: " + camfields.getBeepSound() + "\n" +
                                    "BurstMode: " + camfields.getBurstMode() + "\n" +
                                    "Continuous shot: " + camfields.getContinuousShot() + "\n" +
                                    "Frames per second: " + camfields.getFramesPerSecond() + "\n" +
                                    "Locate: " + camfields.getLocate() + "\n" +
                                    "Mode: " + camfields.getMode() + "\n" +
                                    "Model: " + camfields.getModel() + "\n" +
                                    "Photo Resolution: " + camfields.getPhotoResolution() + "\n" +
                                    "UpDown: " + camfields.getUpdown() + "\n" +
                                    "Video On Card: " + camfields.getVideoOncard() + "\n" +
                                    "Photos On Card: " + camfields.getPhotosOncard()
                                                       );

                    Log.i("tag", "aprete boton despues de imprimir\n");Thread.sleep(1000);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        b_on.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                try {
                    gp_helper.turnOnCamera();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        b_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                try {
                    gp_helper.turnOffCamera();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        b_pl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                try {
                    gp_helper.startRecord();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        b_st.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                try {
                    gp_helper.stopRecord();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        b_v_mod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                try {
                    gp_helper.setCamMode(CameraMode.CAM_MODE_VIDEO);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        b_ph_mod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                try {
                    gp_helper.setCamMode(CameraMode.CAM_MODE_PHOTO);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        b_bur_mod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                try {
                    gp_helper.setCamMode(CameraMode.CAM_MODE_BURST);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        b_Pht_mod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                try {
                    gp_helper.setCamMode(CameraMode.CAM_MODE_TIMELAPSE);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        b_res960.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                try {
                    gp_helper.setCamVideoResolution(CameraMode.R_960_30);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        b_upside.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                try {
                    gp_helper.setCamUpDown(CameraMode.HEAD_UP);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        b_del_lst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                gp_helper.deleteLastFileOnSd();

            }
        });

        b_dell_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                gp_helper.deleteFilesOnSd();

            }
        });

        b_locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                try {
                    gp_helper.setCamLocate(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
