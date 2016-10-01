
package com.dreamer.tool.animation;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Vibrator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.CycleInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LayoutAnimationController;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

/**
 * @author MaoMing.Ke@Plf.GraphicTech
 *
 *         2014年12月12日
 */
public class AnimationManager {

    /*
     * 3.0以前，android支持两种动画模式，tween animation,frame animation，
     * 在android3.0中又引入了一个新的动画系统：property animation， 这三种动画模式在SDK中被称为
     * 
     * @View animation
     * @Frame animation。
     * @Property animation
     */

    /*
     * 一.android animation
     * Animation为Android提供了一系列的动画效果：旋转、缩放、平移、淡入淡出，这些动画效果可以应用于绝大多数控件。
     * Animation可分为两类,有两种实现方法：
     *   @在xml文件中实现，优点是复用性好，可维护性好，多个控件可以使用同一个xml文件。缺点是由于xml文件不进行编译，所以排错比较难。  
     *   @在代码中实现，优点是排错很方便，缺点是重复代码多，可复用性低。：
        1.  Tween Animations：渐变动画，具体就是旋转、缩放、平移、淡入淡出效果。
     *    @xml实现
              <?xml version="1.0" encoding="utf-8"?>
                <set xmlns:android="http://schemas.android.com/apk/res/android" >
                                <alpha
                                    android:duration="3000"
                                    android:fromAlpha="0.1"
                                    android:toAlpha="1.0" />
                                <!-- 透明度控制动画效果 alpha 浮点型值：
                                fromAlpha 属性为动画起始时透明度  
                                toAlpha   属性为动画结束时透明度
                                                                                                                说明:  0.0表示完全透明  1.0表示完全不透明  以上值取0.0-1.0之间的float数据类型的数字                               
                                                                                                                长整型值：duration  属性为动画持续时间  时间以毫秒为单位
                                -->
                                <rotate
                                    android:duration="3000"
                                    android:fromDegrees="0"
                                    android:interpolator="@android:anim/accelerate_decelerate_interpolator"
                                    android:pivotX="50%"
                                    android:pivotY="50%"
                                    android:toDegrees="+350" />
                                <!--rotate 旋转动画效果
                                                                                                                         属性：interpolator 指定一个动画的插入器
                                                                                                                              在我试验过程中，使用android.res.anim中的资源时候发现
                                                                                                                              有三种动画插入器:
                                            accelerate_decelerate_interpolator   加速-减速 动画插入器
                                            accelerate_interpolator               加速-动画插入器
                                            decelerate_interpolator               减速- 动画插入器
                                                                                                                                     其他的属于特定的动画效果
                                                                                                                              浮点数型值:
                                        fromDegrees 属性为动画起始时物件的角度    
                                        toDegrees   属性为动画结束时物件旋转的角度 可以大于360度   
                                                                                                                              说明:
                                                                                                                                      当角度为负数——表示逆时针旋转
                                                                                                                                      当角度为正数——表示顺时针旋转              
                                                 (负数from——to正数:顺时针旋转)   
                                                 (负数from——to负数:逆时针旋转) 
                                                 (正数from——to正数:顺时针旋转) 
                                                 (正数from——to负数:逆时针旋转)       
                                        pivotX     属性为动画相对于物件的X坐标的开始位置
                                        pivotY     属性为动画相对于物件的Y坐标的开始位置
                                                                                                                     说明:  以上两个属性值 从0%-100%中取值          50%为物件的X或Y方向坐标上的中点位置
                                                                                                                 长整型值：duration  属性为动画持续时间
                                                                                                                     说明:时间以毫秒为单位
                                -->
                            
                                <scale
                                    android:duration="700"
                                    android:fillAfter="false"
                                    android:fromXScale="0.0"
                                    android:fromYScale="0.0"
                                    android:interpolator="@android:anim/accelerate_decelerate_interpolator"
                                    android:pivotX="50%"
                                    android:pivotY="50%"
                                    android:toXScale="1.4"
                                    android:toYScale="1.4" />
                            
                                <!--
                                                                                                            尺寸伸缩动画效果 scale
                                                                                                             属性：interpolator 指定一个动画的插入器
                                                                                                             在我试验过程中，使用android.res.anim中的资源时候发现
                                                                                                                有三种动画插入器:
                                        accelerate_decelerate_interpolator  加速-减速 动画插入器
                                        accelerate_interpolator        加速-动画插入器
                                        decelerate_interpolator        减速- 动画插入器
                                                                                                          其他的属于特定的动画效果
                                                                                                        浮点型值：
                                        fromXScale 属性为动画起始时 X坐标上的伸缩尺寸    
                                        toXScale   属性为动画结束时 X坐标上的伸缩尺寸     
                                    
                                        fromYScale 属性为动画起始时Y坐标上的伸缩尺寸    
                                        toYScale   属性为动画结束时Y坐标上的伸缩尺寸    
                                                                                                         说明:
                                                                                                                 以上四种属性值    
                                                                                                                0.0表示收缩到没有 
                                                                                                                1.0表示正常无伸缩     
                                                                                                                   值小于1.0表示收缩  
                                                                                                                   值大于1.0表示放大
                                         pivotX     属性为动画相对于物件的X坐标的开始位置
                                         pivotY     属性为动画相对于物件的Y坐标的开始位置
                                                                                                    说明:以上两个属性值 从0%-100%中取值  50%为物件的X或Y方向坐标上的中点位置
                                                                                                      长整型值：duration  属性为动画持续时间
                                                                                                            说明:   时间以毫秒为单位
                                                                                                      布尔型值:fillAfter 属性 当设置为true ，该动画转化在动画结束后被应用
                                -->
                            
                                <translate
                                    android:duration="2000"
                                    android:fromXDelta="30"
                                    android:fromYDelta="30"
                                    android:toXDelta="-80"
                                    android:toYDelta="300" />
                                <!--
                                 translate 位置转移动画效果
                                                                                                      整型值:fromXDelta 属性为动画起始时 X坐标上的位置    toXDelta   属性为动画结束时 X坐标上的位置 fromYDelta 属性为动画起始时 Y坐标上的位置 toYDelta   属性为动画结束时 Y坐标上的位置
                                                                                                         注意:
                                                                                                                    没有指定fromXType toXType fromYType toYType 时候，
                                                                                                                    默认是以自己为相对参照物             
                                                                                                        长整型值：duration  属性为动画持续时间
                                                                                                         说明:   时间以毫秒为单位
                                -->
                     </set>

     *    @code实现
            Alpha：淡入淡出     : AlphaAnimation alphaAnimation=new AlphaAnimation(fromAlpha,toAlpha); 
                                                                       第一个参数fromAlpha为 动画开始时候透明度;
                                                                       第二个参数toAlpha为 动画结束时候透明度；
                    AlphaAnimation alphaAnimation=new AlphaAnimation(0,1); //淡入
                    AlphaAnimation alphaAnimation=new AlphaAnimation(1,0); //淡出

     *       Scale：缩放              :ScaleAnimation(float fromX, float toX, float fromY, float toY, int pivotXType, float pivotXValue, int pivotYType, float pivotYValue); 
                                                                    参数含义：
                                                                        第一个参数fromX为动画起始时 X坐标上的伸缩尺寸；
                                                                        第二个参数toX为动画结束时 X坐标上的伸缩尺寸；
                                                                        第三个参数fromY为动画起始时Y坐标上的伸缩尺寸；
                                                                        第四个参数toY为动画结束时Y坐标上的伸缩尺寸；
                                                                        说明: 以上四种属性值 ：
                         0.0表示收缩到没有 ；
                         1.0表示正常无伸缩 ；
                                                                                        值小于1.0表示收缩； 
                                                                                        值大于1.0表示放大。
                                                                        第五个参数pivotXType为动画在X轴相对于物件位置类型 ；
                                                                        第六个参数pivotXValue为动画相对于物件的X坐标的开始位置；
                                                                        第七个参数pivotXType为动画在Y轴相对于物件位置类型 ；
                                                                        第八个参数pivotYValue为动画相对于物件的Y坐标的开始位置。

     *       Rotate：旋转          :RotateAnimation(float fromDegrees, float toDegrees, int pivotXType, float pivotXValue, int pivotYType, float pivotYValue);
                                                                参数含义：
                                                                     第一个参数fromDegrees为动画起始时的旋转角度； 
                                                                    第二个参数toDegrees为动画旋转到的角度；
                                                                    第三个参数pivotXType为动画在X轴相对于物件位置类型；
                                                                    第四个参数pivotXValue为动画相对于物件的X坐标的开始位置；
                                                                    第五个参数pivotXType为动画在Y轴相对于物件位置类型；
                                                                    第六个参数pivotYValue为动画相对于物件的Y坐标的开始位置；
                                                                    参数三、四、五、六定义了旋转的圆心；
                                                                    参数三、五对应X、Y坐标的类型，有三种默认值：
                       RELATIVE_TO_PARENT  相对于父控件；                                                       
                       RELATIVE_TO_SELF  相对于自己；                                                       
                       RELATIVE_TO_ABSOLUTE  绝对坐标；
                                                                    参数四、六对应X、Y的坐标值，当其类型为RELATIVE_TO_PARENT 或RELATIVE_TO_SELF时，对应的变化范围是0f——1f。

     *       Translate：移动:TranslateAnimation(float fromXDelta, float toXDelta,float fromYDelta, float toYDelta); 
                                                                  参数含义：
                                                                        第一个参数fromXDelta为动画起始时 X坐标上的移动位置；
                                                                        第二个参数toXDelta为动画结束时 X坐标上的移动位置；
                                                                        第三个参数fromYDelta为动画起始时Y坐标上的移动位置；
                                                                        第四个参数toYDelta为动画结束时Y坐标上的移动位置；
                                                                        可以在每个参数前设置参数类型。
     *    @Animation配置数据
                                            这里说明一下TweenedAnimations的几个通用属性：
              .setDuration(long durationMills);  //动画执行的时间，单位是毫秒。
              .setFillAfter(boolean fillAfter);  //设为true，则动画完成后停留在结束状态
              .setFillBefore(boolean fillBefore); //设为true，则动画完成后停留在初始状态
              .setStartOffset(long startOffset);  //动画执行前的等待时间
              .setRepeatCount(int repeatCount);  //动画执行重复的次数
              
     *    @LayoutAnimaionController
             Tween Animation和Frame Animation的动态效果只适应一个控件，或者说多个控件同时执行一种效果。如果我们需要一个界面中的多个控件按照相同的动画方式但是每个控件完成该动画的时刻不同的话，就可采用本节讲的LayoutAnimationController来方便的完成了。
             LayoutAnimaionController为Layout或者viewGroup里的控件设置动画效果，特点是它会使其中的每个控件都有相同的动画效果，这些控件的动画效果可以在不同的时间显示出来。
               @code:
                LayoutAnimaionController lac=new LayoutAnimaionController(animation);
                                                        设置属性
                      lac.setOrder(LayoutAnimaionController .ORDER_NORMAL);//设置顺序
                                                            有三种默认顺序：
                            LayoutAnimaionController .ORDER_NORMAL       //顺序
                            LayoutAnimaionController .ORDER_REVERSE       //反序
                            LayoutAnimaionController .ORDER_RANDOM      //随机
                      lac.setDelay(***f);//设置执行动画的延迟时间，即时间间隔，单位是秒
                                                        为ListView或ViewGroup设置
                  listView.setLayoutAnimation(lac);
               @xml:   
                <?xml version="1.0" encoding="utf-8"?>
                 <layoutAnimation xmlns:android="http://schemas.android.com/apk/res/android“
                   android:delay="0.5"   //执行动画的延迟时间，即时间间隔，单位是秒                                           
                   android:animationOrder="random"/"normal"/"reverse"  //执行顺序                                          
                   android:animation="@anim/***"      //这是装载具体动画的xml文件
                   />

                                                        在具体的布局文件中配置属性
                      android:layoutAnimation="@anim/***"    
                      
     *    @AnimationListener
             AnimationListener是一个监听器，它在动画执行的各个阶段会得到通知，并且调用相应的方法
                2.方法
                (1) onAnimationStart(Animation animation)    在动画开始的时候调用
                (2) onAnimationEnd(Animation animation)     在动画结束的时候调用
                (3) onAnimationRepeate(Animation animation)    在动画重复的时候调用
       
     *   2  Frame Animations：将一系列Drawable序列一次播放，类似于电影模式，常用来进行定时更新背景等操作。
                                      在drawable文件夹下创建xml文件：
           <?xml version="1.0" encoding="utf-8"?>
               <animation-list xmlms:android="http://schemas.android.com/apk/res/android"
                      android:onshot="false">
                          <item android:drawable=""@drawable/pic1 
                              android:duration="500" />
                          <item android:drawable=""@drawable/pic2
                              android:duration="500" />
                </animation-list>
                                        这个文件就定义了需要一次播放的图片资源，每一个<item>就是一个资源，drawable属性就是资源图片，duration指的是播放的时长,android:onshot表示如果定义为true的话，此动画只会执行一次，如果为false则一直循环。
      
           
     *  3  Property Animation
                                   在Animator框架中使用最多的是AnimatorSet和ObjectAnimator配合，使用ObjectAnimator进行更精细化控制，只控制一个对象的一个属性值，
                                   多个ObjectAnimator组合到AnimatorSet形成一个动画。而且ObjectAnimator能够自动驱动，可以调用setFrameDelay(long frameDelay)
                                   设置动画帧之间的间隙时间，调整帧率，减少动画过程中频繁绘制界面，而在不影响动画效果的前提下减少CPU资源消耗。
          @ ValueAnimator anim = ObjectAnimator.ofInt(view, "backgroundColor", start, ...,end);
            ValueAnimator anim = ObjectAnimator.ofInt(view, "top", start, ...,end);
            ValueAnimator anim = ObjectAnimator.ofInt(view, "bottom", start, ...,end);
            ValueAnimator anim = ObjectAnimator.ofInt(view, "left", start,..., end);
            ValueAnimator anim = ObjectAnimator.ofInt(view, "right", start,..., end);
            ValueAnimator anim = ObjectAnimator.ofFloat(view, "x", start,...,end);
            ValueAnimator anim = ObjectAnimator.ofFloat(view, "y", start,..., end);
            ValueAnimator anim = ObjectAnimator.ofFloat(view, "width", start,..., end);
            ValueAnimator anim = ObjectAnimator.ofFloat(view, "height", start,..., end);
            ValueAnimator anim = ObjectAnimator.ofFloat(view, "alpha", start,..., end);
            ValueAnimator anim = ObjectAnimator.ofFloat(view, "scaleX", start,..., end);
            ValueAnimator anim = ObjectAnimator.ofFloat(view, "scaleY", start,..., end);
            
            ValueAnimator anim = ObjectAnimator.ofInt(view, "drawingCacheBackgroundColor", start, ...,end);
            ValueAnimator anim = ObjectAnimator.ofInt(view, "fadingEdgeLength", start, ...,end);
            ValueAnimator anim = ObjectAnimator.ofInt(view, "pivotX", start, ...,end);
            ValueAnimator anim = ObjectAnimator.ofInt(view, "pivotY", start, ...,end);

            anim.setDuration(3000);
            anim.setEvaluator (new ArgbEvaluator());
            anim.setRepeatCount(ValueAnimator.INFINITE);
            anim.setRepeatMode(ValueAnimator.REVERSE);
            anim.start();
            
          @ ValueAnimator animation = ValueAnimator.ofFloat(0f, 1f);
            animation.setDuration(1000);
            animation.addUpdateListener(new AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Log.i("update", ((Float) animation.getAnimatedValue()).toString());
                }
            });
            animation.setInterpolator(new CycleInterpolator(3));
            animation.start();
            
          @ ViewPropertyAnimator vpa = myView.animate();
            vpa.x(50f).y(100f);
          
          @ Keyframe kf0 = Keyframe.ofInt(0, 400); kf0.setInterpolator(Interpolator);
            Keyframe kf1 = Keyframe.ofInt(0.25f, 200);
            Keyframe kf2 = Keyframe.ofInt(0.5f, 400);
            Keyframe kf4 = Keyframe.ofInt(0.75f, 100);
            Keyframe kf3 = Keyframe.ofInt(1f, 500);
            PropertyValuesHolder pvh = PropertyValuesHolder.ofKeyframe("width", kf0, kf1, kf2, kf4, kf3);
            ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(view, pvh);
            anim.setDuration(2000);
            anim.start();
                                         如果是线性：ObjectAnimator oa=ObjectAnimator.ofInt(view, "width", 400,200,400,100,500);
                    oa.setDuration(2000);
                    oa.start();
            
          @ AnimatorSet set = new AnimatorSet();
            set.play(anim1).before(anim2);
            set.play(anim2).with(anim3);
            set.play(anim2).with(anim4)
            set.play(anim5).after(amin2);
            set.start();


          @ xml animator
              <set android:ordering="sequentially" >
                <set>
                    <objectAnimator
                        android:duration="500"
                        android:propertyName="x"
                        android:valueTo="400"
                        android:valueType="intType" />
                    <objectAnimator
                        android:duration="500"
                        android:propertyName="y"
                        android:valueTo="300"
                        android:valueType="intType" />
                </set>
                <objectAnimator
                    android:duration="500"
                    android:propertyName="alpha"
                    android:valueTo="1f" />
            </set>
            AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(
                myContext, R.anim.property_animator);
            set.setTarget(myObject);
            set.start();
                                   

     */

    private static AnimationManager mAnimationManager;
    private Context context;
    private static int ANIMATION_LONG_TIME = 500;
    private static int ANIMATION_SHORT_TIME = 300;
    public static final int ANIMATION_FADE_IN_OUT = 0x100;
    public static final int ANIMATION_FADE_OUT_IN = 0x101;
    public static final int ANIMATION_SLIDE_LEFT_RIGHT = 0x102;
    public static final int ANIMATION_SLIDE_RIGHT_LEFT = 0x103;

    public static final int ANIMATION_FADE_IN = 0x200;
    public static final int ANIMATION_FADE_OUT = 0x201;

    public static synchronized AnimationManager getInstance() {
        if (mAnimationManager == null) {
            mAnimationManager = new AnimationManager();
        }
        return mAnimationManager;
    }

    private AnimationManager() {}

    public void prepare(Context context) {
        this.context = context;
    }

    public static int getANIMATION_LONG_TIME() {
        return ANIMATION_LONG_TIME;
    }

    public static void setANIMATION_LONG_TIME(int aNIMATION_LONG_TIME) {
        ANIMATION_LONG_TIME = aNIMATION_LONG_TIME;
    }

    public static int getANIMATION_SHORT_TIME() {
        return ANIMATION_SHORT_TIME;
    }

    public static void setANIMATION_SHORT_TIME(int aNIMATION_SHORT_TIME) {
        ANIMATION_SHORT_TIME = aNIMATION_SHORT_TIME;
    }

    /************** View Animation *****************************************************************************************/

    /**
     * code alpha animation
     * 
     * @param fromAlpha
     * @param toAlpha
     * @return AlphaAnimation
     */
    public static AlphaAnimation getAlphaAnimation(float fromAlpha, float toAlpha) {
        AlphaAnimation anim = new AlphaAnimation(fromAlpha, toAlpha);
        anim.setDuration(ANIMATION_LONG_TIME);
        return anim;
    }

    public static AlphaAnimation getAlphaAnimation(float fromAlpha, float toAlpha, long duration) {
        AlphaAnimation anim = new AlphaAnimation(fromAlpha, toAlpha);
        anim.setDuration(duration);
        return anim;
    }

    /**
     * code scale animation
     * 
     * @param fromX
     * @param toX
     * @param fromY
     * @param toY
     * @param pivotXType
     * @param pivotXValue
     * @param pivotYType
     * @param pivotYValue
     * @return ScaleAnimation
     */
    public static ScaleAnimation getScaleAnimation(float fromX, float toX, float fromY, float toY,
            int pivotXType, float pivotXValue, int pivotYType, float pivotYValue) {
        ScaleAnimation anim = new ScaleAnimation(fromX, toX, fromY, toY, pivotXType, pivotXValue,
                pivotYType, pivotYValue);
        anim.setDuration(ANIMATION_LONG_TIME);
        return anim;
    }

    public static ScaleAnimation getScaleAnimation(float fromX, float toX, float fromY, float toY,
            int pivotXType, float pivotXValue, int pivotYType, float pivotYValue, long duration) {
        ScaleAnimation anim = new ScaleAnimation(fromX, toX, fromY, toY, pivotXType, pivotXValue,
                pivotYType, pivotYValue);
        anim.setDuration(duration);
        return anim;
    }

    /**
     * code rotate animation
     * 
     * @param fromDegrees
     * @param toDegrees
     * @param pivotXType
     * @param pivotXValue
     * @param pivotYType
     * @param pivotYValue
     * @return RotateAnimation
     */
    public static RotateAnimation getRotateAnimation(float fromDegrees, float toDegrees,
            int pivotXType, float pivotXValue, int pivotYType, float pivotYValue) {
        RotateAnimation anim = new RotateAnimation(fromDegrees, toDegrees, pivotXType, pivotXValue,
                pivotYType, pivotYValue);
        anim.setDuration(ANIMATION_LONG_TIME);
        return anim;
    }

    public static RotateAnimation getRotateAnimation(float fromDegrees, float toDegrees,
            int pivotXType, float pivotXValue, int pivotYType, float pivotYValue, long duration) {
        RotateAnimation anim = new RotateAnimation(fromDegrees, toDegrees, pivotXType, pivotXValue,
                pivotYType, pivotYValue);
        anim.setDuration(duration);
        return anim;
    }

    /**
     * code translate animation
     * 
     * @param fromXDelta
     * @param toXDelta
     * @param fromYDelta
     * @param toYDelta
     * @return TranslateAnimation
     */
    public static TranslateAnimation getTranslateAnimation(float fromXDelta, float toXDelta,
            float fromYDelta, float toYDelta) {
        TranslateAnimation anim = new TranslateAnimation(fromXDelta, toXDelta, fromYDelta, toYDelta);
        anim.setDuration(ANIMATION_LONG_TIME);
        return anim;
    }

    public static TranslateAnimation getTranslateAnimation(int fromXType, float fromXValue,
            int toXType, float toXValue, int fromYType, float fromYValue, int toYType,
            float toYValue) {
        TranslateAnimation anim = new TranslateAnimation(fromXType, fromXValue, toXType, toXValue,
                fromYType, fromYValue, toYType, toYValue);
        anim.setDuration(ANIMATION_LONG_TIME);
        return anim;
    }

    public static TranslateAnimation getTranslateAnimation(float fromXDelta, float toXDelta,
            float fromYDelta, float toYDelta, long duration) {
        TranslateAnimation anim = new TranslateAnimation(fromXDelta, toXDelta, fromYDelta, toYDelta);
        anim.setDuration(duration);
        return anim;
    }

    /**
     * xml animation
     * 
     * @param context
     * @param rsid
     * @return Animation
     */
    public static Animation getAnimation(Context context, int rsid) {
        return AnimationUtils.loadAnimation(context, rsid);
    }

    public Animation getAnimationByRsid(int rsid) {
        return AnimationUtils.loadAnimation(context, rsid);
    }

    /**
     * animationset
     * 
     * @param share
     * @param interpolator
     * @return AnimationSet
     */
    public static AnimationSet getAnimationSet(boolean share, Interpolator interpolator) {
        AnimationSet animationSet = null;
        if (share) {
            animationSet = new AnimationSet(true);
            animationSet.setInterpolator(interpolator);
        } else {
            animationSet = new AnimationSet(false);
        }
        return animationSet;

    }

    /**
     * layout item animation
     * 
     * @param viewGroup
     * @param animation
     * @param order
     * @param delay void
     */
    public static LayoutAnimationController setLayoutAnimation(ViewGroup viewGroup,
            Animation animation, int order, long delay, Interpolator interpolator) {
        LayoutAnimationController lac = new LayoutAnimationController(animation);
        lac.setOrder(order);
        lac.setDelay(delay);
        lac.setInterpolator(interpolator);
        viewGroup.setLayoutAnimation(lac);
        return lac;
    }

    public static LayoutAnimationController getLayoutAnimation(Animation animation, int order,
            long delay, Interpolator interpolator) {
        LayoutAnimationController lac = new LayoutAnimationController(animation);
        lac.setOrder(order);
        lac.setDelay(delay);
        lac.setInterpolator(interpolator);
        return lac;
    }

    public static LayoutAnimationController setLayoutAnimation(ViewGroup viewGroup,
            Animation animation) {
        LayoutAnimationController lac = new LayoutAnimationController(animation);
        viewGroup.setLayoutAnimation(lac);
        return lac;
    }

    public static LayoutAnimationController getLayoutAnimation(Animation animation) {
        LayoutAnimationController lac = new LayoutAnimationController(animation);
        return lac;
    }

    /************** Frame Animation *****************************************************************************************/

    /**
     * frame animation
     * 
     * @param view
     * @param rsid void
     */
    public static void startAnimationDrawable(View view, int rsid) {
        view.setBackgroundResource(rsid);
        AnimationDrawable animationDrawable = (AnimationDrawable) view.getBackground();
        if (animationDrawable.isRunning()) {
            animationDrawable.stop();
        }
        animationDrawable.start();
    }

    public static void stopAnimationDrawable(View view, int rsid) {
        view.setBackgroundResource(rsid);
        AnimationDrawable animationDrawable = (AnimationDrawable) view.getBackground();
        animationDrawable.stop();
    }

    public static AnimationDrawable getAnimationDrawable(View view, int rsid) {
        view.setBackgroundResource(rsid);
        return (AnimationDrawable) view.getBackground();
    }

    /************** Property Animation *****************************************************************************************/

    // ValueAnimator,ObjectAnimator,AnimatorUpdateListener,Keyframe,PropertyValuesHolder,AnimatorSet
    public static ValueAnimator getAnimator(AnimatorUpdateListener listener) {
        ValueAnimator animation = ValueAnimator.ofFloat(0f, 1f);
        animation.setDuration(ANIMATION_LONG_TIME);
        animation.addUpdateListener(listener);
        return animation;
    }

    public static ValueAnimator getAnimator(AnimatorUpdateListener listener, long duration) {
        ValueAnimator animation = ValueAnimator.ofFloat(0f, 1f);
        animation.setDuration(duration);
        animation.addUpdateListener(listener);
        return animation;
    }

    public static void startAnimator(AnimatorUpdateListener listener) {
        ValueAnimator animation = ValueAnimator.ofFloat(0f, 1f);
        animation.setDuration(ANIMATION_LONG_TIME);
        animation.addUpdateListener(listener);
        animation.start();
    }

    public static void startAnimator(AnimatorUpdateListener listener, long duration) {
        ValueAnimator animation = ValueAnimator.ofFloat(0f, 1f);
        animation.setDuration(duration);
        animation.addUpdateListener(listener);
        animation.start();
    }

    public static void startAnimator(AnimatorUpdateListener listener,
            Interpolator interpolator) {
        ValueAnimator animation = ValueAnimator.ofFloat(0f, 1f);
        animation.setDuration(ANIMATION_LONG_TIME);
        animation.addUpdateListener(listener);
        animation.setInterpolator(interpolator);
        animation.start();
    }

    public static void startAnimator(AnimatorUpdateListener listener, long duration,
            Interpolator interpolator) {
        ValueAnimator animation = ValueAnimator.ofFloat(0f, 1f);
        animation.setDuration(duration);
        animation.addUpdateListener(listener);
        animation.setInterpolator(interpolator);
        animation.start();
    }

    public static ValueAnimator getAnimator(Object target, String propertyName,
            Keyframe... kfs) {
        PropertyValuesHolder pvh = PropertyValuesHolder.ofKeyframe(propertyName, kfs);
        ValueAnimator anim = ObjectAnimator.ofPropertyValuesHolder(target, pvh);
        anim.setDuration(ANIMATION_LONG_TIME);
        return anim;
    }

    public static ValueAnimator getAnimator(Object target, String propertyName,
            long duration, Keyframe... kfs) {
        PropertyValuesHolder pvh = PropertyValuesHolder.ofKeyframe(propertyName, kfs);
        ValueAnimator anim = ObjectAnimator.ofPropertyValuesHolder(target, pvh);
        anim.setDuration(duration);
        return anim;
    }

    public static void startAnimator(Object target, String propertyName, Keyframe... kfs) {
        PropertyValuesHolder pvh = PropertyValuesHolder.ofKeyframe(propertyName, kfs);
        ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(target, pvh);
        anim.setDuration(ANIMATION_LONG_TIME);
        anim.start();
    }

    public static void startAnimator(Object target, String propertyName, long duration,
            Keyframe... kfs) {
        PropertyValuesHolder pvh = PropertyValuesHolder.ofKeyframe(propertyName, kfs);
        ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(target, pvh);
        anim.setDuration(duration);
        anim.start();
    }

    public static ValueAnimator getAnimator(Object target, String propertyName,
            float... values) {
        ValueAnimator oa = ObjectAnimator.ofFloat(target, propertyName, values);
        oa.setDuration(ANIMATION_LONG_TIME);
        return oa;
    }

    public static ValueAnimator getAnimator(Object target, String propertyName,
            long duration, float... values) {
        ValueAnimator oa = ObjectAnimator.ofFloat(target, propertyName, values);
        oa.setDuration(duration);
        return oa;
    }

    public static void startAnimator(Object target, String propertyName, long duration,
            float... values) {
        ValueAnimator oa = ObjectAnimator.ofFloat(target, propertyName, values);
        oa.setDuration(duration);
        oa.start();
    }

    public static void startAnimator(Object target, String propertyName, float... values) {
        ValueAnimator oa = ObjectAnimator.ofFloat(target, propertyName, values);
        oa.setDuration(ANIMATION_LONG_TIME);
        oa.start();
    }

    public static void startAnimator(Object target, String propertyName, long duration,
            Interpolator interpolator, float... values) {
        ValueAnimator oa = ObjectAnimator.ofFloat(target, propertyName, values);
        oa.setDuration(duration);
        oa.setInterpolator(interpolator);
        oa.start();
    }

    /************** Normal Animation *****************************************************************************************/

    public static void startWindowAnimation(Activity activity, int in, int out) {
        activity.overridePendingTransition(in, out);
    }

    public static void startWindowAnimation(Activity activity, int animationType) {
        switch (animationType) {
        case ANIMATION_FADE_IN_OUT:
            activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            break;
        case ANIMATION_FADE_OUT_IN:
            activity.overridePendingTransition(android.R.anim.fade_out, android.R.anim.fade_in);
            break;
        case ANIMATION_SLIDE_LEFT_RIGHT:
            activity.overridePendingTransition(android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right);
            break;
        case ANIMATION_SLIDE_RIGHT_LEFT:
            activity.overridePendingTransition(android.R.anim.slide_out_right,
                    android.R.anim.slide_in_left);
            break;
        default:
            break;
        }
    }

    public static Animation getAnimation(int animationType) {
        Animation anim = null;
        switch (animationType) {
        case ANIMATION_FADE_IN:
            anim = getAlphaAnimation(0f, 1f);
            break;
        case ANIMATION_FADE_OUT:
            anim = getAlphaAnimation(1f, 0f);
            break;
        default:
            break;
        }
        return anim;
    }

    public static ValueAnimator getAnimator(Object target, int animationType) {
        ValueAnimator anim = null;
        switch (animationType) {
        case ANIMATION_FADE_IN:
            anim = getAnimator(target, "alpha", 0f, 1f);
            break;
        case ANIMATION_FADE_OUT:
            anim = getAnimator(target, "alpha", 1f, 0f);
            break;
        default:
            break;
        }
        return anim;
    }

    public static void startAnimator(Object target, int animationType) {
        getAnimator(target, animationType).start();
    }

    /************** Other *****************************************************************************************/

}
