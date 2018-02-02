package com.example.lugman.tareaasinc;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.*;



import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
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
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;



import static android.R.attr.name;

public class MainActivity extends AppCompatActivity {
    Button btn,btn2;
    ListView list,list2;
    Asincrona tarea;
    EditText ed1,ed2,ed3,ed4,ed5,ed6,ed7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tarea = new Asincrona();
        tarea.execute();
        btn =  findViewById(R.id.newB);
        btn2 =  findViewById(R.id.newC);

        ed1 = findViewById(R.id.editText);
        ed2 = findViewById(R.id.editText2);
        ed3 = findViewById(R.id.editText3);
        ed4 = findViewById(R.id.editText4);
        ed5 = findViewById(R.id.editText5);
        ed6 = findViewById(R.id.editText6);
        ed7 = findViewById(R.id.editText7);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Añadir aña =  new Añadir(ed1.getText().toString(),ed2.getText().toString(),ed3.getText().toString(),"a","");
                aña.execute();
                ed1.setText("");
                ed2.setText("");
                ed3.setText("");

            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Añadir aña =  new Añadir(ed4.getText().toString(),ed5.getText().toString(),ed6.getText().toString(),"b",ed7.getText().toString());
                aña.execute();
            }
        });
        list =  findViewById(R.id.list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Añadir aña =  new Añadir(ed4.getText().toString(),ed5.getText().toString(),ed6.getText().toString(),"2",String.valueOf(i));
                aña.execute();
            }
        });

    }

    private class Asincrona extends AsyncTask <Void, String, Void> {
        String MiJSON = "";


        @Override
        protected Void doInBackground(Void... params) {
            URL url = null;
            try {
                url = new URL("http://www.carlossilla.com.es/android/personas.php");
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
        String Nombre,Apellidos,Edad,tipo,id;
        String MiJSON2;

        public Añadir(String nombre, String apellidos, String edad,String tipo,String id) {
            this.Nombre = nombre;
            this.id = id;
            this.Apellidos = apellidos;
            this.Edad = edad;
            this.tipo = tipo;
        }

        @Override
        protected Void doInBackground(String... strings) {

            URL url= null;
            HttpURLConnection urlConnection = null;
            try {
                if (tipo.equals("a")){
                    url = new URL("http://www.carlossilla.com.es/android/insertar.php");
                }else if(tipo.equals("b")){
                    url = new URL("http://www.carlossilla.com.es/android/update.php");
                }else {
                    url = new URL("http://www.carlossilla.com.es/android/delete.php");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                urlConnection.setDoOutput(true);
                urlConnection.setChunkedStreamingMode(0);
                urlConnection.setRequestMethod("POST");



//                List<NameValuePair> params = new ArrayList<NameValuePair>();


                OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());

//                BufferedWriter writer = new BufferedWriter (new OutputStreamWriter(out, "UTF-8"));
                DataOutputStream writer = new DataOutputStream(urlConnection.getOutputStream());

                JSONObject jsonObj = new JSONObject();
                //Añadimos el nombre, apellidos y email del usuario
                try {
                    jsonObj.put("id",id);
                    jsonObj.put("nombre",Nombre);
                    jsonObj.put("apellidos", Apellidos);
                    jsonObj.put("edad", Edad);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                List  l = new LinkedList();
                l.addAll(Arrays.asList(jsonObj));
                String jsonString = l.toString();


                String pasar = jsonObj.toString();
                String urlParameters = "json="+jsonString;
//                Log.d("ENVIAR",urlParameters);
                writer.writeBytes(urlParameters);
                writer.flush();
                writer.close();

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                in = new BufferedInputStream(urlConnection.getInputStream());

                byte[] contents = new byte[1024];
                int bytesRead = 0;
                String strFileContents=null;
                while((bytesRead = in.read(contents)) != -1) {
                    if (strFileContents == null){
                        strFileContents = new String(contents, 0, bytesRead);
                    }
//                    strFileContents += new String(contents, 0, bytesRead);
                }
                 MiJSON2  = strFileContents;


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
            Asincrona tarea2 = new Asincrona();
            tarea2.execute();
//            tarea.execute();
//            try {
//                Log.d("REPETIDO",MiJSON2);
////                jsonRealizar2(MiJSON2);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
        }
    }
    private void jsonRealizar(String json) throws JSONException {
                        JSONArray  arr = new JSONArray(json);
                        JSONObject obj = arr.getJSONObject(1);

                        ArrayList<String> lista = new ArrayList<String>();
                        ArrayList<String> lista2 = new ArrayList<String>();
                                for (int i = 0; i< arr.length(); i++){
                                    lista.add(arr.getJSONObject(i).getString("nombre")+" (id:"+arr.getJSONObject(i).getString("id")+")");
                                    lista2.add("id");
                                }

                        ArrayAdapter<String> adapter =  new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,lista);
                        list.setAdapter(adapter);

//        Log.d("BUENA",json);

    }
//    private void jsonRealizar2(String json) throws JSONException {
//        TextView tv =  findViewById(R.id.textView);
//        tv.setText(json);
//    }
    }


