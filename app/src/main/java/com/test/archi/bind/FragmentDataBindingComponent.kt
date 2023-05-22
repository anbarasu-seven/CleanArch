package com.test.archi.bind

import androidx.fragment.app.Fragment

/**
 * A Data Binding Component implementation for fragments.
 */
class FragmentDataBindingComponent(fragment: Fragment) : androidx.databinding.DataBindingComponent {
    private val adapter = FragmentBindingAdapters(fragment)

    override fun getFragmentBindingAdapters() = adapter
}
