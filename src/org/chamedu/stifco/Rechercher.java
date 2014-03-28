package org.chamedu.stifco;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import network.OnResultListener;
import network.RestClient;

import org.apache.http.HttpException;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;


public class Rechercher extends Activity implements View.OnClickListener {
	
	Button recherche;
	
	// Variables pour la lecture du flux Json
	
	private String jsonString;
	JSONObject jsonResponse;
	JSONArray arrayJson;
	AutoCompleteTextView tvGareAuto;
	ArrayList<String> items = new ArrayList<String>();

	// Variables pour le Spinner

	Spinner spinMonth;
	String[] mois = {
			"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet","Août", "Septembre",
			"Octobre","Novembre","Décembre"
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.rechercher);

		//Création Spinner

		spinMonth = (Spinner)findViewById(R.id.spinMonth);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item, mois);

		spinMonth.setAdapter(adapter);
		spinMonth.setOnItemSelectedListener(
				new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						int position = spinMonth.getSelectedItemPosition();
						// TODO Auto-generated method stub
					}
					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
					}
				}
				);

		//Récupération fichier JSON pour les gares

		// Traitement du textView en autocompl�tion � partir de la source Json
		jsonString = lireJSON();

		try {
			jsonResponse = new JSONObject(jsonString);
			// Création du tableau général à partir d'un JSONObject
			JSONArray jsonArray = jsonResponse.getJSONArray("gares");

			// Pour chaque élément du tableau
			for (int i = 0; i < jsonArray.length(); i++) {

				// Création d'un tableau élément à partir d'un JSONObject
				JSONObject jsonObj = jsonArray.getJSONObject(i);

				// Récupération à partir d'un JSONObject nommé
				JSONObject fields  = jsonObj.getJSONObject("fields");

				// Récupération de l'item qui nous intéresse
				String nom = fields.getString("nom_de_la_gare");

				// Ajout dans l'ArrayList
				items.add(nom);		
			}

			ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, items);
			tvGareAuto = (AutoCompleteTextView)findViewById(R.id.actvGare);
			tvGareAuto.setAdapter(adapter1);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		// Listener sur le bouton d'envoi
				recherche = (Button)findViewById(R.id.btRechercher);
				recherche.setOnClickListener(this);
					
	}


	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub

	}
	
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	//Lecture fichier JSON


public String lireJSON() {
	InputStream is = getResources().openRawResource(R.raw.gares);
	Writer writer = new StringWriter();
	char[] buffer = new char[1024];
	try {
		Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		int n;
		while ((n = reader.read(buffer)) != -1) {
			writer.write(buffer, 0, n);
		}
	} catch (UnsupportedEncodingException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		try {
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	return writer.toString();
}

public void onClick(View v) {
	
	if ( v == recherche ) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		nameValuePairs.add(new BasicNameValuePair("gare",""+tvGareAuto.getText().toString().toLowerCase()));
		nameValuePairs.add(new BasicNameValuePair("mois",""+spinMonth.getSelectedItem().toString().toLowerCase()));
		Log.i("gare", tvGareAuto.getText().toString());
		Log.i("mois", spinMonth.getSelectedItem().toString());

		try {				
			RestClient.doPost("/recherche.php", nameValuePairs, new OnResultListener() {					
				@Override
				public void onResult(String json) {
					if (! json.equals("recherche_vide")) {
						
						Intent iListe = new Intent(getBaseContext(),Liste.class); 
						iListe.putExtra("liste",json);
						startActivityForResult( iListe,10 ); 
						
					} else {
						Toast.makeText(Rechercher.this, json, Toast.LENGTH_LONG).show();
					}					
				}
			});
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}



}









