package com.babe.fata.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffold
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberSupportingPaneScaffoldNavigator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil3.compose.AsyncImage
import com.babe.fata.HomeScreen
import com.babe.fata.model.Movie
import com.babe.fata.ui.viewmodel.MoviesViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject



@Composable
fun MainNavHost2() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "VideoScreen") {
        composable("VideoScreen") {
            VideoScreen(
                onSignInClicked = { /*TODO*/ },
                navController = navController
            )
        }

        composable(
            route = "VoirPlusScreen/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { backStack ->
            val movieId = backStack.arguments?.getInt("movieId")!!
            val vm = object : KoinComponent {
                val mv: MoviesViewModel by inject()
                val viewModel get() = mv
            }.viewModel

            LaunchedEffect(movieId) {
                if (vm.movieDetail?.id != movieId) {
                    vm.loadMovieDetail(movieId)
                }
            }

            val detail by remember { derivedStateOf { vm.movieDetail } }

            when {
                detail == null -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                else -> {
                    AdaptedSupportingPaneScaffold(
                        movie = detail!!,
                        navController = navController
                    )
                }
            }
        }

        // Nouvelle définition avec argument movieId
        composable(
            route = "MovieCreditsScreen/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId")!!

            // Injection du ViewModel
            val vm = object : KoinComponent {
                val mv: MoviesViewModel by inject()
                val viewModel get() = mv
            }.viewModel

            // Chargement des crédits
            LaunchedEffect(movieId) {
                vm.loadMovieCredits(movieId)
            }

            // Récupération du state
            val credits by remember { derivedStateOf { vm.movieCredits } }
            val isLoading by remember { derivedStateOf { vm.isLoading } }
            val castDetail by remember { derivedStateOf { vm.castDetails } }

            // Affichage
            MovieNavGraph(
                credits = credits,
                isLoading = isLoading,

            )
        }
    }
}


@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun VideoScreen(
    onSignInClicked: () -> Unit = {},
    onMovieClick: (Movie) -> Unit = {},
    navController : NavController
) {






    // Récupération du ViewModel via Koin
    val vm = remember {
        object : KoinComponent {
            val mv: MoviesViewModel by inject()
            val viewModel get() = mv
        }
    }.viewModel

    // États locaux
    var selectedMovie by remember { mutableStateOf<Movie?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var currentPage by remember { mutableStateOf(1) }

    // Navigator pour les panneaux
    val navigator = rememberSupportingPaneScaffoldNavigator()

    // Chargement initial des données
    LaunchedEffect(Unit) {
        vm.loadAll(currentPage)
    }

    SupportingPaneScaffold(
        value = navigator.scaffoldValue,
        directive = navigator.scaffoldDirective,
        mainPane = {
            AnimatedPane {
                Column(Modifier.fillMaxSize()) {
                    // Barre de recherche
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { query ->
                            searchQuery = query
                            currentPage = 1
                            vm.searchMovies(query, currentPage)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clip(RoundedCornerShape(50.dp)),
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Search, contentDescription = "Recherche")
                        },
                        placeholder = { Text("Rechercher un film...") }
                        , maxLines = 1,

                    )

                    Box(Modifier.fillMaxSize()) {
                        if (vm.isLoading) {
                            CircularProgressIndicator(Modifier.align(Alignment.Center))
                        } else {
                            if (searchQuery.isNotBlank()) {
                                Section(
                                    title = "Résultats de recherche",
                                    movies = vm.searchResults,
                                    onMovieClick = { movie ->
                                        selectedMovie = movie
                                        navigator.navigateTo(SupportingPaneScaffoldRole.Supporting)
                                    }
                                )

                                // Pagination simple
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    TextButton(
                                        enabled = currentPage > 1,
                                        onClick = {
                                            currentPage--
                                            vm.searchMovies(searchQuery, currentPage)
                                        }
                                    ) { Text("Précédent") }

                                    Text("Page $currentPage")

                                    TextButton(
                                        enabled = vm.searchResults.isNotEmpty(),
                                        onClick = {
                                            currentPage++
                                            vm.searchMovies(searchQuery, currentPage)
                                        }
                                    ) { Text("Suivant") }
                                }
                            } else {
                                LazyColumn(
                                    modifier = Modifier
                                        .padding(vertical = 8.dp)
                                        .fillMaxSize()
                                ) {
                                    item {
                                        Section(
                                            title = "Populaires",
                                            movies = vm.popularMovies,
                                            onMovieClick = { movie ->
                                                selectedMovie = movie
                                                navigator.navigateTo(SupportingPaneScaffoldRole.Supporting)
                                            }
                                        )
                                    }
                                    item {
                                        Section(
                                            title = "Maintenant à l'affiche",
                                            movies = vm.nowPlayingMovies,
                                            onMovieClick = { movie ->
                                                selectedMovie = movie
                                                navigator.navigateTo(SupportingPaneScaffoldRole.Supporting)
                                            }
                                        )
                                    }
                                    item {
                                        Section(
                                            title = "À venir",
                                            movies = vm.upcomingMovies,
                                            onMovieClick = { movie ->
                                                selectedMovie = movie
                                                navigator.navigateTo(SupportingPaneScaffoldRole.Supporting)
                                            }
                                        )
                                    }
                                    item {
                                        Section(
                                            title = "Bien notées",
                                            movies = vm.topRatedMovies,
                                            onMovieClick = { movie ->
                                                selectedMovie = movie
                                                navigator.navigateTo(SupportingPaneScaffoldRole.Supporting)
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        supportingPane = {
            AnimatedPane {
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(16.dp)
                ) {
                    if (navigator.scaffoldValue[SupportingPaneScaffoldRole.Main] == PaneAdaptedValue.Hidden) {
                        IconButton(onClick = { navigator.navigateBack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Retour"
                            )
                        }
                    }

                    Box(Modifier.fillMaxSize()) {
                        selectedMovie?.let { movie ->
                            MovieDetail(movie, navController = navController)
                        } ?: run {
                            Text(
                                text = "Sélectionnez un film",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun Section(
    title: String,
    movies: List<Movie>,
    onMovieClick: (Movie) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(movies) { movie ->
                MovieCard(movie, onClick = { onMovieClick(movie) })
            }
        }
    }
}

@Composable
fun MovieCard(
    movie: Movie,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .size(width = 140.dp, height = 220.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Image with gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.dp)
                    .weight(1f)
            ) {
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w300${movie.posterPath}",
                    contentDescription = movie.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                            )
                        )
                )
            }

            // Title and release date
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = movie.releaseDate.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun MovieDetail(movie: Movie,navController: NavController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500${movie.posterPath}",
            contentDescription = movie.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Crop
        )
        Text(
            text = movie.title,
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Date de sortie : ${movie.releaseDate}",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Note : ${movie.voteAverage} (${movie.voteCount} votes)",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = "Langue originale : ${movie.originalLanguage?.uppercase()}",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = movie.overview,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            navController.navigate("VoirPlusScreen/${movie.id}")
        }) {
            Text("En savoir plus")
        }

    }
}

