package com.example.nationinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    ArrayList<Contry> countryList;
    Spinner spinner;
    TextView textView;
    TextView textView2;
    TextView textView3;
    ImageView imageView;
    private String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       spinner= (Spinner) findViewById(R.id.spinner);
       textView= (TextView) findViewById(R.id.textView);
        textView2= (TextView) findViewById(R.id.textView2);
        textView3= (TextView) findViewById(R.id.textView3);
        imageView= (ImageView) findViewById(R.id.imageView);
       // new GetCountry().execute("http://api.geonames.org/countryInfoJSON?formatted=true&lang=it&username=nannkau&style=full");
//        new ReadJson().execute("http://api.geonames.org/findNearbyPlaceNameJSON?lat=47.3&lng=9&username=nannkau");
        new GetContacts().execute();
        countryList= new ArrayList<Contry>();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                String name =spinner.getItemAtPosition(position).toString();
                for(Contry a :countryList){
                    if(a.getName().equals(name)){
                        textView.setText("Name: "+a.getName());
                        textView2.setText("Population: "+a.getPopulation()+"(people)");
                        textView3.setText("Area:" +a.getArea()+"(km)");
                        Glide.with(MainActivity.this)
                                .load(a.getLinkFlag())
                                .into(imageView);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this,"Json Data is downloading",Toast.LENGTH_LONG).show();
///flags/x/code.gif
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "http://api.geonames.org/countryInfoJSON?formatted=true&lang=it&username=nannkau&style=full";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("geonames");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        String name = c.getString("countryName");
                        String population = c.getString("population");
                        String area = c.getString("areaInSqKm");
                        String linkFlag="http://api.geonames.org/flags/x/"+c.getString("fipsCode").toLowerCase()+".gif";
                      Contry country= new Contry(name,population,area,linkFlag);

                        // adding each child node to HashMap key => value

                        // adding contact to contact list
                        countryList.add(country);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ArrayList<String > arrayList= new ArrayList<>();
            for(Contry c: countryList){
                arrayList.add(c.getName());
            }
           ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,R.layout.support_simple_spinner_dropdown_item,arrayList);
            adapter.setDropDownViewResource(android.R.layout
                    .simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

        }
//    private class ReadJson extends AsyncTask<String, Void, String> {


//        protected String doInBackground(String... strings) {
//
//            StringBuilder content= new StringBuilder();
//            try {
//                URL url= new URL(strings[0]);
//                InputStreamReader inputStreamReader= new InputStreamReader(url.openConnection().getInputStream());
//                BufferedReader bufferedReader= new BufferedReader(inputStreamReader);
//                String line="";
//                while ((line=bufferedReader.readLine())!=null){
//                    content.append(line);
//                }
//                bufferedReader.close();
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return  content.toString();
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//
//            Toast.makeText(MainActivity.this,s+"haha",Toast.LENGTH_SHORT).show();
//            countryList= new ArrayList<>();
//
//                try {
//                    JSONObject jsonObj = new JSONObject(result);
//
//                    // Getting JSON Array node
//                    JSONArray ct = jsonObj.getJSONArray("geonames");
//
//                    // looping through All Contacts
//                    for (int i = 0; i < ct.length(); i++) {
//                        JSONObject c = ct.getJSONObject(i);
//                        String name = c.getString("countryName");
//                        String population = c.getString("population");
//                        String area = c.getString("areaInSqKm");
//
//
//
//
//
//                        // tmp hash map for single contact
//                        Contry country =  new Contry(name,population,area);
//
//                        // adding each child node to HashMap key => value
//
//
//
//                        // adding contact to contact list
//                        countryList.add(country);
//                    }
//
//
//            ArrayList<String> arrayList= new ArrayList<String>();
//            for (Contry a : countryList){
//                arrayList.add(a.getName());
//            }
//
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_expandable_list_item_1,arrayList);
//            spinner.setAdapter(adapter);
//        } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//         }

//}
}}
