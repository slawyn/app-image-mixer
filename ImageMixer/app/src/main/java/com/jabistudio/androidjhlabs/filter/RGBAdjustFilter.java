/*
Copyright 2006 Jerry Huxtable

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.jabistudio.androidjhlabs.filter;

import com.jabistudio.androidjhlabs.filter.util.PixelUtils;

public class RGBAdjustFilter extends PointFilter {
	
	public int  rFactor, gFactor, bFactor;

	public RGBAdjustFilter() {
		this(0, 0, 0);
	}

	public RGBAdjustFilter(int r, int g, int b) {
		rFactor = r;
		gFactor = g;
		bFactor = b;
		canFilterIndexColorModel = true;
	}

	public void setRFactor( int rFactor ) {
		this.rFactor = rFactor;
	}
	
	public float getRFactor() {
		return rFactor;
	}
	
	public void setGFactor( int gFactor ) {
		this.gFactor = gFactor;
	}
	
	public float getGFactor() {
		return gFactor;
	}
	
	public void setBFactor( int bFactor ) {
		this.bFactor = bFactor;
	}
	
	public float getBFactor() {
		return bFactor;
	}

	public int[] getLUT() {
		int[] lut = new int[256];
		for ( int i = 0; i < 256; i++ ) {
			lut[i] = filterRGB( 0, 0, (i << 24) | (i << 16) | (i << 8) | i );
		}
		return lut;
	}
	
	public int filterRGB(int x, int y, int rgb) {
		int a = rgb & 0xff000000;
		int r = (rgb >> 16) & 0xff;
		int g = (rgb >> 8) & 0xff;
		int b = rgb & 0xff;
		r = PixelUtils.clamp((int)(r * rFactor/255));
		g = PixelUtils.clamp((int)(g * gFactor/255));
		b = PixelUtils.clamp((int)(b * bFactor/255));
		return a | (r << 16) | (g << 8) | b;
	}

	public String toString() {
		return "Colors/Adjust RGB...";
	}
}

