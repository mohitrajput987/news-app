package com.otb.news.common.base

import com.otb.news.common.LoadingSpinner

/**
 * Created by Mohit Rajput on 01/10/22.
 */
interface DisplaysLoadingSpinner {
    val loadingSpinner: LoadingSpinner

    fun showLoadingSpinner() {
        loadingSpinner.show()
    }

    fun dismissLoadingSpinner() {
        loadingSpinner.dismiss()
    }
}