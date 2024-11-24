package com.prayatna.storyapp.helper.customView

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.prayatna.storyapp.R

@SuppressLint("ClickableViewAccessibility")
class PasswordEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    private var isPasswordVisible = false

    init {
        setupTextValidation()
        inputType = android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD or android.text.InputType.TYPE_CLASS_TEXT
        transformationMethod = android.text.method.PasswordTransformationMethod.getInstance()
        setOnTouchListener {_, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= right - compoundPaddingRight) {
                    setupPasswordVisibility()
                    return@setOnTouchListener true
                }
            }
            false
        }
    }

    private fun setupTextValidation() {
        hint = ContextCompat.getString(context, R.string.enter_password)
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().length < 8) {
                    setError("Password cannot be less than 8 characters", null)
                } else {
                    setError(null, null)
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

    }
    private fun setupPasswordVisibility() {
        isPasswordVisible != isPasswordVisible
        transformationMethod = if (isPasswordVisible) {
            null
        } else {
            android.text.method.HideReturnsTransformationMethod.getInstance()
        }
        setSelection(text?.length ?: 0)
    }
}