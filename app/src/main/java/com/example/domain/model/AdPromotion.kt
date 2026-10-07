package com.example.domain.model

data class AdPromotion(
    val id: String,
    val title: String,
    val tagline: String,
    val description: String,
    val targetUrl: String,
    val ctaText: String = "Lawati Sekarang",
    val promoBadge: String = "Tajaan Nasadef™"
)

object AdPromotionCatalog {
    val ads = listOf(
        AdPromotion(
            id = "ad_quran",
            title = "Razif Interaktif Al-Quran",
            tagline = "Hayati Bacaan Al-Quran Secara Digital & Interaktif",
            description = "Aplikasi web interaktif dengan teks Mushaf yang jelas, bacaan audio qari terpilih, serta terjemahan mudah difahami pada bila-bila masa.",
            targetUrl = "https://razif-interaktif-al-quran.ai.studio",
            ctaText = "Buka Al-Quran Sekarang",
            promoBadge = "Aplikasi Pilihan"
        ),
        AdPromotion(
            id = "ad_banjir",
            title = "Sistem Pemantauan Banjir IoT",
            tagline = "Pantau Paras Air & Terima Amaran Awal Banjir",
            description = "Sistem penderiaan pintar IoT berketepatan tinggi yang memantau paras air sungai secara masa-nyata untuk melindungi komuniti daripada ancaman banjir.",
            targetUrl = "https://sistem-pemantauan-banjir-iot.ai.studio",
            ctaText = "Pantau Paras Air",
            promoBadge = "Inovasi IoT"
        ),
        AdPromotion(
            id = "ad_metropolis",
            title = "Sky Metropolis 3D",
            tagline = "Eksplorasi Bandar Pintar Masa Hadapan",
            description = "Rasai pengalaman simulasi 3D memukau di bandar metropolis futuristik terus dalam pelayar anda tanpa perlu muat turun yang rumit.",
            targetUrl = "https://remix-sky-metropolis-9687.ai.studio",
            ctaText = "Terokai Bandar 3D",
            promoBadge = "Simulasi 3D"
        ),
        AdPromotion(
            id = "ad_nasadef",
            title = "NASADEF™ SDN. BHD.®",
            tagline = "Peneraju Inovasi Pertahanan & Teknologi Termaju",
            description = "Ketahui kepakaran penyelidikan, pembangunan perisian berteknologi tinggi, serta perkhidmatan kejuruteraan canggih daripada NASADEF SDN BHD.",
            targetUrl = "https://nasadef.com.my",
            ctaText = "Layari Laman Rasmi",
            promoBadge = "Portal Korporat"
        )
    )

    fun getRandomAd(excludeId: String? = null): AdPromotion {
        val candidates = if (excludeId != null && ads.size > 1) {
            ads.filter { it.id != excludeId }
        } else {
            ads
        }
        return candidates.random()
    }
}
