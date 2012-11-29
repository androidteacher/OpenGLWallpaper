package com.Beck.GLExample;

/*
 * Copyright (C) 2007 The Android Open Source Proect
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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import com.Beck.GLExample.R;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Handler;
import android.util.Log;

class GLModel implements Serializable {

	
	private static String mTex;
	private static int intTex;
	private float moonangle = 0f;
	private int IdxCnt;
	private float langle = 0f;
	public static boolean useshading = false;
	public static float shadowcolor = 0.50f;
	private static FileTexture filetexture;
	
	// public static final Handler mHandler = new Handler();
	// Create runnable for posting
	public static final Runnable mUpdateTex = new Runnable() {
		public void run() {
			InitTex();
		}
	};

	public GLModel(Context context) {
		mContext = context;
		loadObj();
		Log.e("Model", "LOADED");
		

	}

	public static void InitTex() {
		intTex = 3;
		setTexture(1);
		filetexture = new FileTexture(gl11, "null");
		filetexture.loadTexture();
		textures = new GLTextures(gl11, mContext);
		textures.add(R.drawable.texture);
		textures.loadTextures();
	}

	public void Init(GL10 gl) {
		if (gl == null)
			return;
		gl11 = (GL11) gl;

		InitTex();

		int[] buffer = new int[1];

	
		gl11.glGenBuffers(1, buffer, 0);
		mVertBufferIndex = buffer[0];
		gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, mVertBufferIndex);
		gl11.glBufferData(GL11.GL_ARRAY_BUFFER, mVertexBuffer.capacity() * 4,
				mVertexBuffer, GL11.GL_STATIC_DRAW);
		Log.e("Vertex", "ok");

		
		gl11.glGenBuffers(1, buffer, 0);
		mNormBufferIndex = buffer[0];
		gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, mNormBufferIndex);
		gl11.glBufferData(GL11.GL_ARRAY_BUFFER, mNormBuffer.capacity() * 4,
				mNormBuffer, GL11.GL_STATIC_DRAW);
		Log.e("Normal", "ok");

		// texcoord buffer
		gl11.glGenBuffers(1, buffer, 0);
		mTexBufferIndex = buffer[0];
		gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, mTexBufferIndex);
		gl11.glBufferData(GL11.GL_ARRAY_BUFFER, mTexBuffer.capacity() * 4,
				mTexBuffer, GL11.GL_STATIC_DRAW);
		Log.e("Texture", "ok");

		// unbind array buffer
		gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);

		// Buffer d'indices
		gl11.glGenBuffers(1, buffer, 0);
		mIndexBufferIndex = buffer[0];
		gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, mIndexBufferIndex);
		gl11.glBufferData(GL11.GL_ELEMENT_ARRAY_BUFFER,
				mIndexBuffer.capacity() * 2, mIndexBuffer, GL11.GL_STATIC_DRAW);

		// Unbind the element array buffer.
		gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);
		Log.e("VboCube", "Idx ok");
		IdxCnt = mIndexBuffer.capacity();
		
	}

	private void loadObj() {
		try {
			AssetManager am = mContext.getAssets();
			String str;
			String[] tmp;
			String[] ftmp;
			float v;
			ArrayList<Float> vlist = new ArrayList<Float>();
			ArrayList<Float> tlist = new ArrayList<Float>();
			ArrayList<Float> nlist = new ArrayList<Float>();
			ArrayList<Fp> fplist = new ArrayList<Fp>();

			BufferedReader inb = new BufferedReader(new InputStreamReader(am
					.open("tree.obj")), 1024);
			while ((str = inb.readLine()) != null) {
				tmp = str.split(" ");
				if (tmp[0].equalsIgnoreCase("v")) {

					for (int i = 1; i < 4; i++) {
						v = Float.parseFloat(tmp[i]);
						vlist.add(v);
					}

				}
				if (tmp[0].equalsIgnoreCase("vn")) {

					for (int i = 1; i < 4; i++) {
						v = Float.parseFloat(tmp[i]);
						nlist.add(v);
					}

				}
				if (tmp[0].equalsIgnoreCase("vt")) {
					for (int i = 1; i < 3; i++) {
						v = Float.parseFloat(tmp[i]);
						tlist.add(v);
					}

				}
				if (tmp[0].equalsIgnoreCase("f")) {
					for (int i = 1; i < 4; i++) {
						ftmp = tmp[i].split("/");

						long chi = Integer.parseInt(ftmp[0]) - 1;
						int cht = Integer.parseInt(ftmp[1]) - 1;
						int chn = Integer.parseInt(ftmp[2]) - 1;

						fplist.add(new Fp(chi, cht, chn));
					}
					NBFACES++;
				}
			}

			ByteBuffer vbb = ByteBuffer.allocateDirect(fplist.size() * 4 * 3);
			vbb.order(ByteOrder.nativeOrder());
			mVertexBuffer = vbb.asFloatBuffer();

			ByteBuffer vtbb = ByteBuffer.allocateDirect(fplist.size() * 4 * 2);
			vtbb.order(ByteOrder.nativeOrder());
			mTexBuffer = vtbb.asFloatBuffer();

			ByteBuffer nbb = ByteBuffer.allocateDirect(fplist.size() * 4 * 3);
			nbb.order(ByteOrder.nativeOrder());
			mNormBuffer = nbb.asFloatBuffer();

			for (int j = 0; j < fplist.size(); j++) {
				mVertexBuffer.put(vlist.get((int) (fplist.get(j).Vi * 3)));
				mVertexBuffer.put(vlist.get((int) (fplist.get(j).Vi * 3 + 1)));
				mVertexBuffer.put(vlist.get((int) (fplist.get(j).Vi * 3 + 2)));

				mTexBuffer.put(tlist.get(fplist.get(j).Ti * 2));
				mTexBuffer.put(tlist.get((fplist.get(j).Ti * 2) + 1));

				mNormBuffer.put(nlist.get(fplist.get(j).Ni * 3));
				mNormBuffer.put(nlist.get((fplist.get(j).Ni * 3) + 1));
				mNormBuffer.put(nlist.get((fplist.get(j).Ni * 3) + 2));
			}

			mIndexBuffer = CharBuffer.allocate(fplist.size());
			for (int j = 0; j < fplist.size(); j++) {
				mIndexBuffer.put((char) j);
			}

			mVertexBuffer.position(0);
			mTexBuffer.position(0);
			mNormBuffer.position(0);
			mIndexBuffer.position(0);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void draw(GL10 gl, boolean showmoon) {
		if (gl11 == null)
			return;
		if (gl11 == null)
			return;
		gl.glPushMatrix();
		gl.glEnable(GL10.GL_TEXTURE_2D);

		
			
			filetexture.setTexture();
	

		gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, mTexBufferIndex);
		
			
			gl11.glClientActiveTexture(GL10.GL_TEXTURE0); // lightmap
			gl11.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			gl11.glTexCoordPointer(2, GL10.GL_FLOAT, 0, 0);

			gl11.glClientActiveTexture(GL10.GL_TEXTURE1); // color
			gl11.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			gl11.glTexCoordPointer(2, GL10.GL_FLOAT, 0, 0);

			gl11.glActiveTexture(GL10.GL_TEXTURE0);
			gl11.glEnable(GL10.GL_TEXTURE_2D);
			gl11.glActiveTexture(GL10.GL_TEXTURE1);
			gl11.glEnable(GL10.GL_TEXTURE_2D);

			gl11.glColor4f(shadowcolor, shadowcolor, shadowcolor, shadowcolor);

			gl11.glActiveTexture(GL10.GL_TEXTURE0);
			
			gl11.glTexEnvi(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,
					GL11.GL_COMBINE);
			gl11.glTexEnvi(GL10.GL_TEXTURE_ENV, GL11.GL_COMBINE_RGB,
					GL10.GL_ADD);
			gl11.glTexEnvi(GL10.GL_TEXTURE_ENV, GL11.GL_SRC0_RGB,
					GL10.GL_TEXTURE);
			gl11.glTexEnvi(GL10.GL_TEXTURE_ENV, GL11.GL_SRC1_RGB,
					GL11.GL_PREVIOUS);

			gl11.glActiveTexture(GL10.GL_TEXTURE1);
		
			filetexture.setTexture();
			
			gl11.glTexEnvi(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,
					GL11.GL_COMBINE);
			
			gl11.glTexEnvi(GL10.GL_TEXTURE_ENV, GL11.GL_COMBINE_RGB,
					GL10.GL_MODULATE);
		
			gl11.glTexEnvi(GL10.GL_TEXTURE_ENV, GL11.GL_SRC0_RGB,
					GL11.GL_PREVIOUS);
		
			gl11.glTexEnvi(GL10.GL_TEXTURE_ENV, GL11.GL_SRC1_RGB,
					GL10.GL_TEXTURE);
			
			gl11.glTexEnvi(GL10.GL_TEXTURE_ENV, GL11.GL_OPERAND0_RGB,
					GL10.GL_SRC_COLOR);
			gl11.glTexEnvi(GL10.GL_TEXTURE_ENV, GL11.GL_OPERAND1_RGB,
					GL10.GL_SRC_COLOR);
		
		gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, mVertBufferIndex);
		gl11.glVertexPointer(3, GL10.GL_FLOAT, 0, 0);

		gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, mNormBufferIndex);
		gl11.glNormalPointer(GL10.GL_FLOAT, 0, 0);

		gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, mIndexBufferIndex);
		gl11.glDrawElements(GL11.GL_TRIANGLES, IdxCnt, GL11.GL_UNSIGNED_SHORT,
				0);

		

		
		gl.glPopMatrix();
		
	}

	public void freeHardwareBuffers() {

		int[] buffer = new int[1];
		buffer[0] = mVertBufferIndex;
		

		buffer[0] = mTexBufferIndex;
		

		buffer[0] = mNormBufferIndex;
		

		buffer[0] = mIndexBufferIndex;
		

		mVertBufferIndex = 0;
		mIndexBufferIndex = 0;
		mTexBufferIndex = 0;
		mNormBufferIndex = 0;

		Log.d("Log", "hardware buffer freed");

	}

	public static void setTexture(int t) {
		intTex = t;
		t = 1;
		switch (t) {
		case 0:
			mTex = "0";
			
			break;
		
		}
	}
	private class Fp {
		public long Vi;
		public int Ti;
		public int Ni;

		public Fp(long chi, int ti, int ni) {
			Vi = chi;
			Ti = ti;
			Ni = ni;
			
		}
	}

	public static GL11 gl11;
	private static int NBFACES = 0;
	private static GLTextures textures;
	
	private static int mVertBufferIndex;
	private static int mNormBufferIndex;
	private static int mTexBufferIndex;
	private static int mIndexBufferIndex;
	private static FloatBuffer mVertexBuffer;
	private static FloatBuffer mTexBuffer;
	private static FloatBuffer mNormBuffer;
	private static CharBuffer mIndexBuffer;
	private static Context mContext;

}
