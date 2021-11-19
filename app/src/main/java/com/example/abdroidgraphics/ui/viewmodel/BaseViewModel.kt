package com.example.abdroidgraphics.ui.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.example.abdroidgraphics.navigation.NavigationCommand
import com.example.abdroidgraphics.navigation.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream

abstract class BaseViewModel : ViewModel() {

    private val _bitmapFlow: MutableStateFlow<Bitmap?> = MutableStateFlow(null)
    val bitmapFlow = _bitmapFlow.asSharedFlow()

    private val _navigationFlow = MutableSharedFlow<NavigationCommand>()
    val navigationFlow = _navigationFlow.asSharedFlow()

    fun navigate(direction: NavDirections) {
        viewModelScope.launch {
            _navigationFlow.emit(NavigationCommand.To(direction))
        }
    }

    fun createBitmapFromUri(uri: Uri, activity: Context) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                createBitmap(uri, activity)
            }
        }
    }

    private suspend fun createBitmap(uri: Uri, activity: Context) {
        activity.contentResolver?.openInputStream(uri)?.let {
            val bitmap = BitmapFactory.decodeStream(it)
            _bitmapFlow.value = getResizedBitmap(bitmap, 500)
            it.close()
        }
    }

    private fun getResizedBitmap(bitmap: Bitmap, maxSize: Int): Bitmap {
        var height = bitmap.height
        var width = bitmap.width
        val bitmapRatio = width.toFloat()/height
        bitmapRatio.takeIf { it > 1 }?.let {
            width = maxSize
            height = (width/bitmapRatio).toInt()
        }?: run {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(bitmap, width, height, true)
    }
}