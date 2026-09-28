package com.example.network

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

class GeminiService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    suspend fun generateCompanionResponse(
        userPrompt: String,
        configuredKey: String?,
        modelName: String = "gemini-2.5-flash",
        languageCode: String = "en"
    ): String = withContext(Dispatchers.IO) {
        val resolvedKey = if (!configuredKey.isNullOrBlank()) {
            configuredKey
        } else {
            try {
                BuildConfig.GEMINI_API_KEY.takeIf { it.isNotBlank() && it != "MY_GEMINI_API_KEY" }
            } catch (e: Throwable) {
                null
            }
        }

        // If no valid key, or offline request, use high-fidelity Smart Offline Companion
        if (resolvedKey.isNullOrBlank()) {
            return@withContext getSmartOfflineResponse(userPrompt, languageCode)
        }

        try {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/$modelName:generateContent?key=$resolvedKey"

            val langName = when (languageCode) {
                "as" -> "Assamese (অসমীয়া)"
                "brx" -> "Bodo (बर')"
                "mni" -> "Manipuri / Meitei (মৈতৈলোন্)"
                "bn" -> "Bengali (বাংলা)"
                "kha" -> "Khasi"
                "lus" -> "Mizo"
                "grt" -> "Garo (A·chik)"
                "hi" -> "Hindi (हिन्दी)"
                else -> "English"
            }

            val systemPrompt = """
                You are Smaran, an empathetic, respectful, and culturally anchored AI memory companion for Bhaben Baruah (Deuta), a 74-year-old elder with mild cognitive impairment / early dementia living peacefully in Guwahati, Assam (near Dighalipukhuri).
                Key context:
                - Son: Rahul Baruah (lives with him, works in Guwahati, returns by 6:00 PM).
                - Daughter-in-law: Priya (prepares his warm ginger tea and meals).
                - Granddaughter: Meera (loves playing Ludo and singing Bihu tunes with him).
                - ASHA Worker: Anjali Deka from Guwahati PHC Sub-Center 04 (checks his BP weekly).
                - Daily routine: Morning tea with Telmisartan blood pressure tablet, afternoon Kaziranga memory game, evening courtyard stroll.
                Rules:
                - CRITICAL LANGUAGE DIRECTIVE: Always reply in $langName. Use gentle, conversational, comforting phrases native to $langName.
                - Speak with gentle warmth, respect ('Baruah Deuta' or 'Deuta'), and patience.
                - Keep answers clear, reassuring, and 2 to 4 sentences long to prevent cognitive fatigue.
                - Never scold, contradict aggressively, or cause anxiety. Orient gently to time, family, and home.
                - When speaking in Assamese or about Northeast India, mention soothing familiar landmarks (Brahmaputra, Kaziranga rhinos, fresh tea leaves, Bihu folk songs).
            """.trimIndent()

            val rootJson = JSONObject()
            val contentsArray = JSONArray()
            val contentObj = JSONObject()
            val partsArray = JSONArray()
            val partObj = JSONObject()
            partObj.put("text", userPrompt)
            partsArray.put(partObj)
            contentObj.put("parts", partsArray)
            contentsArray.put(contentObj)
            rootJson.put("contents", contentsArray)

            // System instruction
            val sysInstructionObj = JSONObject()
            val sysParts = JSONArray()
            val sysPart = JSONObject()
            sysPart.put("text", systemPrompt)
            sysParts.put(sysPart)
            sysInstructionObj.put("parts", sysParts)
            rootJson.put("systemInstruction", sysInstructionObj)

            val requestBody = rootJson.toString().toRequestBody(jsonMediaType)
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            if (response.isSuccessful && !responseBody.isNullOrBlank()) {
                val respJson = JSONObject(responseBody)
                val candidates = respJson.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val candidate = candidates.getJSONObject(0)
                    val content = candidate.optJSONObject("content")
                    val parts = content?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        val text = parts.getJSONObject(0).optString("text")
                        if (text.isNotBlank()) {
                            return@withContext text.trim()
                        }
                    }
                }
            }
            Log.w("GeminiService", "API call returned empty or non-200 ($responseBody), falling back to Smart Offline Mode")
            return@withContext getSmartOfflineResponse(userPrompt, languageCode)
        } catch (e: Exception) {
            Log.e("GeminiService", "Gemini API error, falling back to Smart Offline Mode", e)
            return@withContext getSmartOfflineResponse(userPrompt, languageCode)
        }
    }

    fun getSmartOfflineResponse(query: String, languageCode: String): String {
        val q = query.lowercase().trim()

        return when {
            q.contains("where am i") || q.contains("where are we") || q.contains("place") || q.contains("location") || q.contains("ক'ত") || q.contains("बबेयाव") || q.contains("কদায়দা") -> {
                when (languageCode) {
                    "as" -> "বৰুৱা দেউতা, আপুনি আপোনাৰ নিজৰ ঘৰত গুৱাহাটীৰ দীঘলীপুখুৰীৰ কাষত আছে। আপুনি সম্পূৰ্ণ সুৰক্ষিত আৰু শান্ত পৰিৱেশত আছে। পুত্ৰ ৰাহুল আৰু বোৱাৰী প্ৰিয়া আপোনাৰ লগত আছে।"
                    "brx" -> "बरुवा देउता, नोंथाङा गुवाहाटीयाव गावनि नखराव दं। नोंथाङा रैखाथि आरो गोजोन दं। फिसा राहुल नोंथांनि खाथियाव दं।"
                    "mni" -> "বরুৱা দেউতা, অদোম গুৱাহাটীদা অদোমগী য়ুমদা লৈরি। অদোম সম্পূৰ্ণ শান্তিগী ওইনা লৈরি। রাহুল অমসুং প্রিয়া অদোমগা লৈরি।"
                    "bn" -> "বড়ুয়া দেউতা, আপনি গুয়াহাটির দীঘলীপুকুরির কাছে আপনার নিজের বাড়িতে আছেন। আপনি সম্পূর্ণ নিরাপদ এবং শান্তিতে আছেন।"
                    "kha" -> "Baruah Deuta, phi don ha iing lajong ha Guwahati. Phi shngain bad suk bha bad ka longing jong phi."
                    "lus" -> "Baruah Deuta, Guwahati i in ngeiah i awm e. I chhungte bulah thlamuang takin i awm."
                    "grt" -> "Baruah Deuta, nang·a Guwahati-ni nok·on donga. Nang·ni nokdang nang·baksa donga, chel·chaka."
                    "hi" -> "बरुआ देउता, आप गुवाहाटी में दीघालीपुखुरी के पास अपने घर पर हैं। आप बिल्कुल सुरक्षित हैं और राहुल कुछ ही देर में काम से वापस आ जाएगा।"
                    else -> "Baruah Deuta, you are in your peaceful home in Guwahati, right near Dighalipukhuri pond. You are completely safe and warm. Priya is preparing your fresh tea."
                }
            }

            q.contains("who is rahul") || q.contains("rahul") || q.contains("ৰাহুল") || q.contains("राहुल") -> {
                when (languageCode) {
                    "as" -> "ৰাহুল আপোনাৰ মৰমৰ পুত্ৰ। সি গুৱাহাটীত কাম কৰে আৰু প্ৰতিদিনে সন্ধিয়া ৬ বজাত অফিচৰ পৰা আহি আপোনাৰ লগত দীঘলীপুখুৰীত খোজ কাঢ়ে।"
                    "brx" -> "राहुल नोंथांनि फिसाजो। बियो गुवाहाटीयाव हाबा मावो आरो बेलासियाव ६ बाजायाव नआव फैगोन।"
                    "mni" -> "রাহুল অদোমগী মচা নুপানি। মহাক গুৱাহাটীদা থবক তৌরি অমসুং নুমিদাংদা অদোমগীদমক য়ুমদা হল্লক্কনি।"
                    "bn" -> "রাহুল আপনার স্নেহের পুত্র। সে গুয়াহাটিতে কাজ করে এবং প্রতিদিন সন্ধ্যায় আপনার সাথে সময় কাটাতে বাড়ি ফেরে।"
                    "kha" -> "U Rahul u dei u khun jong phi. U trei ha Guwahati bad un wan phai sha iing mynta ka miet."
                    "lus" -> "Rahul hi i fapa a ni a, Guwahati-ah hna a thawk a, tlai dar 6-ah in a lo thleng ang."
                    "grt" -> "Rahul nang·ni depante, Guwahati-o kam ka·a aro attamo nokona re·bagan."
                    "hi" -> "राहुल आपके प्यारे बेटे हैं। वह गुवाहाटी में काम करते हैं और शाम 6 बजे आपके साथ चाय पीने घर आ जाएंगे।"
                    else -> "Rahul is your loving son. He works here in Guwahati and will be home this evening by 6:00 PM to sit and share tea with you."
                }
            }

            q.contains("medicine") || q.contains("next") || q.contains("pill") || q.contains("tablet") || q.contains("ঔষধ") || q.contains("মুলি") || q.contains("হীদাক") -> {
                when (languageCode) {
                    "as" -> "আপোনাৰ ৰাতিপুৱাৰ ব্লাড প্ৰেচাৰ টেবলেট (Telmisartan) ইতিমধ্যে লোৱা হ'ল। দুপৰীয়া ১:৩০ বজাত স্মৃতি আৰু স্নায়ুৰ ব্ৰাহ্মী ড্ৰপছ লোৱাৰ সময় হ'ব।"
                    "brx" -> "फुंनि मुलि लोंखाबाय। सान्जायाव १:३० बाजायाव गोसोनि ब्राह्मी ड्रप्स लोंनो सम जागोन।"
                    "mni" -> "অয়ুক্কী ব্লাড প্রেসার হীদাক চারে। নুংথিল ১:৩০ তদা ব্রাহ্মী ড্রপ্স চাবগী মতম ওইরগনি।"
                    "bn" -> "সকালের ব্লাড প্রেশারের ওষুধ নেওয়া হয়েছে। দুপুরে ১:৩০ মিনিটে স্মৃতিশক্তি ও নার্ভের ব্রাহ্মী ড্রপস নেওয়ার সময়।"
                    "kha" -> "Ia ka dawai mynstep la dep dih. Sa ka dawai Brahmi ha ka por 1:30 mynsngi."
                    "lus" -> "Zing damdawi chu i ei tawh e. Chhun dar 1:30-ah Brahmi drops i ei leh dawn nia."
                    "grt" -> "Pringni samko ring·man·aha. Saljanto 1:30 bajeo Brahmi samko ring·gen."
                    "hi" -> "सुबह की ब्लड प्रेशर की दवा आप ले चुके हैं। अगली दवा दोपहर 1:30 बजे ब्राह्मी ड्रॉप्स है जो पानी के साथ लेनी है।"
                    else -> "Your morning blood pressure medicine (Telmisartan) is already completed. Next up at 1:30 PM is your memory neuro drops (B-Complex with Brahmi) after lunch."
                }
            }

            q.contains("kaziranga") || q.contains("rhino") || q.contains("wildlife") || q.contains("কাজিৰঙা") -> {
                when (languageCode) {
                    "as" -> "কাজিৰঙা আমাৰ অসমৰ গৌৰৱ! তাত এটা খড়্গ থকা গঁড়, বৰলুইত আৰু সেউজীয়া কাঞ্চনজংঘাৰ দৰে দৃশ্য অতি মনোৰম। আপোনাৰ লগত কাজিৰঙা স্মৃতি খেলখন খেলিবলৈ সাজু?"
                    "brx" -> "काजिरङाया आसामनि मुंदांखा अभयारण्य! बेवहाय गन्दै दं आरो गोजोन हाग्रामा दं। नोंथाङा गेलेनो लुबैयो नामा?"
                    "mni" -> "কাজিরঙ্গা অসি অসামগী চাউথোকচনিংঙাইনি! মফম অদুদা শমাজী অমসুং অশংবা লমদম লৈরি।"
                    "bn" -> "কাজিরাংগা আমাদের আসামের গৌরব! সেখানে একশৃঙ্গ গণ্ডার ও ব্রহ্মপুত্রের স্নিগ্ধ বাতাস মন শান্ত করে।"
                    "hi" -> "काजीरंगा असम का गौरव है! वहां एक सींग वाला गेंडा और ब्रह्मपुत्र की हरियाली बहुत सुंदर है।"
                    else -> "Kaziranga is our beloved Assam sanctuary, home to the magnificent one-horned rhinoceros and gentle deer along the Brahmaputra grasslands. Would you like to play our Kaziranga Memory Match?"
                }
            }

            q.contains("bihu") || q.contains("song") || q.contains("sing") || q.contains("rhyme") || q.contains("গীত") -> {
                when (languageCode) {
                    "as" -> "‘অ' মোৰ চেনেহী দেশ ঐ, অ' মোৰ সোণৰ দেশ ঐ...’ বিহুৰ ঢোল আৰু পেঁপাৰ মাত শুনিলে মনটো শান্ত আৰু আনন্দিত হৈ উঠে, নহয়নে দেউতা?"
                    "bn" -> "বিহুর মিষ্টি সুর মনকে শান্ত আর আনন্দে ভরিয়ে তোলে। 'অ' মোর চেনেহী দেশ ঐ...'।"
                    "hi" -> "बिहू की धुन सुनते ही मन शांत और खुश हो जाता है। बिहू के लोकगीत असम की आत्मा हैं।"
                    else -> "Ah, the sound of the Bihu Dhol and Pepa brings so much joy! 'O Mur Apunar Desh...' Let's hum along to a peaceful melody together."
                }
            }

            q.contains("how are you") || q.contains("hello") || q.contains("namaste") || q.contains("নমস্কাৰ") || q.contains("नमस्ते") || q.contains("खुमुलिया") -> {
                when (languageCode) {
                    "as" -> "নমস্কাৰ বৰুৱা দেউতা! মই স্মৰণ, আপোনাৰ সহযোগী। আপুনি আজি পুৱা কেনে অনুভৱ কৰিছে? আপোনাৰ কিবা সহায়ৰ প্ৰয়োজন আছে নেকি?"
                    "brx" -> "खुमुलिया बरुवा देउता! आं स्मरण, नोंथांनि संगी। दिनै नोंथाङा माबोरै दं?"
                    "mni" -> "খোৰুমজৰি বরুৱা দেউতা! ঐ স্মরণনি। ঙসি অদোম কমদৌরি? করিগুম্বা অমত্তা হংনিংবা লৈব্রা?"
                    "bn" -> "নমস্কার বড়ুয়া দেউতা! আমি স্মরণ, আপনার স্মৃতি সহায়ক। আজ আপনার কেমন লাগছে?"
                    "kha" -> "Khublei Baruah Deuta! Nga dei u Smaran. Kumno phi sngew mynta ka sngi?"
                    "lus" -> "Chibai Baruah Deuta! Smaran i thian ka ni e. Vawiin i tha maw?"
                    "grt" -> "Salam Baruah Deuta! Anga Smaran. Da·alo mai donga?"
                    "hi" -> "नमस्ते बरुआ देउता! मैं स्मरण हूँ, आपका साथी। आज आप कैसा महसूस कर रहे हैं? क्या आप कोई सुखद याद सुनना चाहेंगे?"
                    else -> "Namaste Baruah Deuta! 🙏 I am Smaran, your memory companion. How are you feeling today in our peaceful Guwahati home? Would you like to review your schedule or play a gentle game?"
                }
            }

            else -> {
                when (languageCode) {
                    "as" -> "বৰুৱা দেউতা, আপুনি গুৱাহাটীত নিজৰ ঘৰত সুৰক্ষিত আৰু শান্ত পৰিৱেশত আছে। মই সদায় আপোনাৰ কাষতেই আছোঁ।"
                    "brx" -> "बरुवा देउता, नोंथाङा गुवाहाटीयाव गावनि नखराव रैखाथि दं। आं नोंथांनि खाथियावनो दं।"
                    "mni" -> "বরুৱা দেউতা, অদোম য়ুমদা শান্তিগী ওইনা লৈরি। ঐ অদোমগী লোয়ননা লৈরি।"
                    "bn" -> "বড়ুয়া দেউতা, আপনি গুয়াহাটির নিজের বাড়িতে সম্পূর্ণ নিরাপদে আছেন। আমি সবসময় আপনার পাশেই আছি।"
                    "hi" -> "बरुआ देउता, आप अपने घर में सुरक्षित हैं। मैं हमेशा आपके साथ हूँ।"
                    else -> "Namaste Deuta. You are safe at home with your family in Guwahati. Take a deep breath of the gentle morning breeze. I am always right here with you."
                }
            }
        }
    }
}
