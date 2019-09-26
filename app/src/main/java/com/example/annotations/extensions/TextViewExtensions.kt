package com.example.annotations.extensions

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.SpannedString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.example.annotations.TextClickableSpan
import android.text.Annotation
import android.text.method.LinkMovementMethod

/**
 * Makes specific text (annotated with annotation tag) clickable.
 *
 * @param context the context is used to get string.
 * @param fullText this is the text from strings.xml where the clickable text is surrounded with
 * the annotation tags.
 * @param textsKeys the keys of the annotation textsKeys.
 * @param onSelectedText callback that contains the selected key text.
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

fun TextView.setClickListenerToText(
    context: Context, @StringRes fullText: Int, textsKeys: List<String>,
    onSelectedText: (text: String) -> Unit
) {

    val spannedFullText = context.resources.getText(fullText) as SpannedString
    val spannableString = SpannableString(spannedFullText)
    val annotations = spannedFullText.getSpans(0, spannedFullText.length, Annotation::class.java)

    textsKeys.forEach { clickableText ->

        val clickableSpan = object : TextClickableSpan(clickableText) {
            override fun onClick(widget: View) {
                onSelectedText(identifier)
            }
        }


        annotations.find { it.value == clickableText }?.let {
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