package com.dreamer.opengles;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

public class GlesLoadUtil {

	private static String TAG = GlesLoadUtil.class.getSimpleName();
	private static float repeatCount = 2f;

	public static float[] getCrossProduct(float x1, float y1, float z1,
			float x2, float y2, float z2) {
		float A = y1 * z2 - y2 * z1;
		float B = z1 * x2 - z2 * x1;
		float C = x1 * y2 - x2 * y1;
		return new float[] { A, B, C };
	}

	public static float[] vectorNormal(float[] vector) {
		float module = (float) Math.sqrt(vector[0] * vector[0] + vector[1]
				* vector[1] + vector[2] * vector[2]);
		return new float[] { vector[0] / module, vector[1] / module,
				vector[2] / module, };
	}

	public static GlesLoadedObject loadObjectWithNormalFromFile(String fname,
			Context ctx) {
		GlesLoadedObject mGlesLoadedObject = null;
		List<Float> alvSource = new ArrayList<Float>();
		List<Float> alvResult = new ArrayList<Float>();
		List<Float> altSource = new ArrayList<Float>();
		List<Float> altResult = new ArrayList<Float>();
		List<Float> alnResult = new ArrayList<Float>();
		try {
			InputStream in = ctx.getResources().getAssets().open(fname);
			InputStreamReader isr = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(isr);
			String temps = null;
			while ((temps = br.readLine()) != null) {
				String[] tempsa = temps.split("[ ]+");
				if (tempsa[0].trim().equals("v")) {
					alvSource.add(Float.parseFloat(tempsa[1]));
					alvSource.add(Float.parseFloat(tempsa[2]));
					alvSource.add(Float.parseFloat(tempsa[3]));
				}else if(tempsa[0].trim().equals("vt")){
					altSource.add(Float.parseFloat(tempsa[1])/repeatCount );
					altSource.add(Float.parseFloat(tempsa[2])/repeatCount);
				}else if (tempsa[0].trim().equals("f")) {
					int index = Integer.parseInt(tempsa[1].split("/")[0]) - 1;
					float x0 = alvSource.get(3 * index);
					float y0 = alvSource.get(3 * index + 1);
					float z0 = alvSource.get(3 * index + 2);
					alvResult.add(x0);
					alvResult.add(y0);
					alvResult.add(z0);
					index = Integer.parseInt(tempsa[2].split("/")[0]) - 1;
					float x1 = alvSource.get(3 * index);
					float y1 = alvSource.get(3 * index + 1);
					float z1 = alvSource.get(3 * index + 2);
					alvResult.add(x1);
					alvResult.add(y1);
					alvResult.add(z1);
					index = Integer.parseInt(tempsa[3].split("/")[0]) - 1;
					float x2 = alvSource.get(3 * index);
					float y2 = alvSource.get(3 * index + 1);
					float z2 = alvSource.get(3 * index + 2);
					alvResult.add(x2);
					alvResult.add(y2);
					alvResult.add(z2);
					float vxa = x1 - x0;
					float vya = y1 - y0;
					float vza = z1 - z0;
					float vxb = x2 - x0;
					float vyb = y2 - y0;
					float vzb = z2 - z0;
					float[] vNormal = vectorNormal(getCrossProduct(vxa, vya,
							vza, vxb, vyb, vzb));
					for (int i = 0; i < 3; i++) {
						alnResult.add(vNormal[0]);
						alnResult.add(vNormal[1]);
						alnResult.add(vNormal[2]);
					}
					int indexTex = Integer.parseInt(tempsa[1].split("/")[1])-1;
					altResult.add(altSource.get(indexTex * 2));
					altResult.add(altSource.get(indexTex * 2+1));
					indexTex = Integer.parseInt(tempsa[2].split("/")[1])-1;
					altResult.add(altSource.get(indexTex * 2));
					altResult.add(altSource.get(indexTex * 2+1));
					indexTex = Integer.parseInt(tempsa[3].split("/")[1])-1;
					altResult.add(altSource.get(indexTex * 2));
					altResult.add(altSource.get(indexTex * 2+1));
				}
			}
			int size = alvResult.size();
			float[] vXYZ = new float[size];
			for (int i = 0; i < size; i++) {
				vXYZ[i] = alvResult.get(i);
			}
			size = alnResult.size();
			float[] nXYZ = new float[size];
			for (int i = 0; i < size; i++) {
				nXYZ[i] = alnResult.get(i);
			}
			size = altResult.size();
			float[] tXYZ = new float[size];
			for (int i = 0; i < size; i++) {
				tXYZ[i] = altResult.get(i);
			}
			mGlesLoadedObject = new GlesLoadedObject(ctx, vXYZ,nXYZ,tXYZ);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return mGlesLoadedObject;
	}

	public static GlesVerticeObject loadObjectFromFile(String fname, Context ctx) {
		GlesVerticeObject mGlesVerticeObject = null;
		List<Float> alvSource = new ArrayList<Float>();
		List<Float> alvResult = new ArrayList<Float>();
		try {
			InputStream in = ctx.getResources().getAssets().open(fname);
			InputStreamReader isr = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(isr);
			String temps = null;
			while ((temps = br.readLine()) != null) {
				String[] tempsa = temps.split("[ ]+");
				if (tempsa[0].trim().equals("v")) {
					alvSource.add(Float.parseFloat(tempsa[1]));
					alvSource.add(Float.parseFloat(tempsa[2]));
					alvSource.add(Float.parseFloat(tempsa[3]));
				} else if (tempsa[0].trim().equals("f")) {
					int index = Integer.parseInt(tempsa[1].split("/")[0]) - 1;
					alvResult.add(alvSource.get(3 * index));
					alvResult.add(alvSource.get(3 * index + 1));
					alvResult.add(alvSource.get(3 * index + 2));
					index = Integer.parseInt(tempsa[2].split("/")[0]) - 1;
					alvResult.add(alvSource.get(3 * index));
					alvResult.add(alvSource.get(3 * index + 1));
					alvResult.add(alvSource.get(3 * index + 2));
					index = Integer.parseInt(tempsa[3].split("/")[0]) - 1;
					alvResult.add(alvSource.get(3 * index));
					alvResult.add(alvSource.get(3 * index + 1));
					alvResult.add(alvSource.get(3 * index + 2));
				}
			}
			int size = alvResult.size();
			float[] vXYZ = new float[size];
			for (int i = 0; i < size; i++) {
				vXYZ[i] = alvResult.get(i);
			}
			mGlesVerticeObject = new GlesVerticeObject(ctx, vXYZ);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return mGlesVerticeObject;
	}

	public static float[][] loadLandforms(Bitmap bitmap){
		int colsPlusOne = bitmap.getWidth();
		int rowsPlusOne = bitmap.getHeight();
		float[][] result = new float[rowsPlusOne][colsPlusOne];
		for(int i = 0;i<rowsPlusOne;i++){
			for(int j = 0;j<colsPlusOne;j++){
				int color = bitmap.getPixel(j, i);
				int h = (Color.red(color)+Color.green(color)+Color.blue(color))/3;
				result[i][j] = h;
			}
		}
		return result;
	}
}
