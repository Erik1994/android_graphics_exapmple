package com.example.abdroidgraphics.extension

import android.widget.EditText

fun EditText.getInputList() : List<String> {
    val lineCount = lineCount
    val resultList = mutableListOf<String>()
    val result: String = text.toString()
    var lineOffset = 0
    for(lineN in 0 until lineCount) {
        val lineEnd = layout.getLineEnd(lineN)
        resultList.add(result.substring(lineOffset, lineEnd).replace("\n", ""))
        lineOffset = lineEnd
    }
    return resultList
}