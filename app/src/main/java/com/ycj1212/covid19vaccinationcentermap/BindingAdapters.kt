package com.ycj1212.covid19vaccinationcentermap

import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.updateLayoutParams
import androidx.databinding.BindingAdapter

@BindingAdapter("app:progressTo")
fun progressTo(progressBar: ProgressBar, progress: Int) {
    progressBar.progress = progress
}

@BindingAdapter("layoutMarginBottom")
fun setLayoutMarginBottom(view: View, dimen: Float) {
    view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
        bottomMargin = dimen.toInt()
    }
}
