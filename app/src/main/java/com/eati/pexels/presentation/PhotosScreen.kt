package com.eati.pexels.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import com.eati.pexels.domain.Photo
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.eati.pexels.R
import java.util.*


@Composable
fun PhotosScreen(viewModel: PhotosViewModel) {
    val result by viewModel.photosFlow.collectAsState()

    HomeScreen(result, viewModel::updateResults)
}

@Composable
fun HomeScreen(results: List<Photo>, updateResults: (String) -> Unit) {

    Column {
        Spacer(Modifier.height(16.dp))
        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            updateResults
        )
        PhotoList(results)
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
fun PhotoList(photos: List<Photo>) {

    LazyColumn(modifier = Modifier. padding(10.dp)) {
        items(photos) { message ->
            Card(message)
        }
    }
}

@Composable
fun Card(
    photo: Photo,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.small,
        modifier = modifier
    ) {
        var showPhotoData by remember {
            mutableStateOf(false)
        }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(Modifier.height(16.dp))
                AsyncImage(
                    model = photo.photoUrl,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(88.dp)
                        .clip(CircleShape)
                        .clickable {
                            showPhotoData = !showPhotoData
                        }
                )
                Column(
                    modifier = Modifier.offset(x = 14.dp)
                ) {
                    Text(
                        text = photo.photographer.capitalize(Locale.getDefault()),
                        fontSize = 30.sp,
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 30.dp)
                            .clickable {
                                showPhotoData = !showPhotoData
                            }
                    )
                    GetAutorData(showPhotoData, photo)
                }
            }
        Spacer(Modifier.height(16.dp))
        }
}

@Composable
private fun ColumnScope.GetAutorData(
    showPhotoData: Boolean,
    photo: Photo
) {
    AnimatedVisibility(showPhotoData) {
        Column() {
            GetPhotoDescription(photo)
            GetAvgColor(photo)
            val uriHandler = LocalUriHandler.current
            Row(modifier = Modifier.offset(4.dp)) {
                GetArtistLinkButton(uriHandler, photo)
                Spacer(Modifier.width(16.dp))
                GetPhotoLinkButton(uriHandler, photo)
            }

        }
    }
}

@Composable
private fun GetPhotoLinkButton(
    uriHandler: UriHandler,
    photo: Photo
) {
    Button(onClick = {
        uriHandler.openUri(photo.url)
    }) {
        Text("Photo Link")
    }
}

@Composable
private fun GetArtistLinkButton(
    uriHandler: UriHandler,
    photo: Photo
) {
    Button(onClick = {
        uriHandler.openUri(photo.photographerUrl)
    }) {
        Text("Artist Link")
    }
}

@Composable
private fun GetAvgColor(photo: Photo) {
    Text(
        text = photo.avgColor,
        style = MaterialTheme.typography.body1,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 30.dp)
    )
}

@Composable
private fun GetPhotoDescription(photo: Photo) {
    Text(
        text = photo.alt,
        style = MaterialTheme.typography.body1,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 30.dp)
    )
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    updateResults: (String) -> Unit = {}
) {
    var text by remember {
        mutableStateOf("")
    }
    TextField(
        value = text,
        onValueChange = {
            text = it
            updateResults(text)
        },
        leadingIcon = {
            GetSearchIcon()
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.surface
        ),
        placeholder = {
            Text(stringResource(R.string.placeholder_search))
        },
        modifier = modifier
            .fillMaxWidth()
            .shadow(5.dp, CircleShape)
            .background(Color.White, CircleShape)
            .heightIn(min = 56.dp)
    )
}

@Composable
private fun GetSearchIcon() {
    Icon(
        imageVector = Icons.Default.Search,
        contentDescription = null
    )
}