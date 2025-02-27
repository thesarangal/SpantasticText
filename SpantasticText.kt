import android.content.res.Configuration
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material.LocalTextStyle
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
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import sarangal.packagemanager.presentation.theme.MyAppTheme

/**
 * A composable function that displays text with customizable spans, allowing for
 * different styles and click actions within the same text.
 *
 * ## Overview
 * `SpantasticText` provides a way to create rich text layouts where specific
 * portions of the text can have unique styles and be made clickable. This is
 * achieved by defining `SpanModel` objects that specify the text to be styled,
 * the desired style, and an optional callback key for click actions.
 *
 * ## Features
 * - **Styled Spans:** Apply custom styles (color, font size, font weight, font family,
 *   underline) to specific parts of the text.
 * - **Clickable Spans:** Make specific parts of the text clickable and handle
 *   click events with a callback.
 * - **Multiple Spans:** Use multiple spans within a single text for diverse styling
 *   and click actions.
 * - **Customizable Text:** Customize the overall text appearance with parameters like
 *   color, font size, font style, font weight, font family, letter spacing, text
 *   decoration, text alignment, line height, overflow, soft wrap, max lines, and
 *   min lines.
 * - **Text Layout:** Provides a callback to get the [TextLayoutResult] of the text.
 *
 * ## Parameters
 * @param text The complete text to be displayed.
 * @param modifier The modifier to be applied to the text.
 * @param color The color to be applied to the text.
 * @param fontSize The font size to be applied to the text.
 * @param fontStyle The font style to be applied to the text.
 * @param fontWeight The font weight to be applied to the text.
 * @param fontFamily The font family to be applied to the text.
 * @param letterSpacing The letter spacing to be applied to the text.
 * @param textDecoration The text decoration to be applied to the text.
 * @param textAlign The text alignment to be applied to the text.
 * @param lineHeight The line height to be applied to the text.
 * @param overflow How visual overflow should be handled.
 * @param softWrap Whether the text should break at soft line breaks.
 * @param maxLines The maximum number of lines for the text to span.
 * @param minLines The minimum number of lines for the text to span.
 * @param onTextLayout Callback invoked when the text layout is calculated.
 * @param style The default text style applied to the entire text. Defaults to [LocalTextStyle.current].
 * @param spanModels A list of [SpanModel] that defines the spans and their styles.
 * @param onClick Callback invoked when a clickable span is tapped. The `callbackKey` of the span is passed to the callback.
 *
 * @sample usage:
 * ```
 * SpantasticText(
 *     text = "Click here to learn more.",
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
 * 1.0.1
 *
 * @since December 31, 2024
 * @link https://github.com/thesarangal/SpantasticText
 */
@Composable
fun SpantasticText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    style: TextStyle = LocalTextStyle.current,
    spanModels: List<SpanModel>,
    onClick: ((String?) -> Unit)? = null
) {
    val annotatedString = buildAnnotatedString {
        append(text)
        spanModels.forEach { spanModel ->
            val startIndex = text.indexOf(spanModel.spanString)
            if (startIndex != -1) {
                val endIndex = startIndex + spanModel.spanString.length
                addStyle(
                    style = SpanStyle(
                        color = spanModel.color ?: Color.Unspecified,
                        fontSize = spanModel.fontSize?.sp ?: style.fontSize,
                        fontWeight = spanModel.fontWeight ?: style.fontWeight,
                        fontFamily = spanModel.fontFamily ?: style.fontFamily,
                        textDecoration = if (spanModel.showUnderline) TextDecoration.Underline else null
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

    var layoutResult by remember { mutableStateOf<TextLayoutResult?>(null) } // Corrected: Use a different name for the lambda parameter

    val innerOnTextLayout: (TextLayoutResult) -> Unit = { newLayoutResult ->

        // Store the layout result for tap detection
        layoutResult = newLayoutResult // Corrected: Assign to the state variable

        // Call the outer onTextLayout if provided
        onTextLayout?.invoke(newLayoutResult)
    }

    Text(
        text = annotatedString,
        color = color,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        onTextLayout = innerOnTextLayout,
        modifier = modifier
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
                    modifier
                }
            ),
        style = style,
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
    val callbackKey: String? = null
)

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun HighlightTextPreviewLight() {
    MyAppTheme {
        Surface {
            SpantasticText(
                text = "Jetpack Compose makes UI development fun!",
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
    MyAppTheme {
        Surface {
            SpantasticText(
                text = "Jetpack Compose makes UI development fun!",
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
    MyAppTheme {
        Surface {
            SpantasticText(
                text = "Click here to learn more.",
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
    MyAppTheme {
        Surface {
            SpantasticText(
                text = "Click here to learn more.",
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
    MyAppTheme {
        Surface {
            SpantasticText(
                text = "Learn Jetpack Compose with SpantasticText!",
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
    MyAppTheme {
        Surface {
            SpantasticText(
                text = "Learn Jetpack Compose with SpantasticText!",
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
