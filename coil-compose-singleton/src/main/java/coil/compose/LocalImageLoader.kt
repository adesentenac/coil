@file:Suppress("NOTHING_TO_INLINE", "unused")

package coil.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.ImagePainter.ExecuteCallback
import coil.imageLoader
import coil.request.ImageRequest

/**
 * Return an [ImagePainter] that will execute an [ImageRequest] using [LocalImageLoader].
 *
 * @param data The [ImageRequest.data] to load.
 * @param onExecute Called immediately before the [ImagePainter] launches an image request.
 *  Return 'true' to proceed with the request. Return 'false' to skip executing the request.
 * @param builder An optional lambda to configure the request.
 * @param filterQuality The [FilterQuality] to use when drawing a bitmap.
 */
@Composable
inline fun rememberImagePainter(
    data: Any?,
    onExecute: ExecuteCallback = ExecuteCallback.Lazy,
    builder: ImageRequest.Builder.() -> Unit = {},
    filterQuality: FilterQuality = FilterQuality.Low,
): ImagePainter = rememberImagePainter(data, LocalImageLoader.current, onExecute, builder, filterQuality)

/**
 * Return an [ImagePainter] that will execute the [request] using [LocalImageLoader].
 *
 * @param request The [ImageRequest] to execute.
 * @param onExecute Called immediately before the [ImagePainter] launches an image request.
 *  Return 'true' to proceed with the request. Return 'false' to skip executing the request.
 * @param filterQuality The [FilterQuality] to use when drawing a bitmap.
 */
@Composable
inline fun rememberImagePainter(
    request: ImageRequest,
    onExecute: ExecuteCallback = ExecuteCallback.Lazy,
    filterQuality: FilterQuality = FilterQuality.Low,
): ImagePainter = rememberImagePainter(request, LocalImageLoader.current, onExecute, filterQuality)

/**
 * A pseudo-[CompositionLocal] that returns the current [ImageLoader] for the composition.
 * If a local [ImageLoader] has not been provided, it returns the singleton [ImageLoader].
 */
val LocalImageLoader = ImageLoaderProvidableCompositionLocal()

/** @see LocalImageLoader */
@JvmInline
value class ImageLoaderProvidableCompositionLocal internal constructor(
    private val delegate: ProvidableCompositionLocal<ImageLoader?> = staticCompositionLocalOf { null }
) {

    val current: ImageLoader
        @Composable get() = delegate.current ?: LocalContext.current.imageLoader

    infix fun provides(value: ImageLoader) = delegate provides value

    infix fun providesDefault(value: ImageLoader) = delegate providesDefault value
}
