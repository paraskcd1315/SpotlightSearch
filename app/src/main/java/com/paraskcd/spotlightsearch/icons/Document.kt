package com.paraskcd.spotlightsearch.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Document: ImageVector
    get() {
        if (_Document != null) return _Document!!

        _Document = ImageVector.Builder(
            name = "Document",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF0F172A)),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(19.5f, 14.25f)
                verticalLineTo(11.625f)
                curveTo(19.5f, 9.76104f, 17.989f, 8.25f, 16.125f, 8.25f)
                horizontalLineTo(14.625f)
                curveTo(14.0037f, 8.25f, 13.5f, 7.74632f, 13.5f, 7.125f)
                verticalLineTo(5.625f)
                curveTo(13.5f, 3.76104f, 11.989f, 2.25f, 10.125f, 2.25f)
                horizontalLineTo(8.25f)
                moveTo(10.5f, 2.25f)
                horizontalLineTo(5.625f)
                curveTo(5.00368f, 2.25f, 4.5f, 2.75368f, 4.5f, 3.375f)
                verticalLineTo(20.625f)
                curveTo(4.5f, 21.2463f, 5.00368f, 21.75f, 5.625f, 21.75f)
                horizontalLineTo(18.375f)
                curveTo(18.9963f, 21.75f, 19.5f, 21.2463f, 19.5f, 20.625f)
                verticalLineTo(11.25f)
                curveTo(19.5f, 6.27944f, 15.4706f, 2.25f, 10.5f, 2.25f)
                close()
            }
        }.build()

        return _Document!!
    }

private var _Document: ImageVector? = null