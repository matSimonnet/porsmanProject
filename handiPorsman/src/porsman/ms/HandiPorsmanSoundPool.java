package porsman.ms;

import java.util.ArrayList;
import java.util.List;

import org.mapsforge.applications.android.geotablet.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;

public class HandiPorsmanSoundPool {
	private SoundPool soundPool;
	private int soundIDTown;
	private int soundIDForest;
	private int soundIDHighway;
	private int soundIDFootway;
	private int soundIDBeach;
	private int soundIDCoast;
	private int soundIDCoastline;
	private int soundIDWater;
	private boolean loaded = false;
	private HandiPorsmanMapViewer mapviewer;
	private List<Integer> stopTown = new ArrayList<Integer>();
	private List<Integer> stopForest = new ArrayList<Integer>();
	private List<Integer> stopHighway = new ArrayList<Integer>();
	private List<Integer> stopFootway = new ArrayList<Integer>();
	private List<Integer> stopBeach = new ArrayList<Integer>();
	private List<Integer> stopCoast = new ArrayList<Integer>();
	private List<Integer> stopCoastline = new ArrayList<Integer>();
	private List<Integer> stopWater = new ArrayList<Integer>();

	@SuppressLint("NewApi")
	public HandiPorsmanSoundPool(HandiPorsmanMapViewer mapviewer) {
		this.mapviewer = mapviewer;
		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
				loaded = true;
			}
		});
		loadSound();
	}

	public void loadSound(){
		soundIDTown = soundPool.load(mapviewer, R.raw.town, 0);
		//soundIDForest = soundPool.load(mapviewer, R.raw.natural_wood_and_landuse_forest, 0);
		soundIDHighway = soundPool.load(mapviewer, R.raw.highway, 0);
		soundIDFootway = soundPool.load(mapviewer, R.raw.step, 0);
		soundIDBeach = soundPool.load(mapviewer, R.raw.beachlamp, 0);
		soundIDCoast = soundPool.load(mapviewer, R.raw.coastline, 0);
		soundIDCoastline = soundPool.load(mapviewer, R.raw.step, 0);
		soundIDWater = soundPool.load(mapviewer, R.raw.water, 0);
	}
	
	public void unloadSound(){
		soundPool.unload(soundIDTown);
		soundPool.unload(soundIDHighway);
		soundPool.unload(soundIDFootway);
		soundPool.unload(soundIDFootway);
		soundPool.unload(soundIDCoast);
		soundPool.unload(soundIDCoastline);
		soundPool.unload(soundIDWater);
	}
	
	public void playTown(float left, float right) {
		if (loaded && stopTown.isEmpty() ) {
			stopTown.add(soundPool.play(soundIDTown, left, right, 1, -1, 1f));
		}
	}

	public void playForest(float left, float right) {
		if (loaded && stopForest.isEmpty()) {
			stopForest.add(soundPool.play(soundIDForest, left, right, 1, -1, 1f));
		}
	}

	public void playHighway(float left, float right) {
		if (loaded && stopHighway.isEmpty()) {
			stopHighway.add(soundPool.play(soundIDHighway, left, right, 4, -1, 1f));
		}
	}
	
	public void playFootway(float left, float right) {
		if ( (loaded) && stopFootway.isEmpty() ) {
			stopFootway.add(soundPool.play(soundIDFootway, left, right, 4, -1, 1f));
		}
	}
	
	public void playBeach(float left, float right) {
		if ( (loaded) && stopBeach.isEmpty() ) {
			stopBeach.add(soundPool.play(soundIDBeach, left, right, 4, -1, 1f));
		}
	}
	
	public void playCoast(float left, float right) {
		if ( (loaded) && stopCoast.isEmpty() ) {
			stopCoast.add(soundPool.play(soundIDCoast, left, right, 4, -1, 1f));
		}

	}

	public void playCoastline(float left, float right) {
		if (loaded && stopCoastline.isEmpty()) {
			stopCoastline.add(soundPool.play(soundIDCoastline, left, right, 4, -1, 1f));
		}
	}
	
	public void playWater(float left, float right) {
		if (loaded && stopWater.isEmpty() ) {
			stopWater.add(soundPool.play(soundIDWater, left, right, 1, -1, 1f));
		}
	}

	@SuppressLint("NewApi")
	public void stopTown() {
		for (Integer i : stopTown) {
			soundPool.stop(i);
		}
		stopTown.clear();
	}

	@SuppressLint("NewApi")
	public void stopForest() {
		for (Integer i : stopForest) {
			soundPool.stop(i);
		}
		stopForest.clear();
	}

	@SuppressLint("NewApi")
	public void stopHighway() {
		for (Integer i : stopHighway) {
			soundPool.stop(i);
		}
		stopHighway.clear();
	}
	
	@SuppressLint("NewApi")
	public void stopFootway() {
		for (Integer i : stopFootway) {
			soundPool.stop(i);
		}
		stopFootway.clear();
	}
	
	@SuppressLint("NewApi")
	public void stopBeach() {
		for (Integer i : stopBeach) {
			soundPool.stop(i);
		}
		stopBeach.clear();
	}
	
	@SuppressLint("NewApi")
	public void stopCoast() {
		for (Integer i : stopCoast) {
			soundPool.stop(i);
		}
		stopCoast.clear();
	}

	@SuppressLint("NewApi")
	public void stopCoastline() {
		for (Integer i : stopCoastline) {
			soundPool.stop(i);
		}
		stopCoastline.clear();
	}
	
	@SuppressLint("NewApi")
	public void stopWater() {
		for (Integer i : stopWater) {
			soundPool.stop(i);
		}
		stopWater.clear();
	}
	
	public void stopAll(){
		stopWater();
		stopCoastline();
		stopHighway();
		stopFootway();
		stopBeach();
		stopForest();
		stopTown();
		stopCoast();
	}

	public float getVolume() {
		AudioManager audioManager = (AudioManager) this.mapviewer.getSystemService(Context.AUDIO_SERVICE);
		float actualVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		return actualVolume / maxVolume;
	}
}