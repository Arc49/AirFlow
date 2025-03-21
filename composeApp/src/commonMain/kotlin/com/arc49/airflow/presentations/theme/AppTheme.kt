package com.arc49.airflow.presentations.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveTheme
import io.github.alexzhirkevich.cupertino.adaptive.CupertinoThemeSpec
import io.github.alexzhirkevich.cupertino.adaptive.ExperimentalAdaptiveApi
import io.github.alexzhirkevich.cupertino.adaptive.MaterialThemeSpec
import io.github.alexzhirkevich.cupertino.adaptive.Theme
import io.github.alexzhirkevich.cupertino.theme.darkColorScheme
import io.github.alexzhirkevich.cupertino.theme.lightColorScheme

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun AppTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    theme: Theme = determineTheme(),
    content: @Composable () -> Unit
) {
    AdaptiveTheme(
        material = MaterialThemeSpec.Default(colorScheme = if (useDarkTheme) androidx.compose.material3.darkColorScheme() else androidx.compose.material3.lightColorScheme()), cupertino = CupertinoThemeSpec.Default(colorScheme = if(useDarkTheme) darkColorScheme() else lightColorScheme()),
        target = theme,
        content = content
    )
}