package com.example.lugman.tareaasinc;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Asincrona tarea = new Asincrona();
        tarea.execute();


    }

    private class Asincrona extends AsyncTask <Void, String, Void> {
        Asincrona() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            URL url = null;
            try {
                url = new URL("http://lugman.com.es/index.php");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                InputStream in = null;
                try {


                    in = new BufferedInputStream(urlConnection.getInputStream());
                    
                    byte[] contents = new byte[1024];

                    int bytesRead = 0;
                    String strFileContents = "";
                    while((bytesRead = in.read(contents)) != -1) {
                        strFileContents += new String(contents, 0, bytesRead);
                    }

//                    try {
//                        JSONObject obj =  new JSONObject(strFileContents);
//                        JSONArray  arr = obj.getJSONArray("nombre");
//                        JSONObject obj2 = arr.get(0);
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }


                    publishProgress(strFileContents);
                    in.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } finally {
                urlConnection.disconnect();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "Pree", Toast.LENGTH_SHORT).show();

        }



        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Toast.makeText(MainActivity.this, values[0], Toast.LENGTH_SHORT).show();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(MainActivity.this, "Fin", Toast.LENGTH_SHORT).show();
        }


    }
}


