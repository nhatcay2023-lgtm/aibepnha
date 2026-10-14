package com.example

import com.example.ui.viewmodel.AppScreen
import org.junit.Assert.*
import org.junit.Test
import java.util.regex.Pattern

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun appScreen_allScreensCovered() {
        val screens = AppScreen.values()
        assertEquals(14, screens.size) // 14 screens including MENU and SHOPPING
    }

    @Test
    fun ingredientScaling_scalesCorrectly() {
        val input = "400g cá, 1 củ gừng, 2 thìa mắm"
        // Scale from 4 to 6 people (1.5x)
        val regex = Pattern.compile("(\\d+(\\.\\d+)?)\\s*([a-zA-Zà-ỹÀ-Ỹ]+)?(.*)")
        val items = input.split(",").map { it.trim() }
        val scaled = items.map { item ->
            val matcher = regex.matcher(item)
            if (matcher.find()) {
                val num = matcher.group(1)?.toDoubleOrNull() ?: 1.0
                val unit = matcher.group(3) ?: ""
                val rest = matcher.group(4) ?: ""
                val scaledNum = num * 1.5
                val formatted = if (scaledNum % 1.0 == 0.0) scaledNum.toInt().toString() else "%.1f".format(scaledNum)
                "$formatted$unit$rest".trim()
            } else {
                item
            }
        }.joinToString(", ")
        assertTrue(scaled.contains("600g cá"))
    }

    @Test
    fun shoppingCategorization_categorizesCorrectly() {
        val meatKeywords = listOf("thịt", "cá", "sườn", "tôm", "trứng")
        val vegKeywords = listOf("rau", "bắp cải", "cà chua", "gừng", "hành")
        val dryKeywords = listOf("mắm", "muối", "đường", "dầu ăn")

        fun categorize(name: String): String {
            val lower = name.lowercase().trim()
            return when {
                meatKeywords.any { lower.contains(it) } -> "Thịt, cá, hải sản"
                vegKeywords.any { lower.contains(it) } -> "Rau củ, trái cây"
                dryKeywords.any { lower.contains(it) } -> "Gia vị, đồ khô"
                else -> "Khác"
            }
        }

        assertEquals("Thịt, cá, hải sản", categorize("400g cá quả"))
        assertEquals("Thịt, cá, hải sản", categorize("500g thịt ba chỉ"))
        assertEquals("Rau củ, trái cây", categorize("600g bắp cải"))
        assertEquals("Gia vị, đồ khô", categorize("3 thìa nước mắm"))
    }
}
