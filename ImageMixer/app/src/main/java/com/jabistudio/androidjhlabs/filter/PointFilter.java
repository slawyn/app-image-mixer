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

import static android.R.attr.id;
import static android.R.attr.start;
import static android.R.attr.startX;
import static android.R.attr.startY;
import static android.R.attr.width;

/**
 * An abstract superclass for point filters. The interface is the same as the old RGBImageFilter.
 */
public abstract class PointFilter {

    protected boolean canFilterIndexColorModel = false;

    public void filter(int[] src, int width, int height) {

        int idx = 0;
        //int [] outPixels = new int[(width-startX)*(height-startY)];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                idx = width * y + x;
                src[idx] = filterRGB(x, y, src[idx]);
                idx++;
            }


            // return outPixels;
        }
    }


    public abstract int filterRGB(int x, int y, int rgb);


}