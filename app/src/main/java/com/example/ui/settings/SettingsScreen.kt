package com.example.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Source
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.AppThemeMode
import com.example.ui.components.tvFocusable

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onTriggerAdPreview: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()

    var showFreeReelsDialog by remember { mutableStateOf(false) }
    var freeReelsInput by remember { mutableStateOf("") }

    var showYoutubeApiKeyDialog by remember { mutableStateOf(false) }
    var youtubeApiKeyInput by remember { mutableStateOf("") }

    var showAddSourceDialog by remember { mutableStateOf(false) }
    var customSourceInput by remember { mutableStateOf("") }

    var showClearDataDialog by remember { mutableStateOf(false) }
    var showPrivacyPolicyDialog by remember { mutableStateOf(false) }

    // Dialog: FreeReels Base URL
    if (showFreeReelsDialog) {
        AlertDialog(
            onDismissRequest = { showFreeReelsDialog = false },
            title = { Text("Configure FreeReels URL") },
            text = {
                Column {
                    Text("Customize base domain or endpoint for FreeReels provider:", fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = freeReelsInput,
                        onValueChange = { freeReelsInput = it },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.setFreeReelsBaseUrl(freeReelsInput)
                    showFreeReelsDialog = false
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showFreeReelsDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Dialog: YouTube API Key
    if (showYoutubeApiKeyDialog) {
        AlertDialog(
            onDismissRequest = { showYoutubeApiKeyDialog = false },
            title = { Text("YouTube API Key") },
            text = {
                Column {
                    Text("Optional YouTube Data API v3 key for live YouTube search results:", fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = youtubeApiKeyInput,
                        onValueChange = { youtubeApiKeyInput = it },
                        singleLine = true,
                        placeholder = { Text("Enter API key") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.setCustomYoutubeApiKey(youtubeApiKeyInput)
                    showYoutubeApiKeyDialog = false
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showYoutubeApiKeyDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Dialog: Add Custom Source
    if (showAddSourceDialog) {
        AlertDialog(
            onDismissRequest = { showAddSourceDialog = false },
            title = { Text("Add Custom Web Stream") },
            text = {
                Column {
                    Text("Enter video URL or stream link (HTTPS):", fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = customSourceInput,
                        onValueChange = { customSourceInput = it },
                        singleLine = true,
                        placeholder = { Text("https://example.com/stream.mp4") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (customSourceInput.isNotBlank()) {
                        viewModel.addCustomSource(customSourceInput)
                    }
                    showAddSourceDialog = false
                }) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddSourceDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Dialog: Clear All Data
    if (showClearDataDialog) {
        AlertDialog(
            onDismissRequest = { showClearDataDialog = false },
            title = { Text("Clear All Local Data") },
            text = { Text("This will permanently delete all favorites, watch history, and restore default preferences.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.clearAllData()
                    showClearDataDialog = false
                }) {
                    Text("Clear All", color = MaterialTheme.colorScheme.primary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDataDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Dialog: Privacy Policy
    if (showPrivacyPolicyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyPolicyDialog = false },
            title = { Text("Privacy Policy") },
            text = {
                Text(
                    "Video Hub is committed to privacy and user control:\n\n" +
                    "1. Local Storage Only: Favorites, watch history, and preferences are stored strictly on your device using local Room Database.\n" +
                    "2. No Tracking: Video Hub does not track your viewing habits or share personal information with third parties.\n" +
                    "3. Zero Bypassing: We strictly respect external website policies, DRM, and terms of service. Videos are accessed exclusively through official embedded players or direct links."
                )
            },
            confirmButton = {
                TextButton(onClick = { showPrivacyPolicyDialog = false }) {
                    Text("Close")
                }
            }
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("settings_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // User Profile & Account
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(46.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = (settings.user.name.take(1).ifEmpty { "U" }).uppercase(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = settings.user.name.ifEmpty { "Pengguna Video Hub" },
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${settings.user.email.ifEmpty { "Log Masuk Aktif" }} • ${settings.user.provider}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    OutlinedButton(
                        onClick = { viewModel.logout() },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.tvFocusable(shape = RoundedCornerShape(8.dp), onEnterClick = { viewModel.logout() })
                    ) {
                        Text("Log Keluar", fontSize = 12.sp, color = Color(0xFFFF5252))
                    }
                }
            }
        }

        // Appearance
        item {
            SettingsCategoryCard(title = "Appearance", icon = Icons.Default.Brightness4) {
                AppThemeMode.entries.forEach { mode ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.setTheme(mode) }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = settings.themeMode == mode,
                            onClick = { viewModel.setTheme(mode) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = mode.name.lowercase().replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // Video Sources
        item {
            SettingsCategoryCard(title = "Video Sources", icon = Icons.Default.Source) {
                // FreeReels base URL
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            freeReelsInput = settings.freeReelsBaseUrl
                            showFreeReelsDialog = true
                        }
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("FreeReels Base URL", fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                        Text(settings.freeReelsBaseUrl, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Text("Change", color = MaterialTheme.colorScheme.primary, fontSize = 13.sp)
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                // YouTube API Key
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            youtubeApiKeyInput = settings.customYoutubeApiKey
                            showYoutubeApiKeyDialog = true
                        }
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("YouTube Data API Key", fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                        Text(
                            if (settings.customYoutubeApiKey.isNotBlank()) "Configured" else "Not set (Using built-in playback)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text("Configure", color = MaterialTheme.colorScheme.primary, fontSize = 13.sp)
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                // Custom Web Sources
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            customSourceInput = ""
                            showAddSourceDialog = true
                        }
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Custom Web Sources", fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                        Text("${settings.customSources.size} sources configured", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add", tint = MaterialTheme.colorScheme.primary)
                }

                settings.customSources.forEach { source ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = source,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { viewModel.removeCustomSource(source) }) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Remove", tint = Color(0xFFFF5252), modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }

        // Playback
        item {
            SettingsCategoryCard(title = "Playback", icon = Icons.Default.PlayCircle) {
                SettingsSwitchRow(
                    title = "Auto Fullscreen",
                    subtitle = "Automatically open player in landscape fullscreen",
                    checked = settings.autoFullscreen,
                    onCheckedChange = { viewModel.setAutoFullscreen(it) }
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                SettingsSwitchRow(
                    title = "Remember Playback Position",
                    subtitle = "Resume video where you left off",
                    checked = settings.rememberPosition,
                    onCheckedChange = { viewModel.setRememberPosition(it) }
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                SettingsSwitchRow(
                    title = "Autoplay",
                    subtitle = "Automatically start playback when opening video",
                    checked = settings.autoplay,
                    onCheckedChange = { viewModel.setAutoplay(it) }
                )
            }
        }

        // Network
        item {
            SettingsCategoryCard(title = "Network", icon = Icons.Default.NetworkCheck) {
                SettingsSwitchRow(
                    title = "Stream on Wi-Fi Only",
                    subtitle = "Avoid streaming video on mobile cellular data",
                    checked = settings.wifiOnly,
                    onCheckedChange = { viewModel.setWifiOnly(it) }
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                SettingsSwitchRow(
                    title = "Mobile Data Allowed",
                    subtitle = "Allow browsing video catalogs over mobile network",
                    checked = settings.mobileDataAllowed,
                    onCheckedChange = { viewModel.setMobileDataAllowed(it) }
                )
            }
        }

        // Privacy & Data
        item {
            SettingsCategoryCard(title = "Privacy & Local Data", icon = Icons.Default.Security) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.clearHistory() }
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Clear Watch History", color = MaterialTheme.colorScheme.onSurface)
                    Text("Clear", color = MaterialTheme.colorScheme.primary, fontSize = 13.sp)
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showClearDataDialog = true }
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Clear All Local Data", color = Color(0xFFFF5252))
                    Icon(imageVector = Icons.Default.DeleteForever, contentDescription = null, tint = Color(0xFFFF5252))
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showPrivacyPolicyDialog = true }
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Privacy Policy", color = MaterialTheme.colorScheme.onSurface)
                    Text("Read", color = MaterialTheme.colorScheme.primary, fontSize = 13.sp)
                }
            }
        }

        // Iklan Promosi Nasadef (Requirement 2)
        item {
            SettingsCategoryCard(title = "Iklan Promosi Nasadef™", icon = Icons.Default.Campaign) {
                Text(
                    text = "Iklan promosi dipaparkan secara rawak setiap 20 minit penggunaan aktif bagi mempromosikan web app rasmi (Al-Quran Interaktif, Pemantauan Banjir IoT, Sky Metropolis 3D, dan Portal Nasadef).",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(10.dp))
                if (onTriggerAdPreview != null) {
                    OutlinedButton(
                        onClick = onTriggerAdPreview,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.tvFocusable(shape = RoundedCornerShape(8.dp), onEnterClick = onTriggerAdPreview)
                    ) {
                        Icon(imageVector = Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Pratonton Iklan Promosi Sekarang", fontSize = 12.sp)
                    }
                }
            }
        }

        // About
        item {
            SettingsCategoryCard(title = "About Video Hub", icon = Icons.Default.Info) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Application Version", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("1.0.0", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Target Platforms", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("Phone, Tablet, Android TV", color = MaterialTheme.colorScheme.onSurface)
                }
            }
        }

        // Nasadef Branding Footer
        item {
            com.example.ui.components.NasadefFooter()
        }
    }
}

@Composable
fun SettingsCategoryCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
fun SettingsSwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}
