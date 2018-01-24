package com.example.lugman.tareaasinc;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.*;



import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static android.R.attr.name;

public class MainActivity extends AppCompatActivity {
    Button btn;
    ListView list;
    Asincrona tarea;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         tarea = new Asincrona();
        tarea.execute();
        btn =  findViewById(R.id.newB);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Añadir aña =  new Añadir("LECHE");
                aña.execute();
            }
        });

        list =  findViewById(R.id.list);
    }

    private class Asincrona extends AsyncTask <Void, String, Void> {
        String MiJSON = "";


        @Override
        protected Void doInBackground(Void... params) {
            URL url = null;
            try {
                url = new URL("http://www.lugman.com.es/tienda/php/datosAndroid.php");
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
                    String strFileContents=null;
                    while((bytesRead = in.read(contents)) != -1) {
                        if (strFileContents == null){
                            strFileContents = new String(contents, 0, bytesRead);
                        }
                        strFileContents += new String(contents, 0, bytesRead);
                    }
                    MiJSON  = strFileContents;




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
//            Toast.makeText(MainActivity.this, "Pree", Toast.LENGTH_SHORT).show();

        }



        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
//            Toast.makeText(MainActivity.this, values[0], Toast.LENGTH_SHORT).show();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                jsonRealizar(MiJSON);
            } catch (JSONException e) {
                e.printStackTrace();
            }

//            Toast.makeText(MainActivity.this, "Fin", Toast.LENGTH_SHORT).show();
        }


    }
    private class Añadir  extends AsyncTask <String,String,Void>{
        String Producto;
        Añadir(String aña){
         Producto=aña;
        }

        @Override
        protected Void doInBackground(String... strings) {

            URL url= null;
            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                urlConnection.setDoOutput(true);
                urlConnection.setChunkedStreamingMode(0);
                urlConnection.setRequestMethod("POST");


                OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());

                BufferedWriter writer = new BufferedWriter (new OutputStreamWriter(out, "UTF-8"));



                String donnees = URLEncoder.encode("Nombre", "UTF-8")+ "="+URLEncoder.encode("LECHE", "UTF-8");

                writer.write(donnees);

                writer.flush();

                writer.close();


                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            tarea.execute();
        }
    }

    private void jsonRealizar(String json) throws JSONException {
                        JSONArray  arr = new JSONArray(json);
                        JSONObject obj = arr.getJSONObject(1);
                        String TXT = obj.getString("Nombre");
                        ArrayList<String> lista = new ArrayList<String>();


                                for (int i = 0; i< arr.length(); i++){
                                    lista.add(arr.getJSONObject(i).getString("Nombre"));
                                }


                        ArrayAdapter<String> adapter =  new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,lista);
                        list.setAdapter(adapter);

        Log.d("BUENA",json);

    }
}


