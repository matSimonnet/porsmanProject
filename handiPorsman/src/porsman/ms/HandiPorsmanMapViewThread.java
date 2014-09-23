package porsman.ms;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.mapsforge.android.maps.Projection;
import org.mapsforge.core.GeoPoint;
import org.mapsforge.core.MercatorProjection;
import org.mapsforge.core.Tag;
import org.mapsforge.core.Tile;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;

public class HandiPorsmanMapViewThread extends Thread {

	final float scale = (float)1.8;
	final int SEUIL = 120;
	
	// Variables
	private HandiPorsmanMapView mapView;
	private String valeurAnt = "";
	private boolean dansFiltre = false;
	public List<Tag> tags = new ArrayList<Tag>();
	public List<ArrayList<Tag>> listtags = new ArrayList<ArrayList<Tag>>();
	public int mActivePointerId = MotionEvent.INVALID_POINTER_ID;

	public List<Point> test = new ArrayList<Point>();
	public List<Point> precision = new ArrayList<Point>();
	boolean finger1Droite = false;
	public List<Integer> bouge = new ArrayList<Integer>();
	public List<Point> quiBouge = new ArrayList<Point>();
	public String tampon0 = "";
	public String tampon1 = "";
	public boolean trouve;
	public GeoPoint geopointReal;
	public File file;
	public boolean Landuse;
	public boolean Forest;
	public boolean Beach;
	public boolean isHighway = false;
	public boolean isFootway = false;
	public boolean isCoast = false;
	public boolean isCoastline = false;
	public boolean isWater = false;
	public PrintWriter pw = null;
	public FileWriter fw = null;
	public String wayName = "";
	boolean gpsIn = false;

	@SuppressLint("NewApi")
	public HandiPorsmanMapViewThread(HandiPorsmanMapView mapView) {
		this.mapView = mapView;
		mapView.setScaleX(scale);
		mapView.setScaleY(scale);
	}


	
	@SuppressLint({ "NewApi", "NewApi" })
	public void run(MotionEvent motionEvent) {

		
		if (mapView.mapviewer.mode.equals("2 Doigts")) {

			int action = motionEvent.getAction();
			int actionCode = action & MotionEvent.ACTION_MASK;
			Projection p = this.mapView.getProjection();

			
			switch (actionCode) {
			
			case MotionEvent.ACTION_DOWN:
				
//				if (motionEvent.getX() > 1300  || motionEvent.getX() < 600 
//						||	motionEvent.getY() > 780  || motionEvent.getY() < 350
//							) {
//						Log.i(" ###############################" , "*");
//						mapView.mapviewer.speakFlush("Bord de carte", "0f");
//					}
				
				mActivePointerId = motionEvent.getPointerId(0);
				test.clear();
				test.add(new Point((int) motionEvent.getX(0), (int) motionEvent.getY(0)));
				precision.add(new Point((int) motionEvent.getX(0),(int) motionEvent.getY(0)));
				initValues();

				generateExecuteQuery(motionEvent, mActivePointerId);

				gestionWayNode(motionEvent, mActivePointerId);
				
				
						//, longitude), y1, x2, y2) )
				
				if (mapView.callback.Pois.size() > 0 || mapView.callback.listIn.size() > 0 || Beach	) {
					gestionTts(motionEvent, mActivePointerId);
					//gestionTtsWay(motionEvent, mActivePointerId);
					gestionSoundPool(motionEvent, mActivePointerId);
				}
					
				mapView.callback.Pois.clear();
							
				//GPS
				/*
				Point positionPixel = new Point();
				p.toPixels(new GeoPoint(HandiPorsmanMapViewer.position.getLatitude(),HandiPorsmanMapViewer.position.getLongitude()), positionPixel);
				
				if (Distance(positionPixel.x, positionPixel.y, motionEvent.getX(0), motionEvent.getY(0)) < 20.0 ){
					mapView.mapviewer.speak("vous êtes ici" , "0f");
					Log.e(" DISTANCE ACTION DOWN" , " < 20 " );
				}*/
				
				
				break;
			
			case MotionEvent.ACTION_POINTER_DOWN:
				
//				if (motionEvent.getX() > 1300  || motionEvent.getX() < 600 
//						||	motionEvent.getY() > 780  || motionEvent.getY() < 350
//							) {
//						Log.i(" ###############################" , "*");
//						mapView.mapviewer.speakFlush("Bord de carte", "0f");
//					}
				
				identifyTwoFingers(motionEvent);

				int pointerIndex = (action & MotionEvent.ACTION_POINTER_ID_MASK) >> MotionEvent.ACTION_POINTER_ID_SHIFT;
				mActivePointerId = motionEvent.getPointerId(pointerIndex);

				initValues();

				generateExecuteQuery(motionEvent, mActivePointerId);

				gestionWayNode(motionEvent, mActivePointerId);

				if (mapView.callback.Pois.size() > 0 || mapView.callback.listIn.size() > 0 || Beach) 
				{
					gestionTts(motionEvent, mActivePointerId);
//					gestionTtsWay(motionEvent, mActivePointerId);
					Log.i("MotionEvent","ACTION_POINTER_DOWN");
					gestionSoundPool(motionEvent, mActivePointerId);
				}
				
				gestionWater(motionEvent, mActivePointerId);
				
				mapView.callback.Pois.clear();

				test.clear();
				
				for (int i = 0; i < motionEvent.getPointerCount(); i++) {
					test.add(new Point((int) motionEvent.getX(i),
							(int) motionEvent.getY(i)));
				}
				
				precision.clear();
				
				for (int i = 0; i < motionEvent.getPointerCount(); i++) {
					precision.add(new Point((int) motionEvent.getX(i), (int) motionEvent.getY(i)));
				}
								
				break;
			
			case MotionEvent.ACTION_MOVE:
				
//				if (motionEvent.getX() > 1300  || motionEvent.getX() < 600 
//						||	motionEvent.getY() > 780  || motionEvent.getY() < 350
//							) {
//						Log.i(" ###############################" , "*");
//						mapView.mapviewer.speakFlush("Bord de carte", "0f");
//					}
				
				quiBouge.clear();
				
				findIdFinger(motionEvent);
				
				for (Integer i : bouge) {
					quiBouge.add(new Point((int) motionEvent.getX(i),
							(int) motionEvent.getY(i)));
				}

				for (Integer i : bouge) {
					initValues();

					generateExecuteQuery(motionEvent, i);
//					gestionTtsWay(motionEvent, mActivePointerId);
					//Log.i("MotionEvent","ACTION_POINTER_DOWN");
					gestionWayNode(motionEvent, i);

					// Si la liste de POI n'est pas vide
					if (mapView.callback.Pois.size() > 0 || mapView.callback.listIn.size() > 0  || Beach) {
						gestionTts(motionEvent, i);
						
						
						gestionSoundPool(motionEvent, i);
					}

					gestionWater(motionEvent, i);

					mapView.callback.Pois.clear();
				}
				
				try {
					

				//GPS
				Point positionPixelMove = new Point();
				p.toPixels(new GeoPoint(HandiPorsmanMapViewer.position.getLatitude(),HandiPorsmanMapViewer.position.getLongitude()), positionPixelMove);
				
				if (Distance(positionPixelMove.x, positionPixelMove.y, motionEvent.getX(0), motionEvent.getY(0)) < 20.0 
						&& gpsIn == false){
					//mapView.mySoundPool.playTown(1.0f, 0.0f);
					mapView.mapviewer.vibrate(100);
					mapView.mapviewer.speak("vous êtes ici" , "0f");
					Log.e(" DISTANCE_ACTION MOVE" , " < 20 " );
					gpsIn = true;
				}
				
				if (Distance(positionPixelMove.x, positionPixelMove.y, motionEvent.getX(0), motionEvent.getY(0)) > 20.0){
					gpsIn = false  ; 
				}

				} catch (Exception e) {
					// TODO: handle exception
				}
				
				break;
				
				
			case MotionEvent.ACTION_POINTER_UP:
				
//				if (motionEvent.getX() > 1300  || motionEvent.getX() < 600 
//						||	motionEvent.getY() > 780  || motionEvent.getY() < 350
//							) {
//						Log.i(" ###############################" , "*");
//						mapView.mapviewer.speakFlush("Bord de carte", "0f");
//					}
				
				pointerIndex = (motionEvent.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
				final int pointerId = motionEvent.getPointerId(pointerIndex);
				if (pointerId == 0)
					tampon0 = "";
				if (pointerId == 1)
					tampon1 = "";
				mapView.mySoundPool.stopAll();
				mapView.mapviewer.stopspeak();
				
				//Log.i("MotionEvent","ACTION_POINTER_UP");
				
				break;
				
			case MotionEvent.ACTION_UP:
				tampon0 = "";
				mapView.mySoundPool.stopAll();
				mapView.mapviewer.stopspeak();
				//Log.i("MotionEvent","ACTION_UP");
				
				break;
			}
		}
	}

	@SuppressLint("NewApi")
	private void gestionWater(MotionEvent m, int mActivePointer) {
		if (!mapView.callback.inCallback) {
			if (m.getPointerCount() == 1) {
				mapView.mySoundPool.playWater(0.5f, 0.5f);
			} else {
				if (m.getPointerCount() == 2) {
					if (mActivePointer == 0) {
						if (finger1Droite == true) {
							mapView.mySoundPool.playWater(0.5f, 0.0f);
						} else if (finger1Droite == false) {
							mapView.mySoundPool.playWater(0.0f, 0.5f);
						}
					}
					if (mActivePointer == 1) {
						if (finger1Droite == true) {
							mapView.mySoundPool.playWater(0.0f, 0.5f);
						} else if (finger1Droite == false) {
							mapView.mySoundPool.playWater(0.5f, 0.0f);
						}
					}
				}
			}
			//writeToCsvSound("water");
		} else {
			mapView.callback.inCallback = false;
			if (isWater) {
				if (m.getPointerCount() == 1) {
					mapView.mySoundPool.playWater(0.5f, 0.5f);
				} else {
					if (m.getPointerCount() == 2) {
						if (mActivePointer == 0) {
							if (finger1Droite == true) {
								mapView.mySoundPool.playWater(0.5f, 0.0f);
							} else if (finger1Droite == false) {
								mapView.mySoundPool.playWater(0.0f, 0.5f);
							}
						}
						if (mActivePointer == 1) {
							if (finger1Droite == true) {
								mapView.mySoundPool.playWater(0.0f, 0.5f);
							} else if (finger1Droite == false) {
								mapView.mySoundPool.playWater(0.5f, 0.0f);
							}
						}
					}
				}
				//writeToCsvSound("water");
			} else {
				mapView.mySoundPool.stopWater();
			}
		}
	}

	private void initValues() {
		trouve = false;
		mapView.callback.listIn.clear();
		Landuse = false;
		Forest = false;
		Beach = false;
		isHighway = false;
		isFootway = false;
		isCoast = false;
		isCoastline = false;
		isWater = false;
	}

	@SuppressLint("NewApi")
	private void generateExecuteQuery(MotionEvent m, int mActivePointer) {
		// Zoom et centre.
		byte zoomLevel = this.mapView.getMapPosition().getZoomLevel();

		long x = (long) m.getX(mActivePointer);
		long y = (long) m.getY(mActivePointer);
		Projection p = this.mapView.getProjection();
		geopointReal = p.fromPixels((int) x, (int) y);
		double latitude = geopointReal.getLatitude();
		double longitude = geopointReal.getLongitude();

		long tileX = MercatorProjection.longitudeToTileX(longitude, zoomLevel);
		long tileY = MercatorProjection.latitudeToTileY(latitude, zoomLevel);
		Tile tile = new Tile(tileX, tileY, zoomLevel);

		/*
		 * Requete pour connaitre contenu de la tuile
		 */
		mapView.mapDatabase.executeQuery(tile, mapView.callback);
	}

	@SuppressLint("NewApi")
	private void gestionSoundPool(MotionEvent m, int mActionPointer) {
		if (mapView.callback.listIn.contains(true)) {
			if (m.getPointerCount() == 1) {
				if (Landuse)
				{
					mapView.mySoundPool.playTown(0.15f, 0.15f);
				}
				if (Forest)
					mapView.mySoundPool.playForest(0.15f, 0.15f);
				if (Beach)
				{
					mapView.mySoundPool.playBeach(0.15f, 0.15f);
				}
				
					
			} else {
				if (m.getPointerCount() == 2) {
					if (mActionPointer == 0) {
						if (finger1Droite == true) {
							if (Landuse)
								mapView.mySoundPool.playTown(0.3f, 0.0f);
							if (Forest)
								mapView.mySoundPool.playForest(0.3f, 0.0f);
							if (Beach)
								mapView.mySoundPool.playBeach(0.3f, 0.0f);
						} else if (finger1Droite == false) {
							if (Landuse)
								mapView.mySoundPool.playTown(0.0f, 0.3f);
							if (Forest)
								mapView.mySoundPool.playForest(0.0f, 0.3f);
							if (Beach)
								mapView.mySoundPool.playBeach(0.0f, 0.3f);
						}
					}
					if (mActionPointer == 1) {
						if (finger1Droite == true) {
							if (Landuse)
								mapView.mySoundPool.playTown(0.0f, 0.3f);
							if (Forest)
								mapView.mySoundPool.playForest(0.0f, 0.3f);
							if (Beach)
								mapView.mySoundPool.playBeach(0.0f, 0.3f);
						} else if (finger1Droite == false) {
							if (Landuse)
								mapView.mySoundPool.playTown(0.3f, 0.0f);
							if (Forest)
								mapView.mySoundPool.playForest(0.3f, 0.0f);
							if (Beach)
								mapView.mySoundPool.playBeach(0.3f, 0.0f);
						}
					}
				}
			}
			/**if(Landuse)
				writeToCsvSound("town");
			if(Forest)
				writeToCsvSound("forest");
			if(Beach)
				writeToCsvSound("Beach");**/
			
		} else {
			mapView.mySoundPool.stopTown();
			mapView.mySoundPool.stopForest();
			mapView.mySoundPool.stopBeach();
		}
	}

	@SuppressLint("NewApi")
	private void gestionWayNode(MotionEvent m, int mActivePointer) {
		
		if (isFootway) {
			if (m.getPointerCount() == 1) {
				mapView.mySoundPool.playFootway(0.5f, 0.5f);
			} else {
				if (m.getPointerCount() == 2) {
					if (mActivePointer == 0) {
						if (finger1Droite == true) {
							mapView.mySoundPool.playFootway(0.5f, 0.0f);
						} else if (finger1Droite == false) {
							mapView.mySoundPool.playFootway(0.0f, 0.5f);
						}
					}
					if (mActivePointer == 1) {
						if (finger1Droite == true) {
							mapView.mySoundPool.playFootway(0.0f, 0.5f);
						} else if (finger1Droite == false) {
							mapView.mySoundPool.playFootway(0.5f, 0.0f);
						}
					}
				}
			}
//			try {
//				//writeToCsvSound("highway");
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			
		} else {
			mapView.mySoundPool.stopFootway();
		}
		
		if (isCoast) {
			if (m.getPointerCount() == 1) {
				mapView.mySoundPool.playCoast(0.15f, 0.15f);
			} else {
				if (m.getPointerCount() == 2) {
					if (mActivePointer == 0) {
						if (finger1Droite == true) {
							mapView.mySoundPool.playCoast(0.3f, 0.0f);
						} else if (finger1Droite == false) {
							mapView.mySoundPool.playCoast(0.0f, 0.3f);
						}
					}
					if (mActivePointer == 1) {
						if (finger1Droite == true) {
							mapView.mySoundPool.playCoast(0.0f, 0.3f);
						} else if (finger1Droite == false) {
							mapView.mySoundPool.playCoast(0.3f, 0.0f);
						}
					}
				}
			}
//			try {
//				writeToCsvSound("coast");
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			
		} else {
			mapView.mySoundPool.stopCoast();
		}
		
		
		
		
		if (isHighway) {
			if (m.getPointerCount() == 1) {
				mapView.mySoundPool.playHighway(0.15f, 0.15f);
			} else {
				if (m.getPointerCount() == 2) {
					if (mActivePointer == 0) {
						if (finger1Droite == true) {
							mapView.mySoundPool.playHighway(0.3f, 0.0f);
						} else if (finger1Droite == false) {
							mapView.mySoundPool.playHighway(0.0f, 0.3f);
						}
					}
					if (mActivePointer == 1) {
						if (finger1Droite == true) {
							mapView.mySoundPool.playHighway(0.0f, 0.3f);
						} else if (finger1Droite == false) {
							mapView.mySoundPool.playHighway(0.3f, 0.0f);
						}
					}
				}
			}
//			try {
//				writeToCsvSound("highway");
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			
		} else {
			mapView.mySoundPool.stopHighway();
		}

		if (isWater) {
			if (m.getPointerCount() == 1) {
				mapView.mySoundPool.playWater(0.15f, 0.15f);
			} else {
				if (m.getPointerCount() == 2) {
					if (mActivePointer == 0) {
						if (finger1Droite == true) {
							mapView.mySoundPool.playWater(0.3f, 0.0f);
						} else if (finger1Droite == false) {
							mapView.mySoundPool.playWater(0.0f, 0.3f);
						}
					}
					if (mActivePointer == 1) {
						if (finger1Droite == true) {
							mapView.mySoundPool.playWater(0.0f, 0.3f);
						} else if (finger1Droite == false) {
							mapView.mySoundPool.playWater(0.3f, 0.0f);
						}
					}
				}
			}
			//writeToCsvSound("water");
		} else {
			mapView.mySoundPool.stopWater();
		}
	}

	@SuppressLint("NewApi")
	private void gestionTts(MotionEvent m, int mActivePointer) {
		/*
		 * Recuperation du POI le plus proche puis sa valeur
		 */
		PointOfInterest nearestPOI = getNearestPOI(mapView.callback.Pois, geopointReal);
		
		
		try {
			if (nearestPOI != null) {
//				List<Tag> tags = nearestPOI.getTags();
//				for (int iT = 0; iT < tags.size(); iT++)
//				{
//					Log.i("Tag"," ########### ###### " + iT + " "+ tags.get(iT).key + " : "+tags.get(iT).value);
//				}
				String valeur = getName(nearestPOI);

				if (m.getPointerCount() == 1) {
					if (!tampon0.equals(valeur)) {
						mapView.mapviewer.speakFlush(valeur, "0f");
						tampon0 = valeur;
					}
				} else {
					if (m.getPointerCount() == 2) {
						if (mActivePointer == 0 && !tampon0.equals(valeur)) {
							if (finger1Droite == true) {
								mapView.mapviewer.speakFlush(valeur, "-1f");
							} else if (finger1Droite == false) {
								mapView.mapviewer.speakFlush(valeur, "1f");
							}
							tampon0 = valeur;
						}
						if (mActivePointer == 1 && !tampon1.equals(valeur)) {
							if (finger1Droite == true) {
								mapView.mapviewer.speakFlush(valeur, "1f");
							} else if (finger1Droite == false) {
								mapView.mapviewer.speakFlush(valeur, "-1f");
							}
							tampon1 = valeur;
						}
					}
				}
			} else {
				if (mActivePointer == 0) {
					tampon0 = "";
				}
				if (mActivePointer == 1) {
					tampon1 = "";
				}
				if (!gpsIn) mapView.mapviewer.speakFlush("", "0f");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@SuppressLint("NewApi")
	private void gestionTtsWay(MotionEvent m, int mActivePointer) {
		/*
		 * Recuperation du POI le plus proche puis sa valeur
		 */
		
		try {
			if (Beach) {
//				List<Tag> tags = nearestPOI.getTags();
//				for (int iT = 0; iT < tags.size(); iT++)
//				{
//					Log.i("Tag"," ########### ###### " + iT + " "+ tags.get(iT).key + " : "+tags.get(iT).value);
//				}
				String valeur = wayName;
				

				if (m.getPointerCount() == 1) {
					if (!tampon0.equals(valeur)) {
						mapView.mapviewer.speakFlush(wayName, "0f");
						Log.i("Tag"," ########### ###### " + wayName);
						tampon0 = valeur;
					}
				} else {
					if (m.getPointerCount() == 2) {
						if (mActivePointer == 0 && !tampon0.equals(valeur)) {
							if (finger1Droite == true) {
								mapView.mapviewer.speakFlush(valeur, "-1f");
							} else if (finger1Droite == false) {
								mapView.mapviewer.speakFlush(valeur, "1f");
							}
							tampon0 = valeur;
						}
						if (mActivePointer == 1 && !tampon1.equals(valeur)) {
							if (finger1Droite == true) {
								mapView.mapviewer.speakFlush(valeur, "1f");
							} else if (finger1Droite == false) {
								mapView.mapviewer.speakFlush(valeur, "-1f");
							}
							tampon1 = valeur;
						}
					}
				}
			} else {
				if (mActivePointer == 0) {
					tampon0 = "";
				}
				if (mActivePointer == 1) {
					tampon1 = "";
				}
				mapView.mapviewer.speakFlush("", "0f");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressLint({ "NewApi", "NewApi" })
	public void identifyTwoFingers(MotionEvent m) {

		if (m.getPointerCount() == 2) {
			Point finger0 = new Point((int) m.getX(0), (int) m.getY(0));
			Point finger1 = new Point((int) m.getX(1), (int) m.getY(1));

			if (finger0.x < finger1.x) {
				finger1Droite = true;
				//Log.v("tyty", "1 est a droite");
			} else {
				finger1Droite = false;
				//Log.v("tyty", "1 est a gauche");
			}
		}
	}

	@SuppressLint({ "NewApi", "NewApi" })
	private void precision(MotionEvent m, int i) {

		if ((precision.get(i).x + 8 < m.getX(i) || precision.get(i).x - 8 > m
				.getX(i))
				|| ((precision.get(i).y + 8 < m.getY(i) || precision.get(i).y - 8 > m
						.getY(i)))) {
			precision.get(i).x = (int) m.getX(i);
			precision.get(i).y = (int) m.getY(i);

			bouge.add(i);
			//Log.v("aaaaa9", "dedans " + i);
		}
	}

	@SuppressLint({ "NewApi", "NewApi", "NewApi", "NewApi" })
	public void findIdFinger(MotionEvent m) {
		
		//if (new GeoTabletActivity().talkback)
			
		
		
		bouge.clear();
		for (int i = 0; i < m.getPointerCount(); i++) {
			try {
				if ((test.get(i).x != m.getX(i) || test.get(i).y != m.getY(i))) {
					precision(m, i);
				//	Log.v("ppppp", "Px: " + precision.get(i).x + " Py: "
					//		+ precision.get(i).y);

				}
			} catch (Exception e) {
				Log.v("erreur", "plusieurs doigts posés");
			}

		}

		test.clear();
		for (int i = 0; i < m.getPointerCount(); i++) {
			test.add(new Point((int) m.getX(i), (int) m.getY(i)));
		}
	}

	// Fonction pour avoir la distance entre 2 points
	public double Distance(double x1, double y1, double x2, double y2) {
		double distance;
		
		distance = Math.sqrt( ((x2-x1) * (x2-x1)) + ((y2-y1) * (y2-y1)) );
		
		return distance;
	}
	
	// Recuperation du POI le plus proche
	public PointOfInterest getNearestPOI(List<PointOfInterest> Pois, GeoPoint origine) {
	    double distanceMin = 0;
	    PointOfInterest poiNearest = null;
	    
	    // Get Projection
	    Projection p = this.mapView.getProjection();
	    
	    // Valeur en pixel de origine
	    Point posOrigine = new Point();
		p.toPixels(origine, posOrigine);
		//Log.i("Dist","" + posOrigine.x + " : "+ posOrigine.y);
	    
		if (Pois.size() > 0) 
		{
			// Le premier POI est le plus proche
			poiNearest = Pois.get(0);
			
			// Cree un GeoPoint a partir de poiNearest;
			GeoPoint geoPOI = new GeoPoint(poiNearest.getLatitude()* Math.pow(10, -6), poiNearest.getLongitude()* Math.pow(10, -6));
			Point posPOI = new Point();
			p.toPixels(geoPOI, posPOI);
		    
			// calcul distance
			distanceMin = this.Distance(posOrigine.x,posOrigine.y,posPOI.x,posPOI.y);
			//Log.i("Dist", " 0 : " + distanceMin + "("+posPOI.x+" / "+posPOI.y+")");
			
			for (int iP = 1; iP < Pois.size(); iP++)
			{
				// En fait non c'est peut celui ci qui est le plus proche
				PointOfInterest poi = Pois.get(iP);
				
				// Cree un GeoPoint a partir de poi; 
				geoPOI = new GeoPoint(poi.getLatitude()* Math.pow(10, -6), poi.getLongitude()* Math.pow(10, -6));
				p.toPixels(geoPOI, posPOI);
				
				// Calcul la distance
				double distance = this.Distance(posOrigine.x,posOrigine.y,posPOI.x,posPOI.y);
				//Log.i("Dist", " "+iP+" : " + distance + "("+posPOI.x+" / "+posPOI.y+")");
//				List<Tag> tags = poi.getTags();
//				for (int iT = 0; iT < tags.size(); iT++)
//				{
//					Log.i("Dist",""+tags.get(iT).key + " / "+ tags.get(iT).value);
//				}
//				
				// Si distance inferieur, c'etait bien lui le plus proche
				if (distance < distanceMin)
				{
					distanceMin = distance;
					poiNearest = poi;
				}
			}
			
			//Log.i("Dist", " MinDist : " + distanceMin);
		}
		else 
			return null;
		
//		List<Tag> tags = poiNearest.getTags();
//		for (int iT = 0; iT < tags.size(); iT++)
//		{
//			Log.i("Dist",""+tags.get(iT).key + " / "+ tags.get(iT).value);
//		}
		
		if (distanceMin > (SEUIL/scale))
			poiNearest = null;
		
		return poiNearest;
			
//			
//			//PointOfInterest poiNearest = new PointOfInterest();
//			// double min = 1000000;
//			for (PointOfInterest pointOfInterest : Pois) {
//
//				double x1 = pointOfInterest.getLatitude() * Math.pow(10, -6);
//				// Log.v("testDistance  ","X1"+ x1);
//				double y1 = pointOfInterest.getLongitude() * Math.pow(10, -6);
//				// Log.v("testDistance  ","Y1"+ y1);
//				double x2 = (float) geopointReal.getLatitude();
//				// Log.v("testDistance  ","X2"+ x2);
//				double y2 = (float) geopointReal.getLongitude();
//				// Log.v("testDistance  ","Y2"+ y2);
//				distance = Distance(x1, y1, x2, y2);
//				
//				Log.i("distance", "distance = " + distance);
//				
//				if (distance) {
//
//					Log.v("testDistance   ", "je suis lààààààà");
//					poiNearest = pointOfInterest;
//					break;
//				} else {
//					Log.v("testDistance   ", "je suis nullllllll");
//					poiNearest = null;
//				}
//			}
	}

	// Recuperation de la valeur du POI
	public String getName(PointOfInterest p) {
		
		boolean tourism = false;
		boolean amenity = false;
		boolean leisure = false;
		boolean natural = false;
		
		String value = "";
		String name = "";

//		for (Tag tag : p.getTags()) {
//			if ( (tag.key.compareTo("natural") == 0)  ) {
//				natural = true;
//				value = tag.value;
//			}		
//			if ( (tag.key.compareTo("name") == 0)  ) {
//				name = tag.value;
//			}
//		}
		
		for (Tag tag : p.getTags()) {
			if ( (tag.key.compareTo("tourism") == 0)  ) {
				tourism = true;
				value = tag.value;
			}		
			if ( (tag.key.compareTo("name") == 0)  ) {
				name = tag.value;
			}
		}
		
		for (Tag tag : p.getTags()) {
			if ( (tag.key.compareTo("amenity") == 0)  ) {
				amenity = true;
				value = tag.value;
			}		
			if ( (tag.key.compareTo("name") == 0)  ) {
				name = tag.value;
			}
		}
		
		for (Tag tag : p.getTags()) {
			if ( (tag.key.compareTo("leisure") == 0)  ) {
				leisure = true;
				value = tag.value;
			}		
			if ( (tag.key.compareTo("name") == 0)  ) {
				name = tag.value;
			}
		}
		
		if (amenity) return name ;
		else if (leisure) return name ;
		else if (tourism) return name ;
		
		else return null;

	}
	////BackUp
//	public String getName(PointOfInterest p) {
//		for (Tag tag : p.getTags()) {
//			if (tag.key.compareTo("tourism") == 0) {
//				return tag.value;
//			}
//		}
//		return null;
//	}
	
	
	

//	private void writeToCsvSound(String sound){
//		pw.print("Son "+sound+" joué");
//		pw.print("\n");
//		pw.flush();
//	}
	
//	private void writeToCsv(String time, int action, double x, double y) {
//		// Write to file for the first row
//		pw.print(time);
//		pw.print("\t | \t");
//		switch (action) {
//		case 0:
//			pw.print("Down");
//			break;
//		case 1:
//			pw.print("Up");
//			break;
//		case 2:
//			pw.print("Move");
//			break;
//		case 6:
//			pw.print("PointerUp");
//			break;
//		case 261:
//			pw.print("PointerDown");
//			break;
//		case 262:
//			pw.print("PointerUp");
//			break;
//		default:
//			pw.print(action);
//			break;
//		}
//		pw.print("\t | \t");
//		pw.print(x);
//		pw.print("\t | \t");
//		pw.print(y);
//		pw.print("\t | \t");
//		pw.print("\n");
//		// Flush the output to the file
//		pw.flush();
//	}
}