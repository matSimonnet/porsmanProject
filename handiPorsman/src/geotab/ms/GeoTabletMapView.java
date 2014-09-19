package geotab.ms;

import java.util.ArrayList;

import org.mapsforge.android.maps.MapView;
import org.mapsforge.map.reader.MapDatabase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.MotionEvent;

public class GeoTabletMapView extends MapView {

	// Variables
	public GeoTabletMapDatabaseCallback callback;
	public MapDatabase mapDatabase;
	public GeoTabletMapViewer mapviewer;
	public GeoTabletMapViewThread thread;
	public GeoTabletSoundPool mySoundPool;
	public ArrayList<String> filtre;
	

	@SuppressLint("NewApi")
	public GeoTabletMapView(Context context) {
		super(context);
		
		callback = new GeoTabletMapDatabaseCallback(this);
		mapDatabase = this.getMapDatabase();
		
		// Si mapviewer appelle le mapview alors on connait le mapviewer
		if (context instanceof GeoTabletMapViewer) {
			mapviewer = (GeoTabletMapViewer) context;
			mySoundPool = new GeoTabletSoundPool(mapviewer);
		}
		
		// Create Thread and start it
		thread = new GeoTabletMapViewThread(this);
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