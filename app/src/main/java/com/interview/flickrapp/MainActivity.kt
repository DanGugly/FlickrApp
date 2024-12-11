package com.interview.flickrapp

import android.os.Bundle
import android.text.Html
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.interview.flickrapp.Utility.Utility.parseImageItem
import com.interview.flickrapp.Utility.Utility.serializeImageItem
import com.interview.flickrapp.model.ImageItem
import com.interview.flickrapp.states.ImageSearchState
import com.interview.flickrapp.ui.theme.FlickrAppTheme
import com.interview.flickrapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.net.URLEncoder

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlickrAppNavHost(viewModel)
        }
    }
}

@Composable
fun FlickrAppNavHost(viewModel: MainViewModel) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "main") {
        composable("main") {
            FlickrSearchApp(viewModel, navController)
        }
        composable("detail/{imageItem}") { backStackEntry ->
            val imageItemJson = backStackEntry.arguments?.getString("imageItem")
            imageItemJson?.let {
                val imageItem = parseImageItem(it) // Deserialize JSON string to ImageItem
                ImageDetailView(imageItem)
            }
        }
    }
}


@Composable
fun FlickrSearchApp(viewModel: MainViewModel, navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(searchQuery) { query ->
            searchQuery = query
            viewModel.searchImages(query)
        }

        when (val state = viewModel.state.collectAsState().value) {
            is ImageSearchState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            is ImageSearchState.Success -> {
                ImageGrid(state.images) { imageItem ->
                    val imageItemJson = serializeImageItem(imageItem)
                    navController.navigate("detail/${URLEncoder.encode(imageItemJson, "UTF-8")}")
                }
            }
            is ImageSearchState.Error -> {
                Text(state.message, color = Color.Red)
            }
        }
    }
}




@Composable
fun SearchBar(query: String, onSearchQueryChanged: (String) -> Unit) {
    TextField(
        value = query,
        onValueChange = { onSearchQueryChanged(it) },
        label = { Text("Search Images") },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun ImageGrid(images: List<ImageItem>, onImageClick:  (ImageItem) -> Unit) {
    LazyColumn {
        items(images) { imageItem ->
            ImageCard(imageItem, onImageClick)
        }
    }
}

@Composable
fun ImageCard(imageItem: ImageItem, onImageClick: (ImageItem) -> Unit) {
    Card(modifier = Modifier.padding(8.dp).clickable { onImageClick(imageItem) }) {
        Column {
            Image(
                painter = rememberImagePainter(imageItem.media.m),
                contentDescription = imageItem.title,
                modifier = Modifier.fillMaxWidth().height(200.dp),
                contentScale = ContentScale.Crop
            )
            Text(text = imageItem.title, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun ImageDetailView(imageItem: ImageItem) {
    Column(modifier = Modifier.padding(16.dp)) {
        Image(
            painter = rememberImagePainter(imageItem.media.m),
            contentDescription = imageItem.title,
            modifier = Modifier.fillMaxWidth().height(300.dp),
            contentScale = ContentScale.Crop
        )
        Text(text = imageItem.title, style = MaterialTheme.typography.bodyLarge)
        Text(text = "Author: ${imageItem.author}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Published: ${imageItem.published}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Description:", style = MaterialTheme.typography.bodyMedium)
        Text(text = Html.fromHtml(imageItem.description, Html.FROM_HTML_MODE_COMPACT).toString(), style = MaterialTheme.typography.bodySmall)
    }
}




