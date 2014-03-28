package org.chamedu.stifco;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.ListView;



public class Liste extends Activity implements View.OnClickListener {

	private String jsonString;
	JSONObject jsonResponse;
	JSONArray arrayJson;

	ArrayList<String> items = new ArrayList<String>();


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.liste);

		
		// Traitement du textView en autocompl�tion � partir de la source Json
		jsonString = getIntent().getExtras().getString("liste");
		//Toast.makeText(Liste.this, jsonString, Toast.LENGTH_LONG).show();

		try {
			jsonResponse = new JSONObject(jsonString);
			// Création du tableau général à partir d'un JSONObject
			JSONArray jsonArray = jsonResponse.getJSONArray("propositions");

			// Pour chaque élément du tableau
			for (int i = 0; i < jsonArray.length(); i++) {

				// Création d'un tableau élément à partir d'un JSONObject
				JSONObject jsonObj = jsonArray.getJSONObject(i);

				// Récupération à partir d'un JSONObject nommé
				//JSONObject fields  = jsonObj.getJSONObject("fields");

				// Récupération de l'item qui nous intéresse
				//String nom = fields.getString("id");
				String chp1 = jsonObj.getString("id");
				String chp2 = jsonObj.getString("ville");
				String chp3 = jsonObj.getString("lieu");
				String chp4 = jsonObj.getString("places");
				String chp5 = jsonObj.getString("gare");
				// Ajout dans l'ArrayList
				items.add("Horaires: " + chp1+ "\n" + "Ville: " + chp2 +  "\n"  + "Lieu: " + chp3 +  "\n"  + "Place: " + chp4 +  "\n" + "Gare: " + chp5);		
			}
			 
			
			ListView lv = (ListView) findViewById(R.id.listView);
			
			ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, items);
			
			lv.setAdapter(adapter1);
			 

		} catch (JSONException e) {
			e.printStackTrace();
		}

					
	}


	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub

	}
	
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}


	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}






	



}









