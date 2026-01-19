import com.github.ajalt.mordant.rendering.BorderType
import com.github.ajalt.mordant.rendering.TextColors.Companion.rgb
import com.github.ajalt.mordant.rendering.TextStyles.bold
import com.github.ajalt.mordant.rendering.TextStyles.dim
import com.github.ajalt.mordant.rendering.Theme
import com.github.ajalt.mordant.table.Borders
import com.github.ajalt.mordant.table.TableBuilder
import com.github.ajalt.mordant.table.table
import com.github.ajalt.mordant.terminal.Terminal

// Necessary? No. Fun and awesome? Yes.

val themeColor = rgb("#FFBF00")
val primaryColor = rgb("#7CB6A3")
val secondaryColor = rgb("#EB9147")
val tertiaryColor = rgb("#915A08")

val awesomeTheme = Theme {
    styles["list.number"] = tertiaryColor
    styles["list.bullet"] = tertiaryColor
    styles["panel.border"] = tertiaryColor
    styles["hr.rule"] = tertiaryColor
}

val t = Terminal(theme = awesomeTheme)

fun awesomeTable(init: TableBuilder.() -> Unit) = table {
    borderType = BorderType.ROUNDED
    borderStyle = themeColor + dim
    cellBorders = Borders.TOP_BOTTOM
    tableBorders = Borders.ALL

    header { style = themeColor + bold }
    footer { style = tertiaryColor + bold }

    init()
}