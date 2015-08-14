package sk.berops.android.fueller.gui.maintenance;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import sk.berops.android.fueller.dataModel.Axle;
import sk.berops.android.fueller.dataModel.maintenance.Tyre;
import sk.berops.android.fueller.gui.common.GuiUtils;

/**
 * Graphical representation of a Tyre
 * @author bernard
 */
public class TyreGUIContainer {
	private AssetManager manager;
	
	private final static String tyreSummerBitmapFilename = "axles/tyre_summer.png";
	private final static String tyreAllSeasonsBitmapFilename = "axles/tyre_allseasons.png";
	private final static String tyreWinterBitmapFilename = "axles/tyre_winter.png";
	private final static String tyreSpikesBitmapFilename = "axles/tyre_spikes.png";
	private final static String tyreEmptyBitmapFilename = "axles/tyre_empty.png";
	private final static String tyreFlashingBitmapFilename = "axles/tyre_flash.png";

	private static Bitmap tyreSummerBitmap;
	private static Bitmap tyreAllSeasonsBitmap;
	private static Bitmap tyreWinterBitmap;
	private static Bitmap tyreSpikesBitmap;
	private static Bitmap tyreEmptyBitmap;
	private static Bitmap tyreFlashingBitmap;
	
	/**
	 * X-axis coordinate of the tyre on the graphics
	 */
	private int x;
	
	/**
	 * Y-axis coordinate of the tyre on the graphics
	 */
	private int y;
	
	/**
	 * Width of the tyre graphics
	 */
	private int width;
	
	/**
	 * Height of the tyre graphics
	 */
	private int height;
	
	/**
	 * Tyre object this container is responsible for displaying
	 */
	private Tyre tyre;
	
	public TyreGUIContainer(Context context, Tyre tyre, int x, int y, int width, int height) {
		this.manager = context.getAssets();
		this.tyre = tyre;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public int getTyreColor() {
		if (getTyre() == null) {
			return 0xFF000000;
		} else {
			// orange in the middle
			return GuiUtils.getShade(Color.GREEN, 0xFFFFFF00, Color.RED, getTyre().getWearLevel() / 100.0);
		}
	}
	
	public Bitmap getTyreBitmap() {
		if (tyre == null)
			return getTyreEmptyBitmap();
		if (tyre.getSeason() == null)
			return getTyreSummerBitmap();

		switch (tyre.getSeason()) {
		case ALL_SEASON:
			return getTyreAllSeasonsBitmap();
		case SPIKES:
			return getTyreSpikesBitmap();
		case SUMMER:
			return getTyreSummerBitmap();
		case WINTER:
			return getTyreWinterBitmap();
		default:
			Log.d("ERROR", "Unknown tyre type encountered");
			return null;
		}
	}

	public Bitmap getTyreSummerBitmap() {
		if (tyreSummerBitmap == null) {
			tyreSummerBitmap = loadBitmap(tyreSummerBitmapFilename);
		}

		return tyreSummerBitmap;
	}

	public Bitmap getTyreAllSeasonsBitmap() {
		if (tyreAllSeasonsBitmap == null) {
			tyreAllSeasonsBitmap = loadBitmap(tyreAllSeasonsBitmapFilename);
		}

		return tyreAllSeasonsBitmap;
	}

	public Bitmap getTyreWinterBitmap() {
		if (tyreWinterBitmap == null) {
			tyreWinterBitmap = loadBitmap(tyreWinterBitmapFilename);
		}

		return tyreWinterBitmap;
	}

	public Bitmap getTyreSpikesBitmap() {
		if (tyreSpikesBitmap == null) {
			tyreSpikesBitmap = loadBitmap(tyreSpikesBitmapFilename);
		}

		return tyreSpikesBitmap;
	}

	public Bitmap getTyreEmptyBitmap() {
		if (tyreEmptyBitmap == null) {
			tyreEmptyBitmap = loadBitmap(tyreEmptyBitmapFilename);
		}

		return tyreEmptyBitmap;
	}

	public Bitmap getTyreFlashingBitmap() {
		if (tyreFlashingBitmap == null) {
			tyreFlashingBitmap = loadBitmap(tyreFlashingBitmapFilename);
		}
		return tyreFlashingBitmap;
	}

	private Bitmap loadBitmap(String filename) {
		Bitmap bitmap = null;

		try {
			InputStream istr = manager.open(filename);
			bitmap = BitmapFactory.decodeStream(istr);
		} catch (IOException e) {
			Log.d("ERROR", "init: problem reading asset " + filename);
		} catch (Exception e) {
			Log.d("ERROR", "init: exception " + e.getStackTrace());
		}

		return bitmap;
	}

	public BitmapDrawable getTyreBitmapDrawable(Resources resources) {
		return new BitmapDrawable(resources, getTyreBitmap());
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Tyre getTyre() {
		return tyre;
	}

	public void setTyre(Tyre tyre) {
		this.tyre = tyre;
	}
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}