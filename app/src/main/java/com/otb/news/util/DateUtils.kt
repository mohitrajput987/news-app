package com.otb.news.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Mohit Rajput on 01/10/22.
 */
object DateUtils {
    fun getFormattedTime(date: Date) =
        SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(date) ?: ""
}