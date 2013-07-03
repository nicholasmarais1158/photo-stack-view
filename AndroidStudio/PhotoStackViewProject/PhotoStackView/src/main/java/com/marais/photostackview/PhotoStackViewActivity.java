package com.marais.photostackview;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;

public class PhotoStackViewActivity extends Activity {

    private PhotoStackView mPhotoStackView;
    private SeekBar mHeightSeek;
    private SeekBar mWidthSeek;
    private SeekBar mSpinSeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_photo_stack_view);

        mPhotoStackView = (PhotoStackView) findViewById(R.id.activity_imagethumbview_image);
        ViewGroup.LayoutParams lp = mPhotoStackView.getLayoutParams();

        mHeightSeek = (SeekBar) findViewById(R.id.activity_imagethumbview_height_size);
        mWidthSeek = (SeekBar) findViewById(R.id.activity_imagethumbview_width_size);
        RadioGroup heightRadio = (RadioGroup) findViewById(R.id.activity_imagethumbview_height_mode);
        RadioGroup widthRadio = (RadioGroup) findViewById(R.id.activity_imagethumbview_width_mode);
        mSpinSeek = (SeekBar) findViewById(R.id.activity_imagethumbview_spin);

        mHeightSeek.setProgress(mPhotoStackView.getLayoutParams().height);
        mSpinSeek.setProgress(mPhotoStackView.getSpin());

        switch (lp.width) {
            case ViewGroup.LayoutParams.MATCH_PARENT:
                widthRadio.check(R.id.activity_imagethumbview_width_mode_match);
                mWidthSeek.setEnabled(false);
                break;
            case ViewGroup.LayoutParams.WRAP_CONTENT:
                widthRadio.check(R.id.activity_imagethumbview_width_mode_wrap);
                mWidthSeek.setEnabled(false);
                break;
            default:
                widthRadio.check(R.id.activity_imagethumbview_width_mode_custom);
                mWidthSeek.setEnabled(true);
                break;
        }
        mWidthSeek.setProgress(mPhotoStackView.getMeasuredWidth());

        switch (lp.height) {
            case ViewGroup.LayoutParams.MATCH_PARENT:
                heightRadio.check(R.id.activity_imagethumbview_height_mode_match);
                mHeightSeek.setEnabled(false);
                break;
            case ViewGroup.LayoutParams.WRAP_CONTENT:
                heightRadio.check(R.id.activity_imagethumbview_height_mode_wrap);
                mHeightSeek.setEnabled(false);
                break;
            default:
                heightRadio.check(R.id.activity_imagethumbview_height_mode_custom);
                mHeightSeek.setEnabled(true);
                break;
        }
        mHeightSeek.setProgress(mPhotoStackView.getMeasuredHeight());

        widthRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                ViewGroup.LayoutParams lp = mPhotoStackView.getLayoutParams();

                switch (checkedId) {
                    case R.id.activity_imagethumbview_width_mode_wrap:
                        //mPhotoStackView.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                        lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                        mWidthSeek.setProgress(mPhotoStackView.getMeasuredWidth());
                        mWidthSeek.setEnabled(false);
                        break;
                    case R.id.activity_imagethumbview_width_mode_match:
                        //mPhotoStackView.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
                        mWidthSeek.setProgress(mPhotoStackView.getMeasuredWidth());
                        mWidthSeek.setEnabled(false);
                        break;
                    case R.id.activity_imagethumbview_width_mode_custom:
                        //mPhotoStackView.setHeight(mWidthSeek.getProgress());
                        lp.width = mWidthSeek.getProgress();
                        mWidthSeek.setEnabled(true);
                        break;
                }

                mPhotoStackView.setLayoutParams(lp);
            }
        });

        heightRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                ViewGroup.LayoutParams lp = mPhotoStackView.getLayoutParams();

                switch (checkedId) {
                    case R.id.activity_imagethumbview_height_mode_wrap:
                        //mPhotoStackView.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        mHeightSeek.setProgress(mPhotoStackView.getMeasuredHeight());
                        mHeightSeek.setEnabled(false);
                        break;
                    case R.id.activity_imagethumbview_height_mode_match:
                        //mPhotoStackView.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                        lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
                        mHeightSeek.setProgress(mPhotoStackView.getMeasuredHeight());
                        mHeightSeek.setEnabled(false);
                        break;
                    case R.id.activity_imagethumbview_height_mode_custom:
                        //mPhotoStackView.setHeight(mHeightSeek.getProgress());
                        lp.height = mHeightSeek.getProgress();
                        mHeightSeek.setEnabled(true);
                        break;
                }

                mPhotoStackView.setLayoutParams(lp);
            }
        });

        CheckBox showStackGap = (CheckBox) findViewById(R.id.activity_imagethumbview_stack_gap);
        CheckBox showStackFade = (CheckBox) findViewById(R.id.activity_imagethumbview_stack_fade);

        showStackGap.setChecked(mPhotoStackView.showStackGap());
        showStackFade.setChecked(mPhotoStackView.showStackFade());

        showStackGap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                mPhotoStackView.setShowStackGap(isChecked);
            }
        });

        showStackFade.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                mPhotoStackView.setShowStackFade(isChecked);
            }
        });

        mHeightSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ViewGroup.LayoutParams lp = mPhotoStackView.getLayoutParams();
                lp.height = progress;
                mPhotoStackView.setLayoutParams(lp);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mWidthSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ViewGroup.LayoutParams lp = mPhotoStackView.getLayoutParams();
                lp.width = progress;
                mPhotoStackView.setLayoutParams(lp);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mSpinSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mPhotoStackView.setSpin(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
