package dev.fabirt.composelab.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.flowlayout.FlowRow
import dev.fabirt.composelab.R
import dev.fabirt.composelab.ui.navigation.pushDestination
import dev.fabirt.composelab.ui.screen.sample.SampleBuilder
import dev.fabirt.composelab.ui.screen.sample.samplesMenu
import dev.fabirt.composelab.util.clearFocus

@Composable
fun HomeScreen(navController: NavController) {
    var viewMode by rememberSaveable {
        mutableStateOf("list")
    }
    var searchText by rememberSaveable {
        mutableStateOf("")
    }
    val menu = remember(key1 = searchText) {
        samplesMenu
            .filter {
                val matchTitle = it.title.contains(searchText, ignoreCase = true)
                if (matchTitle) {
                    return@filter matchTitle
                } else if (!it.tags.isNullOrEmpty()) {
                    return@filter it.tags.any { tag ->
                        tag.contains(searchText, ignoreCase = true)
                    }
                }
                false
            }
    }

    val focusManager = LocalFocusManager.current
    val textInputService = LocalTextInputService.current

    HomeScreenContent(
        menu = menu,
        viewMode = viewMode,
        searchText = searchText,
        onItemClick = {
            clearFocus(focusManager, textInputService)
            navController.pushDestination(it.destination)
        },
        onViewModeClick = { viewMode = it },
        onSearch = { searchText = it },
        onSearchDone = {
            clearFocus(focusManager, textInputService)
        }
    )
}

@Composable
fun HomeScreenContent(
    menu: List<SampleBuilder>,
    onItemClick: (SampleBuilder) -> Unit,
    viewMode: String = "list",
    onViewModeClick: (String) -> Unit,
    searchText: String = "",
    onSearch: ((String) -> Unit)? = null,
    onSearchDone: (() -> Unit)? = null,
) {

    val viewInListMode = viewMode == "list"

    val viewModeIcon =
        if (viewInListMode) R.drawable.ic_round_view_module else R.drawable.ic_round_view_list

    Scaffold(
        topBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = 9.dp)
                    .padding(top = 8.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_app),
                    contentDescription = null,
                    modifier = Modifier.size(48.dp)
                )
                Text(
                    text = "Jetpack Compose Lab",
                    style = MaterialTheme.typography.h6
                )
            }
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier.padding(contentPadding)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(
                    end = 8.dp,
                    start = 16.dp,
                    top = 8.dp
                )
            ) {
                CustomTextField(
                    value = searchText,
                    onValueChange = { onSearch?.invoke(it) },
                    placeholderText = "Search sample",
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(20.dp),
                            tint = MaterialTheme.colors.onSurface.copy(alpha = 0.3f)
                        )
                    },
                    modifier = Modifier
                        .height(40.dp)
                        .weight(1f),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            onSearchDone?.invoke()
                        }
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        onViewModeClick(
                            if (viewInListMode) "module" else "list"
                        )
                    }
                ) {
                    Icon(
                        painter = painterResource(viewModeIcon),
                        contentDescription = "View in $viewMode mode"
                    )
                }
            }

            if (menu.isEmpty()) {
                Text(
                    text = "No results found",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.body1.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            } else if (viewInListMode) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(
                        top = 16.dp,
                        bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                    )
                ) {
                    items(menu, key = { it.title }) { sample ->
                        SampleListItem(sample) {
                            onItemClick(sample)
                        }
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.weight(1f),
                    contentPadding = WindowInsets.navigationBars.asPaddingValues()
                ) {
                    items(menu, key = { it.title }) { sample ->
                        SampleGridItem(sample) {
                            onItemClick(sample)
                        }
                    }
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SampleListItem(
    sample: SampleBuilder,
    onClick: () -> Unit
) {
    ListItem(
        modifier = Modifier.clickable(onClick = onClick),
        trailing = {
            Icon(
                imageVector = Icons.Rounded.KeyboardArrowRight,
                contentDescription = "Go to ${sample.title}"
            )
        },
        secondaryText = if (sample.tags.isNullOrEmpty()) null else {
            {
                FlowRow(
                    mainAxisSpacing = 4.dp,
                    crossAxisSpacing = 4.dp,
                    modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                ) {
                    sample.tags.map { tag ->
                        Surface(
                            color = Color(0xFFF0F0F0),
                            shape = MaterialTheme.shapes.small.copy(CornerSize(percent = 50)),
                        ) {
                            Text(
                                text = tag,
                                style = MaterialTheme.typography.caption,
                                modifier = Modifier.padding(
                                    horizontal = 8.dp,
                                    vertical = 4.dp
                                )
                            )
                        }
                    }
                }
            }
        },
    ) {
        Text(text = sample.title)
    }
}

@Composable
fun SampleGridItem(
    sample: SampleBuilder,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Image(
            painter = painterResource(sample.thumbnailResId ?: R.drawable.compose_gray),
            contentDescription = null,
            modifier = Modifier
                .size(76.dp)
                .clip(MaterialTheme.shapes.small)
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = sample.title)
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreenContent(
        menu = samplesMenu,
        viewMode = "list",
        onItemClick = { },
        onViewModeClick = { }
    )
}

@Composable
private fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    placeholderText: String = "Placeholder",
    fontSize: TextUnit = MaterialTheme.typography.body2.fontSize,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    BasicTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        cursorBrush = SolidColor(MaterialTheme.colors.primary),
        textStyle = LocalTextStyle.current.copy(
            color = MaterialTheme.colors.onSurface,
            fontSize = fontSize
        ),
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF0F0F0), MaterialTheme.shapes.medium)
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (leadingIcon != null) leadingIcon()
                Box(Modifier.weight(1f)) {
                    if (value.isEmpty()) Text(
                        placeholderText,
                        style = LocalTextStyle.current.copy(
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f),
                            fontSize = fontSize
                        )
                    )
                    innerTextField()
                }
                if (trailingIcon != null) trailingIcon()
            }
        }
    )
}