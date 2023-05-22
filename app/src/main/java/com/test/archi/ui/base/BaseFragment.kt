package com.test.archi.ui.base

import androidx.fragment.app.Fragment

abstract class BaseFragment<VM : BaseViewModel> : Fragment() {
    protected lateinit var viewModel: VM
}