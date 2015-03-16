package com.brutus.andbrutus.view;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageButton;

import com.brutus.andbrutus.R;

public class FloatingActionButton extends ImageButton {

	public static final int SIZE_NORMAL = 0;
	public static final int SIZE_MINI = 1;

	private static final int HALF_TRANSPARENT_WHITE = Color.argb(128, 255, 255, 255);
	private static final int HALF_TRANSPARENT_BLACK = Color.argb(128, 0, 0, 0);

	int mColorNormal;
	int mColorPressed;
	@DrawableRes
	private int mIcon;
	private int mSize;

	private float mCircleSize;
	private float mShadowRadius;
	private float mShadowOffset;
	private int mDrawableSize;

	//circleButton
	private static final int PRESSED_RING_ALPHA = 75;
	private static final int DEFAULT_PRESSED_RING_WIDTH_DIP = 4;
	private static final int ANIMATION_TIME_ID = android.R.integer.config_shortAnimTime;
	private int centerY;
	private int centerX;
	private int outerRadius;
	private int pressedRingRadius;
	private Paint focusPaint;
	private float animationProgress;
	private int pressedRingWidth;
	private int defaultColor = Color.TRANSPARENT;
	private ObjectAnimator pressedAnimator;
	private boolean showBorder = false;
	
	public FloatingActionButton(Context context) {
		this(context, null);
	}

	public FloatingActionButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public FloatingActionButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	//Circle Button

	public void setIcon(int res){
		this.mIcon = res;
		updateBackground();
	}

	public int getIcon(){
		return mIcon;
	}
	
	public void setShowBorder(boolean showBorder){
		this.showBorder = showBorder;
	}
	
	@Override
	public void setPressed(boolean pressed) {
		super.setPressed(pressed);
		if (focusPaint != null) {
			setColor(pressed ? adjustAlpha(mColorNormal, 0.9f) : adjustAlpha(mColorNormal, 0.000f));
		}
		if (pressed) {
			showPressedRing();
		} else {
			hidePressedRing();
		}
	}



	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawCircle(centerX, centerY, pressedRingRadius + animationProgress, focusPaint);
		super.onDraw(canvas);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		centerX = w / 2;
		centerY = h / 2;
		outerRadius = Math.min(w, h) / 2;
		pressedRingRadius = outerRadius - pressedRingWidth - pressedRingWidth / 2;
	}

	public float getAnimationProgress() {
		return animationProgress;
	}

	public void setAnimationProgress(float animationProgress) {
		this.animationProgress = animationProgress;
		this.invalidate();
	}

	public void setColorNormal(int color){
		mColorNormal = color;
		mColorPressed = color;
		updateBackground();
	}
	
	public void setColor(int color) {
		this.defaultColor = color;
		if(!showBorder)
		focusPaint.setColor(defaultColor);//comment for border color
		focusPaint.setAlpha(PRESSED_RING_ALPHA);
		this.invalidate();
	}

	private void hidePressedRing() {
		pressedAnimator.setFloatValues(pressedRingWidth, 0f);
		pressedAnimator.start();
	}

	private void showPressedRing() {
		pressedAnimator.setFloatValues(animationProgress, pressedRingWidth);
		pressedAnimator.start();
	}

	//end Circle Button




	void init(Context context, AttributeSet attributeSet) {
		mColorNormal = getColor(android.R.color.holo_blue_dark);
		mColorPressed = getColor(android.R.color.holo_blue_light);
		mIcon = 0;
		mSize = SIZE_NORMAL;
		if (attributeSet != null) {
			initAttributes(context, attributeSet);
		}

		mCircleSize = getDimension(mSize == SIZE_NORMAL ? R.dimen.fab_size_normal : R.dimen.fab_size_mini);
		mShadowRadius = getDimension(R.dimen.fab_shadow_radius);
		mShadowOffset = getDimension(R.dimen.fab_shadow_offset);
		mDrawableSize = (int) (mCircleSize + 2 * mShadowRadius);
		//circleButton
		this.setFocusable(true);
		//		this.setScaleType(ScaleType.CENTER_INSIDE);
		setClickable(true);
		focusPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		focusPaint.setStyle(Paint.Style.STROKE);
		pressedRingWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_PRESSED_RING_WIDTH_DIP, getResources().getDisplayMetrics());
		//		focusPaint.setColor(adjustAlpha(0xFFFFFFFF, 0.2f));
		focusPaint.setStrokeWidth(pressedRingWidth);
		final int pressedAnimationTime = getResources().getInteger(ANIMATION_TIME_ID);
		pressedAnimator = ObjectAnimator.ofFloat(this, "animationProgress", 0f, 0f);
		pressedAnimator.setDuration(pressedAnimationTime);
		//		setColor(mColorNormal);
		//end circleButton
		updateBackground();
	}
	public int adjustAlpha(int color, float factor) {
		int alpha = Math.round(Color.alpha(color) * factor);
		int red = Color.red(color);
		int green = Color.green(color);
		int blue = Color.blue(color);
		return Color.argb(alpha, red, green, blue);
	}
	int getColor(@ColorRes int id) {
		return getResources().getColor(id);
	}

	float getDimension(@DimenRes int id) {
		return getResources().getDimension(id);
	}

	private void initAttributes(Context context, AttributeSet attributeSet) {
		TypedArray attr = context.obtainStyledAttributes(attributeSet, R.styleable.FloatingActionButton, 0, 0);
		if (attr != null) { 
			try {
				mColorNormal = attr.getColor(R.styleable.FloatingActionButton_fab_colorNormal, getColor(android.R.color.holo_blue_dark));
				mColorPressed = attr.getColor(R.styleable.FloatingActionButton_fab_colorPressed, getColor(android.R.color.holo_blue_light));
				mSize = attr.getInt(R.styleable.FloatingActionButton_fab_size, SIZE_NORMAL);
				mIcon = attr.getResourceId(R.styleable.FloatingActionButton_fab_icon, 0);
				//circle
				pressedRingWidth = (int) attr.getDimension(R.styleable.FloatingActionButton_fab_pressedRingWidth, pressedRingWidth);
			} finally {
				attr.recycle();
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(mDrawableSize, mDrawableSize);
	}

	void updateBackground() {
		float circleLeft = mShadowRadius;
		float circleTop = mShadowRadius - mShadowOffset;
		final RectF circleRect = new RectF(circleLeft, circleTop, circleLeft + mCircleSize, circleTop + mCircleSize);
		LayerDrawable layerDrawable = new LayerDrawable(
				new Drawable[] {
//						getResources().getDrawable(mSize == SIZE_NORMAL ? R.drawable.fab_bg_normal : R.drawable.fab_bg_mini),
						getResources().getDrawable(mSize == SIZE_NORMAL ? R.drawable.icon0 : R.drawable.icon0),
						createFillDrawable(circleRect),
						createStrokesDrawable(circleRect),
						getIconDrawable()
				});

		float iconOffset = (mCircleSize - getDimension(R.dimen.fab_icon_size)) / 2f;

		int iconInsetHorizontal = (int) (mShadowRadius + iconOffset);
		int iconInsetTop = (int) (circleTop + iconOffset);
		int iconInsetBottom = (int) (mShadowRadius + mShadowOffset + iconOffset);
		setColor(adjustAlpha(mColorNormal, 1f));
		layerDrawable.setLayerInset(3, iconInsetHorizontal, iconInsetTop, iconInsetHorizontal, iconInsetBottom);

		setBackgroundCompat(layerDrawable);
	}

	Drawable getIconDrawable() {
		if (mIcon != 0) {
			return getResources().getDrawable(mIcon);
		} else {
			return new ColorDrawable(Color.TRANSPARENT);
		}
	}

	private StateListDrawable createFillDrawable(RectF circleRect) {
		StateListDrawable drawable = new StateListDrawable();
		drawable.addState(new int[] { android.R.attr.state_pressed }, createCircleDrawable(circleRect, mColorPressed));
		drawable.addState(new int[] { }, createCircleDrawable(circleRect, mColorNormal));
		return drawable;
	}

	private Drawable createCircleDrawable(RectF circleRect, int color) {
		final Bitmap bitmap = Bitmap.createBitmap(mDrawableSize, mDrawableSize, Config.ARGB_8888);
		final Canvas canvas = new Canvas(bitmap);
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(color);
		canvas.drawOval(circleRect, paint);
		return new BitmapDrawable(getResources(), bitmap);
	}

	private int opacityToAlpha(float opacity) {
		return (int) (255f * opacity);
	}

	private Drawable createStrokesDrawable(RectF circleRect) {
		final Bitmap bitmap = Bitmap.createBitmap(mDrawableSize, mDrawableSize, Config.ARGB_8888);
		final Canvas canvas = new Canvas(bitmap);
		final float strokeWidth = getDimension(R.dimen.fab_stroke_width);
		final float halfStrokeWidth = strokeWidth / 2f;
		RectF outerStrokeRect = new RectF(
				circleRect.left - halfStrokeWidth,
				circleRect.top - halfStrokeWidth,
				circleRect.right + halfStrokeWidth,
				circleRect.bottom + halfStrokeWidth
				);
		RectF innerStrokeRect = new RectF(
				circleRect.left + halfStrokeWidth,
				circleRect.top + halfStrokeWidth,
				circleRect.right - halfStrokeWidth,
				circleRect.bottom - halfStrokeWidth
				);
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStrokeWidth(strokeWidth);
		paint.setStyle(Style.STROKE);
		// outer
		paint.setColor(Color.BLACK);
		paint.setAlpha(opacityToAlpha(0.02f));
		canvas.drawOval(outerStrokeRect, paint);
		// inner bottom
		paint.setShader(new LinearGradient(innerStrokeRect.centerX(), innerStrokeRect.top, innerStrokeRect.centerX(), innerStrokeRect.bottom,
				new int[] { Color.TRANSPARENT, HALF_TRANSPARENT_BLACK, Color.BLACK },
				new float[] { 0f, 0.8f, 1f },
				TileMode.CLAMP
				));
		paint.setAlpha(opacityToAlpha(0.04f));
		canvas.drawOval(innerStrokeRect, paint);
		// inner top
		paint.setShader(new LinearGradient(innerStrokeRect.centerX(), innerStrokeRect.top, innerStrokeRect.centerX(), innerStrokeRect.bottom,
				new int[] { Color.WHITE, HALF_TRANSPARENT_WHITE, Color.TRANSPARENT },
				new float[] { 0f, 0.2f, 1f },
				TileMode.CLAMP
				));
		paint.setAlpha(opacityToAlpha(0.8f));
		canvas.drawOval(innerStrokeRect, paint);
		return new BitmapDrawable(getResources(), bitmap);
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private void setBackgroundCompat(Drawable drawable) {
		if (Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
			setBackground(drawable);
		} else {
			setBackgroundDrawable(drawable);
		}
	}
}
