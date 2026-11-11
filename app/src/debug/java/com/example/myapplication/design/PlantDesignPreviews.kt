package com.example.myapplication.design

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R

// Lab 2 design only: fixtures are static and button callbacks intentionally do nothing.
private val lightColors = lightColorScheme(
    primary = Color(0xFF2E6B45), onPrimary = Color.White,
    background = Color(0xFFF6F8F4), onBackground = Color(0xFF1D2B20),
    surface = Color.White, onSurface = Color(0xFF1D2B20),
    onSurfaceVariant = Color(0xFF536457), outline = Color(0xFFD2DCD1),
    primaryContainer = Color(0xFFE7F1E6), onPrimaryContainer = Color(0xFF2E6B45),
    error = Color(0xFF9D3933), errorContainer = Color(0xFFFCE9E7),
)

private val darkColors = darkColorScheme(
    primary = Color(0xFFA4D5A6), onPrimary = Color(0xFF143821),
    background = Color(0xFF151C17), onBackground = Color(0xFFE4EDE3),
    surface = Color(0xFF1F2922), onSurface = Color(0xFFE4EDE3),
    onSurfaceVariant = Color(0xFFB4C3B4), outline = Color(0xFF465448),
    primaryContainer = Color(0xFF253E2C), onPrimaryContainer = Color(0xFFA4D5A6),
    error = Color(0xFFFFB4AB), errorContainer = Color(0xFF4B2624),
)

private val designTypography = Typography(
    titleLarge = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Medium),
    headlineSmall = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.Medium),
    titleMedium = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Medium),
    bodyLarge = TextStyle(fontSize = 16.sp),
    bodyMedium = TextStyle(fontSize = 14.sp),
    bodySmall = TextStyle(fontSize = 12.sp),
    labelLarge = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium),
    labelMedium = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Medium),
)

@Composable
private fun DesignTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) darkColors else lightColors,
        typography = designTypography,
        content = content,
    )
}

@Composable
private fun LeafSymbol(modifier: Modifier = Modifier) {
    val color = MaterialTheme.colorScheme.primary
    Canvas(modifier.background(MaterialTheme.colorScheme.primaryContainer, CircleShape)) {
        val leaf = Path().apply {
            moveTo(size.width * .30f, size.height * .69f)
            cubicTo(size.width * .17f, size.height * .32f,
                size.width * .52f, size.height * .24f, size.width * .75f, size.height * .24f)
            cubicTo(size.width * .77f, size.height * .58f,
                size.width * .55f, size.height * .79f, size.width * .30f, size.height * .69f)
            close()
        }
        drawPath(leaf, color, style = Stroke(width = 2.dp.toPx()))
        drawLine(color, androidx.compose.ui.geometry.Offset(size.width * .26f, size.height * .76f),
            androidx.compose.ui.geometry.Offset(size.width * .63f, size.height * .38f), 2.dp.toPx())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DesignPage(title: String, back: Boolean = false, edit: Boolean = false,
    add: Boolean = false, content: @Composable (PaddingValues) -> Unit) {
    val backLabel = stringResource(R.string.design_back)
    val addLabel = stringResource(R.string.design_add)
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text(title, fontSize = if (back) 20.sp else 24.sp,
                    fontWeight = FontWeight.Medium, maxLines = 1) },
                navigationIcon = { if (back) IconButton(onClick = {},
                    modifier = Modifier.semantics { contentDescription = backLabel }) {
                    Text("←", fontSize = 26.sp)
                } },
                actions = { if (edit) TextButton(onClick = {}) {
                    Text(stringResource(R.string.design_edit))
                } },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background),
            )
        },
        floatingActionButton = { if (add) FloatingActionButton(
            onClick = {}, shape = RoundedCornerShape(16.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.semantics { contentDescription = addLabel },
        ) { Text("+", fontSize = 30.sp) } },
        content = content,
    )
}

@Composable
private fun StatusLabel(kind: Int) {
    val dark = isSystemInDarkTheme()
    val foreground = when (kind) {
        -1 -> MaterialTheme.colorScheme.error
        0 -> if (dark) Color(0xFFF0C77B) else Color(0xFF8A5B16)
        else -> MaterialTheme.colorScheme.primary
    }
    val background = when (kind) {
        -1 -> MaterialTheme.colorScheme.errorContainer
        0 -> if (dark) Color(0xFF45371F) else Color(0xFFFFF1D5)
        else -> MaterialTheme.colorScheme.primaryContainer
    }
    val textId = when (kind) {
        -1 -> R.string.design_overdue_status
        0 -> R.string.design_today_status
        else -> R.string.design_future_status
    }
    Text(stringResource(textId), color = foreground, style = MaterialTheme.typography.labelMedium,
        modifier = Modifier.background(background, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp))
}

@Composable
private fun PlantRow(name: String, date: String, kind: Int) {
    Surface(onClick = {}, shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.surface) {
        Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically) {
            LeafSymbol(Modifier.size(48.dp))
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(name, style = MaterialTheme.typography.titleMedium,
                    maxLines = 2, overflow = TextOverflow.Ellipsis)
                Text(stringResource(R.string.design_next_on, date),
                    style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                StatusLabel(kind)
            }
        }
    }
}

@Composable
private fun PlantListDesign(empty: Boolean = false) {
    DesignPage(stringResource(R.string.design_list_title), add = !empty) { insets ->
        if (empty) {
            Column(Modifier.fillMaxSize().padding(insets).padding(32.dp),
                verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                LeafSymbol(Modifier.size(88.dp))
                Spacer(Modifier.height(24.dp))
                Text(stringResource(R.string.design_empty_title), style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(12.dp))
                Text(stringResource(R.string.design_empty_hint), color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                Spacer(Modifier.height(24.dp))
                PrimaryButton(stringResource(R.string.design_add))
            }
        } else Column(Modifier.padding(insets).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(stringResource(R.string.design_upcoming), style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary)
            Text(stringResource(R.string.design_list_hint), style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(0.dp))
            PlantRow("Фикус", stringResource(R.string.design_overdue_date), -1)
            PlantRow("Монстера", stringResource(R.string.design_today_date), 0)
            PlantRow("Сансевиерия", stringResource(R.string.design_future_date), 1)
        }
    }
}

@Composable
private fun PrimaryButton(label: String) {
    Button(onClick = {}, modifier = Modifier.fillMaxWidth().height(52.dp),
        shape = RoundedCornerShape(16.dp)) { Text(label) }
}

@Composable
private fun PlantDetailsDesign() {
    DesignPage(stringResource(R.string.design_details_title), back = true, edit = true) { insets ->
        Column(Modifier.padding(insets).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)) {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)) {
                LeafSymbol(Modifier.size(88.dp))
                Text("Монстера", style = MaterialTheme.typography.headlineSmall)
            }
            Surface(shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.primaryContainer) {
                Column(Modifier.fillMaxWidth().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(stringResource(R.string.design_next), color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(stringResource(R.string.design_today_date), style = MaterialTheme.typography.titleLarge)
                    StatusLabel(0)
                }
            }
            Surface(shape = RoundedCornerShape(16.dp)) {
                Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    DetailValue(stringResource(R.string.design_interval), stringResource(R.string.design_every_seven))
                    DetailValue(stringResource(R.string.design_last), stringResource(R.string.design_last_date))
                    DetailValue(stringResource(R.string.design_note), stringResource(R.string.design_sample_note))
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                PrimaryButton(stringResource(R.string.design_water))
                TextButton(onClick = {}, modifier = Modifier.fillMaxWidth().height(48.dp),
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)) {
                    Text(stringResource(R.string.design_delete))
                }
            }
        }
    }
}

@Composable
private fun DetailValue(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun PlantEditorDesign(error: Boolean = false) {
    DesignPage(stringResource(R.string.design_editor_title), back = true) { insets ->
        Column(Modifier.padding(insets).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(stringResource(R.string.design_editor_hint), style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            DesignField(stringResource(R.string.design_name), "", stringResource(R.string.design_name_hint),
                if (error) stringResource(R.string.design_name_error) else null)
            DesignField(stringResource(R.string.design_days_label), if (error) "0" else "7", "",
                if (error) stringResource(R.string.design_days_error) else null)
            DesignField(stringResource(R.string.design_last), stringResource(R.string.design_today_date), "", date = true)
            DesignField(stringResource(R.string.design_note_label), "", stringResource(R.string.design_note_hint),
                multiline = true)
            Spacer(Modifier.height(8.dp))
            PrimaryButton(stringResource(R.string.design_save))
        }
    }
}

@Composable
private fun DesignField(label: String, value: String, hint: String,
    error: String? = null, multiline: Boolean = false, date: Boolean = false) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        OutlinedTextField(value = value, onValueChange = {},
            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp),
            placeholder = { Text(hint, style = MaterialTheme.typography.bodyMedium) },
            textStyle = MaterialTheme.typography.bodyLarge,
            readOnly = date,
            trailingIcon = if (!date) null else { { CalendarSymbol() } },
            isError = error != null, singleLine = !multiline, minLines = if (multiline) 3 else 1,
            supportingText = if (error == null) null else { { Text(error) } })
    }
}

@Composable
private fun CalendarSymbol() {
    val color = MaterialTheme.colorScheme.onSurfaceVariant
    Canvas(Modifier.size(24.dp)) {
        val stroke = 1.5.dp.toPx()
        drawRect(color, androidx.compose.ui.geometry.Offset(size.width * .1f, size.height * .2f),
            androidx.compose.ui.geometry.Size(size.width * .8f, size.height * .7f), style = Stroke(stroke))
        drawLine(color, androidx.compose.ui.geometry.Offset(size.width * .1f, size.height * .4f),
            androidx.compose.ui.geometry.Offset(size.width * .9f, size.height * .4f), stroke)
        for (x in listOf(.3f, .7f)) drawLine(color,
            androidx.compose.ui.geometry.Offset(size.width * x, size.height * .1f),
            androidx.compose.ui.geometry.Offset(size.width * x, size.height * .3f), stroke)
    }
}

@Preview(name = "List RU", locale = "ru", widthDp = 360, heightDp = 752, showSystemUi = true)
@Preview(name = "List EN", locale = "en", widthDp = 360, heightDp = 752, showSystemUi = true)
@Preview(name = "List dark", locale = "ru", uiMode = Configuration.UI_MODE_NIGHT_YES,
    widthDp = 360, heightDp = 752, showSystemUi = true)
@Composable
private fun ListPreview() = DesignTheme { PlantListDesign() }

@Preview(name = "Details RU", locale = "ru", widthDp = 360, heightDp = 752, showSystemUi = true)
@Preview(name = "Details EN", locale = "en", widthDp = 360, heightDp = 752, showSystemUi = true)
@Preview(name = "Details dark", locale = "ru", uiMode = Configuration.UI_MODE_NIGHT_YES,
    widthDp = 360, heightDp = 752, showSystemUi = true)
@Composable
private fun DetailsPreview() = DesignTheme { PlantDetailsDesign() }

@Preview(name = "Editor RU", locale = "ru", widthDp = 360, heightDp = 752, showSystemUi = true)
@Preview(name = "Editor EN", locale = "en", widthDp = 360, heightDp = 752, showSystemUi = true)
@Preview(name = "Editor dark", locale = "ru", uiMode = Configuration.UI_MODE_NIGHT_YES,
    widthDp = 360, heightDp = 752, showSystemUi = true)
@Composable
private fun EditorPreview() = DesignTheme { PlantEditorDesign() }

@Preview(name = "Empty list", locale = "ru", widthDp = 360, heightDp = 752, showSystemUi = true)
@Composable
private fun EmptyListPreview() = DesignTheme { PlantListDesign(empty = true) }

@Preview(name = "Invalid fields", locale = "ru", widthDp = 360, heightDp = 752, showSystemUi = true)
@Composable
private fun InvalidEditorPreview() = DesignTheme { PlantEditorDesign(error = true) }
