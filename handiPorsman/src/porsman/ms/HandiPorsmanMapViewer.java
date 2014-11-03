package porsman.ms;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapController;
import org.mapsforge.android.maps.overlay.ArrayCircleOverlay;
import org.mapsforge.android.maps.overlay.OverlayCircle;
import org.mapsforge.applications.android.geotablet.R;
import org.mapsforge.core.GeoPoint;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.Engine;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class HandiPorsmanMapViewer extends MapActivity{

	// Variables
	//PowerManager pm = null;
	//PowerManager.WakeLock wl = null;
	
	public static GeoPoint position = null;
	
	private static int TTS_DATA_CHECK = 1;
	private TextToSpeech tts = null;
	private boolean ttsIsInit = false;
	private HandiPorsmanMapView mapView;
	private MapController mapController;
	public ArrayList<String> itemsSelected;
	private MediaPlayer mPlayer = null;
	public String mode;
	
	private ArrayCircleOverlay circleOverlay; // -> the circles list 
	private OverlayCircle circle; // -> the overlay

	@SuppressWarnings("deprecation")
	@SuppressLint({ "SdCardPath", "NewApi" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		//wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
		
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		//this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getWindow().getDecorView().setSystemU2iVisibility(View.STATUS_BAR_HIDDEN);

		// Init Text To Speech
		initTextToSpeech();
		String choice = "Porsman";
		byte zoomLevel = 19;
		mode = "2 Doigts";

		/*
		 * Affichage de la carte choisie.
		 */
		mapView = new HandiPorsmanMapView(this);
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
		//if (choice.equals("Plouarzel"))	
		//	mapController.setCenter(new GeoPoint(48.4322, -4.7352));
		//if (choice.equals("Porsman"))	
			//mapController.setCenter(new GeoPoint(48.4426, -4.778));
		    mapController.setCenter(new GeoPoint(48.4426, -4.77875));

		setContentView(mapView);
        // create the paint objects for overlay circles
        Paint circleDefaultPaintFill = new Paint(Paint.ANTI_ALIAS_FLAG);
        circleDefaultPaintFill.setStyle(Paint.Style.FILL);
        circleDefaultPaintFill.setColor(Color.RED);
        circleDefaultPaintFill.setAlpha(20);
 
        Paint circleDefaultPaintOutline = new Paint(Paint.ANTI_ALIAS_FLAG);
        circleDefaultPaintOutline.setStyle(Paint.Style.STROKE);
        circleDefaultPaintOutline.setColor(Color.RED);
        circleDefaultPaintOutline.setAlpha(128);
        circleDefaultPaintOutline.setStrokeWidth(3);
 
        // create the CircleOverlay
        circleOverlay = new ArrayCircleOverlay(circleDefaultPaintFill,circleDefaultPaintOutline);

        // add all overlays to the MapView
        mapView.getOverlays().add(circleOverlay);
        
		//circle = new OverlayCircle(new GeoPoint(48.4426, -4.77865), 5f , "first overlay"); 
		//circleOverlay.addCircle(circle);
		
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        LocationListener ll = new MyLocationListener();		
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);
		
	}
	
	private class MyLocationListener implements LocationListener{
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {	
		}
		@Override
		public void onProviderEnabled(String provider) {	
		}
		@Override
		public void onProviderDisabled(String provider) {
		}
		@Override
		public void onLocationChanged(Location location) {
			circleOverlay.clear();
			position = new GeoPoint(location.getLatitude(), location.getLongitude());
			circle = new OverlayCircle(position, 5f , "first overlay"); 
			circleOverlay.addCircle(circle);	
			
		}	
	}

	// Initialisation de la voix
	private void initTextToSpeech() {
		//
		 //Appel du moteur Text To Speech, avec pour action premiere la
		 // verification de la presence des bibliotheques necessaires
		 //
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
							//if (tts.isLanguageAvailable(Locale.FRANCE) >= 0)
							//	tts.setLanguage(Locale.FRANCE);

							// Volume TTS
							//AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
							//int amStreamMusicMaxVol = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
							//am.setStreamVolume(AudioManager.STREAM_MUSIC,amStreamMusicMaxVol, 0);

							// Tonalite voix
							//tts.setPitch(0.8f);

							// Vitesse voix
							//tts.setSpeechRate(1.1f);
						}

							mapView.mapviewer.speak("Bienvenue sur la carte audio tactile du sentier adapté de la plage de porsman.", "0f");	
							
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
	
	
	
	
	
	public GeoPoint getPosition() {
		return position;
	}

	public void setPosition(GeoPoint position) {
		this.position = position;
	}

	@Override
	public void onResume() {
		super.onResume();
		mapView.mySoundPool.loadSound();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem Item){
		switch (Item.getItemId()) {
		case R.id.item_help:
			Intent help = new Intent(HandiPorsmanMapViewer.this,Help.class);
			startActivity(help);
			break;
		default:
			break;
		}
		return false;
	}
	
	
	
	
	
	@Override
	public void onDestroy() {
		// Don't forget to shutdown!
		//wl.release();
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
