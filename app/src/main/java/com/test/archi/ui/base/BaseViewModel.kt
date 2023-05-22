package com.test.archi.ui.base

import androidx.lifecycle.ViewModel
import com.test.archi.data.DB
import com.test.archi.utils.DLog
import javax.inject.Inject

abstract class BaseViewModel : ViewModel() {
    @Inject
    lateinit var dLog: DLog
    @Inject
    lateinit var db: DB
}
