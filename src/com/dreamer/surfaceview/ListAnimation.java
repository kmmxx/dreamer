package com.dreamer.surfaceview;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Canvas;

public class ListAnimation {

	private int tmpIndex = 0;
	private List<SVImageView> mSVImageViews;
	private List<SVImageView> mSelectSVImageViews;
	private List<SVTextView> mSVTextViews;
	private List<SVTextView> mSelectSVTextViews;

	// animation
	private float srcAnimX = 0f;
	private float srcAnimY = 0f;
	private boolean isAnimFocusMove;
	private float tmpAnimProgress = 0;
	private float tmpAnimX = srcAnimX;
	private float dstAnimX = srcAnimX;
	private float tmpAnimY = srcAnimY;
	private float dstAnimY = srcAnimY;
	private float animPaddingX = 0f;
	private float animPaddingY = 0f;
	private int indexAnimY = 0;
	private int indexAnimX = 0;
	private float alphaDES = 1;
	private float alphaASC = 0;
	private boolean hasAnimation = false;
	private boolean isUP = true;

	// list
	private int offset = 0;
	private float srcX = 0f;
	private float srcY = 0f;
	private float paddingX = 0f;
	private float paddingY = 0;
	private int index = 0; // list 索引
	private int animSize = 0; // 动画时list的最大长度
	private int animDirect = ANIM_X; // 0 为X ，1 为Y
	public static final int ANIM_X = 0;
	public static final int ANIM_Y = 1;
	public static final int ANIM_XY = 2;

	/*--
	 *  //初始化   
	 *  1.mSVListAnimation = new SVListAnimation();
	 *	  mSVListAnimation.setInitData(SVListAnimation.ANIM_Y, 0.02f, 0.15f, 0.35f, 0.15f, 10);
	 *	//绘制
	 *  2.mSVListAnimation.setDrawData(textList,selectTextList, offset, index);
	 *	  mSVListAnimation.onDraw(gl);
	 *  //事件驱动设置动画，连续动画时需要停止当前动画
	 *  3.mSVListAnimation.stopAnimation();
	 *    mSVListAnimation.setHasAnimation(true);
	 *	  mSVListAnimation.setUP(true);
	 *	  mSVListAnimation.setIndexAnim(1);
	 * --*/
	public ListAnimation() {

	}

	public void moveAnimation() {
		// TODO Auto-generated method stub
		dstAnimX = animPaddingX * indexAnimX;
		dstAnimY = animPaddingY * indexAnimY;
		if (tmpAnimY != dstAnimY || tmpAnimX != dstAnimX)
			isAnimFocusMove = true;
		if (isAnimFocusMove) {
			tmpAnimProgress += 0.1f;
			alphaDES -= 0.1f;
			alphaASC += 0.1f;
			// tmpAnimY += 0.5f * (dstAnimY - tmpAnimY);
			tmpAnimX += 0.1f * animPaddingX * indexAnimX;
			tmpAnimY += 0.1f * animPaddingY * indexAnimY;
			if (tmpAnimProgress > 1.0f) {
				tmpAnimProgress = 0f;
				tmpAnimX = dstAnimX = 0;
				tmpAnimY = dstAnimY = 0;
				indexAnimX = 0;
				indexAnimY = 0;
				alphaDES = 1;
				alphaASC = 0;
				hasAnimation = false;
				isAnimFocusMove = false;
			}
		}
	}

	public void stopAnimation() {
		hasAnimation = false;
		tmpAnimProgress = 0f;
		indexAnimX = 0;
		indexAnimY = 0;
		tmpAnimY = dstAnimY = 0;
		tmpAnimX = dstAnimX = 0;
		alphaDES = 1;
		alphaASC = 0;
	}

	public void setDrawData(List<SVTextView> list, List<SVTextView> selectList,
			int offset, int index) {
		this.mSVTextViews = list;
		this.mSelectSVTextViews = selectList;
		this.offset = offset;
		this.index = index;
	}

	public void setImageDrawData(List<SVImageView> list,
			List<SVImageView> selectList, int offset, int index) {
		this.mSVImageViews = list;
		this.mSelectSVImageViews = selectList;
		this.offset = offset;
		this.index = index;
	}

	public void setImageDrawData(SVImageView[] list, SVImageView[] selectList,
			int offset, int index) {
		this.mSVImageViews = toImageList(list);
		this.mSelectSVImageViews = toImageList(selectList);
		this.offset = offset;
		this.index = index;
	}

	public void setInitData(int dir, float srcX, float srcY, float padding,
			float animPadding, int animSize) {
		this.animDirect = dir;
		this.srcX = srcX;
		this.srcY = srcY;
		if (dir == ANIM_X) {
			this.paddingX = padding;
			this.animPaddingX = animPadding;
		} else if (dir == ANIM_Y) {
			this.animPaddingY = animPadding;
			this.paddingY = padding;
		} else if (dir == ANIM_XY) {
			this.animPaddingX = animPadding;
			this.animPaddingY = animPadding;
			this.paddingX = padding;
			this.paddingY = padding;
		}
		this.animSize = animSize;

	}

	public List<SVImageView> toImageList(Object[] arrays) {
		List<SVImageView> list = new ArrayList<SVImageView>();
		for (int i = 0; i < arrays.length; i++) {
			list.add((SVImageView) arrays[i]);
		}
		return list;
	}

	public List<Object> objectToList(Object[] arrays) {
		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < arrays.length; i++) {
			list.add(arrays[i]);
		}
		return list;
	}

	public boolean onDraw(Canvas canvas) {
		tmpIndex = animSize - 1;
		/*--if (mSVTextViews != null && mSVTextViews.size() > 0
				&& mSelectSVTextViews != null && mSelectSVTextViews.size() > 0) {
			for (int i = 0; i < tmpIndex && i + offset < mSVTextViews.size(); i++) {
				if (hasAnimation) {
					tmpIndex = animSize;
					if (isUP) {
						gl.glTranslatef(srcX + (i - 1) * paddingX + tmpAnimX,
								srcY + (i - 1) * paddingY + tmpAnimY, 0f);
						if (i == 0) {
							gl.glColor4f(1, 1, 1, alphaASC);
						}
						if (i == animSize - 1) {
							gl.glColor4f(1, 1, 1, alphaDES);
						}
						if (mSVTextViews.get(i + offset) != null)
							mSVTextViews.get(i + offset).onDraw(gl);
					} else {
						gl.glTranslatef(srcX + i * paddingX + tmpAnimX, srcY
								+ i * paddingY + tmpAnimY, 0f);
						if (i == 0) {
							gl.glColor4f(1, 1, 1, alphaDES);
						}
						if (i == animSize - 1) {
							gl.glColor4f(1, 1, 1, alphaASC);
						}
						if (i + offset - 1 < 0) {
							if (mSVTextViews.get(i + offset) != null)
								mSVTextViews.get(i + offset).onDraw(gl);
						} else {
							if (mSVTextViews.get(i + offset - 1) != null)
								mSVTextViews.get(i + offset - 1).onDraw(gl);
						}
					}
					gl.glColor4f(1, 1, 1, 1);
				} else {
					gl.glTranslatef(srcX + i * paddingX, srcY + i * paddingY,
							0f);
					if (index == i + offset) {
						if (mSelectSVTextViews.get(i + offset) != null)
							mSelectSVTextViews.get(i + offset).onDraw(gl);
					} else {
						if (mSVTextViews.get(i + offset) != null)
							mSVTextViews.get(i + offset).onDraw(gl);
					}
				}
				gl.glPopMatrix();
			}
			moveAnimation();
		}
		
		if (mSVImageViews != null && mSVImageViews.size() > 0&&mSelectSVImageViews!=null&&mSelectSVImageViews.size()>0) {
			for (int i = 0; i < tmpIndex && i + offset < mSVImageViews.size(); i++) {
				gl.glPushMatrix();
				if (hasAnimation) {
					tmpIndex = animSize;
					if (isUP) {
						gl.glTranslatef(srcX + (i - 1) * paddingX + tmpAnimX,
								srcY + (i - 1) * paddingY + tmpAnimY, 0f);
						if (i == 0) {
							gl.glColor4f(1, 1, 1, alphaASC);
						}
						if (i == animSize - 1) {
							gl.glColor4f(1, 1, 1, alphaDES);
						}
						if (mSVImageViews.get(i + offset) != null)
							mSVImageViews.get(i + offset).onDraw(gl);
					} else {
						gl.glTranslatef(srcX + i * paddingX + tmpAnimX, srcY
								+ i * paddingY + tmpAnimY, 0f);
						if (i == 0) {
							gl.glColor4f(1, 1, 1, alphaDES);
						}
						if (i == animSize - 1) {
							gl.glColor4f(1, 1, 1, alphaASC);
						}
						if (i + offset - 1 < 0) {
							if (mSVImageViews.get(i + offset) != null)
								mSVImageViews.get(i + offset).onDraw(gl);
						} else {
							if (mSVImageViews.get(i + offset - 1) != null)
								mSVImageViews.get(i + offset - 1).onDraw(gl);
						}
					}
					gl.glColor4f(1, 1, 1, 1);
				} else {
					gl.glTranslatef(srcX + i * paddingX, srcY + i * paddingY,
							0f);
					if (index == i + offset) {
						if (mSelectSVImageViews.get(i + offset) != null)
							mSelectSVImageViews.get(i + offset).onDraw(gl);
					} else {
						if (mSVImageViews.get(i + offset) != null)
							mSVImageViews.get(i + offset).onDraw(gl);
					}
				}
				gl.glPopMatrix();
			}
			moveAnimation();
		}*/
		return true;
	}

	// 属性设置
	public void setAnimSize(int s) {
		this.animSize = s;
	}

	public int getTmpIndex() {
		return tmpIndex;
	}

	public void setTmpIndex(int tmpIndex) {
		this.tmpIndex = tmpIndex;
	}

	public List<SVImageView> getmSVImageViews() {
		return mSVImageViews;
	}

	public void setmSVImageViews(List<SVImageView> mSVImageViews) {
		this.mSVImageViews = mSVImageViews;
	}

	public List<SVTextView> getmSVTextViews() {
		return mSVTextViews;
	}

	public void setmSVTextViews(List<SVTextView> mSVTextViews) {
		this.mSVTextViews = mSVTextViews;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public float getSrcAnimX() {
		return srcAnimX;
	}

	public void setSrcAnimX(float srcAnimX) {
		this.srcAnimX = srcAnimX;
	}

	public float getSrcAnimY() {
		return srcAnimY;
	}

	public void setSrcAnimY(float srcAnimY) {
		this.srcAnimY = srcAnimY;
	}

	public boolean isAnimFocusMove() {
		return isAnimFocusMove;
	}

	public void setAnimFocusMove(boolean isAnimFocusMove) {
		this.isAnimFocusMove = isAnimFocusMove;
	}

	public float getTmpAnimProgress() {
		return tmpAnimProgress;
	}

	public void setTmpAnimProgress(float tmpAnimProgress) {
		this.tmpAnimProgress = tmpAnimProgress;
	}

	public float getTmpAnimX() {
		return tmpAnimX;
	}

	public void setTmpAnimX(float tmpAnimX) {
		this.tmpAnimX = tmpAnimX;
	}

	public float getDstAnimX() {
		return dstAnimX;
	}

	public void setDstAnimX(float dstAnimX) {
		this.dstAnimX = dstAnimX;
	}

	public float getTmpAnimY() {
		return tmpAnimY;
	}

	public void setTmpAnimY(float tmpAnimY) {
		this.tmpAnimY = tmpAnimY;
	}

	public float getDstAnimY() {
		return dstAnimY;
	}

	public void setDstAnimY(float dstAnimY) {
		this.dstAnimY = dstAnimY;
	}

	public float getAnimPaddingX() {
		return animPaddingX;
	}

	public void setAnimPaddingX(float animPaddingX) {
		this.animPaddingX = animPaddingX;
	}

	public float getAnimPaddingY() {
		return animPaddingY;
	}

	public void setAnimPaddingY(float animPaddingY) {
		this.animPaddingY = animPaddingY;
	}

	public int getIndexAnim() {
		return indexAnimY;
	}

	public int getIndexAnimY() {
		return indexAnimY;
	}

	public void setIndexAnimY(int indexAnimY) {
		this.indexAnimY = indexAnimY;
	}

	public int getIndexAnimX() {
		return indexAnimX;
	}

	public void setIndexAnimX(int indexAnimX) {
		this.indexAnimX = indexAnimX;
	}

	public int getAnimDirect() {
		return animDirect;
	}

	public void setAnimDirect(int animDirect) {
		this.animDirect = animDirect;
	}

	public void setIndexAnim(int indexAnim) {
		if (animDirect == ANIM_X) {
			this.indexAnimX = indexAnim;
		} else if (animDirect == ANIM_Y) {
			this.indexAnimY = indexAnim;
		} else if (animDirect == ANIM_XY) {
			this.indexAnimX = indexAnim;
			this.indexAnimY = indexAnim;
		}
	}

	public float getAlphaDES() {
		return alphaDES;
	}

	public void setAlphaDES(float alphaDES) {
		this.alphaDES = alphaDES;
	}

	public float getAlphaASC() {
		return alphaASC;
	}

	public void setAlphaASC(float alphaASC) {
		this.alphaASC = alphaASC;
	}

	public boolean isHasAnimation() {
		return hasAnimation;
	}

	public void setHasAnimation(boolean hasAnimation) {
		this.hasAnimation = hasAnimation;
	}

	public List<SVImageView> getmSelectSVImageViews() {
		return mSelectSVImageViews;
	}

	public void setmSelectSVImageViews(List<SVImageView> mSelectSVImageViews) {
		this.mSelectSVImageViews = mSelectSVImageViews;
	}

	public boolean isUP() {
		return isUP;
	}

	public void setUP(boolean isUP) {
		this.isUP = isUP;
	}

	public float getSrcX() {
		return srcX;
	}

	public void setSrcX(float srcX) {
		this.srcX = srcX;
	}

	public float getSrcY() {
		return srcY;
	}

	public void setSrcY(float srcY) {
		this.srcY = srcY;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getAnimSize() {
		return animSize;
	}

	public List<SVTextView> getmSelectSVTextViews() {
		return mSelectSVTextViews;
	}

	public void setmSelectSVTextViews(List<SVTextView> mSelectSVTextViews) {
		this.mSelectSVTextViews = mSelectSVTextViews;
	}

	public float getPaddingX() {
		return paddingX;
	}

	public void setPaddingX(float paddingX) {
		this.paddingX = paddingX;
	}

	public float getPaddingY() {
		return paddingY;
	}

	public void setPaddingY(float paddingY) {
		this.paddingY = paddingY;
	}

}
