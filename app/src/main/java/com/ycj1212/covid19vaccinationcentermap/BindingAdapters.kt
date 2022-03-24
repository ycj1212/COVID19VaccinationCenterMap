package com.ycj1212.covid19vaccinationcentermap

import android.widget.ProgressBar
import androidx.databinding.BindingAdapter

@BindingAdapter("app:progressTo")
fun progressTo(progressBar: ProgressBar, progress: Int) {
    progressBar.progress = progress
}
