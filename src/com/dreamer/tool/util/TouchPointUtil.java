package com.dreamer.tool.util;

import com.dreamer.opengles.GlesToolkit;

public class TouchPointUtil {

	private int arrayX = 1;
	private int arrayY = 1;
	private  float[][] AREAY;
	private  float[][] AREAX;
	
	public TouchPointUtil(int xs,int ys,int srcX,int srcY,int dstX,int dstY,int spaceX,int spaceY){
		this.arrayX = xs;
		this.arrayY = ys;
		AREAY = new float[arrayY][2];
		AREAX = new float[arrayX][2];
		for (int i = 0; i < arrayX; i++) {
			AREAX[i][0] = GlesToolkit.translateX(srcX + spaceX * i);
			AREAX[i][1] = GlesToolkit.translateX(dstX + spaceX * i);
		}
		for(int j = 0 ;j<arrayY;j++){
			AREAY[j][0] = GlesToolkit.translateY(srcY + spaceY * j);
			AREAY[j][1] = GlesToolkit.translateY(dstY + spaceY * j);
		}
	}
	
	public TouchPointUtil(int xs,int ys,float srcX,float srcY,float dstX,float dstY,float spaceX,float spaceY){
		this.arrayX = xs;
		this.arrayY = ys;
		AREAY = new float[arrayY][2];
		AREAX = new float[arrayX][2];
		for (int i = 0; i < arrayX; i++) {
			AREAX[i][0] = srcX + spaceX * i;
			AREAX[i][1] = dstX + spaceX * i;
		}
		for(int j = 0 ;j<arrayY;j++){
			AREAY[j][0] = srcY + spaceY * j;
			AREAY[j][1] = dstY + spaceY * j;
		}
	}
	
	
	public  int getIndexOfTouchPoint(float x, float y) {
		int n = -1;
		for (int i = 0; i < arrayX; i++) {
			if (x > AREAX[i][0] && x < AREAX[i][1] ){
				for(int j = 0 ;j<arrayY;j++){
					if( y > AREAY[j][0]&& y < AREAY[j][1]){
						n = j*arrayX + i;
					}
				}
			}
		}
		return n;
	}
}