import android.content.res.Configuration
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.security.myapplication.ui.theme.MyApplicationTheme

/**
 * Spantastic Composable for Jetpack Compose
 *
 * A utility composable to create styled and clickable spans within a text.
 *
 * ## Features:
 * - Apply custom styles (color, size, weight, underline, etc.) to specific parts of the text.
 * - Make specific parts of the text clickable and handle events with a callback.
 * - Use multiple spans within a single text for diverse styling.
 *
 * @param fullString The complete text to be displayed.
 * @param spanModels A list of [SpanModel] that defines the spans and their styles.
 * @param defaultStyle The default text style applied to the entire text. Defaults to MaterialTheme.typography.body1.
 * @param onClick Callback invoked when a clickable span is tapped. The `callbackKey` of the span is passed to the callback.
 *
 * @sample usage:
 * ```
 * SpantasticText(
 *     fullString = "Click here to learn more.",
 *     spanModels = listOf(
 *         SpanModel(
 *             spanString = "Click here",
 *             color = Color.Red,
 *             showUnderline = true,
 *             callbackKey = "learn_more"
 *         )
 *     ),
 *     onClick = { key ->
 *         if (key == "learn_more") {
 *             println("Navigate to the learn more section")
 *         }
 *     }
 * )
 * ```
 *
 * @author
 * Created by Rajat Sarangal
 *
 * @version
 * 1.0.0
 *
 * @since December 31, 2024
 * @link https://github.com/thesarangal/SpantasticText
 */
@Composable
fun SpantasticText(
    fullString: String,
    spanModels: List<SpanModel>,
    defaultStyle: TextStyle = MaterialTheme.typography.body1,
    onClick: ((String?) -> Unit)? = null
) {
    val annotatedString = buildAnnotatedString {
        append(fullString)
        spanModels.forEach { spanModel ->
            val startIndex = fullString.indexOf(spanModel.spanString)
            if (startIndex != -1) {
                val endIndex = startIndex + spanModel.spanString.length
                addStyle(
                    style = SpanStyle(
                        color = spanModel.color ?: Color.Unspecified,
                        fontSize = spanModel.fontSize?.sp ?: defaultStyle.fontSize,
                        fontWeight = spanModel.fontWeight ?: defaultStyle.fontWeight,
                        fontFamily = spanModel.fontFamily ?: defaultStyle.fontFamily,
                        textDecoration = if (spanModel.showUnderline && spanModel.showStrikeThrough) {
                            TextDecoration.Underline + TextDecoration.LineThrough
                        } else if (spanModel.showUnderline) {
                            TextDecoration.Underline
                        } else if (spanModel.showStrikeThrough) {
                            TextDecoration.LineThrough
                        } else null,
                        background = spanModel.backgroundColor
                    ),
                    start = startIndex,
                    end = endIndex
                )
                addStringAnnotation(
                    tag = "CLICKABLE",
                    annotation = spanModel.callbackKey ?: spanModel.spanString,
                    start = startIndex,
                    end = endIndex
                )
            }
        }
    }

    var layoutResult: TextLayoutResult? by remember { mutableStateOf(null) }

    Text(
        text = annotatedString,
        onTextLayout = if (onClick != null) {
            { layoutResult = it }
        } else {
            {}
        },
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) {
                    Modifier.pointerInput(Unit) {
                        detectTapGestures { offset ->
                            layoutResult?.let { layout ->
                                val position = layout.getOffsetForPosition(offset)
                                annotatedString
                                    .getStringAnnotations(
                                        tag = "CLICKABLE",
                                        start = position,
                                        end = position
                                    )
                                    .firstOrNull()
                                    ?.let { annotation ->
                                        onClick(annotation.item)
                                    }
                            }
                        }
                    }
                } else {
                    Modifier
                }
            ),
        style = defaultStyle,
    )
}

/**
 * Data model to represent attributes of a span.
 *
 * @param spanString The text that will be styled or made interactive.
 * @param color The color of the span text. Defaults to `null` (uses default style).
 * @param fontSize The font size of the span text in sp. Defaults to `null` (uses default style).
 * @param fontWeight The font weight of the span text. Defaults to `null` (uses default style).
 * @param fontFamily The font family of the span text. Defaults to `null` (uses default style).
 * @param showUnderline Whether the text should be underlined. Defaults to `false`.
 * @param callbackKey The key used to identify the span when it is clicked. Defaults to the span text.
 *
 * @author
 * Created by Rajat Sarangal
 *
 * @version
 * 1.0.0
 */
data class SpanModel(
    val spanString: String,
    val color: Color? = null,
    val fontSize: Float? = null,
    val fontWeight: FontWeight? = null,
    val fontFamily: FontFamily? = null,
    val showUnderline: Boolean = false,
    val callbackKey: String? = null,
    val showStrikeThrough: Boolean = false,
    val backgroundColor: Color = Color.Unspecified
)

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun HighlightTextPreviewLight() {
    MyApplicationTheme {
        Surface {
            SpantasticText(
                fullString = "Jetpack Compose makes UI development fun!",
                spanModels = listOf(
                    SpanModel(
                        spanString = "Jetpack Compose",
                        color = Color.Blue,
                        fontWeight = FontWeight.Bold
                    )
                )
            )
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun HighlightTextPreviewDark() {
    MyApplicationTheme {
        Surface {
            SpantasticText(
                fullString = "Jetpack Compose makes UI development fun!",
                spanModels = listOf(
                    SpanModel(
                        spanString = "Jetpack Compose",
                        color = Color.Cyan,
                        fontWeight = FontWeight.Bold
                    )
                )
            )
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun ClickableTextPreviewLight() {
    MyApplicationTheme {
        Surface {
            SpantasticText(
                fullString = "Click here to learn more.",
                spanModels = listOf(
                    SpanModel(
                        spanString = "Click here",
                        color = Color.Red,
                        showUnderline = true,
                        callbackKey = "learn_more"
                    )
                ),
                onClick = { key -> println("Clicked on: $key") }
            )
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ClickableTextPreviewDark() {
    MyApplicationTheme {
        Surface {
            SpantasticText(
                fullString = "Click here to learn more.",
                spanModels = listOf(
                    SpanModel(
                        spanString = "Click here",
                        color = Color.Magenta,
                        showUnderline = true,
                        callbackKey = "learn_more"
                    )
                ),
                onClick = { key -> println("Clicked on: $key") }
            )
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun MultiSpanTextPreviewLight() {
    MyApplicationTheme {
        Surface {
            SpantasticText(
                fullString = "Learn Jetpack Compose with SpantasticText!",
                spanModels = listOf(
                    SpanModel(
                        spanString = "Learn",
                        color = Color.Green,
                        fontWeight = FontWeight.Bold
                    ),
                    SpanModel(
                        spanString = "Jetpack Compose",
                        color = Color.Blue,
                        fontSize = 20f,
                        showUnderline = true
                    ),
                    SpanModel(
                        spanString = "SpantasticText",
                        color = Color.Magenta,
                        fontFamily = FontFamily.Cursive
                    )
                )
            )
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun MultiSpanTextPreviewDark() {
    MyApplicationTheme {
        Surface {
            SpantasticText(
                fullString = "Learn Jetpack Compose with SpantasticText!",
                spanModels = listOf(
                    SpanModel(
                        spanString = "Learn",
                        color = Color.Yellow,
                        fontWeight = FontWeight.Bold
                    ),
                    SpanModel(
                        spanString = "Jetpack Compose",
                        color = Color.Cyan,
                        fontSize = 20f,
                        showUnderline = true
                    ),
                    SpanModel(
                        spanString = "SpantasticText",
                        color = Color.Magenta,
                        fontFamily = FontFamily.Cursive
                    )
                )
            )
        }
    }
}
