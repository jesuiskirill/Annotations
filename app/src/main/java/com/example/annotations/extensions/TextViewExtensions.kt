package com.example.annotations.extensions

import android.graphics.Typeface
import android.text.Annotation
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.SpannedString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes

/**
 * Makes specific text (annotated with annotation tag) clickable.
 *
 * @param fullText this is the StringRes from strings.xml where the clickable text is surrounded with
 * the annotation tags.
 * @param annotationsActions list of AnnotationAction instances contains the key of the annotation
 * and a callback.
 *
 * In case programmer would like to add specific custom color to the clickable text
 * should add new span in spannableString.
 * E.g:
 * setSpan(
 * ForegroundColorSpan(
 * ContextCompat.getColor(context, android.R.color.white)),
 * spannedFullText.getSpanStart(it),
 * (spannedFullText.getSpanEnd(it),
 * 0)
 *
 */

fun TextView.setClickListenerToText(@StringRes fullText: Int,
    annotationsActions: List<AnnotationAction>
) {
    val spannedFullText = context.resources.getText(fullText) as SpannedString
    val spannableString = SpannableString(spannedFullText)
    val annotations = spannedFullText.getSpans(0, spannedFullText.length, Annotation::class.java)

    annotationsActions.forEach { annotationAction ->

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                annotationAction.action.invoke()
            }
        }

        annotations.find { it.value == annotationAction.annotation }?.let {
            spannableString.apply {
                setSpan(
                    clickableSpan,
                    spannedFullText.getSpanStart(it),
                    spannedFullText.getSpanEnd(it),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                setSpan(
                    RelativeSizeSpan(1.1f),
                    spannedFullText.getSpanStart(it),
                    spannedFullText.getSpanEnd(it),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                setSpan(
                    StyleSpan(Typeface.BOLD),
                    spannedFullText.getSpanStart(it),
                    spannedFullText.getSpanEnd(it),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
    }

    this.apply {
        text = spannableString
        movementMethod = LinkMovementMethod.getInstance()
    }
}

data class AnnotationAction(val annotation: String, val action: (() -> Unit))
