# SpantasticText - Easy Text Styling with Spans in Jetpack Compose

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

SpantasticText is a powerful Kotlin library designed to enhance text styling in Jetpack Compose. It allows developers to apply custom styles to specific text segments, enabling bold, italic, underlined, and colorized text with clickable actions.

## Key Features

-   Apply **custom styling** (color, font weight, font family, size, underline) to specific text parts.
-   Support for **clickable spans** with callback handling.
-   Simple and **lightweight** implementation for Jetpack Compose.
    
-   Ideal for **highlighting** important text or creating interactive UI components.
    
-   Supports **multiple styles in a single text block**.
    
-   Enable **custom font application**.
    
-   Easily integrates **clickable URLs** with custom actions.
    
-   Perfect for **terms & conditions acceptance prompts**.

## Getting Started

1. Download and Add the `SpantasticText.kt` File into your application.

2.  Import the `SpantasticText` class into the screen where you want to use it.


## Usage Examples

Kotlin

```
SpantasticText(  
    text = """  
 SpantasticText makes styling effortless! Apply bold highlights,    underlines for emphasis, and strikethroughs for corrections.  
 Change colors, add backgrounds, or even create clickable actions! Need variety? Choose from different fonts like Serif, Monospace, or Cursive. Customize text sizes, from small to LARGE, and even detect invalid inputs like user@example. Click here to explore more: https://spantastic.docs. Remember: Beautiful text makes a better UI experience!""".trimIndent(),  
    spanModels = listOf(  
        	// Bold Text  
		SpanModel(  
            		spanString = "bold",  
            		color = Color.Blue,  
            		fontWeight = FontWeight.Bold  
  		),  
  
        	// Underlined Text  
  		SpanModel(  
            		spanString = "underlines",  
            		color = Color.Red,  
            		showUnderline = true  
  		),  
  
        	// Strikethrough Text  
  		SpanModel(  
            		spanString = "strikethroughs",  
            		color = Color.Gray,  
            		showStrikeThrough = true  
  		),  
  
        	// Color Text  
  		SpanModel(  
            		spanString = "colors",  
            		color = Color.Magenta  
  		),  
  
        	// Background Color Text  
  		SpanModel(  
            		spanString = "backgrounds",  
            		backgroundColor = Color.LightGray  
  		),  
  
        	// Clickable Action  
  		SpanModel(  
            		spanString = "clickable",  
            		color = Color.Green,  
            		showUnderline = true,  
            		callbackKey = "click_action"  
  		),  
  
        	// Font Variations  
  		SpanModel(  
            		spanString = "Serif",  
            		fontFamily = FontFamily.Serif,  
            		fontSize = 18f  
  		),  
        	SpanModel(  
            		spanString = "Monospace",  
            		fontFamily = FontFamily.Monospace,  
            		fontSize = 18f  
  		),  
        	SpanModel(  
            		spanString = "Cursive",  
            		fontFamily = FontFamily.Cursive,  
            		fontSize = 18f  
  		),  
  
        	// Text Size Variations  
  		SpanModel(  
            		spanString = "small",  
            		fontSize = 12f  
  		),  
        	SpanModel(  
            		spanString = "LARGE",  
            		fontSize = 24f,  
            		fontWeight = FontWeight.Bold  
  		),  
  
        	// Highlighting Invalid Input  
  		SpanModel(  
            		spanString = "user@example",  
            		color = Color.Red,  
            		fontWeight = FontWeight.Bold  
  		),  
  
        	// Clickable URL  
  		SpanModel(  
            		spanString = "https://spantastic.docs",  
            		color = Color.Blue,  
            		showUnderline = true,  
            		callbackKey = "docs_url"  
  		),  
  
        	// Important Reminder  
  		SpanModel(  
            		spanString = "Beautiful text",  
            		backgroundColor = Color.Yellow,  
            		fontWeight = FontWeight.Bold  
  		)  
    ),  
    onClick = { key ->  
	  when (key) {  
            "click_action" -> Toast.makeText(context, "Performing a clickable action!", Toast.LENGTH_SHORT).show()  
            "docs_url" -> Toast.makeText(context, "Opening SpantasticText documentation...", Toast.LENGTH_SHORT).show()  
        }  
    }  
)
```
## Output of Above Example
![SpantasticTextScreenshot](https://github.com/user-attachments/assets/e828c632-4853-49d2-83a1-2b14b373e0a5)
## Contributing

Contributions are welcome! Feel free to submit pull requests or open issues.

## License

MIT License
