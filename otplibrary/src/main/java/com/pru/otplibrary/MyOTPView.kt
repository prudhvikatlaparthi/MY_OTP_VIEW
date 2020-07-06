package com.pru.otplibrary

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat

class MyOTPView : LinearLayout {
    private var digitColor: Int = 0
    private var digitDefaultColor: Int = 0
    private var textSize: Float = 17f
    private var pinSecureDigitListener: SecureDigitListener? = null
    private var mContext: Context
    private var dataView: LinearLayout? = null
    private var sizeDigits = 0
    private var default: String = "*"

    constructor(context: Context) : super(context){
        mContext = context
        init(null)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs) {
        mContext = context
        init(attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        mContext = context
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.MyOTPView
        )
        sizeDigits = typedArray.getInt(R.styleable.MyOTPView_digits_count, 4)
        default = typedArray.getString(R.styleable.MyOTPView_digits_default_text) ?: "*"
        digitColor = typedArray.getColor(
            R.styleable.MyOTPView_digits_color, ContextCompat.getColor(
                mContext,
                R.color.black
            )
        )
        digitDefaultColor = typedArray.getColor(
            R.styleable.MyOTPView_digits_default_text_color, ContextCompat.getColor(
                mContext,
                R.color.tint
            )
        )
        textSize = typedArray.getDimension(
            R.styleable.MyOTPView_digits_textSize, 17f
        )

        typedArray.recycle()

        val mRootView =
            View.inflate(mContext, R.layout.secure_view, this) as LinearLayout
        dataView = mRootView.findViewById(R.id.data_View)
        setItemCount(sizeDigits)
    }

    fun setItemCount(count: Int) {
        this.sizeDigits = count
        for (i in 0 until count) {
            val OTPEditTextView =
                com.pru.otplibrary.DigitView(
                    i,
                    mContext,
                    dataView,
                    count,
                    default,
                    textSize,
                    digitColor,
                    digitDefaultColor,
                    object : SecureDigitListener {
                        override fun lastDigitChecked() {
                            pinSecureDigitListener?.lastDigitChecked()
                        }

                    })
            dataView!!.addView(OTPEditTextView)
        }
    }

    val number: String
        get() {
            var value = ""
            if (dataView != null) {
                for (i in 0 until dataView!!.childCount) {
                    val view = dataView!!.getChildAt(i) as com.pru.otplibrary.DigitView
                    value += view.editView.text.toString()
                }
            }
            return value
        }

    fun clearDigits() {
        if (dataView != null) {
            for (i in 0 until dataView!!.childCount) {
                var view = dataView!!.getChildAt(i) as com.pru.otplibrary.DigitView
                view.clearOTP()
                if (i == dataView!!.childCount - 1) {
                    view = dataView!!.getChildAt(0) as com.pru.otplibrary.DigitView
                    view.requestFocus()
                }
            }
        }
    }

    fun setListener(pinSecureDigitListener: SecureDigitListener) {
        this.pinSecureDigitListener = pinSecureDigitListener
    }

}