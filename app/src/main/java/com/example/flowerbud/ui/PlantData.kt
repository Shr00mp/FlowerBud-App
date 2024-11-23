package com.example.flowerbud.ui

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.example.flowerbud.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

data class Plant(
    val name: String,
    val image: Int,
    val price: Int,
    val waterWeek: Int,
    val space: Int,
    val light: Int,
    val toxic: Boolean,
    val outdoor: Boolean,
    val lightImage: Int,
    val priceImage: Int,
    val waterImage: Int,
    val plantId: String,
    val plantPrice: String,
    val plantSun: String,
    val height: String,
    val description: String,
    val commonIssues: String,
    val issueSolutions: String,
)

val allPlants: List<Plant> = listOf(
    Plant("Aloe Vera", R.drawable.aloevera, 10,4, 2,3, true,false,
        R.drawable.sunicon, R.drawable.poundicon, R.drawable.watericon, "1", "1 - 10", "High", "30–60 cm",
        "A popular houseplant that thrives indoors with minimal care. Aloe Vera prefers bright, indirect light and needs occasional watering, making it perfect for those seeking a low-maintenance, medicinal plant.",
        "Aloe Vera often experiences root rot and mushy leaves due to overwatering. It can also suffer from pale or weak leaves when exposed to insufficient sunlight or cold drafts.",
        "To prevent issues, water Aloe Vera sparingly, allowing the soil to dry out completely between waterings. Place it in bright, indirect sunlight and protect it from cold temperatures or drafts."),
    Plant("Areca Palm", R.drawable.arecapalm,30,1,5,2,false,true,
        R.drawable.cloudsunicon, R.drawable.triplepoundicon, R.drawable.triplewatericon,"2", "20 - 30", "Medium", "90–180 cm",
        "This indoor palm adds a tropical touch with its lush, feathery fronds. Areca Palms are great air purifiers and thrive in bright, indirect light, requiring moderate watering to keep their foliage vibrant.",
        "Areca Palm commonly has browning leaf tips caused by low humidity, underwatering, or nutrient deficiencies. The leaves may also scorch if the plant is exposed to direct sunlight for too long.",
        "Increase humidity around the plant, water regularly but avoid waterlogging, and feed with a balanced fertilizer during the growing season. Place in bright, indirect light to prevent leaf scorch."),
    Plant("Zanzibar Gem", R.drawable.zzplant,20,3,4,1,true,false,
        R.drawable.cloudicon, R.drawable.doublepoundicon, R.drawable.doublewatericon,"3", "10 - 20", "Low", "40–90 cm",
        "A nearly indestructible houseplant that tolerates low light and infrequent watering. The ZZ Plant's shiny, deep green leaves make it a stylish addition to any indoor space, especially for beginners.",
        "ZZ Plant often suffers from yellowing leaves due to overwatering or poor drainage. While it tolerates low light, insufficient light can slow its growth, making the stems leggy and weak.",
        "Water only when the soil is dry, and ensure the pot has good drainage. Place the ZZ Plant in moderate to low indirect light, and prune leggy stems to encourage fuller growth."),
    Plant("Spider Plant", R.drawable.spiderplant,10,1,2,3,false,false,
        R.drawable.sunicon, R.drawable.poundicon, R.drawable.triplewatericon,"4", "1 - 10", "High", "30–60 cm",
        "An easy-to-care-for indoor plant that adapts well to different conditions. Spider Plants thrive in bright, indirect light and produce “babies” that can be propagated, making them ideal for hanging baskets.",
        "Spider Plants frequently develop brown leaf tips from fluoride in tap water or dry air. Overwatering can also cause root rot, while too much direct sunlight may scorch the leaves.",
        "Use distilled or rainwater to avoid fluoride, and increase humidity if air is dry. Water moderately, allowing the soil to dry slightly between waterings, and provide bright, indirect light."),
    Plant("Peace Lily", R.drawable.peacelily,10,1,2,3,true,false,
        R.drawable.sunicon, R.drawable.poundicon, R.drawable.triplewatericon,"5", "1 - 10", "High", "45–90 cm",
        "A classic houseplant with white blooms that add elegance to any room. Peace Lilies thrive in low light and prefer moist soil, making them a favorite for darker corners and humid areas like bathrooms.",
        "Peace Lilies show drooping leaves and yellowing when underwatered. Brown leaf tips are often due to dry air or chemicals in tap water, while too much light can cause leaf burn.",
        "Keep soil consistently moist, but not soggy. Use filtered water to avoid chemicals and increase humidity around the plant. Place the Peace Lily in low to moderate indirect light for optimal growth."),
    Plant("Pansy Orchid", R.drawable.pansyorchid,20,1,2,3,false,false,
        R.drawable.sunicon, R.drawable.doublepoundicon, R.drawable.triplewatericon,"6", "10 - 20", "High", "30–60 cm",
        "This orchid variety brings unique beauty indoors with its colorful, pansy-like flowers. It prefers bright, indirect light and consistent watering, making it a charming but slightly demanding choice for orchid enthusiasts.",
        "Pansy Orchids are sensitive to temperature fluctuations and low humidity, leading to leaf problems. Overwatering can result in root rot, while lack of light may cause poor flowering and growth.",
        "Maintain a stable temperature and increase humidity with a humidifier or misting. Water only when the top inch of soil is dry, and provide bright, indirect light for healthy growth."),
    Plant("Succulent", R.drawable.succulent,10,3,1,2,true,false,
        R.drawable.cloudsunicon, R.drawable.poundicon, R.drawable.doublewatericon,"7", "1 - 10", "Medium", "5–30 cm",
        "Perfect for sunny indoor spaces, succulents are drought-tolerant and require minimal care. Their diverse shapes and colors add visual interest to any room, with occasional watering needed for healthy growth.",
        "Succulents often face root rot due to overwatering. Insufficient light can cause stretching, while very dry conditions may result in wrinkled or shriveled leaves, affecting the plant's appearance and growth.",
        "Water sparingly, allowing the soil to dry out completely between waterings. Place the plant in bright, direct light for at least six hours daily, and ensure the pot has adequate drainage."),
    Plant("Bonsai", R.drawable.bonsai,20,1,1,2,false,true,
        R.drawable.cloudsunicon, R.drawable.doublepoundicon, R.drawable.triplewatericon,"8", "10 - 20", "Medium", "15–90 cm",
        "A captivating houseplant that brings nature’s grandeur indoors in miniature form. Bonsai trees require precise care, including pruning and regular watering, making them ideal for plant enthusiasts seeking a challenge.",
        "Bonsai trees are prone to root rot from overwatering or leaf drop from underwatering. They can also experience leaf burn from direct sunlight, and poor pruning may lead to uneven growth.",
        "Water Bonsai regularly, keeping the soil slightly moist but not soggy. Provide filtered light or partial shade, and prune regularly to maintain shape. Use well-draining soil to prevent waterlogging."),
    Plant("Maranta", R.drawable.maranta,10,2,2,3,false,false,
        R.drawable.sunicon, R.drawable.poundicon, R.drawable.doublewatericon,"9", "1 - 10", "High", "30–45 cm",
        "Also known as the Prayer Plant, Maranta adds a dynamic touch to indoor décor with its colorful, folding leaves. It prefers indirect light and consistent moisture, thriving in humid indoor environments.",
        "Maranta may develop brown leaf tips or edges from low humidity or watering issues. The plant can also suffer from root rot due to overwatering and struggle in bright, direct sunlight.",
        "Increase humidity by misting or using a pebble tray. Water when the top inch of soil is dry, and place the plant in low to moderate indirect light for optimal health."),
    Plant("Chinese Evergreen", R.drawable.chineseevergreen,10,1,2,2,true,false,
        R.drawable.cloudsunicon, R.drawable.poundicon, R.drawable.triplewatericon,"10", "1 - 10", "Medium", "30–90 cm",
        "A versatile houseplant that can tolerate low light and infrequent watering. Its colorful, variegated foliage makes it a great decorative choice for indoor spaces, even for those with little plant care experience.",
        "Chinese Evergreens are susceptible to root rot from overwatering and may develop brown leaf tips in dry air. They can also show leaf yellowing or scorched leaves if exposed to direct sunlight.",
        "Water moderately, allowing the soil to dry slightly between waterings, and increase humidity if necessary. Place the plant in low to medium indirect light, avoiding direct sun to prevent scorch."),
    Plant("Snake Plant", R.drawable.snakeplant,10,4,2,2,true,true,
        R.drawable.cloudsunicon, R.drawable.poundicon, R.drawable.watericon,"11", "1 - 10", "Medium", "30–120 cm",
        "An excellent low-maintenance houseplant that tolerates neglect, low light, and dry conditions. The Snake Plant's upright leaves and air-purifying properties make it a fantastic choice for bedrooms or offices.",
        "Water moderately, allowing the soil to dry slightly between waterings, and increase humidity if necessary. Place the plant in low to medium indirect light, avoiding direct sun to prevent scorch.",
        "Water sparingly, allowing the soil to dry out between waterings. Provide the plant with moderate indirect light, and ensure the pot has good drainage to avoid water accumulation at the roots."),
)

// Returns all plants that fit user search (query)
fun getPlantsBySearch(query: String): List<Plant> {
    if (query.isEmpty()) {
        return listOf()
    }
    // Returns all plants that contain the user search
    return allPlants.filter { plant -> plant.name.contains(query, ignoreCase = true) }
}

// Constants to specify navigation routes
enum class PlantScreens(var title: String) {
    Home(title = "Home"),
    Quiz(title = "Quiz"),
    Search(title = "Search"),
    Journal(title= "Journal"),
    CameraScreen(title = "CameraScreen"),
    Profile(title = "Profile"),
    Login(title = "Login"),
    PlantDetails(title = "Plant Details")
}

fun idToPlant(id: String?) : Plant {
    for (plant in allPlants) {
        if (plant.plantId == id) {
            return plant
        }
    }
    return allPlants[1]
}

// Class for data stored across different pages (i.e. ui state that can be accessed by entire app)
data class PlantUiState(
    val favourites: List<String> = emptyList<String>(),
    val myPlants: List<UserPlant> = listOf(UserPlant(plantId = "4", waterWeek = 1, lastWateredDates = emptyList(), nextWaterDay = LocalDate.now().minusDays(1), plantName = "Spider Plant", plantImage = R.drawable.spiderplant)),
//    val myPlants: List<UserPlant> = emptyList<UserPlant>(),
    val quizChoices: QuizChoices = QuizChoices(),
    val username: String? = null,
    val imgs: List<JournalImg> = emptyList<JournalImg>()
)
// Class for plants that the user owns (may contain water schedule)
data class UserPlant (
    val plantId: String,
    val waterWeek: Int,
    var lastWateredDates: List<LocalDate>,
    var nextWaterDay: LocalDate,
    val plantName: String,
    val plantImage: Int,
)

data class QuizChoices (
    var priceStart: Int = 0,
    var priceEnd: Int = 50,
    var waterStart: Int = 1,
    var waterEnd: Int = 4,
    var spaceStart: Int = 1,
    var spaceEnd: Int = 5,
    var lightStart: Int = 1,
    var lightEnd: Int = 3,
    var toxicYn: Boolean? = null,
    var outdoor: Boolean? = null,
    var indoor: Boolean? = null,
)

/*
PlantViewModel includes:
1. UI state, which stores data that can be passed to composable functions
2. Functions triggered by events in the UI element (e.g. "Add to My Plants" button) that alter UI state
*/
class PlantViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(PlantUiState())
    val uiState: StateFlow<PlantUiState> = _uiState.asStateFlow()

    // Add to favourites list
    fun addToFavourites(plantId: String) {
        if (!_uiState.value.favourites.contains(plantId)) {
            val updatedFavourites = _uiState.value.favourites + plantId
            _uiState.update { currentState -> currentState.copy(favourites = updatedFavourites) }
        }
    }

    // Remove to favourites list
    fun removeFromFavourites(plantId: String) {
        val updatedFavourites = _uiState.value.favourites.filter{it != plantId}
        _uiState.update { currentState -> currentState.copy(favourites = updatedFavourites) }
    }

    // Add to My plants list
    fun addToMyPlants(plant: Plant) {
        if (!_uiState.value.myPlants.any {it.plantId == plant.plantId}) {
            val updatedMyPlants = _uiState.value.myPlants + UserPlant(plant.plantId, plant.waterWeek, emptyList(), LocalDate.now(), plant.name, plant.image)
            _uiState.update { currentState -> currentState.copy(myPlants = updatedMyPlants) }
        }
    }

    // Remove from My plants list
    fun removeFromMyPlants(plantId: String) {
        val updatedMyPlants = _uiState.value.myPlants.filter{ it.plantId != plantId }
        _uiState.update { currentState -> currentState.copy(myPlants = updatedMyPlants) }
    }

    // Water the plant
    fun waterPlant(plantId: String) {

        val mappedPlant = _uiState.value.myPlants.map {
            if (it.plantId == plantId)
                UserPlant(it.plantId, it.waterWeek, it.lastWateredDates + LocalDate.now(), LocalDate.now().plusWeeks(it.waterWeek.toLong()), it.plantName, it.plantImage)
            else it
        }
        _uiState.update { currentState -> currentState.copy(myPlants = mappedPlant) }
    }

    fun addUser(username: String) {
        _uiState.update { currentState -> currentState.copy(username = username) }
    }

    fun updatePriceStart(priceStart: Int) {
        val newChoices = _uiState.value.quizChoices.copy(priceStart = priceStart)
        _uiState.update { currentState -> currentState.copy(quizChoices = newChoices) }
    }

    fun updatePriceEnd(priceEnd: Int) {
        val newChoices = _uiState.value.quizChoices.copy(priceEnd = priceEnd)
        _uiState.update { currentState -> currentState.copy(quizChoices = newChoices) }
    }

    fun updateWaterStart(waterStart: Int) {
        val newChoices = _uiState.value.quizChoices.copy(waterStart = waterStart)
        _uiState.update { currentState -> currentState.copy(quizChoices = newChoices) }
    }

    fun updateWaterEnd(waterEnd: Int) {
        val newChoices = _uiState.value.quizChoices.copy(waterEnd = waterEnd)
        _uiState.update { currentState -> currentState.copy(quizChoices = newChoices) }
    }

    fun updateSpaceStart(spaceStart: Int) {
        val newChoices = _uiState.value.quizChoices.copy(spaceStart = spaceStart)
        _uiState.update { currentState -> currentState.copy(quizChoices = newChoices) }
    }

    fun updateSpaceEnd(spaceEnd: Int) {
        val newChoices = _uiState.value.quizChoices.copy(spaceEnd = spaceEnd)
        _uiState.update { currentState -> currentState.copy(quizChoices = newChoices) }
    }

    fun updateLightStart(lightStart: Int) {
        val newChoices = _uiState.value.quizChoices.copy(lightStart = lightStart)
        _uiState.update { currentState -> currentState.copy(quizChoices = newChoices) }
    }

    fun updateLightEnd(lightEnd: Int) {
        val newChoices = _uiState.value.quizChoices.copy(lightEnd = lightEnd)
        _uiState.update { currentState -> currentState.copy(quizChoices = newChoices) }
    }

    fun updateToxicYn(toxicYn: Boolean) {
        val newChoices = _uiState.value.quizChoices.copy(toxicYn = toxicYn)
        _uiState.update { currentState -> currentState.copy(quizChoices = newChoices) }
    }

    fun updateOutdoor(outdoor: Boolean) {
        val newChoices = _uiState.value.quizChoices.copy(outdoor = outdoor)
        _uiState.update { currentState -> currentState.copy(quizChoices = newChoices) }
    }

    fun updateIndoor(indoor: Boolean) {
        val newChoices = _uiState.value.quizChoices.copy(indoor = indoor)
        _uiState.update { currentState -> currentState.copy(quizChoices = newChoices) }
    }

    fun addImg(img: JournalImg) {
        val updatedImgs = _uiState.value.imgs + img
        _uiState.update { currentState -> currentState.copy(imgs = updatedImgs) }
    }
}

data class JournalImg (
    val selectedDate: String,
    val bitmap: Bitmap?,
)