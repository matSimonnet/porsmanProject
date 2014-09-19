package geotab.ms;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapController;
import org.mapsforge.applications.android.geotablet.R;
import org.mapsforge.core.GeoPoint;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.Engine;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class GeoTabletMapViewer extends MapActivity{

//	PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//	PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");

	// Variables
	private static int TTS_DATA_CHECK = 1;
	private TextToSpeech tts = null;
	private boolean ttsIsInit = false;
	private GeoTabletMapView mapView;
	private MapController mapController;
	public ArrayList<String> itemsSelected;
	private MediaPlayer mPlayer = null;
	public String mode;

	@SuppressWarnings("deprecation")
	@SuppressLint({ "SdCardPath", "NewApi" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.STATUS_BAR_HIDDEN);

		// Init Text To Speech
		initTextToSpeech();
		
		
		// Recuperation des informations de l'activity principale
		Intent thisIntent = getIntent();
		//String choice = thisIntent.getExtras().getString("myChoice");
		String choice = "Porsman";
		//byte zoomLevel = thisIntent.getExtras().getByte("zoomLevel");
		byte zoomLevel = 18;
		//mode = thisIntent.getExtras().getString("selectedMode");
		mode = "2 Doigts";
		itemsSelected = thisIntent.getStringArrayListExtra("Liste");

		/*
		 * Affichage de la carte choisie.
		 */
		mapView = new GeoTabletMapView(this);
		mapController = mapView.getController();

		mapView.setClickable(true);
		mapView.setBuiltInZoomControls(true);
		//mapView.setMapFile(new File("/sdcard/map/bretagne.map"));
		//mapView.setMapFile(new File("/sdcard/map/porsmanNewNew.map"));
		
		//if ( !new File("/sdcard/porsmanHandiMap/porsman.map").exists() ){
		if ( !new File("/sdcard/porsmanHandiMap/porsman.map").exists() ){
			
		try {
			
			new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ "/porsmanHandiMap/").mkdir();
			
			// write the inputStream to a FileOutputStream
			InputStream in = getResources().openRawResource(R.raw.porsman);
			new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ "/porsmanHandiMap/").mkdir();
			OutputStream out = new FileOutputStream(new File("/sdcard/porsmanHandiMap/porsman.map"));
		 
			int read = 0;
			int i = 0;
			byte[] bytes = new byte[2048];
		 
			while ((read = in.read(bytes)) != -1) {
				out.write(bytes, 0, read);
				i++;
				Log.i("i = ","" + i );
			}
		 
			getResources().openRawResource(R.raw.porsman).close();
			out.flush();
			out.close();
		 
			Log.i("New file created!","New file created!" );
		    } catch (IOException e) {
			e.printStackTrace();
		    }
		
		}
		else Log.i("map", "already created"); 

		mapView.setMapFile(new File("/sdcard/porsmanHandiMap/porsman.map"));
		mapController.setZoom(zoomLevel);
		

		/*
		 * Centre la carte en fonction du lieu selectionne
		 */
		if (choice.equals("Plouarzel"))	
			mapController.setCenter(new GeoPoint(48.4322, -4.7352));
		if (choice.equals("Porsman"))	
			//mapController.setCenter(new GeoPoint(48.4426, -4.778));
		    mapController.setCenter(new GeoPoint(48.4426, -4.77865));
		else if (choice.equals("Moulin blanc"))
			mapController.setCenter(new GeoPoint(48.3913, -4.4669));
		else if (choice.equals("Trezien"))
			mapController.setCenter(new GeoPoint(48.4268, -4.7867));

		setContentView(R.layout.main);
		setContentView(mapView);
		if (tts!=null)
		tts.speak("bienvenue à Porsman sur le sentier cotier adapté de Plouarzel", TextToSpeech.QUEUE_FLUSH, null);

	}

	// Initialisation de la voix
	private void initTextToSpeech() {
		/*
		 * Appel du moteur Text To Speech, avec pour action premiere la
		 * verification de la presence des bibliotheques necessaires
		 */
		Intent intent = new Intent(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(intent, TTS_DATA_CHECK);
	}

	// Resultat de l'initialiation
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == TTS_DATA_CHECK) {
			// Si les bibliotheques TTS (Text to speech) existent
			if (resultCode == Engine.CHECK_VOICE_DATA_PASS) {
				// On initialise le text to speech
				tts = new TextToSpeech(this, new OnInitListener() {
					@Override
					public void onInit(int status) {
						if (status == TextToSpeech.SUCCESS) {
							ttsIsInit = true;
							// Selection langue
							if (tts.isLanguageAvailable(Locale.FRANCE) >= 0)
								tts.setLanguage(Locale.FRANCE);

							// Volume TTS
							AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
							int amStreamMusicMaxVol = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
							//am.setStreamVolume(AudioManager.STREAM_MUSIC,amStreamMusicMaxVol, 0);

							// Tonalite voix
							tts.setPitch(0.8f);

							// Vitesse voix
							tts.setSpeechRate(1.1f);
						}
					}
				});
			}
		}
		// Si les bibliotheques TTS n'existent pas on les installent
		else {
			Intent installVoice = new Intent(Engine.ACTION_INSTALL_TTS_DATA);
			startActivity(installVoice);
		}
	}

	// Faire parler en flush
	public void speakFlush(String text,String side) {
		if (tts != null && ttsIsInit ) {
			HashMap<String, String> ok = new HashMap<String, String>();
			ok.put(TextToSpeech.Engine.KEY_PARAM_PAN, side);
			tts.speak(text, TextToSpeech.QUEUE_FLUSH, ok);
		}
	}

	// Faire parler en ajoutant
	public void speak(String text,String side) {
		if (tts != null && ttsIsInit) {
			HashMap<String, String> ok = new HashMap<String, String>();
			ok.put(TextToSpeech.Engine.KEY_PARAM_PAN, side);
			tts.speak(text, TextToSpeech.QUEUE_ADD, ok);
		}
	}
	
	// Faire parler en ajoutant
	public void stopspeak() {
		try {
			tts.stop();
		} catch (Exception e) {
			Log.e("mathieu", "stopspeak");
			e.printStackTrace();
		}
	}

	// Jouer son
	public void playSound(int resId) {
		if (mPlayer != null) {
			mPlayer.stop();
			mPlayer.release();
		}
		mPlayer = MediaPlayer.create(this, resId);
		mPlayer.setVolume((float) 0.05, (float) 0.05);
		mPlayer.start();
	}

	// Faire vibrer
	public void vibrate(int tempsVib) {
		// Get instance of Vibrator from current Context and vibrate
		Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(tempsVib);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mapView.mySoundPool.loadSound();
	}

	@Override
	public void onDestroy() {
		// Don't forget to shutdown!
//		 wl.release();
		new File("/sdcard/porsmanHandiMap/porsman.map").delete();
		
		
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}
		if (mPlayer != null) {
			mPlayer.stop();
			mPlayer.release();
		}
		mapView.thread.interrupt();
		super.onDestroy();
	}
}
