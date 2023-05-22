package com.test.archi.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity<V : BaseViewModel> : AppCompatActivity() {

    protected abstract fun observeViewModel()
    protected abstract fun initViewBinding()
    protected abstract fun initViewModel()
    protected lateinit var viewModel: V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        initViewBinding()
        observeViewModel()
    }

}
