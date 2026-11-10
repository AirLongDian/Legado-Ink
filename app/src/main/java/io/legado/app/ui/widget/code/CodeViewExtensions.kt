@file:Suppress("unused")

package io.legado.app.ui.widget.code

import android.content.Context
import android.graphics.Color
import android.widget.ArrayAdapter
import io.legado.app.R
import java.util.regex.Pattern

val legadoPattern: Pattern = Pattern.compile("\\|\\||&&|%%|@js:|@Json:|@css:|@@|@XPath:")
val jsonPattern: Pattern = Pattern.compile("\"[A-Za-z0-9]*?\"\\:|\"|\\{|\\}|\\[|\\]")
val wrapPattern: Pattern = Pattern.compile("\\\\n")
val operationPattern: Pattern =
    Pattern.compile(":|==|>|<|!=|>=|<=|->|=|>|<|%|-|-=|%=|\\+|\\-|\\-=|\\+=|\\^|\\&|\\|::|\\?|\\*")
val jsPattern: Pattern = Pattern.compile("var")

fun CodeView.addLegadoPattern() {
    addSyntaxPattern(legadoPattern, Color.BLACK)
}

fun CodeView.addJsonPattern() {
    addSyntaxPattern(jsonPattern, Color.DKGRAY)
}

fun CodeView.addJsPattern() {
    addSyntaxPattern(wrapPattern, Color.GRAY)
    addSyntaxPattern(operationPattern, Color.BLACK)
    addSyntaxPattern(jsPattern, Color.DKGRAY)
}

fun Context.arrayAdapter(keywords: Array<String>): ArrayAdapter<String> {
    return ArrayAdapter(this, R.layout.item_1line_text_and_del, R.id.text_view, keywords)
}
