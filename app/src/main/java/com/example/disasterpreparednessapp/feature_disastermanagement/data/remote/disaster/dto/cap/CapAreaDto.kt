package com.example.disasterpreparednessapp.feature_disastermanagement.data.remote.disaster.dto.cap

import com.google.android.gms.maps.model.LatLng
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "area", strict = false)
data class CapArea @JvmOverloads constructor(

    @field:Element(name = "areaDesc", required = false)
    var areaDesc: String? = null,

    // NEW: district-level codes — this alert had 16 of these
    @field:ElementList(entry = "geocode", inline = true, required = false)
    var geocodes: List<CapGeocodeDto>? = null,

    // CAP encodes each boundary ring as whitespace-separated "lat,lon" pairs.
    @field:ElementList(entry = "polygon", inline = true, required = false)
    var polygons: List<String>? = null,

    // NEW: seen as "0" in the payload
    @field:Element(name = "altitude", required = false)
    var altitude: String? = null,

    // NEW: seen as "0" in the payload
    @field:Element(name = "ceiling", required = false)
    var ceiling: String? = null
)

/**
 * Parses altitude/ceiling as latitude/longitude doubles.
 * Returns null if parsing fails or coordinates fall outside India range (Lat: 6.0..38.0, Lng: 68.0..98.0).
 */
fun CapArea.toLatLng(): LatLng? {
    val lat = altitude?.toDoubleOrNull() ?: return null
    val lng = ceiling?.toDoubleOrNull() ?: return null
    if (lat !in 6.0..38.0 || lng !in 68.0..98.0) return null
    return LatLng(lat, lng)
}
