package com.example.domain.model

data class WebLinkItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val url: String,
    val iconDescription: String,
    val category: String,
    val badge: String = "Nasadef"
)

object DefaultWebLinks {
    val items = listOf(
        WebLinkItem(
            id = "quran",
            title = "Razif Interaktif Al-Quran",
            subtitle = "Aplikasi Al-Quran digital pintar interaktif dengan tafsir dan audio.",
            url = "https://razif-interaktif-al-quran.ai.studio",
            iconDescription = "Al-Quran",
            category = "Pendidikan & Agama",
            badge = "Popular"
        ),
        WebLinkItem(
            id = "banjir",
            title = "Sistem Pemantauan Banjir IoT",
            subtitle = "Telemetri penderia paras air masa-nyata & sistem amaran awal banjir.",
            url = "https://sistem-pemantauan-banjir-iot.ai.studio",
            iconDescription = "Sensor IoT",
            category = "Keselamatan & IoT",
            badge = "IoT System"
        ),
        WebLinkItem(
            id = "metropolis",
            title = "Sky Metropolis 3D",
            subtitle = "Simulasi dan visualisasi 3D bandar metropolitan pintar masa depan.",
            url = "https://remix-sky-metropolis-9687.ai.studio",
            iconDescription = "3D Simulation",
            category = "Visualisasi 3D",
            badge = "3D Interactive"
        ),
        WebLinkItem(
            id = "nasadef",
            title = "Laman Rasmi NASADEF™",
            subtitle = "Penyelesaian teknologi, pertahanan, dan perkhidmatan inovasi NASADEF SDN BHD.",
            url = "https://nasadef.com.my",
            iconDescription = "Portal Korporat",
            category = "Korporat & Inovasi",
            badge = "Official"
        )
    )
}
