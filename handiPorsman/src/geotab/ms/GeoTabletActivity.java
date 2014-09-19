package geotab.ms;
//package org.mapsforge.applications.android.geotablet;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Locale;
//
//import org.mapsforge.android.maps.MapActivity;
//import org.mapsforge.applications.android.geotablet.MultiChoiceSpinner.MultiChoiceSpinnerListener;
//import org.mapsforge.core.Tag;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.ActivityInfo;
//import android.media.AudioManager;
//import android.media.MediaPlayer;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Vibrator;
//import android.speech.tts.TextToSpeech;
//import android.speech.tts.TextToSpeech.Engine;
//import android.speech.tts.TextToSpeech.OnInitListener;
//import android.util.Log;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
//import android.view.View.OnClickListener;
//import android.view.accessibility.AccessibilityManager;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemSelectedListener;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.Spinner;
//
//public class GeoTabletActivity extends MapActivity {
//	/** Called when the activity is first created. */
//
//	// Variables
//	public boolean talkback = false;
//	private String choice = new String();
//	private String mode = new String();
//	private byte zoomLevel = 0;
//	private Spinner spinner_zone;
//	private Spinner spinner_echelle;
//	private Spinner spinner_mode;
//	private MultiChoiceSpinner spinner_filtre;
//	private Button btn_affiche;
//	private List<String> items = new ArrayList<String>();
//	private ArrayList<String> itemsSelected = new ArrayList<String>();
//	private GeoTabletMapView mapView;
//	private Tag[] allTags;
//	private static int TTS_DATA_CHECK = 1;
//	private TextToSpeech tts = null;
//	private boolean ttsIsInit = false;
//	private MediaPlayer mPlayer = null;
//
//	@SuppressLint("SdCardPath")
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		
//		AccessibilityManager am = (AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE);
//		
//		if (am.isEnabled()) {
//			talkback = true;
//			Log.i("TalkBack Enabled",""+ am.isEnabled());
//		}
//		
//		Log.i("Touch exploration Enabled",""+ am.isTouchExplorationEnabled()  );
//		
//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().getDecorView().setSystemUiVisibility(View.STATUS_BAR_HIDDEN);
//
//
//		// Init Text To Speech
//		initTextToSpeech();
//
//		
//		Intent intent = new Intent(GeoTabletActivity.this,GeoTabletMapViewer.class);
//		//intent.putExtra("myChoice", choice);
//		intent.putExtra("zoomLevel", zoomLevel);
//		intent.putExtra("selectedMode", mode);
//		intent.putStringArrayListExtra("Liste", itemsSelected);
//		startActivity(intent);
//		
//		// Listener du clic bouton
////		OnClickListener ButtonDisplay = new OnClickListener() {
////			public void onClick(View actualView) {
////				// Changement activite et transmission de la variable.
////				Intent intent = new Intent(GeoTabletActivity.this,GeoTabletMapViewer.class);
////				intent.putExtra("myChoice", choice);
////				intent.putExtra("zoomLevel", zoomLevel);
////				intent.putExtra("selectedMode", mode);
////				intent.putStringArrayListExtra("Liste", itemsSelected);
////				startActivity(intent);
////			}
////		};
//
//		/*
//		 * **************************************
//		 * Recuperation des filtres et affichage
//		 * **************************************
//		 */
//		mapView = new GeoTabletMapView(this);
//		//mapView.setMapFile(new File("/sdcard/map/porsmanNewNew.map"));
//
//		
//		if ( !new File("/sdcard/porsmanHandiMap/porsman.map").exists() ){
//		
//		try {
//			
//			new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ "/porsmanHandiMap/").mkdir();
//			
//			// write the inputStream to a FileOutputStream
//			InputStream in = getResources().openRawResource(R.raw.porsman);
//			new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ "/porsmanHandiMap/").mkdir();
//			OutputStream out = new FileOutputStream(new File("/sdcard/porsmanHandiMap/porsman.map"));
//		 
//			int read = 0;
//			int i = 0;
//			byte[] bytes = new byte[2048];
//		 
//			while ((read = in.read(bytes)) != -1) {
//				out.write(bytes, 0, read);
//				i++;
//				Log.i("i = ","" + i );
//			}
//		 
//			getResources().openRawResource(R.raw.porsman).close();
//			out.flush();
//			out.close();
//		 
//			Log.i("New file created!","New file created!" );
//		    } catch (IOException e) {
//			e.printStackTrace();
//		    }
//		
//		}
//		else Log.i("map", "already created"); 
//			
//		mapView.setMapFile(new File("/sdcard/porsmanHandiMap/porsman.map"));
//		
//		
////		mapView.setMapFile(new File("sdcard/map/bretagne2.map"));
//		allTags = mapView.mapDatabase.getMapFileInfo().poiTags;
//
//		for (int i = 0; i < allTags.length; i++) {
//			items.add(allTags[i].value);
//		}
//
//		/*
//		 * **************************************
//		 * Construction de l'IHM 
//		 * **************************************
//		 */
//		setContentView(R.layout.main);
//
//		// Liste deroulante choix ville dans strings.xml
//		spinner_zone = (Spinner) findViewById(R.id.sp_zone);
//		ArrayAdapter<CharSequence> choiceAdapter = ArrayAdapter
//				.createFromResource(this, R.array.map_choice,
//						android.R.layout.simple_spinner_dropdown_item);
//		choiceAdapter
//				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		spinner_zone.setAdapter(choiceAdapter);
//		spinner_zone.setOnItemSelectedListener(new ChoiceSelectedListener());
//
//		// La liste deroulante level
//		spinner_echelle = (Spinner) findViewById(R.id.sp_echelle);
//		ArrayAdapter<CharSequence> levelAdapter = ArrayAdapter
//				.createFromResource(this, R.array.zoom_level,
//						android.R.layout.simple_spinner_dropdown_item);
//		levelAdapter
//				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		spinner_echelle.setAdapter(levelAdapter);
//		spinner_echelle.setOnItemSelectedListener(new LevelSelectedListener());
//
//		// La liste deroulante mode
//		spinner_mode = (Spinner) findViewById(R.id.sp_mode);
//		ArrayAdapter<CharSequence> modeAdapter = ArrayAdapter
//				.createFromResource(this, R.array.mode,
//						android.R.layout.simple_spinner_dropdown_item);
//		modeAdapter
//				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		spinner_mode.setAdapter(modeAdapter);
//		spinner_mode.setOnItemSelectedListener(new ModeSelectedListener());
//
//		spinner_filtre = (MultiChoiceSpinner) findViewById(R.id.sp_filtre);
//		spinner_filtre.setItems(items, "filtre", new filtreSelectedListener());
//
//		btn_affiche = (Button) findViewById(R.id.btn_affiche_carte);
//
//		//btn_affiche.setOnClickListener(ButtonDisplay);
//	}
//
//	@Override
//	public void onResume() {
//		super.onResume();
//		if(mapView.mySoundPool != null)
//			mapView.mySoundPool.unloadSound();
//	}
//	
//	public String getChoice() {
//		return choice;
//	}
//
//	public void setChoice(String choice) {
//		this.choice = choice;
//	}
//
//	public int getZoomLevel() {
//		return zoomLevel;
//	}
//
//	public void setZoomLevel(byte zoomLevel) {
//		this.zoomLevel = zoomLevel;
//	}
//
//	public String getMode() {
//		return mode;
//	}
//
//	public void setMode(String mode) {
//		this.mode = mode;
//	}
//
//	// Initialisation de la voix
//	private void initTextToSpeech() {
//		/*
//		 * Appel du moteur Text To Speech, avec pour action premiere la
//		 * verification de la presence des bibliotheques necessaires
//		 */
//		Intent intent = new Intent(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
//		startActivityForResult(intent, TTS_DATA_CHECK);
//	}
//
//	// Resultat de l'initialiation
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (requestCode == TTS_DATA_CHECK) {
//			// Si les bibliotheques TTS (Text to speech) existent
//			if (resultCode == Engine.CHECK_VOICE_DATA_PASS) {
//				// On initialise le text to speech
//				tts = new TextToSpeech(this, new OnInitListener() {
//					@Override
//					public void onInit(int status) {
//						if (status == TextToSpeech.SUCCESS) {
//							ttsIsInit = true;
//							// Selection langue
//							if (tts.isLanguageAvailable(Locale.FRANCE) >= 0)
//								tts.setLanguage(Locale.FRANCE);
//
//							// Volume TTS
//							AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//							int amStreamMusicMaxVol = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
//							//am.setStreamVolume(AudioManager.STREAM_MUSIC,amStreamMusicMaxVol, 0);
//
//							// Tonalite voix
//							tts.setPitch(0.8f);
//
//							// Vitesse voix
//							tts.setSpeechRate(1.1f);
//						}
//					}
//				});
//			}
//		}
//		// Si les bibliotheques TTS n'existent pas on les installent
//		else {
//			Intent installVoice = new Intent(Engine.ACTION_INSTALL_TTS_DATA);
//			startActivity(installVoice);
//		}
//	}
//
//	// Faire parler
//	public void speak(String text) {
//		if (tts != null && ttsIsInit) {
//			tts.speak(text, TextToSpeech.QUEUE_ADD, null);
//		}
//	}
//
//	// Jouer son
//	private void playSound(int resId) {
//		if (mPlayer != null) {
//			mPlayer.stop();
//			mPlayer.release();
//		}
//		mPlayer = MediaPlayer.create(this, resId);
//		mPlayer.start();
//	}
//
//	// Faire vibrer
//	private void vibrate(int tempsVib) {
//		// Get instance of Vibrator from current Context and vibrate
//		Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//		v.vibrate(tempsVib);
//	}
//
//	public class filtreSelectedListener implements MultiChoiceSpinnerListener {
//		@Override
//		public void onItemsSelected(boolean[] selected) {
//			itemsSelected.clear();
//			for (int i = 0; i < items.size(); i++) {
//				if (selected[i] == true) {
//					itemsSelected.add(items.get(i));
//				}
//			}
//		}
//	}
//
//	// Gestion de l'evenement sur la liste deroulante map.
//	public class ChoiceSelectedListener implements OnItemSelectedListener {
//
//		@Override
//		public void onItemSelected(AdapterView<?> parent, View view, int pos,
//				long id) {
//			// Affichage
//			Log.v("Choix", "Vous avez choisis "
//					+ parent.getItemAtPosition(pos).toString());
//			setChoice(parent.getItemAtPosition(pos).toString());
//			speak("Vous avez choisi " + choice);
////			vibrate(1000);
//		}
//
//		@Override
//		public void onNothingSelected(AdapterView<?> parent) {
//			// Do nothing.
//		}
//	}
//
//	// Gestion de l'evenement sur la liste deroulante level.
//	public class ModeSelectedListener implements OnItemSelectedListener {
//
//		@Override
//		public void onItemSelected(AdapterView<?> parent, View view, int pos,
//				long id) {
//			setMode(parent.getItemAtPosition(pos).toString());
//		}
//
//		@Override
//		public void onNothingSelected(AdapterView<?> parent) {
//			// Do nothing.
//		}
//	}
//
//	// Gestion de l'evenement sur la liste deroulante level.
//	public class LevelSelectedListener implements OnItemSelectedListener {
//
//		@Override
//		public void onItemSelected(AdapterView<?> parent, View view, int pos,
//				long id) {
//			Log.v("Choix", "Vous avez choisis "
//					+ parent.getItemAtPosition(pos).toString());
//			setZoomLevel(Byte.parseByte(parent.getItemAtPosition(pos)
//					.toString()));
//			speak("zoom " + String.valueOf(zoomLevel));
////			vibrate(1000);
//		}
//
//		@Override
//		public void onNothingSelected(AdapterView<?> parent) {
//			// Do nothing.
//		}
//	}
//
//	@Override
//	public void onDestroy() {
//		// Don't forget to shutdown!
//		if (tts != null) {
//			tts.stop();
//			tts.shutdown();
//		}
//		if (mPlayer != null) {
//			mPlayer.stop();
//			mPlayer.release();
//		}
//		super.onDestroy();
//	}
//	
//}