import io.nacular.doodle.application.Application
import io.nacular.doodle.application.Modules
import io.nacular.doodle.application.application
import io.nacular.doodle.controls.SimpleMutableListModel
import io.nacular.doodle.controls.itemVisualizer
import io.nacular.doodle.controls.list.VerticalDynamicList
import io.nacular.doodle.controls.panels.ScrollPanel
import io.nacular.doodle.controls.text.Label
import io.nacular.doodle.controls.text.TextField
import io.nacular.doodle.core.Display
import io.nacular.doodle.geometry.Size
import io.nacular.doodle.layout.constraints.constrain
import io.nacular.doodle.layout.constraints.fill
import io.nacular.doodle.theme.ThemeManager
import io.nacular.doodle.theme.adhoc.DynamicTheme
import io.nacular.doodle.theme.basic.BasicTheme
import io.nacular.doodle.theme.basic.list.basicVerticalListBehavior
import io.nacular.doodle.theme.native.NativeTheme
import io.nacular.doodle.utils.Dimension
import org.kodein.di.instance

fun main() {
    val themes = listOf(
        BasicTheme.basicButtonBehavior(),
        BasicTheme.basicLabelBehavior(),
        NativeTheme.nativeTextFieldBehavior(),
        BasicTheme.basicSwitchBehavior(),
        NativeTheme.nativeScrollPanelBehavior(),
        BasicTheme.basicListBehavior()
    )
    val controls = listOf(Modules.PointerModule, Modules.ImageModule, Modules.FontModule)
    val allModules = themes + controls

    application(modules = allModules) {
        MainApp(
            display = instance(),
            manager = instance(),
            theme = instance()
        )
    }
}

class MainApp(
    display: Display,
    manager: ThemeManager,
    theme: DynamicTheme
) : Application {

    init {

        manager.selected = theme

        val list = SimpleMutableListModel<String>()
        val mList = (0..40).map { "Country: $it" }
        list.addAll(mList)

        val dy = VerticalDynamicList(
            list,
            itemVisualizer = itemVisualizer { item, previous, context ->
                if (context.index % 2 == 0){
                    TextField(item).apply {
                        size = Size(700, 70)
                    }
                }else Label(item).apply {
                    size = Size(200, 60)
                }
            },
            fitContent = setOf(Dimension.Width)
        ).apply {
            acceptsThemes = false
            behavior      = basicVerticalListBehavior(itemHeight = 50.0)
            this.cellAlignment = fill
        }
        display += ScrollPanel(dy).apply {
            contentWidthConstraints = { it eq parent.width }
        }
        display.layout = constrain(display.children[0]) {
            it.width   eq parent.width
            it.height  eq parent.height
            it.centerX eq parent.centerX
        }

    }


    override fun shutdown() {}

}