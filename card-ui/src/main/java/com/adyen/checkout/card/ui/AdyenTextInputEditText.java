/*
 * Copyright (c) 2019 Adyen N.V.
 *
 * This file is open source and available under the MIT license. See the LICENSE file for more info.
 *
 * Created by arman on 22/7/2019.
 */

package com.adyen.checkout.card.ui;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

public class AdyenTextInputEditText extends TextInputEditText {

    @Nullable
    private Listener mListener;

    public AdyenTextInputEditText(@NonNull Context context) {
        this(context, null);
    }

    public AdyenTextInputEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Constructor of AdyenTextInputEditText.
     */
    public AdyenTextInputEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr == 0 ? android.support.design.R.attr.editTextStyle : defStyleAttr);
        addTextChangedListener(getTextWatcher());
    }

    public void setOnChangeListener(@NonNull Listener listener) {
        this.mListener = listener;
    }

    @NonNull
    public String getRawValue() {
        return getText().toString();
    }

    @CallSuper
    protected void afterTextChanged(@NonNull Editable editable) {
        if (mListener != null) {
            mListener.onTextChanged(editable);
        }
    }

    @NonNull
    private TextWatcher getTextWatcher() {
        return new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Empty
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Empty
            }

            @Override
            public void afterTextChanged(Editable s) {
                AdyenTextInputEditText.this.afterTextChanged(s);
            }
        };
    }

    public interface Listener {
        void onTextChanged(@NonNull Editable editable);
    }
}
