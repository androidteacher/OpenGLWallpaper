package com.Beck.GLExample;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.util.Log;

public class GLTextures {
	private int[] mCropWorkspace;
	private static BitmapFactory.Options sBitmapOptions = new BitmapFactory.Options();
	public GLTextures(GL10 gl, Context context) {
		if(gl==null)return;
		this.gl = gl;
		this.context = context;
		this.textureMap = new java.util.HashMap<Integer, Integer>();
		mCropWorkspace = new int[4];
		sBitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
	}

	public void freeTexs(){
		gl.glDeleteTextures(textures.length, textures,0);
	}
	
	public void loadTextures() {
		if(gl==null)return;
		int[] tmp_tex = new int[textureFiles.length];
		gl.glGenTextures(textureFiles.length, tmp_tex, 0);
		textures = tmp_tex;
		for (int i = 0; i < textureFiles.length; i++) {
			// Load it up
			this.textureMap.put(new Integer(textureFiles[i]), new Integer(i));
			int tex = tmp_tex[i];
			
            gl.glBindTexture(GL10.GL_TEXTURE_2D, tex);
            gl.glPixelStorei(GL10.GL_UNPACK_ALIGNMENT, 1);
          
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
            
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);

            gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE);

            InputStream is = context.getResources().openRawResource(textureFiles[i]);
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(is, null, sBitmapOptions);
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                   
                }
            }

        
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

       
            
           
            
            bitmap.recycle();

            ((GL11) gl).glTexParameteriv(GL10.GL_TEXTURE_2D, 
                    GL11Ext.GL_TEXTURE_CROP_RECT_OES, mCropWorkspace, 0);

            
            int error = gl.glGetError();
            if (error != GL10.GL_NO_ERROR) {
                Log.w("VboCube", "Texture Load GLError: " + error);
            }

		}
	}

	public void setTexture(int id) {
		if(gl==null)return;
		try {
			int textureid = this.textureMap.get(new Integer(id)).intValue();
			gl.glBindTexture(GL10.GL_TEXTURE_2D, this.textures[textureid]);

		} catch (Exception e) {
			return;
		}
	}

	public void add(int resource) {
		if (textureFiles == null) {
			textureFiles = new int[1];
			textureFiles[0] = resource;
		} else {
			int[] newarray = new int[textureFiles.length + 1];
			for (int i = 0; i < textureFiles.length; i++) {
				newarray[i] = textureFiles[i];
			}
			newarray[textureFiles.length] = resource;
			textureFiles = newarray;
		}
	}

	private java.util.HashMap<Integer, Integer> textureMap;
	private int[] textureFiles;
	private GL10 gl;
	private Context context;
	private int[] textures;
}
