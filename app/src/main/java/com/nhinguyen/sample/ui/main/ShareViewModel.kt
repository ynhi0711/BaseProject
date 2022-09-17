package com.nhinguyen.sample.ui.main

import com.nhinguyen.sample.core.repository.Repository
import com.nhinguyen.sample.ui.base.BaseViewModel
import com.nhinguyen.sample.utils.SPUtils
import com.squareup.moshi.Moshi
import javax.inject.Inject

class ShareViewModel @Inject constructor(
    private val repository: Repository,
    val spUtils: SPUtils,
    val moshi: Moshi
) : BaseViewModel() {
}
