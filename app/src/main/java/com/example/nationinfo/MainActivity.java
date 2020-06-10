package com.example.nationinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner= (Spinner) findViewById(R.id.spinner);
       // new GetCountry().execute("http://api.geonames.org/countryInfoJSON?formatted=true&lang=it&username=nannkau&style=full");
        new GetCountry().execute("http://api.geonames.org/findNearbyPlaceNameJSON?lat=47.3&lng=9&username=nannkau");

    }
    private class GetCountry extends AsyncTask<String, Void, String> {


        protected String doInBackground(String... strings) {

            StringBuilder content= new StringBuilder();
            try {
                URL url= new URL(strings[0]);
                InputStreamReader inputStreamReader= new InputStreamReader(url.openConnection().getInputStream());
                BufferedReader bufferedReader= new BufferedReader(inputStreamReader);
                String line="";
                while ((line=bufferedReader.readLine())!=null){
                    content.append(line);
                }
                bufferedReader.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return  content.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Toast.makeText(MainActivity.this,s,Toast.LENGTH_SHORT).show();
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
         }

}}
