package com.sehatq.test.fragment.core

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * The parent fragment, all other fragments **should** extend from this class.
 */
abstract class CoreFragment : Fragment() {

    /**
     * The layout res for the current fragment.
     * @return the layout resource ID, return a null to make the fragment not set a content view.
     */
    abstract val viewRes: Int?

    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Initialize the fragment's view binding
        val view: View?
        if (viewRes != null) {
            view = inflater.inflate(viewRes!!, container, false)
        } else {
            view = null
        }

        return view
    }
}
