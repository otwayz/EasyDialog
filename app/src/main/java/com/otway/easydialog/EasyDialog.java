package com.otway.easydialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;

/**
 * Created by Otway on 2018/2/1.
 */

public class EasyDialog implements DialogInterface.OnDismissListener {
	private final String TAG = getClass().getSimpleName();

	public static final int MATCH_PARENT = -1;
	public static final int WRAP_CONTENT = -2;
	private final Builder mBuilder;

	@IntDef({MATCH_PARENT, WRAP_CONTENT})
	public @interface LayoutParams {
	}

	private static SparseArray<View.OnClickListener> clickListenerMap = new SparseArray<>();
	private static SparseArray<View.OnLongClickListener> longClickListenerMap = new SparseArray<>();
	private static SparseArray<CompoundButton.OnCheckedChangeListener> checkedChangeListenerMap = new SparseArray<>();

	private final AlertDialog mAlertDialog;
	private final View mContentView;

	private EasyDialog(@NonNull Builder builder) {
		mBuilder = builder;
		mContentView = builder.contentView;
		mAlertDialog = new AlertDialog.Builder(builder.context, builder.themeResId).create();
//		mAlertDialog.setView(builder.contentRes);
		mAlertDialog.setOnDismissListener(this);
		mAlertDialog.setCancelable(builder.cancelable);
		mAlertDialog.setCanceledOnTouchOutside(builder.cancelOutside);

		initEvent(builder.contentView);
	}

	private void initParams(@NonNull Builder builder) {
		Window window = mAlertDialog.getWindow();
		if (window != null) {
			window.setBackgroundDrawable(new ColorDrawable(builder.bgColor));
			WindowManager.LayoutParams params = window.getAttributes();
			if ((builder.viewWidth > 0 && builder.viewWidth < 1) ||
					(builder.viewHeight > 0 && builder.viewHeight < 1)) {
				DisplayMetrics displayMetrics = builder.context.getResources().getDisplayMetrics();
				int widthPixels = displayMetrics.widthPixels;
				int heightPixels = displayMetrics.heightPixels;
				if (builder.viewWidth > 0 && builder.viewWidth < 1) {
					params.width = (int) (widthPixels * builder.viewWidth);
				} else {
					params.width = (int) builder.viewWidth;
				}
				if (builder.viewHeight > 0 && builder.viewHeight < 1) {
					params.height = (int) (heightPixels * builder.viewHeight);
				} else {
					params.height = (int) builder.viewHeight;
				}
			} else {
				params.width = (int) builder.viewWidth;
				params.height = (int) builder.viewHeight;
			}
			params.gravity = builder.viewGravity;
			window.setAttributes(params);
		}
	}


	public void show() {
		mAlertDialog.show();
		mAlertDialog.setContentView(mBuilder.contentView);
		initParams(mBuilder);
	}

	public boolean isShowing() {
		return mAlertDialog.isShowing();
	}

	public void dismiss() {
		mAlertDialog.dismiss();
	}

	public final <T extends View> T getViewById(@IdRes int viewId) {
		return mContentView.findViewById(viewId);
	}

	private void initEvent(@NonNull View contentView) {
		int clickSize = clickListenerMap.size();
		for (int i = 0; i < clickSize; i++) {
			int key = clickListenerMap.keyAt(i);
			View.OnClickListener onClickListener = clickListenerMap.get(key);
			View viewById = contentView.findViewById(key);
			if (viewById != null) {
				viewById.setOnClickListener(onClickListener);
			}
		}

		int longSize = longClickListenerMap.size();
		for (int i = 0; i < longSize; i++) {
			int key = longClickListenerMap.keyAt(i);
			View.OnLongClickListener onLongClickListener = longClickListenerMap.get(key);
			View viewById = contentView.findViewById(key);
			if (viewById != null) {
				viewById.setOnLongClickListener(onLongClickListener);
			}
		}

		int checkSize = checkedChangeListenerMap.size();
		for (int i = 0; i < checkSize; i++) {
			int key = checkedChangeListenerMap.keyAt(i);
			CompoundButton.OnCheckedChangeListener onCheckedChangeListener = checkedChangeListenerMap.get(key);
			View viewById = contentView.findViewById(key);
			if (viewById != null && viewById instanceof CompoundButton) {
				((CompoundButton) viewById).setOnCheckedChangeListener(onCheckedChangeListener);
			}
		}

		clickListenerMap.clear();
		longClickListenerMap.clear();
		checkedChangeListenerMap.clear();
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		Log.d(TAG, "onDismiss: --------dialog:" + dialog);
	}

	public static class Builder {
		private View contentView;
		private int bgColor;
		private int themeResId;
		private final Context context;
		private float viewWidth;
		private float viewHeight;
		private int viewGravity;
		private boolean cancelable;
		private boolean cancelOutside;

		public Builder(@NonNull Context ct) {
			context = ct;
			viewHeight = WRAP_CONTENT;
			viewWidth = WRAP_CONTENT;
			bgColor = Color.TRANSPARENT;
			viewGravity = Gravity.CENTER;
			cancelable = true;
			cancelOutside = true;
		}

		/**
		 * support the ratio to screen  <= 1  valid
		 *
		 * @param width {@link #WRAP_CONTENT} and {@link #MATCH_PARENT}
		 * @return
		 */
		public Builder setWidth(float width) {
			viewWidth = width == 1 ? MATCH_PARENT : width;
			return this;
		}

		public Builder setWidth(int width) {
			viewWidth = width;
			return this;
		}

		/**
		 * support the ratio to screen  <= 1 valid
		 *
		 * @param height {@link #WRAP_CONTENT} and {@link #MATCH_PARENT}
		 * @return
		 */
		public Builder setHeight(float height) {
			viewHeight = height == 1 ? MATCH_PARENT : height;
			return this;
		}

		public Builder setHeight(int height) {
			viewHeight = height;
			return this;
		}

		/**
		 * @param gravity {@link Gravity#CENTER } and so on
		 * @return
		 */
		public Builder setGravity(int gravity) {
			viewGravity = gravity;
			return this;
		}

		public Builder setContentViewId(@LayoutRes int resId) {
			contentView = LayoutInflater.from(context).inflate(resId, null);
			return this;
		}

		public Builder setContentView(View view) {
			contentView = view;
			return this;
		}

		public Builder setBackgroundColor(@ColorInt int color) {
			bgColor = color;
			return this;
		}

		public Builder setThemeRes(@StyleRes int resId) {
			themeResId = resId;
			return this;
		}

		public Builder setCancelable(boolean able) {
			cancelable = able;
			return this;
		}

		public Builder setCancelOutside(boolean cancelOutside) {
			this.cancelOutside = cancelOutside;
			return this;
		}

		public Builder setOnClickListener(@IdRes int viewId, View.OnClickListener listener) {
			clickListenerMap.put(viewId, listener);
			return this;
		}

		public Builder setOnCheckedChangeListener(@IdRes int viewId, CompoundButton.OnCheckedChangeListener listener) {
			checkedChangeListenerMap.put(viewId, listener);
			return this;
		}

		public Builder setOnLongClickListener(@IdRes int viewId, View.OnLongClickListener listener) {
			longClickListenerMap.put(viewId, listener);
			return this;
		}

		public EasyDialog create() {
			return new EasyDialog(this);
		}

		public EasyDialog show() {
			EasyDialog centerWrapDialog = create();
			centerWrapDialog.show();
			return centerWrapDialog;
		}
	}
}
