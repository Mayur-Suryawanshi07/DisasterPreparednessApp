package com.example.disasterpreparednessapp.feature_map.presentaion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.model.disaster.CapInfo
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.model.disaster.DisasterAlert
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.use_cases.disaster.GetDisaster
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.use_cases.disaster.GetDisasterDetails
import com.example.disasterpreparednessapp.feature_map.API
import com.example.disasterpreparednessapp.feature_map.data.GeoCodingApiService
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AreaMarker(
    val id: String,
    val title: String,
    val position: LatLng
)

@HiltViewModel
class AlertMapViewModel @Inject constructor(
    getDisaster: GetDisaster,
    private val getDisasterDetails: GetDisasterDetails,
    private val geoCodingApiService: GeoCodingApiService
) : ViewModel() {

    private val _alerts = MutableStateFlow<List<DisasterAlert>>(emptyList())
    val alerts = _alerts.asStateFlow()

    private val _selectedAlertId = MutableStateFlow<String?>(null)
    val selectedAlertId = _selectedAlertId.asStateFlow()

    private val _capInfo = MutableStateFlow<CapInfo?>(null)
    val capInfo = _capInfo.asStateFlow()

    private val _areaMarkers = MutableStateFlow<List<AreaMarker>>(emptyList())
    val areaMarkers: StateFlow<List<AreaMarker>> = _areaMarkers.asStateFlow()

    private val _stateBounds = MutableStateFlow<LatLngBounds?>(null)
    val stateBounds: StateFlow<LatLngBounds?> = _stateBounds.asStateFlow()

    private val _highlightPolygons = MutableStateFlow<List<String>>(emptyList())
    val highlightPolygons = _highlightPolygons.asStateFlow()

    private val _approximateHighlight = MutableStateFlow(false)
    val approximateHighlight = _approximateHighlight.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private var detailJob: Job? = null
    private var requestedAlertId: String? = null

    // Comprehensive offline dictionary for Indian States and Major Districts
    private val indianStates = mapOf(
        "Maharashtra" to LatLng(19.7515, 75.7139),
        "Madhya Pradesh" to LatLng(22.9734, 78.6569),
        "Uttar Pradesh" to LatLng(26.8467, 80.9462),
        "Bihar" to LatLng(25.0961, 85.3131),
        "West Bengal" to LatLng(22.9868, 87.8550),
        "Odisha" to LatLng(20.9517, 85.0985),
        "Assam" to LatLng(26.2006, 92.9376),
        "Gujarat" to LatLng(22.2587, 71.1924),
        "Rajasthan" to LatLng(27.0238, 74.2179),
        "Tamil Nadu" to LatLng(11.1271, 78.6569),
        "Kerala" to LatLng(10.8505, 76.2711),
        "Karnataka" to LatLng(15.3173, 75.7139),
        "Andhra Pradesh" to LatLng(15.9129, 79.7400),
        "Telangana" to LatLng(18.1124, 79.0193),
        "Punjab" to LatLng(31.1471, 75.3412),
        "Haryana" to LatLng(29.0588, 76.0856),
        "Himachal Pradesh" to LatLng(31.1048, 77.1734),
        "Uttarakhand" to LatLng(30.0668, 79.0193),
        "Jharkhand" to LatLng(23.6102, 85.2799),
        "Chhattisgarh" to LatLng(21.2787, 81.8661),
        "Jammu and Kashmir" to LatLng(33.7782, 76.5762),
        "Ladakh" to LatLng(34.1526, 77.5771),
        "Manipur" to LatLng(24.6637, 93.9063),
        "Meghalaya" to LatLng(25.4670, 91.3662),
        "Mizoram" to LatLng(23.1645, 92.9376),
        "Nagaland" to LatLng(26.1584, 94.5624),
        "Tripura" to LatLng(23.9408, 91.9882),
        "Sikkim" to LatLng(27.5330, 88.5122),
        "Goa" to LatLng(15.2993, 74.1240),
        "Delhi" to LatLng(28.7041, 77.1025),
        "Puducherry" to LatLng(11.9416, 79.8083),
        "Andaman and Nicobar" to LatLng(11.7401, 92.6586),
        "Chandigarh" to LatLng(30.7333, 76.7794),
        "Dadra and Nagar Haveli" to LatLng(20.1809, 73.0169),
        "Lakshadweep" to LatLng(10.5667, 72.6417)
    )

    private val offlineDistricts = mapOf(
        "mandla" to LatLng(22.5986, 80.3725),
        "narmada" to LatLng(21.8741, 73.5594),
        "bhadrak" to LatLng(21.0575, 86.4950),
        "akhuapada" to LatLng(20.9328, 86.3262),
        "cuttack" to LatLng(20.4625, 85.8828),
        "puri" to LatLng(19.8135, 85.8312),
        "balasore" to LatLng(21.4934, 86.9135),
        "sambalpur" to LatLng(21.4669, 83.9812),
        "ganjam" to LatLng(19.3807, 85.0519),
        "pune" to LatLng(18.5204, 73.8567),
        "thane" to LatLng(19.2183, 72.9781),
        "mumbai" to LatLng(19.0760, 72.8777),
        "nagpur" to LatLng(21.1458, 79.0882),
        "nashik" to LatLng(19.9975, 73.7898),
        "kolhapur" to LatLng(16.7050, 74.2433),
        "solapur" to LatLng(17.6599, 75.9064),
        "raigad" to LatLng(18.5158, 73.1822),
        "palghar" to LatLng(19.6936, 72.7655),
        "satara" to LatLng(17.6805, 74.0183),
        "bhopal" to LatLng(23.2599, 77.4126),
        "indore" to LatLng(22.7196, 75.8577),
        "jabalpur" to LatLng(23.1815, 79.9864),
        "gwalior" to LatLng(26.2183, 78.1828),
        "ujjain" to LatLng(23.1765, 75.7885),
        "sagar" to LatLng(23.8388, 78.7378),
        "rewa" to LatLng(24.5362, 81.3037),
        "patna" to LatLng(25.5941, 85.1376),
        "gaya" to LatLng(24.7914, 85.0002),
        "muzaffarpur" to LatLng(26.1209, 85.3647),
        "darbhanga" to LatLng(26.1542, 85.8918),
        "bhagalpur" to LatLng(25.2425, 86.9842),
        "purnea" to LatLng(25.7771, 87.4753),
        "lucknow" to LatLng(26.8467, 80.9462),
        "kanpur" to LatLng(26.4499, 80.3319),
        "varanasi" to LatLng(25.3176, 82.9739),
        "prayagraj" to LatLng(25.4358, 81.8463),
        "agra" to LatLng(27.1767, 78.0081),
        "gorakhpur" to LatLng(26.7606, 83.3732),
        "meerut" to LatLng(28.9845, 77.7064),
        "bareilly" to LatLng(28.3670, 79.4304),
        "aligarh" to LatLng(27.8974, 78.0880),
        "moradabad" to LatLng(28.8386, 78.7733),
        "ayodhya" to LatLng(26.7922, 82.1998),
        "jaipur" to LatLng(26.9124, 75.7873),
        "jodhpur" to LatLng(26.2389, 73.0243),
        "udaipur" to LatLng(24.5854, 73.7125),
        "kota" to LatLng(25.2138, 75.8648),
        "bikaner" to LatLng(28.0229, 73.3119),
        "ajmer" to LatLng(26.4499, 74.6399),
        "barmer" to LatLng(25.7532, 71.4181),
        "ahmedabad" to LatLng(23.0225, 72.5714),
        "surat" to LatLng(21.1702, 72.8311),
        "vadodara" to LatLng(22.3072, 73.1812),
        "rajkot" to LatLng(22.3039, 70.8022),
        "kutch" to LatLng(23.7337, 69.8597),
        "junagadh" to LatLng(21.5222, 70.4579),
        "kolkata" to LatLng(22.5726, 88.3639),
        "howrah" to LatLng(22.5958, 88.2636),
        "darjeeling" to LatLng(27.0410, 88.2663),
        "jalpaiguri" to LatLng(26.5411, 88.7190),
        "malda" to LatLng(25.0108, 88.1411),
        "murshidabad" to LatLng(24.1750, 88.2801),
        "asansol" to LatLng(23.6889, 86.9661),
        "durgapur" to LatLng(23.5204, 87.3119),
        "wayanad" to LatLng(11.6854, 76.1320),
        "kozhikode" to LatLng(11.2588, 75.7804),
        "ernakulam" to LatLng(9.9816, 76.2999),
        "trivandrum" to LatLng(8.5241, 76.9366),
        "thiruvananthapuram" to LatLng(8.5241, 76.9366),
        "palakkad" to LatLng(10.7867, 76.6548),
        "thrissur" to LatLng(10.5276, 76.2144),
        "kannur" to LatLng(11.8745, 75.3704),
        "kottayam" to LatLng(9.5916, 76.5222),
        "idukki" to LatLng(9.8494, 76.9800),
        "chennai" to LatLng(13.0827, 80.2707),
        "coimbatore" to LatLng(11.0168, 76.9558),
        "madurai" to LatLng(9.9252, 78.1198),
        "salem" to LatLng(11.6643, 78.1460),
        "tiruchirappalli" to LatLng(10.7905, 78.7047),
        "tirunelveli" to LatLng(8.7139, 77.7567),
        "kanyakumari" to LatLng(8.0883, 77.5385),
        "bengaluru" to LatLng(12.9716, 77.5946),
        "bangalore" to LatLng(12.9716, 77.5946),
        "mysore" to LatLng(12.2958, 76.6394),
        "mangalore" to LatLng(12.9141, 74.8560),
        "hubli" to LatLng(15.3647, 75.1240),
        "belgaum" to LatLng(15.8497, 74.4977),
        "shimoga" to LatLng(13.9299, 75.5681),
        "hyderabad" to LatLng(17.3850, 78.4867),
        "warangal" to LatLng(17.9689, 79.5941),
        "karimnagar" to LatLng(18.4386, 79.1288),
        "nizamabad" to LatLng(18.6725, 78.0941),
        "vijayawada" to LatLng(16.5062, 80.6480),
        "visakhapatnam" to LatLng(17.6868, 83.2185),
        "guntur" to LatLng(16.3067, 80.4365),
        "tirupati" to LatLng(13.6288, 79.4192),
        "kurnool" to LatLng(15.8281, 78.0373),
        "anantapur" to LatLng(14.6819, 77.6006),
        "guwahati" to LatLng(26.1445, 91.7362),
        "silchar" to LatLng(24.8333, 92.7789),
        "dibrugarh" to LatLng(27.4728, 94.9120),
        "jorhat" to LatLng(26.7509, 94.2037),
        "tezpur" to LatLng(26.6338, 92.8000),
        "cachar" to LatLng(24.7891, 92.7212),
        "kamrup" to LatLng(26.3161, 91.5984),
        "ranchi" to LatLng(23.3441, 85.3096),
        "jamshedpur" to LatLng(22.8046, 86.2029),
        "dhanbad" to LatLng(23.7957, 86.4304),
        "bokaro" to LatLng(23.6693, 86.1511),
        "raipur" to LatLng(21.2514, 81.6296),
        "bilaspur" to LatLng(22.0797, 82.1391),
        "durg" to LatLng(21.1904, 81.2849),
        "bastar" to LatLng(19.2227, 81.8661),
        "shimla" to LatLng(31.1048, 77.1734),
        "kullu" to LatLng(31.9579, 77.1095),
        "manali" to LatLng(32.2432, 77.1892),
        "kangra" to LatLng(32.0998, 76.2691),
        "mandi" to LatLng(31.5892, 76.9182),
        "dehradun" to LatLng(30.3165, 78.0322),
        "haridwar" to LatLng(29.9457, 78.1642),
        "nainital" to LatLng(29.3919, 79.4542),
        "chamoli" to LatLng(30.4227, 79.3242),
        "uttarkashi" to LatLng(30.7268, 78.4354),
        "srinagar" to LatLng(34.0837, 74.7973),
        "jammu" to LatLng(32.7266, 74.8570),
        "anantnag" to LatLng(33.7311, 75.1500),
        "baramulla" to LatLng(34.2033, 74.3436),
        "leh" to LatLng(34.1526, 77.5771),
        "kargil" to LatLng(34.5539, 76.1349),
        "imphal" to LatLng(24.8170, 93.9368),
        "shillong" to LatLng(25.5788, 91.8933),
        "aizawl" to LatLng(23.7271, 92.7176),
        "kohima" to LatLng(25.6751, 94.1086),
        "agartala" to LatLng(23.8315, 91.2868),
        "gangtok" to LatLng(27.3389, 88.6065),
        "panaji" to LatLng(15.4909, 73.8278),
        "margao" to LatLng(15.2832, 73.9862)
    )

    init {
        viewModelScope.launch {
            getDisaster()
                .catch { _isLoading.value = false }
                .collect { events ->
                    _alerts.value = events
                    _isLoading.value = false
                    if (_selectedAlertId.value == null && events.isNotEmpty()) {
                        val requested = events.firstOrNull { it.id == requestedAlertId }
                        selectAlert(requested ?: events.first())
                    }
                }
        }
    }

    fun selectAlertById(alertId: String?) {
        requestedAlertId = alertId
        val alert = _alerts.value.firstOrNull { it.id == alertId }
        if (alert != null) selectAlert(alert)
    }

    fun selectAlert(alert: DisasterAlert) {
        if (_selectedAlertId.value == alert.id) return
        _selectedAlertId.value = alert.id
        _capInfo.value = alert.capInfo
        _highlightPolygons.value = alert.capInfo?.polygons.orEmpty()
        _approximateHighlight.value = false
        _areaMarkers.value = emptyList()
        _stateBounds.value = null

        detailJob?.cancel()
        detailJob = viewModelScope.launch {
            val initialInfo = alert.capInfo
            if (initialInfo != null) {
                resolveLocations(alert.id, alert, initialInfo)
            }
            getDisasterDetails(alert.id)
                .catch { /* Keep cached/feed details */ }
                .collect { infos ->
                    val info = infos.firstOrNull { it.language?.startsWith("en", true) == true }
                        ?: infos.firstOrNull()
                    if (info != null) {
                        _capInfo.value = info
                        resolveLocations(alert.id, alert, info)
                    }
                }
        }
    }

    private suspend fun resolveLocations(alertId: String, alert: DisasterAlert, info: CapInfo) {
        if (_selectedAlertId.value != alertId) return

        _areaMarkers.value = emptyList()
        _stateBounds.value = null

        // 1. Official boundary polygons
        val officialPolygons = info.polygons.filter(String::isNotBlank)
        if (officialPolygons.isNotEmpty()) {
            _highlightPolygons.value = officialPolygons
            _approximateHighlight.value = false
            info.markerPosition?.let { pos ->
                _areaMarkers.value = listOf(
                    AreaMarker("marker_${alertId}", info.affectedAreas.firstOrNull() ?: alert.title, pos)
                )
            }
            return
        }

        // 2. Extract area strings & detect State context
        val rawAreaList = info.affectedAreas.filter { it.isNotBlank() }.ifEmpty {
            listOfNotNull(
                alert.title.takeIf { it.isNotBlank() },
                alert.description?.takeIf { it.isNotBlank() }
            )
        }

        val fullContextText = (rawAreaList.joinToString(" ") + " " + alert.title + " " + (alert.description ?: "")).trim()
        val detectedStateEntry = indianStates.entries.firstOrNull { (stateName, _) ->
            fullContextText.contains(stateName, ignoreCase = true)
        }
        val detectedStateName = detectedStateEntry?.key
        val detectedStatePos = detectedStateEntry?.value

        val resolvedMarkers = mutableListOf<AreaMarker>()
        val seenCoordinates = mutableSetOf<String>()

        fun tryAddMarker(title: String, pos: LatLng): Boolean {
            val coordKey = "${"%.3f".format(pos.latitude)}_${"%.3f".format(pos.longitude)}"
            if (seenCoordinates.add(coordKey)) {
                resolvedMarkers.add(
                    AreaMarker(
                        id = "${title}_${pos.latitude}_${pos.longitude}",
                        title = title,
                        position = pos
                    )
                )
                return true
            }
            return false
        }

        suspend fun tryGeocode(query: String, labelTitle: String): Boolean {
            if (_selectedAlertId.value != alertId) return false
            val formattedQuery = if (!query.contains("India", ignoreCase = true)) "$query, India" else query
            val response = runCatching {
                geoCodingApiService.getCoordinatesFromAddress(formattedQuery, API.MY_API)
            }.getOrNull() ?: return false

            val result = response.results.firstOrNull() ?: return false
            val loc = result.geometry?.location ?: return false

            if (loc.lat in 6.0..38.0 && loc.lng in 68.0..98.0) {
                return tryAddMarker(labelTitle, LatLng(loc.lat, loc.lng))
            }
            return false
        }

        // 3. Check offline district dictionary first
        val placeTokens = rawAreaList
            .flatMap { it.split(',') }
            .map { it.trim().lowercase().removeSuffix(" district").removeSuffix(" districts").trim() }
            .filter { token ->
                token.isNotBlank() && (detectedStateName == null || !token.equals(detectedStateName, ignoreCase = true))
            }
            .distinct()

        for (token in placeTokens) {
            val offlinePos = offlineDistricts[token]
            if (offlinePos != null) {
                tryAddMarker(token.replaceFirstChar { it.uppercase() }, offlinePos)
            }
        }

        // 4. Online Geocoding attempt if offline dictionary didn't catch all
        if (resolvedMarkers.isEmpty()) {
            for (areaPhrase in rawAreaList.take(5)) {
                if (_selectedAlertId.value != alertId) return
                val phraseQuery = if (detectedStateName != null && !areaPhrase.contains(detectedStateName, ignoreCase = true)) {
                    "$areaPhrase, $detectedStateName"
                } else {
                    areaPhrase
                }
                val cleanLabel = areaPhrase.split(',').firstOrNull()?.trim() ?: areaPhrase
                tryGeocode(phraseQuery, cleanLabel)
            }

            for (token in placeTokens.take(8)) {
                if (_selectedAlertId.value != alertId) return
                val tokenQuery = if (detectedStateName != null) "$token, $detectedStateName" else token
                tryGeocode(tokenQuery, token.replaceFirstChar { it.uppercase() })
            }

            if (resolvedMarkers.isEmpty() && alert.title.isNotBlank()) {
                val titleQuery = if (detectedStateName != null && !alert.title.contains(detectedStateName, ignoreCase = true)) {
                    "${alert.title}, $detectedStateName"
                } else {
                    alert.title
                }
                tryGeocode(titleQuery, alert.title.take(30))
            }
        }

        if (resolvedMarkers.isNotEmpty()) {
            _areaMarkers.value = resolvedMarkers
            _stateBounds.value = null
            return
        }

        // 5. Fallback: Geocode or lookup State boundary if district/village markers fail
        if (detectedStatePos != null) {
            val delta = 1.5
            _stateBounds.value = LatLngBounds(
                LatLng(detectedStatePos.latitude - delta, detectedStatePos.longitude - delta),
                LatLng(detectedStatePos.latitude + delta, detectedStatePos.longitude + delta)
            )
            return
        }

        // 6. If state name is not given or geocoding fails, do not zoom at all
        _areaMarkers.value = emptyList()
        _stateBounds.value = null
    }
}
