package moe.feng.danmaqua.util.ext

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.squareup.picasso.Picasso
import moe.feng.danmaqua.R

var TextView.compoundDrawableStart: Drawable?
    get() = compoundDrawablesRelative[0]
    set(value) {
        with (compoundDrawablesRelative) {
            setCompoundDrawablesRelativeWithIntrinsicBounds(value, this[1], this[2], this[3])
        }
    }

var TextView.compoundDrawableStartRes: Int
    get() = throw UnsupportedOperationException()
    set(@DrawableRes value) {
        this.compoundDrawableStart = context.getDrawable(value)
    }

fun Float.dpToPx(context: Context): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics)
}

fun Float.spToPx(context: Context): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP, this, context.resources.displayMetrics)
}

var ImageView.avatarUrl: String?
    get() = throw UnsupportedOperationException("ImageView#avatarUrl can only set")
    set(value) {
        if (value.isNullOrEmpty()) {
            setImageResource(R.drawable.avatar_placeholder_empty)
        } else {
            Picasso.get()
                .load(value)
                .placeholder(R.drawable.avatar_placeholder_empty)
                .into(this)
        }
    }
