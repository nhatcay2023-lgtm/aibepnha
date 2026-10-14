package com.example.data.remote

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiApiService {
    private const val TAG = "GeminiApiService"
    private const val MODEL = "gemini-3.5-flash"
    private const val ENDPOINT = "https://generativelanguage.googleapis.com/v1beta/models/$MODEL:generateContent"

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    suspend fun generateRecipeOrSuggestion(prompt: String, customApiKey: String? = null): String = withContext(Dispatchers.IO) {
        val apiKey = if (!customApiKey.isNullOrBlank()) {
            customApiKey
        } else {
            try {
                BuildConfig.GEMINI_API_KEY
            } catch (e: Exception) {
                ""
            }
        }

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            Log.w(TAG, "Gemini API key is not configured. Falling back to local AI assistant response.")
            return@withContext getOfflineAiSuggestion(prompt)
        }

        try {
            val jsonBody = JSONObject().apply {
                val contents = JSONArray()
                val contentObj = JSONObject()
                val parts = JSONArray()
                val partObj = JSONObject()
                partObj.put("text", "Bạn là AI Bếp Nhà - trợ lý ẩm thực và đầu bếp thông minh gia đình Việt Nam. Hãy trả lời thân thiện, súc tích và thiết thực: $prompt")
                parts.put(partObj)
                contentObj.put("parts", parts)
                contents.put(contentObj)
                put("contents", contents)
            }

            val requestBody = jsonBody.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url("$ENDPOINT?key=$apiKey")
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            val responseString = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                Log.e(TAG, "API call failed code ${response.code}: $responseString")
                return@withContext getOfflineAiSuggestion(prompt)
            }

            val jsonResponse = JSONObject(responseString)
            val candidates = jsonResponse.optJSONArray("candidates")
            if (candidates != null && candidates.length() > 0) {
                val firstCandidate = candidates.getJSONObject(0)
                val content = firstCandidate.optJSONObject("content")
                val partsArray = content?.optJSONArray("parts")
                if (partsArray != null && partsArray.length() > 0) {
                    return@withContext partsArray.getJSONObject(0).optString("text", "Không nhận được phản hồi từ AI.")
                }
            }
            getOfflineAiSuggestion(prompt)
        } catch (e: Exception) {
            Log.e(TAG, "Error querying Gemini API", e)
            getOfflineAiSuggestion(prompt)
        }
    }

    private fun getOfflineAiSuggestion(prompt: String): String {
        val lower = prompt.lowercase()
        return when {
            lower.contains("cá kho") || lower.contains("kho") -> {
                "💡 Gợi ý AI Bếp Nhà: Món Cá kho gừng đậm đà chuẩn vị!\n- 400g cá quả/cá bống\n- Gừng tươi thái sợi, ớt hiểm, nước mắm ngon, tiêu sọ\nKho lửa riu riu 30 phút cho cá săn chắc, nước sốt sánh mịn ăn kèm cơm nóng."
            }
            lower.contains("bắp cải") || lower.contains("luộc") || lower.contains("canh") -> {
                "💡 Gợi ý AI Bếp Nhà: Bắp cải luộc chấm trứng giầm mắm hoặc kho quẹt:\n- 600g bắp cải ngọt mọng\n- Nước luộc thêm chút gừng đập dập làm nước canh thanh mát giải nhiệt."
            }
            lower.contains("chi tiêu") || lower.contains("tiết kiệm") -> {
                "💡 Lời khuyên chi tiêu: Ngân sách hôm nay còn 2.680.000 đ cho nửa tháng còn lại. Ưu tiên đi chợ buổi sáng sớm mua rau củ quả tươi ngon và thịt cá giá tốt nhất!"
            }
            else -> {
                "🤖 AI Bếp Nhà sẵn sàng hỗ trợ bạn!\nThực đơn đề xuất hôm nay rất cân bằng dinh dưỡng: Cá kho gừng bổ sung đạm và omega-3, kết hợp Bắp cải luộc thanh mát giàu chất xơ cho cả nhà 4 người."
            }
        }
    }
}
