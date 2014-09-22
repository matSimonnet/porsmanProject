package porsman.ms;

import java.util.ArrayList;

import org.mapsforge.android.maps.MapView;
import org.mapsforge.map.reader.MapDatabase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.MotionEvent;

public class HandiPorsmanMapView extends MapView {

	// Variables
	public HandiPorsmanDatabaseCallback callback;
	public MapDatabase mapDatabase;
	public HandiPorsmanMapViewer mapviewer;
	public HandiPorsmanMapViewThread thread;
	public HandiPorsmanSoundPool mySoundPool;
	public ArrayList<String> filtre;
	

	@SuppressLint("NewApi")
	public HandiPorsmanMapView(Context context) {
		super(context);
		
		callback = new HandiPorsmanDatabaseCallback(this);
		mapDatabase = this.getMapDatabase();
		
		// Si mapviewer appelle le mapview alors on connait le mapviewer
		if (context instanceof HandiPorsmanMapViewer) {
			mapviewer = (HandiPorsmanMapViewer) context;
			mySoundPool = new HandiPorsmanSoundPool(mapviewer);
		}
		
		// Create Thread and start it
		thread = new HandiPorsmanMapViewThread(this);
		thread.start();
	}

	@Override
	public boolean onTouchEvent(MotionEvent motionEvent) {
		thread.run(motionEvent);
		return true;
	}
	
	public boolean setZoom(byte zoomLevel) {
		return true;
	}
}