package com.example.headwaytestapp

import android.view.View
import androidx.databinding.BindingAdapter


@BindingAdapter("toVisibleGone")
fun toVisibleGone(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}
