package com.paraskcd.spotlightsearch.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Bars: ImageVector
    get() {
        if (_Bars2 != null) return _Bars2!!

        _Bars2 = ImageVector.Builder(
            name = "Bars2",
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
                moveTo(3.75f, 9f)
                horizontalLineTo(20.25f)
                moveTo(3.75f, 15.75f)
                horizontalLineTo(20.25f)
            }
        }.build()

        return _Bars2!!
    }

private var _Bars2: ImageVector? = null

