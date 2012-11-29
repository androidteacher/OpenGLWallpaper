/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.Beck.GLExample;



import java.io.InputStream;
import java.util.Calendar;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;





import android.content.Context;
import android.content.SharedPreferences;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.service.wallpaper.WallpaperService;
import android.service.wallpaper.WallpaperService.Engine;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.hardware.*;

public class GLExample extends GLWallpaperService  {
	public static final String SHARED_PREFS_NAME="flicker";
	
    private SharedPreferences mPrefs;
    public static Context context; 
    private OpenGLRenderer renderer;
    private float mOffset;
  
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
      
        
    }
   
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public Engine onCreateEngine() {
        return new CubeEngine();
    }

   

    class CubeEngine extends GLEngine  implements SharedPreferences.OnSharedPreferenceChangeListener {
    	
      
        private boolean mVisible;

       
        CubeEngine() {
        	
        	
        	
        	 mPrefs = GLExample.this.getSharedPreferences(SHARED_PREFS_NAME, 0);
             mPrefs.registerOnSharedPreferenceChangeListener(this);
             onSharedPreferenceChanged(mPrefs, null);
           
            
        }
        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
        
            // By default we don't get touch events, so enable them.
            setTouchEventsEnabled(true);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            mVisible = visible;
            
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
           
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            
        }

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset,
                float xStep, float yStep, int xPixels, int yPixels) {
         
        }

     
        @Override
        public void onTouchEvent(MotionEvent event) {
        	
             	        
     	             
     	             
        	super.onTouchEvent(event);
        	
        }
		public void onSharedPreferenceChanged(
				SharedPreferences sharedPreferences, String key) {
			
		
			String mode = sharedPreferences.getString("cube2_mode", "2"); 
			
			
			    

		}
        
       
      
    }



	

   

      
       

		
            }
            


    
