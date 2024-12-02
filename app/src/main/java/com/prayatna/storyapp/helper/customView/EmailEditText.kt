package com.prayatna.storyapp.helper.customView

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import com.prayatna.storyapp.R

class EmailEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    init {
        setupTextValidation()
        hint = context.getString(R.string.enter_email)
        inputType = android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS or android.text.InputType.TYPE_CLASS_TEXT
    }

    private fun setupTextValidation() {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateEmail(s)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun validateEmail(email: CharSequence?) {
        if (email.isNullOrEmpty()) {
            setError(context.getString(R.string.email_required), null)
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            setError(context.getString(R.string.invalid_email), null)
        } else {
            setError(null, null)
        }
    }
}