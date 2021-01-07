package com.sehatq.extension.android.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Allows quick inflater
 *
 * ```
 * val inflater = LayoutInflater.from(viewGroup.context)
 * val view = inflater.inflate(R.layout.my_layout, parent, false)
 * ```
 *
 * ```
 * val view = viewGroup.inflate(R.layout.my_layout)
 * ```
 */
fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}
