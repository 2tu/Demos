package com.tu.widget.stepview;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.CallSuper;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.tu.stepview.R;

/**
 * @author Tu enum@foxmail.com
 * @version 1.0 2019/6/3
 */
public class StepIndicatorView extends LinearLayout {

  String[] mStepNames = {};

  int[] mStepIcons = {
      R.drawable.ic_step_10, R.drawable.ic_step_10, R.drawable.ic_step_10,
      R.drawable.ic_step_10, R.drawable.ic_step_10
  };

  TextView[] mStepIconView;

  TextView[] mStepNameView;

  View[] mStepLeftDivider;

  View[] mStepRightDivider;

  int mCurrentStep = 0;

  int mSelectStep = 0;

  int textSize;

  public StepIndicatorView(Context context) {
    super(context);
    init(context, null);
  }

  public StepIndicatorView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  public StepIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }

  private void init(Context context, AttributeSet attrs) {
    if (attrs != null) {
      TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StepIndicatorView);
      CharSequence[] names = typedArray.getTextArray(R.styleable.StepIndicatorView_stepsName);
      textSize = typedArray.getDimensionPixelSize(R.styleable.StepIndicatorView_stepTextSize, 15);
      if (names != null) {
        mStepNames = new String[names.length];
        for (int i = 0, size = names.length; i < size; i++) {
          mStepNames[i] = names[i].toString();
        }
      }
      typedArray.recycle();
    }
    reCreateContentView();
  }

  void reCreateContentView() {
    int itemCounts = mStepNames.length;
    mStepNameView = new TextView[itemCounts];
    mStepLeftDivider = new View[itemCounts];
    mStepRightDivider = new View[itemCounts];
    mStepIconView = new TextView[itemCounts];

    removeAllViews();

    for (int i = 0; i < itemCounts; i++) {
      View itemView =
          LayoutInflater.from(getContext()).inflate(R.layout.apply_indicator_item, this, false);

      View leftDividerView = itemView.findViewById(R.id.material_step_left_divider);
      View rightDividerView = itemView.findViewById(R.id.material_step_right_divider);
      TextView stepIconView =
          (TextView) itemView.findViewById(R.id.material_step_index);
      TextView stepName = (TextView) itemView.findViewById(R.id.material_step_name);

      if (textSize > 0) {
        stepName.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
      }

      stepName.setText(mStepNames[i]);

      stepIconView.setBackgroundResource(mStepIcons[i]);
      stepIconView.setText(String.valueOf(i + 1));
      mStepNameView[i] = stepName;
      mStepIconView[i] = stepIconView;
      mStepLeftDivider[i] = leftDividerView;
      mStepRightDivider[i] = rightDividerView;
      addView(itemView);
    }

    refresh();
  }

  /**
   * 设置步骤名称和数量。默认ICON支持最多支持5步。超过5步可使用{@link #setSteps(int[], String[])}
   */
  public void setSteps(String... stepNames) {
    this.mStepNames = stepNames;
    reCreateContentView();
  }

  /**
   * 设置步骤名称和ICON
   */
  public void setSteps(@DrawableRes int[] setpIcons, String[] stepNames) {
    this.mStepIcons = setpIcons;
    this.mStepNames = stepNames;
    reCreateContentView();
  }

  /**
   * 激活步骤 step
   *
   * @param step step index
   */
  public void activateStep(int step) {
    mCurrentStep = step;
    mSelectStep = step;
    refresh();
  }

  public void selectStep(int step) {
    mCurrentStep = step;
    mSelectStep = step;
    //if (mSelectStep > mCurrentStep) {
    //  mCurrentStep = mSelectStep;
    //}
    refresh();
  }

  private void refresh() {
    for (int i = 0, size = mStepNames.length; i < size; i++) {
      mStepLeftDivider[i].setSelected(mCurrentStep >= i);
      mStepRightDivider[i].setSelected(mCurrentStep >= i);

      //mStepIconView[i].setSelected(mSelectStep == i);
      mStepIconView[i].setSelected(mCurrentStep >= i);

      mStepNameView[i].setSelected(mCurrentStep >= i);
    }
  }

  @CallSuper
  @Override protected Parcelable onSaveInstanceState() {
    super.onSaveInstanceState();
    Parcelable superState = super.onSaveInstanceState();
    SavedState savedState =
        new SavedState(superState, mCurrentStep, mSelectStep, mStepNames,
            mStepIcons);
    return savedState;
  }

  @CallSuper
  @Override protected void onRestoreInstanceState(Parcelable state) {
    SavedState ss = (SavedState) state;
    super.onRestoreInstanceState(ss.getSuperState());
    mCurrentStep = ss.currentStep;
    mSelectStep = ss.selectedStep;
    mStepNames = ss.stepNames;
    mStepIcons = ss.stepIcons;

    reCreateContentView();
  }

  public static class SavedState extends BaseSavedState {
    int currentStep, selectedStep;
    String[] stepNames;
    int[] stepIcons;

    public SavedState(Parcelable source, int currentStep, int selectedStep, String[] stepNames,
        int[] stepIcons) {
      super(source);
      this.currentStep = currentStep;
      this.selectedStep = selectedStep;
      this.stepIcons = stepIcons;
      this.stepNames = stepNames;
    }

    private SavedState(Parcel in) {
      super(in);
      currentStep = in.readInt();
      selectedStep = in.readInt();
      stepIcons = in.createIntArray();
      stepNames = in.createStringArray();
    }

    @Override public void writeToParcel(Parcel out, int flags) {
      super.writeToParcel(out, flags);
      out.writeInt(currentStep);
      out.writeInt(selectedStep);
      out.writeStringArray(stepNames);
      out.writeIntArray(stepIcons);
    }

    public static final Creator<StepIndicatorView.SavedState> CREATOR =
        new Creator<StepIndicatorView.SavedState>() {

          @Override
          public StepIndicatorView.SavedState createFromParcel(Parcel in) {
            return new StepIndicatorView.SavedState(in);
          }

          @Override
          public StepIndicatorView.SavedState[] newArray(int size) {
            return new StepIndicatorView.SavedState[size];
          }
        };
  }
}
