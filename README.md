# Video Hub (Android)

Video Hub is a production-ready video aggregator application for Android smartphones, tablets, and Android TV / Google TV. It provides a unified, modern streaming interface to access curated videos from YouTube, FreeReels, and open Web video sources while respecting content security policies and copyright requirements.

---

## 🌟 Key Features

1. **Authentication Gate (`LoginScreen`)**:
   - **Mandatory Login**: Users must authenticate before accessing the video aggregator.
   - **Multiple Login Methods**:
     - **Google Account**: Quick sign-in with Google profile.
     - **NASA DEF Account**: Direct credentials sign-in (Nasadef ID & Password) or 1-Click SSO Guest Access.
     - **Facebook Account**: Sign-in with Facebook profile.
   - **Hero Banner**: Features official NASA DEF SDN BHD emblem backdrop.
   - **Branding**: Includes RazifApps@Nasadef™ watermark (linking to `https://apps.nasadef.com.my`) and NASADEF™ SDN.BHD.® copyright footer (linking to `https://nasadef.com.my`).

2. **Multi-Provider Architecture**:
   - **YouTube**: Official embedded playback (`youtube-nocookie.com`), video search, and direct YouTube URL handling.
   - **FreeReels**: Configurable base URL with graceful fallback ("FreeReels tidak membenarkan video ini dimainkan secara embedded") and direct website launcher.
   - **Web & Open Streams**: HTML5 video playback for open-access streams and user-defined custom video URLs.
   - **Extensible Plugin System**: Simple `VideoProvider` interface and `ProviderManager` enabling new providers without refactoring existing code.

2. **Adaptive Multi-Form-Factor UI**:
   - **Android TV / Google TV**: Full D-Pad remote control support (`Up`, `Down`, `Left`, `Right`, `Enter`/`Center`, `Back`), prominent focus indicators with glowing borders and scaling animations.
   - **Smartphones**: Bottom navigation, edge-to-edge support, portrait-to-landscape video rotation.
   - **Tablets & Foldables**: Adaptive Navigation Rail for wide-screen ergonomics.

3. **Local Persistence (Room & DataStore)**:
   - **Favorites**: Stored locally in Room (`favorites` table).
   - **Watch History & Continue Watching**: Automatically logs viewed videos, preserves resume position, and displays visual progress bars (capped at 1,000 items).
   - **Settings & Preferences**: Managed with Jetpack DataStore Preferences (theme, autoplay, auto-fullscreen, Wi-Fi only, custom sources).

4. **Security & Zero Bypassing**:
   - No DRM bypass, no scraping, no stream extraction, no token manipulation.
   - Strict `NetworkSecurityConfig` blocking cleartext HTTP.
   - Hardened `EmbeddedWebView` (`allowFileAccess = false`, `mixedContentMode = MIXED_CONTENT_NEVER_ALLOW`, leak-free lifecycle cleanup).

5. **Deep Linking & Sharing**:
   - Custom URI schemes: `videohub://youtube/{videoId}`, `videohub://freereels/{id}`, `videohub://web?url={url}`.
   - System Share sheet for sharing original video URLs.

---

## 📁 Project Directory Structure

```text
app/src/main/
├── AndroidManifest.xml                  # Permissions, Leanback TV flags, deep links
├── java/com/example/
│   ├── MainActivity.kt                  # Entry activity, deep link parser, DI wiring
│   ├── data/
│   │   ├── local/
│   │   │   ├── AppDatabase.kt           # Room Database definition
│   │   │   ├── PreferencesManager.kt    # Jetpack DataStore preferences
│   │   │   ├── dao/
│   │   │   │   ├── FavoriteDao.kt       # Favorite CRUD operations
│   │   │   │   └── WatchHistoryDao.kt   # History & Continue Watching operations
│   │   │   └── entity/
│   │   │       ├── FavoriteEntity.kt    # Room entity for favorites
│   │   │       └── WatchHistoryEntity.kt# Room entity for watch history
│   │   ├── remote/
│   │   │   └── YouTubeApiService.kt     # Retrofit & Moshi service for YouTube API v3
│   │   └── repository/
│   │       ├── VideoRepository.kt       # Aggregates providers, favorites, and history
│   │       └── SettingsRepository.kt    # Exposes app settings flow
│   ├── domain/
│   │   └── model/
│   │       ├── UiState.kt               # Sealed interface: Loading, Success, Empty, Error
│   │       ├── VideoItem.kt             # Domain data model
│   │       └── VideoProviderType.kt     # YOUTUBE, FREEREELS, WEB enum
│   ├── provider/
│   │   ├── VideoProvider.kt             # Provider contract interface
│   │   ├── YouTubeProvider.kt           # Official YouTube embed & API provider
│   │   ├── FreeReelsProvider.kt         # FreeReels source provider with safety fallback
│   │   ├── WebProvider.kt               # Open web streams & custom URLs provider
│   │   └── ProviderManager.kt           # Dynamic provider registry & search aggregator
│   └── ui/
│       ├── components/
│       │   ├── EmbeddedWebView.kt       # Safe WebView with WebChromeClient & fallback
│       │   ├── EmptyStateView.kt        # Empty placeholder states
│       │   ├── ErrorStateView.kt        # Error state with retry action
│       │   ├── NavigationBars.kt        # Adaptive BottomBar and NavigationRail
│       │   ├── TvFocusable.kt           # D-pad remote focus modifier
│       │   └── VideoCard.kt             # Video card with thumbnail, badge & progress
│       ├── favorites/                   # Favorites Screen & ViewModel
│       ├── history/                     # Watch History Screen & ViewModel
│       ├── home/                        # Home Screen (Hero banner, Continue Watching, Categories)
│       ├── navigation/                  # VideoHubApp scaffold & navigation handler
│       ├── player/                      # Video Player Screen & controls
│       ├── search/                      # Real-time search with provider filtering
│       ├── settings/                    # Appearance, Sources, Playback & Privacy
│       └── theme/                       # Color, Theme, and Typography tokens
└── res/
    ├── drawable/                        # Vector assets & adaptive launcher icons
    ├── mipmap-*/                        # Launcher PNG icons across all densities
    ├── values/                          # strings.xml, colors.xml, themes.xml
    └── xml/
        └── network_security_config.xml  # HTTPS enforcement configuration
```

---

## 🚀 How to Build & Run

### Prerequisites
- Android Studio Ladybug (or newer)
- JDK 11 or higher
- Android SDK 36 (Minimum SDK 26 / Android 8.0)

### Configuration (Optional)
Configure API keys via `.env` file or Secrets panel:
- `YOUTUBE_API_KEY`: YouTube Data API v3 key for live YouTube search results.
- `FREE_REELS_BASE_URL`: Base domain for FreeReels provider (defaults to `https://freereels.com`).

### Build Steps
1. Open the project folder in **Android Studio**.
2. Wait for Gradle sync to complete.
3. Select your target device (Smartphone emulator, Tablet, or Android TV / Google TV emulator).
4. Click **Run** (`Shift + F10`) or run from terminal:
   ```bash
   gradle assembleDebug
   ```
5. Run unit and local tests:
   ```bash
   gradle :app:testDebugUnitTest
   ```
