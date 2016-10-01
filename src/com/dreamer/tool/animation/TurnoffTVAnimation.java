package com.dreamer.tool.animation;

import android.graphics.Matrix; 
import android.view.animation.AccelerateDecelerateInterpolator; 
import android.view.animation.Animation; 
import android.view.animation.Transformation; 
 
public class TurnoffTVAnimation extends Animation { 
    /** ���ĵ�X���� **/ 
    private int centerX = 0; 
    /** ���ĵ�Y���� **/ 
    private int centerY = 0; 
 
    @Override 
    public void initialize(int width, int height, int parentWidth, 
            int parentHeight) { 
        // void setDuration (long durationMillis) 
        // Since: API Level 1 How long this animation should last. 
        // The duration cannot be negative. 
        setDuration(500); 
 
        // void setFillAfter(boolean fillAfter) 
        // If fillAfter is true, the transformation that this animation 
        // performed will persist when it is finished. 
        setFillAfter(true); 
 
        // ���ͼƬ���ĵ�X���� 
        centerX = width / 2; 
        // ���ͼƬ���ĵ�Y���� 
        centerY = height / 2; 
 
        // void setInterpolator (Interpolator i) 
        // Since: API Level 1 Sets the acceleration curve for this animation. 
        // Defaults to a linear interpolation. 
        // setInterpolator(new AccelerateDecelerateInterpolator()) 
        // ѡ��һ���ٶȵ�Ч�� 
        // AccelerateDecelerateInterpolator 
        // An interpolator where the rate of change starts and ends slowly 
        // but accelerates through the middle. 
        setInterpolator(new AccelerateDecelerateInterpolator()); 
    } 
 
    /**
     * preScale(float sx, float sy, float px, float py) 
     * px ��  py �ǹ̶��㣬
     * ���� px,py=0,0 �Ļ���
     * ͼ��������Ͻ�Ϊ���㣬�������·Ŵ���С��
     * 
     * �����ͼ���ĵĻ���ͼ������ͼ����Ϊ���㣬
     * ���������ҵȱ����ķŴ���С��
     * 
     * һ������£����ͼ����ڲ����겻��Ҫ�Ļ���
     * ֻ��preScale(sy, sy)�Ϳ����ˡ�
     * Ҫ�õ�px,py�������ͨ����ǰ����ǣ��Animation��������
     * 
     * �򵥽����Ŵ��������ı䣬���ǰ�sx��sy������
     * ֻ��px,py�ǵ㣬�ڷŴ�ǰ�ͷŴ�󶼻᲻�䡣
     * 
     * ����: һ��(width)20 (height)10�ĳ����Σ�
     * ���Ͻ�������(0,0)�������½���(20,10)��
     * ���sx,sy=2,2  ���Ŵ�������
     * ��px ,py=0,0�Ŵ�����Ͻ���Ȼ��(0,0)�������½ǻ���(40,20)��
     * 
     * ��ͬ����sx,sy=2,2����px,py=10,5�Ļ���
     * �Ŵ�����Ͻǻ���(-10,-5)�����½ǻ���(30,15)��
     * Ψһ���겻��ľ�ֻ��10,5 �ǵ㡣����������Ȼ��Ŵ�������
     * 
     * ����ȥû��ʲô��ͬ�����������Animation�Ļ���
     * ��ΪAnimation����������Ҫ������Ч��Ҳ���в�ͬ��
     * 
     * 
     * interpolatedTime ��ʾ���ǵ�ǰ�����ļ��ʱ�� ��Χ��0-1
     * 
     * ��ô��������ǰ80%��ʱ������Ҫ�������쵽150%��
     * �仯������Ϊ 0.5 / 0.8 = 0.625 
     * ���Ժ�������ֵΪ 1 + 0.625f * interpolatedTime
     * 
     * ������ǰ80%��ʱ����ֱ�Ӽ�С�����ֻ��һ���߶�Ϊ0.01f���ߡ� 
     * �仯������Ϊ 1 / 0.8 = 1.25 
     * ������������ֵΪ 1 - 1.25f * interpolatedTime + 0.01f
     * ��ȻҲ����д�� 1 - interpolatedTime / 0.8f + 0.01f
     * 
     * ��20%��ʱ��������Ҫ�����150%ѹ����0%�� 
     * �仯������Ϊ 1.5 / 0.2 = 7.5 
     * ���Ժ�������ֵΪ 7.5f * (1 - interpolatedTime)
     * 
     * ���򱣳ֲ���ͺ��ˣ�������Ϊ0��ʱ���ȫ����ʧ�ˡ�
     */ 
    @Override 
    protected void applyTransformation(float interpolatedTime, Transformation t) { 
        final Matrix matrix = t.getMatrix(); 
        if (interpolatedTime < 0.8) { 
            matrix.preScale(1 + 0.625f * interpolatedTime, 
                    1 - 1.25f * interpolatedTime + 0.01f, centerX, centerY); 
        } else { 
            matrix.preScale(7.5f * (1 - interpolatedTime), 0.01f,  
                    centerX, centerY); 
        } 
    } 
} 