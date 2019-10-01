package com.example.annotations.extensions

import android.app.Activity
import android.content.Intent
import android.net.Uri


fun Activity.openUrl(url: String) {
    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
}