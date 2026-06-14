package com.pperotti.android.moviescatalogapp.presentation.common

import org.junit.Assert.*
import org.junit.Test

class UrlFormatterTest {

    @Test
    fun `null input returns null`() {
        assertNull(UrlFormatter.parse(null))
    }

    @Test
    fun `blank input returns null`() {
        assertNull(UrlFormatter.parse("   "))
    }

    @Test
    fun `invalid scheme returns null`() {
        assertNull(UrlFormatter.parse("ftp://files.example.com"))
        assertNull(UrlFormatter.parse("javascript:alert(1)"))
    }

    @Test
    fun `valid https url returns formatted result`() {
        val input = " https://www.example.com/movie/123 "
        val formatted = UrlFormatter.parse(input)
        assertNotNull(formatted)
        formatted?.let {
            assertEquals("www.example.com", it.displayText)
            assertEquals("https://www.example.com/movie/123", it.rawUrl)
            assertNotNull(it.onClick)
        }
    }

    @Test
    fun `missing host returns null`() {
        // URI like https:///path has no host
        assertNull(UrlFormatter.parse("https:///some/path"))
    }
}
