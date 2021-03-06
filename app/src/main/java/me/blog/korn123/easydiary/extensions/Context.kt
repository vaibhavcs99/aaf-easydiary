package me.blog.korn123.easydiary.extensions

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.simplemobiletools.commons.extensions.adjustAlpha
import com.simplemobiletools.commons.extensions.baseConfig
import com.simplemobiletools.commons.extensions.isBlackAndWhiteTheme
import com.simplemobiletools.commons.views.*
import io.github.hanjoongcho.commons.views.ModalView
import me.blog.korn123.commons.utils.CommonUtils
import me.blog.korn123.easydiary.helper.Config
import me.blog.korn123.easydiary.helper.DEFAULT_FONT_SIZE_SUPPORT_LANGUAGE
import me.blog.korn123.easydiary.helper.SETTING_FONT_SIZE
import me.blog.korn123.easydiary.views.LabelLayout

/**
 * Created by CHO HANJOONG on 2018-02-06.
 * This code based 'Simple-Commons' package
 * You can see original 'Simple-Commons' from below link.
 * https://github.com/SimpleMobileTools/Simple-Commons
 */

val Context.config: Config get() = Config.newInstance(this)

fun Context.updateTextColors(viewGroup: ViewGroup, tmpTextColor: Int = 0, tmpAccentColor: Int = 0) {
    val textColor = if (tmpTextColor == 0) baseConfig.textColor else tmpTextColor
    val backgroundColor = baseConfig.backgroundColor
    val accentColor = if (tmpAccentColor == 0) {
        if (isBlackAndWhiteTheme()) {
            Color.WHITE
        } else {
            baseConfig.primaryColor
        }
    } else {
        tmpAccentColor
    }

    val cnt = viewGroup.childCount
    (0 until cnt)
            .map { viewGroup.getChildAt(it) }
            .forEach {
                when (it) {
                    is MyTextView -> it.setColors(textColor, accentColor, backgroundColor)
                    is MyAppCompatSpinner -> it.setColors(textColor, accentColor, backgroundColor)
                    is MySwitchCompat -> it.setColors(textColor, accentColor, backgroundColor)
                    is MyCompatRadioButton -> it.setColors(textColor, accentColor, backgroundColor)
                    is MyAppCompatCheckbox -> it.setColors(textColor, accentColor, backgroundColor)
                    is MyEditText -> {
                        it.setTextColor(textColor)
                        it.setHintTextColor(textColor.adjustAlpha(0.5f))
                        it.setLinkTextColor(accentColor)
                    }
                    is MyFloatingActionButton -> it.backgroundTintList = ColorStateList.valueOf(accentColor)
                    is MySeekBar -> it.setColors(textColor, accentColor, backgroundColor)
                    is MyButton -> it.setColors(textColor, accentColor, backgroundColor)
                    is ModalView -> it.setBackgroundColor(accentColor)
                    is LabelLayout -> it.setBackgroundColor(accentColor)
                    is CardView -> {
                        it.setCardBackgroundColor(backgroundColor)
                        updateTextColors(it, textColor, accentColor)
                    }
                    is ViewGroup -> updateTextColors(it, textColor, accentColor)
                }
            }
}

fun Context.initTextSize(viewGroup: ViewGroup, context: Context) {
    val cnt = viewGroup.childCount
    val defaultFontSize: Float = CommonUtils.dpToPixel(context, DEFAULT_FONT_SIZE_SUPPORT_LANGUAGE).toFloat()
    val settingFontSize: Float = config.settingFontSize
    (0 until cnt)
            .map { viewGroup.getChildAt(it) }
            .forEach {
                when (it) {
                    is TextView -> it.setTextSize(TypedValue.COMPLEX_UNIT_PX, settingFontSize)
                    is ViewGroup -> initTextSize(it, context)
                }
            }
}

fun Context.initTextSize(textView: TextView, context: Context) {
    val defaultFontSize: Float = CommonUtils.dpToPixel(context, DEFAULT_FONT_SIZE_SUPPORT_LANGUAGE).toFloat()
    val settingFontSize: Float = config.settingFontSize
    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, settingFontSize)
}

fun Context.checkPermission(permissions: Array<String>): Boolean {
    val listDeniedPermissions: List<String> = permissions.filter { permission -> 
        ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED
    }
    return listDeniedPermissions.isEmpty()
}

fun Context.makeSnackBar(view: View, message: String) {
    Snackbar.make(view, message, Snackbar.LENGTH_SHORT).setAction("Action", null).show()
}

fun Context.showAlertDialog(message: String, positiveListener: DialogInterface.OnClickListener, negativeListener: DialogInterface.OnClickListener) {
    val builder = AlertDialog.Builder(this)
    //        builder.setIcon(R.drawable.ic_launcher);
    //        builder.setTitle("일기삭제");
    builder.setMessage(message)
    builder.setCancelable(true)
    builder.setNegativeButton("취소", negativeListener)
    builder.setPositiveButton("확인", positiveListener)
    val alert = builder.create()
    alert.show()
}

fun Context.showAlertDialog(message: String, positiveListener: DialogInterface.OnClickListener) {
    val builder = AlertDialog.Builder(this)
    builder.setMessage(message)
    builder.setCancelable(true)
    builder.setPositiveButton("확인", positiveListener)
    val alert = builder.create()
    alert.show()
}

fun Context.showAlertDialog(title: String, message: String, positiveListener: DialogInterface.OnClickListener) {
    val builder = AlertDialog.Builder(this)
    builder.setTitle(title)
    //        builder.setIcon(R.drawable.book);
    builder.setMessage(message)
    builder.setCancelable(true)
    builder.setPositiveButton("확인", positiveListener)
    val alert = builder.create()
    alert.show()
}

fun Context.preferencesContains(key: String): Boolean {
    val preferences = PreferenceManager.getDefaultSharedPreferences(this)
    return preferences.contains(key)
}