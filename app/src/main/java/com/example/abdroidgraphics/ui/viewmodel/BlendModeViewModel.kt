package com.example.abdroidgraphics.ui.viewmodel

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PorterDuff
import android.net.Uri
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.abdroidgraphics.R
import com.example.abdroidgraphics.ui.fragment.ImagePosition
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream

class BlendModeViewModel: BaseViewModel() {
    var imagePosition = ImagePosition.NONE
    fun getBlendModeMap(context: Context) =
        HashMap<String, PorterDuff.Mode?>().apply {
            put(context.getString(R.string.effect_param_blendmode_normal), null)
            put(context.getString(R.string.effect_param_blendmode_multiply), PorterDuff.Mode.MULTIPLY)
            put(context.getString(R.string.effect_param_blendmode_darken), PorterDuff.Mode.DARKEN)
            put(context.getString(R.string.effect_param_blendmode_lighten), PorterDuff.Mode.LIGHTEN)
            put(context.getString(R.string.effect_param_blendmode_overlay), PorterDuff.Mode.OVERLAY)
            put(context.getString(R.string.effect_param_blendmode_add), PorterDuff.Mode.ADD)
            put(context.getString(R.string.effect_param_blendmode_dstint), PorterDuff.Mode.DST_IN)
            put(context.getString(R.string.effect_param_blendmode_dstover), PorterDuff.Mode.DST_OVER)
            put(context.getString(R.string.effect_param_blendmode_dstout), PorterDuff.Mode.DST_OUT)
            put(context.getString(R.string.effect_param_blendmode_srcnt), PorterDuff.Mode.SRC_IN)
            put(context.getString(R.string.effect_param_blendmode_srcout), PorterDuff.Mode.SRC_OUT)
            put(context.getString(R.string.effect_param_blendmode_srcover), PorterDuff.Mode.SRC_OVER)
        }
}