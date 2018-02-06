package com.example.lugman.tareaasinc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Editar extends AppCompatActivity {
    Button btn2;
    EditText ed4,ed5,ed6;
    String id;
    ProgressDialog pd;

TextView ed7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);
        btn2 =  findViewById(R.id.newC);
        ed4 = findViewById(R.id.editText4);
        ed5 = findViewById(R.id.editText5);
        ed6 = findViewById(R.id.editText6);
        ed7 = findViewById(R.id.textView2);
        Intent i =  getIntent();
        id =  i.getExtras().getString("ID");

        ed7.setText(id);

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Editar.Añadir aña =  new Editar.Añadir(ed4.getText().toString(),ed5.getText().toString(),ed6.getText().toString(),"b",id);
                aña.execute();
            }
        });
    }
    private class Añadir  extends AsyncTask<String,String,Void> {
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


                List l = new LinkedList();
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
        Intent in = new Intent(Editar.this,MainActivity.class);
            startActivity(in);
            pd.cancel();
//
        }

        @Override
        protected void onPreExecute() {
            pd= new ProgressDialog(Editar.this);
            pd.setProgressStyle(R.style.Widget_AppCompat_ProgressBar_Horizontal);
            pd.setMessage("loading");
            pd.show();
            super.onPreExecute();
        }
    }
}
