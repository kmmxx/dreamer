package com.dreamer.opengles;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.HashMap;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

public class GlesToolkit {

	public static final int WRAP_ST_REPEAT = 0x100;
	public static final int WRAP_ST_EDGE = 0x101;
	public static final int WRAP_S_REPEAT_T_EDGE = 0x102;
	public static final int WRAP_S_EDGE_T_REPEAT = 0x103;

	private final static String TAG = GlesToolkit.class.getSimpleName();

	private static HashMap<String, Integer> programsMap = new HashMap<String, Integer>();

	private static HashMap<Integer, Integer> textureIdsMap = new HashMap<Integer, Integer>();

	private static int textureIds = 0;

	/**
	 * 锟斤拷锟街革拷锟斤拷锟斤拷锟斤拷锟斤拷锟�?
	 * 
	 * @param bitmap
	 * @param isRepeat
	 *            锟角凤拷锟截革拷锟斤拷锟斤拷
	 * @param minSample
	 * @param magSample
	 * @return 锟斤拷锟斤拷id
	 */
	public static int genTexture(Bitmap bitmap, boolean isRepeat,
			int minSample, int magSample) {
		try {
			if (bitmap == null)
				return -1;
			int[] textures = new int[1];
			GLES20.glGenTextures(1, textures, 0);
			int textureId = textures[0];
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
			// GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
			// GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
			// GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
			// GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
			GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
					GLES20.GL_TEXTURE_MIN_FILTER, minSample);
			GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
					GLES20.GL_TEXTURE_MAG_FILTER, magSample);
			if (minSample == GLES20.GL_LINEAR_MIPMAP_NEAREST) {
				GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
			}
			if (isRepeat) {
				GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
						GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
				GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
						GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
			} else {
				GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
						GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
				GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
						GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
			}
			GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
			if (bitmap != null)
				bitmap.recycle();
			textureIdsMap.put(textureIds++, textureId);
			Log.i(TAG, "all textureIds :" + textureIdsMap.size());
			return textureId;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return -1;
		}
	}

	public static int genTexture(Bitmap bitmap, boolean isRepeat,
			int minSample, int magSample, boolean recycle) {
		try {
			if (bitmap == null)
				return -1;
			int[] textures = new int[1];
			GLES20.glGenTextures(1, textures, 0);
			int textureId = textures[0];
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
			// GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
			// GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
			// GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
			// GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
			GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
					GLES20.GL_TEXTURE_MIN_FILTER, minSample);
			GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
					GLES20.GL_TEXTURE_MAG_FILTER, magSample);
			if (minSample == GLES20.GL_LINEAR_MIPMAP_NEAREST) {
				GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
			}
			if (isRepeat) {
				GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
						GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
				GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
						GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
			} else {
				GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
						GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
				GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
						GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
			}
			GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
			if (bitmap != null && recycle)
				bitmap.recycle();
			textureIdsMap.put(textureId, textureIds++);
			Log.i(TAG, "all textureIds :" + textureIdsMap.size());
			return textureId;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return -1;
		}
	}

	public static int genTexture(Bitmap bitmap, int wrap, int minSample,
			int magSample, boolean recycle) {
		try {
			if (bitmap == null)
				return -1;
			int[] textures = new int[1];
			GLES20.glGenTextures(1, textures, 0);
			int textureId = textures[0];
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
			GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
					GLES20.GL_TEXTURE_MIN_FILTER, minSample);
			GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
					GLES20.GL_TEXTURE_MAG_FILTER, magSample);
			if (minSample == GLES20.GL_LINEAR_MIPMAP_NEAREST) {
				GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
			}
			if (wrap == WRAP_ST_REPEAT) {
				GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
						GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
				GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
						GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
			} else if (wrap == WRAP_ST_EDGE) {
				GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
						GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
				GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
						GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
			} else if (wrap == WRAP_S_EDGE_T_REPEAT) {
				GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
						GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
				GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
						GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
			} else if (wrap == WRAP_S_REPEAT_T_EDGE) {
				GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
						GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
				GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
						GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
			}
			GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
			if (bitmap != null && recycle)
				bitmap.recycle();
			textureIdsMap.put(textureId, textureIds++);
			Log.i(TAG, "all textureIds :" + textureIdsMap.size());
			return textureId;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return -1;
		}
	}

	public int getTextureId(int id) {
		return textureIdsMap.get(id);
	}

	public static void removeTextureId(int id) {
		if (textureIdsMap.containsKey(id)) {
			Log.i(TAG, "removeTextureId:" + id);
			textureIdsMap.remove(id);
		}
		Log.i(TAG, "all textureIds :" + textureIdsMap.size());
	}

	/**
	 * 锟斤拷锟侥拷锟斤拷锟斤拷锟�?
	 * 
	 * @param bitmap
	 * @return
	 */
	public static int genTexture(Bitmap bitmap) {

		int[] textures = new int[1];
		GLES20.glGenTextures(1, textures, 0);
		int textureId = textures[0];
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
				GLES20.GL_REPEAT);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
				GLES20.GL_REPEAT);
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
		if (bitmap != null)
			bitmap.recycle();
		return textureId;
	}

	public static Bitmap getBitmap(Context ctx, int rsid) {
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeResource(ctx.getResources(), rsid);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return bitmap;
	}

	public static int loadShader(int shaderType, String source) {
		int shader = GLES20.glCreateShader(shaderType);
		if (shader != 0) {
			GLES20.glShaderSource(shader, source);
			GLES20.glCompileShader(shader);
			int[] compiled = new int[1];
			GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
			if (compiled[0] == 0) {
				Log.e(TAG, "compile error:" + GLES20.glGetShaderInfoLog(shader));
				GLES20.glDeleteShader(shader);
				shader = 0;
			}
		}
		return shader;
	}

	// 锟斤拷锟斤拷锟斤拷色锟斤拷锟斤拷锟斤拷姆锟斤拷锟�?
	public static int createProgram(String programNmae, String vertexSource,
			String fragmentSource) {
		// 锟斤拷锟截讹拷锟斤拷锟斤拷色锟斤�?
		int vetexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
		if (vetexShader == 0)
			return 0;
		// 锟斤拷锟斤拷片元锟斤拷色锟斤�?
		int pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
		if (pixelShader == 0)
			return 0;
		int program = GLES20.glCreateProgram();
		programsMap.put(programNmae, program);
		Log.d(TAG, "programNmae:" + programNmae + "program:" + program);
		try {
			if (program != 0) {
				GLES20.glAttachShader(program, vetexShader);
				checkGlError("glAttachShader");
				GLES20.glAttachShader(program, pixelShader);
				checkGlError("glAttachShader");
				GLES20.glLinkProgram(program);
				int[] linkStatus = new int[1];
				GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS,
						linkStatus, 0);
				if (linkStatus[0] != GLES20.GL_TRUE) {
					Log.e(TAG,
							"link error:" + GLES20.glGetProgramInfoLog(program));
					GLES20.glDeleteProgram(program);
					program = 0;
				}
			}
			return program;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			programsMap.remove(programNmae);
			return 0;
		}

	}

	public static int getGlesObjectProgram(Context context) {
		if (programsMap.get(GlesObject.class.getSimpleName()) != null) {
			return programsMap.get(GlesObject.class.getSimpleName());
		}
		String mVertexShader = GlesToolkit.loadFromAssetsFile(
				"object_vertex.sh", context.getResources());
		String mFragmentShader = GlesToolkit.loadFromAssetsFile(
				"object_frag.sh", context.getResources());
		GlesToolkit.createProgram(GlesObject.class.getSimpleName(),
				mVertexShader, mFragmentShader);
		return getProgram(GlesObject.class.getSimpleName());
	}

	public static int getProgram(String name) {
		return programsMap.get(name) == null ? 0 : programsMap.get(name);
	}

	public static void checkGlError(String op) {
		int error = 0;
		while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
			Log.e(TAG, "checkGlError:" + op + "->" + error);
			throw new RuntimeException(op + "->glerror:" + error);
		}
	}

	// 锟斤拷sh锟脚憋拷锟叫硷拷锟斤拷锟斤拷色锟斤拷锟斤拷锟捷的凤拷锟斤�?
	public static String loadFromAssetsFile(String fname, Resources r) {
		String result = null;
		try {
			InputStream in = r.getAssets().open(fname);
			int ch = 0;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while ((ch = in.read()) != -1) {
				baos.write(ch);
			}
			byte[] buff = baos.toByteArray();
			baos.close();
			in.close();
			result = new String(buff, "UTF-8");
			result = result.replace("\\r\\n", "\n");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Log.d(TAG, "loadFromAssetsFile error");
		}
		return result;
	}

	/**
	 * 锟斤拷锟斤拷Float锟斤拷锟斤拷锟斤�?
	 * 
	 * @param length
	 * @return
	 */
	public static FloatBuffer createFloatBuffer(int length) {
		ByteBuffer vBuffer = ByteBuffer.allocateDirect(length);
		vBuffer.order(ByteOrder.nativeOrder());
		FloatBuffer buf = vBuffer.asFloatBuffer();
		return buf;
	}

	// ------------------------------------------------------------------------------------------------------
	// //
	// --------------------------------|
	// |---------------------------------------- //
	// --------------------------------| Protected Function
	// |---------------------------------------- //
	// --------------------------------|
	// |---------------------------------------- //
	// ------------------------------------------------------------------------------------------------------
	// //
	/**
	 * 锟斤拷锟绞碉拷实锟斤拷锟斤拷锟轿伙拷锟絏转锟斤拷锟缴碉拷�?D透锟斤拷锟斤�?,0锟斤拷锟较碉
	 * 拷锟絋ranslate X�?
	 * 
	 * @param x
	 * @return
	 */
	public static float translateX(float x) {
		if (Global.TV_WIDTH > Global.TV_HEIGHT) {
			if (Global.TV_WIDTH == 1280) {
				// x = x / 1.5f;
			} else if (Global.TV_WIDTH == 1920) {
				x = x * 1.5f;
			} else if (Global.TV_WIDTH == 800) {
				x = x / 1.6f;
			}
		} else {
			if (Global.TV_WIDTH == 720) {
				// x = x / 1.7778f;
			} else if (Global.TV_WIDTH == 1080) {
				x = x / 1.185f;
			} else if (Global.TV_WIDTH == 480) {
				x = x / 2.666667f;
			}
		}

		return (GlesSurfaceView.RIGHT + GlesSurfaceView.RIGHT)
				/ (GlesSurfaceView.FULL_SCREEN_WIDTH / x);
	}

	/**
	 * 锟斤拷锟绞碉拷实锟斤拷锟斤拷锟轿伙拷锟結转锟斤拷锟缴碉拷�?D透锟斤拷锟斤�?,0锟斤拷锟较碉
	 * 拷锟絋ranslate Y�?
	 * 
	 * @param y
	 * @return
	 */
	public static float translateY(float y) {
		if (Global.TV_WIDTH > Global.TV_HEIGHT) {
			if (Global.TV_HEIGHT == 720) {
				// y = y / 1.5f;
			} else if (Global.TV_HEIGHT == 1080) {
				y = y * 1.5f;
			} else if (Global.TV_HEIGHT == 480) {
				y = y / 1.5f;
			}
		} else {
			if (Global.TV_HEIGHT == 1280) {
				// y = y * 1.5f;
			} else if (Global.TV_HEIGHT == 1920) {
				y = y * 2.66667f;
			} else if (Global.TV_HEIGHT == 800) {
				y = y * 1.11f;
			}
		}
		return (GlesSurfaceView.TOP + GlesSurfaceView.TOP)
				/ (GlesSurfaceView.FULL_SCREEN_HEIGHT / y);
	}

	public static float translateZ(int z) {
		// TODO Auto-generated method stub
		return (float) (z);
		// return (GlesSurfaceView.NEAR + GlesSurfaceView.FAR)
		// / (GlesSurfaceView.FULL_SCREEN_HEIGHT / z);
	}

	/**
	 * 锟斤拷锟绞碉拷实锟斤拷锟斤拷乜锟斤拷转锟斤拷锟缴讹拷应锟斤拷�?D透锟斤拷锟斤拷目锟�
	 * 
	 * @param width
	 * @return
	 */
	public static float translateWidth(float x) {
		if (Global.TV_WIDTH > Global.TV_HEIGHT) {
			if (Global.TV_WIDTH == 1280) {
				// x = x / 1.5f;
			} else if (Global.TV_WIDTH == 1920) {
				x = x * 1.5f;
			} else if (Global.TV_WIDTH == 800) {
				x = x / 1.6f;
			}
		} else {
			if (Global.TV_WIDTH == 720) {
				// x = x / 1.7778f;
			} else if (Global.TV_WIDTH == 1080) {
				x = x / 1.185f;
			} else if (Global.TV_WIDTH == 480) {
				x = x / 2.666667f;
			}
		}
		return x
				/ (GlesSurfaceView.FULL_SCREEN_WIDTH / (Math
						.abs(GlesSurfaceView.LEFT) + GlesSurfaceView.RIGHT));
	}

	/**
	 * 锟斤拷锟绞碉拷实锟斤拷锟斤拷馗叨锟阶拷锟斤拷啥锟接︼拷锟角�D透锟斤拷锟斤拷母锟�
	 * 
	 * @param height
	 * @return
	 */
	public static float translateHeight(float y) {
		if (Global.TV_WIDTH > Global.TV_HEIGHT) {
			if (Global.TV_HEIGHT == 720) {
				// y = y / 1.5f;
			} else if (Global.TV_HEIGHT == 1080) {
				y = y * 1.5f;
			} else if (Global.TV_HEIGHT == 480) {
				y = y / 1.5f;
			}
		} else {
			if (Global.TV_HEIGHT == 1280) {
				// y = y * 1.5f;
			} else if (Global.TV_HEIGHT == 1920) {
				y = y * 2.66667f;
			} else if (Global.TV_HEIGHT == 800) {
				y = y * 1.11f;
			}
		}
		return y
				/ (GlesSurfaceView.FULL_SCREEN_HEIGHT / (GlesSurfaceView.TOP + Math
						.abs(GlesSurfaceView.BOTTOM)));
	}
	// ------------------------------------------------------------------------------------------------------
	// //

}
