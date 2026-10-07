package com.example.ui.web

import androidx.lifecycle.ViewModel
import com.example.domain.model.DefaultWebLinks
import com.example.domain.model.WebLinkItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class WebViewModel : ViewModel() {

    val webLinks: List<WebLinkItem> = DefaultWebLinks.items

    private val _selectedWebLink = MutableStateFlow(webLinks.first())
    val selectedWebLink: StateFlow<WebLinkItem> = _selectedWebLink.asStateFlow()

    fun selectLink(item: WebLinkItem) {
        _selectedWebLink.value = item
    }

    fun selectLinkByUrl(url: String) {
        val matched = webLinks.firstOrNull { it.url == url }
        if (matched != null) {
            _selectedWebLink.value = matched
        }
    }
}
