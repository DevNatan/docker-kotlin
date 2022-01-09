import io.kotest.core.config.*

/**
 * Kotest project level configuration.
 * @see <a href="https://kotest.io/docs/framework/project-config.html">Project Level Config</a>
 */
@Suppress("UNUSED")
object ProjectConfig : AbstractProjectConfig() {

    override val testNameRemoveWhitespace = true

}