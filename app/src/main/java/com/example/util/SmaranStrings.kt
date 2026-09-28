package com.example.util

import com.example.model.AppLanguage

object SmaranStrings {

    fun getDashboardGreeting(language: AppLanguage, name: String): String {
        return when (language) {
            AppLanguage.ASSAMESE -> "নমস্কাৰ $name! 🙏"
            AppLanguage.BODO -> "खुमुलिया $name! 🙏"
            AppLanguage.MEITEI -> "খোৰুমজৰি $name! 🙏"
            AppLanguage.BENGALI -> "নমস্কার $name! 🙏"
            AppLanguage.KHASI -> "Khublei $name! 🙏"
            AppLanguage.MIZO -> "Chibai $name! 🙏"
            AppLanguage.GARO -> "Salam $name! 🙏"
            AppLanguage.HINDI -> "नमस्ते $name! 🙏"
            AppLanguage.ENGLISH -> "Namaste $name! 🙏"
        }
    }

    fun getDashboardSubtitle(language: AppLanguage): String {
        return when (language) {
            AppLanguage.ASSAMESE -> "গুৱাহাটীৰ নিজৰ ঘৰত আপুনি শান্ত আৰু সুৰক্ষিত"
            AppLanguage.BODO -> "गावनि नखराव नोंथाङा गोजोन आरो रैखाथि दं"
            AppLanguage.MEITEI -> "অদোমগী য়ুমদা শান্তিগী ওইনা লৈরি"
            AppLanguage.BENGALI -> "নিজের বাড়িতে আপনি শান্ত ও সম্পূর্ণ নিরাপদ"
            AppLanguage.KHASI -> "Phi don ha iing ba shngain bad suk"
            AppLanguage.MIZO -> "I in ah thlamuang leh him takin i awm"
            AppLanguage.GARO -> "Nang·ni nok·o tom·tom aro chel·chake dong·a"
            AppLanguage.HINDI -> "आप अपने घर में शांत और पूर्ण सुरक्षित हैं"
            AppLanguage.ENGLISH -> "You are safe, calm and peaceful at home"
        }
    }

    fun getPhaseOfDay(language: AppLanguage, phase: String): String {
        return when (language) {
            AppLanguage.ASSAMESE -> when (phase) {
                "Morning" -> "ৰাতিপুৱা • Morning"
                "Afternoon" -> "দুপৰীয়া • Afternoon"
                "Evening" -> "সন্ধিয়া • Evening"
                else -> "ৰাতি • Night"
            }
            AppLanguage.BODO -> when (phase) {
                "Morning" -> "फुंनि समाव • Morning"
                "Afternoon" -> "सान्जा समाव • Afternoon"
                "Evening" -> "बेलासिया समाव • Evening"
                else -> "होरनि समाव • Night"
            }
            AppLanguage.MEITEI -> when (phase) {
                "Morning" -> "অয়ুক্কী মতম • Morning"
                "Afternoon" -> "নুংথিলগী মতম • Afternoon"
                "Evening" -> "নুমিদাংগী মতম • Evening"
                else -> "অহিংগী মতম • Night"
            }
            AppLanguage.BENGALI -> when (phase) {
                "Morning" -> "সকাল বেলা • Morning"
                "Afternoon" -> "দুপুর বেলা • Afternoon"
                "Evening" -> "সন্ধ্যা বেলা • Evening"
                else -> "রাত্রি • Night"
            }
            AppLanguage.KHASI -> when (phase) {
                "Morning" -> "Mynstep • Morning"
                "Afternoon" -> "Mynsngi • Afternoon"
                "Evening" -> "Mynmiet • Evening"
                else -> "Mynstep • Night"
            }
            AppLanguage.MIZO -> when (phase) {
                "Morning" -> "Zinglam • Morning"
                "Afternoon" -> "Chhunlam • Afternoon"
                "Evening" -> "Tlai lam • Evening"
                else -> "Zan lam • Night"
            }
            AppLanguage.GARO -> when (phase) {
                "Morning" -> "Pringo • Morning"
                "Afternoon" -> "Saljanto • Afternoon"
                "Evening" -> "Attamo • Evening"
                else -> "Walo • Night"
            }
            AppLanguage.HINDI -> when (phase) {
                "Morning" -> "सुबह का समय • Morning"
                "Afternoon" -> "दोपहर का समय • Afternoon"
                "Evening" -> "शाम का समय • Evening"
                else -> "रात का समय • Night"
            }
            AppLanguage.ENGLISH -> "$phase Time"
        }
    }

    fun getMoodTitle(language: AppLanguage): String {
        return when (language) {
            AppLanguage.ASSAMESE -> "আজি আপোনাৰ মন কেনে লাগিছে?"
            AppLanguage.BODO -> "दिनै नोंथांनि गोसोआ माबोरै दं?"
            AppLanguage.MEITEI -> "ঙসি অদোমগী ৱাখল কমদৌরি?"
            AppLanguage.BENGALI -> "আজ আপনার মন কেমন লাগছে?"
            AppLanguage.KHASI -> "Kumno phi sngew mynta ka sngi?"
            AppLanguage.MIZO -> "Vawiin i rilru a nuam em?"
            AppLanguage.GARO -> "Da·alo nang·ni gisik mai donga?"
            AppLanguage.HINDI -> "आज आपका मन कैसा लग रहा है?"
            AppLanguage.ENGLISH -> "How are you feeling right now?"
        }
    }

    fun getMoodLabel(language: AppLanguage, moodKey: String): String {
        return when (moodKey) {
            "Peaceful" -> when (language) {
                AppLanguage.ASSAMESE -> "শান্ত • Peaceful"
                AppLanguage.BODO -> "गोजোন • Peaceful"
                AppLanguage.MEITEI -> "শান্তি • Peaceful"
                AppLanguage.BENGALI -> "শান্ত • Peaceful"
                AppLanguage.KHASI -> "Suk • Peaceful"
                AppLanguage.MIZO -> "Thlamuang • Peaceful"
                AppLanguage.GARO -> "Tom·tom • Peaceful"
                AppLanguage.HINDI -> "शांत • Peaceful"
                AppLanguage.ENGLISH -> "Peaceful"
            }
            "Happy" -> when (language) {
                AppLanguage.ASSAMESE -> "আনন্দিত • Happy"
                AppLanguage.BODO -> "रंजानाय • Happy"
                AppLanguage.MEITEI -> "হরাওবা • Happy"
                AppLanguage.BENGALI -> "আনন্দিত • Happy"
                AppLanguage.KHASI -> "Kmen • Happy"
                AppLanguage.MIZO -> "Hlim • Happy"
                AppLanguage.GARO -> "Kusi • Happy"
                AppLanguage.HINDI -> "प्रसन्न • Happy"
                AppLanguage.ENGLISH -> "Happy"
            }
            "Confused" -> when (language) {
                AppLanguage.ASSAMESE -> "বিভ্ৰান্ত • Confused"
                AppLanguage.BODO -> "गोन्दोगोला • Confused"
                AppLanguage.MEITEI -> "খঙদবা • Confused"
                AppLanguage.BENGALI -> "বিভ্রান্ত • Confused"
                AppLanguage.KHASI -> "Kulmar • Confused"
                AppLanguage.MIZO -> "Hrilhhai • Confused"
                AppLanguage.GARO -> "Jajreng • Confused"
                AppLanguage.HINDI -> "उलझन में • Confused"
                AppLanguage.ENGLISH -> "Confused"
            }
            "Sad" -> when (language) {
                AppLanguage.ASSAMESE -> "দুখী • Sad"
                AppLanguage.BODO -> "दुखु • Sad"
                AppLanguage.MEITEI -> "নুংঙাইতবা • Sad"
                AppLanguage.BENGALI -> "মন খারাপ • Sad"
                AppLanguage.KHASI -> "Sngewsih • Sad"
                AppLanguage.MIZO -> "Lungngai • Sad"
                AppLanguage.GARO -> "Duk • Sad"
                AppLanguage.HINDI -> "उदास • Sad"
                AppLanguage.ENGLISH -> "Sad"
            }
            "Tired" -> when (language) {
                AppLanguage.ASSAMESE -> "ভাগৰুৱা • Tired"
                AppLanguage.BODO -> "थावनाय • Tired"
                AppLanguage.MEITEI -> "থোকপা • Tired"
                AppLanguage.BENGALI -> "ক্লান্ত • Tired"
                AppLanguage.KHASI -> "Thait • Tired"
                AppLanguage.MIZO -> "Chau • Tired"
                AppLanguage.GARO -> "Neng·a • Tired"
                AppLanguage.HINDI -> "थका हुआ • Tired"
                AppLanguage.ENGLISH -> "Tired"
            }
            else -> moodKey
        }
    }

    fun getWhereAmILabel(language: AppLanguage): String {
        return when (language) {
            AppLanguage.ASSAMESE -> "মই ক'ত আছোঁ? (Where Am I?)"
            AppLanguage.BODO -> "आं बबेयाव दं? (Where Am I?)"
            AppLanguage.MEITEI -> "ঐ কদায়দা লৈরি? (Where Am I?)"
            AppLanguage.BENGALI -> "আমি কোথায় আছি? (Where Am I?)"
            AppLanguage.KHASI -> "Hangno nga don? (Where Am I?)"
            AppLanguage.MIZO -> "Khawiah nge ka awm? (Where Am I?)"
            AppLanguage.GARO -> "Bano nga donga? (Where Am I?)"
            AppLanguage.HINDI -> "मैं कहाँ हूँ? (Where Am I?)"
            AppLanguage.ENGLISH -> "Where Am I? (Tap to orient)"
        }
    }

    fun getWhereAmISubtitle(language: AppLanguage): String {
        return when (language) {
            AppLanguage.ASSAMESE -> "গুৱাহাটীৰ নিজৰ ঘৰত • পৰিয়ালৰ লগত"
            AppLanguage.BODO -> "गुवाहाटी गावनि नखराव • नखरजों लोगोसे"
            AppLanguage.MEITEI -> "গুৱাহাটীগী য়ুমদা • ইমুংগা লোয়ননা"
            AppLanguage.BENGALI -> "গুয়াহাটির নিজের বাড়িতে • পরিবারের সাথে"
            AppLanguage.KHASI -> "Ha iing ha Guwahati • Bad ka longing"
            AppLanguage.MIZO -> "Guwahati in ah • Chhungte nen"
            AppLanguage.GARO -> "Guwahati noko • Nokdang baksa"
            AppLanguage.HINDI -> "गुवाहाटी के अपने घर में • परिवार के साथ"
            AppLanguage.ENGLISH -> "Guwahati home • With your loving family"
        }
    }

    fun getMedicineSectionTitle(language: AppLanguage): String {
        return when (language) {
            AppLanguage.ASSAMESE -> "ঔষধ আৰু পথ্যৰ সময়"
            AppLanguage.BODO -> "मुलि लोंनायनि सम"
            AppLanguage.MEITEI -> "হীদাক চা órgী মতম"
            AppLanguage.BENGALI -> "ওষুধ ও স্বাস্থ্যের সময়"
            AppLanguage.KHASI -> "Ka por dih dawai"
            AppLanguage.MIZO -> "Damdawi ei hun"
            AppLanguage.GARO -> "Sam rona somoi"
            AppLanguage.HINDI -> "दवा और स्वास्थ्य का समय"
            AppLanguage.ENGLISH -> "Medicines & Daily Health"
        }
    }

    fun getPlayGamesTitle(language: AppLanguage): String {
        return when (language) {
            AppLanguage.ASSAMESE -> "মগজুৰ আনন্দ খেল"
            AppLanguage.BODO -> "मेमोरी गेलेनाय"
            AppLanguage.MEITEI -> "ৱাখলগী শান্নবা"
            AppLanguage.BENGALI -> "স্মৃতির খেলা"
            AppLanguage.KHASI -> "Kynmaw Games"
            AppLanguage.MIZO -> "Hriatreuna Games"
            AppLanguage.GARO -> "Gisik Kal·ani"
            AppLanguage.HINDI -> "स्मृति और दिमागी खेल"
            AppLanguage.ENGLISH -> "Memory & Cognitive Games"
        }
    }

    fun getFamilyTitle(language: AppLanguage): String {
        return when (language) {
            AppLanguage.ASSAMESE -> "মোৰ মৰমৰ পৰিয়াল"
            AppLanguage.BODO -> "आंनि नखर"
            AppLanguage.MEITEI -> "ঐগী ইমুং"
            AppLanguage.BENGALI -> "আমার প্রিয় পরিবার"
            AppLanguage.KHASI -> "Ka Longing jong nga"
            AppLanguage.MIZO -> "Ka Chhungte"
            AppLanguage.GARO -> "Angni Nokdang"
            AppLanguage.HINDI -> "मेरा प्रिय परिवार"
            AppLanguage.ENGLISH -> "My Loving Family"
        }
    }

    fun getAiCompanionTitle(language: AppLanguage): String {
        return when (language) {
            AppLanguage.ASSAMESE -> "স্মৰণ এআই সহায়ক"
            AppLanguage.BODO -> "स्मरण एआई संगी"
            AppLanguage.MEITEI -> "স্মরণ এআই মতেংপাংবা"
            AppLanguage.BENGALI -> "স্মরণ এআই সাথী"
            AppLanguage.KHASI -> "Smaran AI Companion"
            AppLanguage.MIZO -> "Smaran AI Thian"
            AppLanguage.GARO -> "Smaran AI Rirang"
            AppLanguage.HINDI -> "स्मरण एआई साथी"
            AppLanguage.ENGLISH -> "Smaran AI Companion"
        }
    }

    fun getSosButtonTitle(language: AppLanguage): String {
        return when (language) {
            AppLanguage.ASSAMESE -> "জৰুৰীকালীন সাহায্য (SOS)"
            AppLanguage.BODO -> "जुरुरी हेफाजाब (SOS)"
            AppLanguage.MEITEI -> "অকনবা মতেং (SOS)"
            AppLanguage.BENGALI -> "জরুরী সাহায্য (SOS)"
            AppLanguage.KHASI -> "Ka Jingiarap Kyrkieh (SOS)"
            AppLanguage.MIZO -> "Taimakna Hmanhmawh (SOS)"
            AppLanguage.GARO -> "Rang·san Dakchakani (SOS)"
            AppLanguage.HINDI -> "आपातकालीन सहायता (SOS)"
            AppLanguage.ENGLISH -> "Emergency SOS Alert"
        }
    }

    fun getLanguageBarLabel(language: AppLanguage): String {
        return when (language) {
            AppLanguage.ASSAMESE -> "🗣️ ভাষা সলনি কৰক (Choose Language):"
            AppLanguage.BODO -> "🗣️ राव सायख'नाय (Language):"
            AppLanguage.MEITEI -> "🗣️ লোন খনবা (Language):"
            AppLanguage.BENGALI -> "🗣️ ভাষা পরিবর্তন করুন (Language):"
            AppLanguage.KHASI -> "🗣️ Jied ia ka Ktien (Language):"
            AppLanguage.MIZO -> "🗣️ Tawng thlang rawh (Language):"
            AppLanguage.GARO -> "🗣️ Ku·sik seokbo (Language):"
            AppLanguage.HINDI -> "🗣️ भाषा चुनें (Choose Language):"
            AppLanguage.ENGLISH -> "🗣️ Choose Language / ভাষা:"
        }
    }

    fun getWhereAmIDialogText(language: AppLanguage): String {
        return when (language) {
            AppLanguage.ASSAMESE -> "বৰুৱা দেউতা, আপুনি আপোনাৰ নিজৰ ঘৰত গুৱাহাটীৰ দীঘলীপুখুৰীৰ কাষত আছে। আপোনাৰ লগত পুত্ৰ ৰাহুল আৰু বোৱাৰী প্ৰিয়া আছে। আশা কৰ্মী অঞ্জলী ডেকাই আপোনাৰ স্বাস্থ্য নিয়মিত পৰীক্ষা কৰে। আপুনি সম্পূৰ্ণ সুৰক্ষিত!"
            AppLanguage.BODO -> "बरुवा देउता, नोंथाङा गावनि नखराव गुवाहाटीयाव दं। नोंथांनि पुत्रा राहुल आरो बवा'री प्रिया दं। आंखा खर्मी अनजली देगा नोंथांनि सावस्रिखौ नोजोर लायो। नोंथाङा रैखाथि दं!"
            AppLanguage.MEITEI -> "বরুৱা দেউতা, অদোম গুৱাহাটীদা অদোমগী য়ুমদা লৈরি। অদোমগী মচা নুপা রাহুল অমসুং মচা নুপী প্রিয়া লোয়ননা লৈরি। অদোম সম্পূৰ্ণ শান্তিগী ওইনা লৈরি!"
            AppLanguage.BENGALI -> "বড়ুয়া দেউতা, আপনি আপনার নিজের বাড়িতে গুয়াহাটিতে আছেন। আপনার সঙ্গে ছেলে রাহুল ও পুত্রবধূ প্রিয়া আছেন। আশা কর্মী অঞ্জলি ডেকা আপনার স্বাস্থ্যের খেয়াল রাখছেন। আপনি সম্পূর্ণ নিরাপদ!"
            AppLanguage.KHASI -> "Baruah Deuta, phi don ha iing lajong ha Guwahati. U khun jong phi u Rahul bad i Priya ki don bad phi. Phi shngain bad suk bha!"
            AppLanguage.MIZO -> "Baruah Deuta, Guwahati i in ngeiah i awm. I fapa Rahul leh i monu Priya te i bulah an awm e. Hlau suh, i him em em e!"
            AppLanguage.GARO -> "Baruah Deuta, nang·a Guwahati-ni nok·on donga. Nang·ni depante Rahul aro Priya nang·baksa donga. Nang·a namen chel·chaka!"
            AppLanguage.HINDI -> "बरुआ देउता, आप गुवाहाटी में अपने घर पर हैं। आपके बेटे राहुल और बहू प्रिया आपके साथ हैं। आशा कार्यकर्ता अंजलि डेका आपके स्वास्थ्य का ध्यान रखती हैं। आप बिल्कुल सुरक्षित हैं!"
            AppLanguage.ENGLISH -> "Baruah Deuta, you are peacefully at your home in Guwahati near Dighalipukhuri. Your son Rahul and daughter-in-law Priya are with you. ASHA worker Anjali Deka visits weekly. You are completely safe and cared for!"
        }
    }

    fun getChatInitialGreeting(language: AppLanguage): String {
        return when (language) {
            AppLanguage.ASSAMESE -> "নমস্কাৰ বৰুৱা দেউতা! 🙏 মই আপোনাৰ স্মৃতি সহযোগী স্মৰণ। আজি আপুনি কেনে অনুভৱ কৰিছে? আপোনাৰ কিবা সুধিবলৈ মন আছে নেকি?"
            AppLanguage.BODO -> "खुमुलिया बरुवा देउता! 🙏 आं नोंथांनि स्मरण संगी। दिनै नोंथाङा माबोरै दं? आंखौ जायखिजाया सोंनो हाগौ।"
            AppLanguage.MEITEI -> "খোৰুমজৰি বরুৱা দেউতা! 🙏 ঐ অদোমগী স্মরণ মতেংপাংবনি। ঙসি অদোম কমদৌরি? করিগুম্বা অমত্তা হংনিংবা লৈব্রা?"
            AppLanguage.BENGALI -> "নমস্কার বড়ুয়া দেউতা! 🙏 আমি আপনার স্মৃতি সঙ্গী স্মরণ। আজ আপনি কেমন আছেন? কোনো কথা জানতে চান?"
            AppLanguage.KHASI -> "Khublei Baruah Deuta! 🙏 Nga dei u Smaran, u paralok jong phi. Kumno phi sngew mynta ka sngi?"
            AppLanguage.MIZO -> "Chibai Baruah Deuta! 🙏 Smaran i thian ka ni e. Vawiin i tha em? Enge i sawi duh le?"
            AppLanguage.GARO -> "Salam Baruah Deuta! 🙏 Anga Smaran nang·ni rirang. Da·alo mai donga?"
            AppLanguage.HINDI -> "नमस्ते बरुआ देउता! 🙏 मैं आपका स्मृति साथी स्मरण हूँ। आज आप कैसा महसूस कर रहे हैं? क्या आप कुछ पूछना चाहते हैं?"
            AppLanguage.ENGLISH -> "Namaste Baruah Deuta! 🙏 I am Smaran, your memory companion. How are you feeling today in our peaceful Guwahati home?"
        }
    }

    fun getChatSuggestedPrompts(language: AppLanguage): List<String> {
        return when (language) {
            AppLanguage.ASSAMESE -> listOf(
                "মই ক'ত আছোঁ?",
                "ৰাহুল কোন?",
                "পৰৱৰ্তী ঔষধ কি?",
                "কাজিৰঙাৰ কথা কওক",
                "এটা বিহু গীত গাওক",
                "অঞ্জলী ডেকা কোন?"
            )
            AppLanguage.BODO -> listOf(
                "आं बबेयाव दं?",
                "राहुल सोर?",
                "मुलि लोंनायनि सम?",
                "काजिरङानि बाथ्रा खोनथा",
                "बिहु मेथाय खोन",
                "अनजली देगा सोर?"
            )
            AppLanguage.MEITEI -> listOf(
                "ঐ কদায়দা লৈরি?",
                "রাহুল কনানো?",
                "হীদাক চা órgী মতম?",
                "কাজিরঙ্গা ৱারী লীবিয়ু",
                "বিহু ঈশৈ শেকপিয়ু",
                "অঞ্জলি ডেকা কনানো?"
            )
            AppLanguage.BENGALI -> listOf(
                "আমি কোথায় আছি?",
                "রাহুল কে?",
                "পরের ওষুধ কোনটি?",
                "কাজিরাংগার গল্প বলুন",
                "একটি বিহু গান করুন",
                "অঞ্জলি ডেকা কে?"
            )
            AppLanguage.KHASI -> listOf(
                "Hangno nga don?",
                "Uei u Rahul?",
                "Ka dawai kaba bud?",
                "Iathuh shaphang Kaziranga",
                "Rwai ia ka jingrwai Bihu",
                "Uei i Anjali Deka?"
            )
            AppLanguage.MIZO -> listOf(
                "Khawiah nge ka awm?",
                "Rahul hi tunge?",
                "Damdawi ei leh tur?",
                "Kaziranga chanchin min hrilh teh",
                "Bihu hla sa teh",
                "Anjali Deka hi tunge?"
            )
            AppLanguage.GARO -> listOf(
                "Bano nga donga?",
                "Rahul sawa?",
                "Sam rona somoi?",
                "Kaziranga aganbo",
                "Bihu git ring·bo",
                "Anjali Deka sawa?"
            )
            AppLanguage.HINDI -> listOf(
                "मैं कहाँ हूँ?",
                "राहुल कौन है?",
                "अगली दवा कौन सी है?",
                "काजीरंगा के बारे में बताइए",
                "बिहू का गीत सुनाइए",
                "अंजलि डेका कौन हैं?"
            )
            AppLanguage.ENGLISH -> listOf(
                "Where am I?",
                "Who is Rahul?",
                "What medicine is next?",
                "Tell me about Kaziranga",
                "Sing a Bihu tune",
                "Who is Anjali Deka?"
            )
        }
    }

    fun getReadOrientationButton(language: AppLanguage): String {
        return when (language) {
            AppLanguage.ASSAMESE -> "🔊 সময় আৰু স্থান মোক শুনাই দিয়ক"
            AppLanguage.BODO -> "🔊 समाव आरो जायगानि बाथ्रा खोनथा"
            AppLanguage.MEITEI -> "🔊 মতম অমসুং মফম অদু তাবিয়ু"
            AppLanguage.BENGALI -> "🔊 সময় ও স্থান আমাকে পড়ে শোনান"
            AppLanguage.KHASI -> "🔊 Pule ia ka por bad ka jaka"
            AppLanguage.MIZO -> "🔊 Hun leh hmun min chhiarsak rawh"
            AppLanguage.GARO -> "🔊 Somoi aro biapko kna·atbo"
            AppLanguage.HINDI -> "🔊 समय और स्थान बोलकर सुनाएं"
            AppLanguage.ENGLISH -> "🔊 Read Out Time & Location to Me"
        }
    }

    fun getConfusedPromptTitle(language: AppLanguage): String {
        return when (language) {
            AppLanguage.ASSAMESE -> "বিভ্ৰান্ত বা দিশহাৰা অনুভৱ কৰিছে নেকি?"
            AppLanguage.BODO -> "गोन्दोगोला सानदों नामा?"
            AppLanguage.MEITEI -> "ৱাখল খঙদবা ওইরিবা?"
            AppLanguage.BENGALI -> "বিভ্রান্ত বা দিশেহারা লাগছে?"
            AppLanguage.KHASI -> "Sngew kulmar ne jah lynti?"
            AppLanguage.MIZO -> "I buai em ni? Khawiah nge ka awm?"
            AppLanguage.GARO -> "Jajrengenga ba rama gimaenga?"
            AppLanguage.HINDI -> "क्या आप उलझन या भटके हुए महसूस कर रहे हैं?"
            AppLanguage.ENGLISH -> "Feeling confused or lost?"
        }
    }

    fun getConfusedPromptSubtitle(language: AppLanguage): String {
        return when (language) {
            AppLanguage.ASSAMESE -> "ইয়াত স্পৰ্শ কৰক: আপুনি ক'ত আছে আমি বুজাই দিম"
            AppLanguage.BODO -> "बेयाव थु: नोंथाङा बबेयाव दं आं बुंगोन"
            AppLanguage.MEITEI -> "মসিদা নম্বিয়ু: অদোম কদায়দা লৈরি ঐখোয় খঙহনগনি"
            AppLanguage.BENGALI -> "এখানে স্পর্শ করুন: আপনি কোথায় আছেন মনে করিয়ে দেব"
            AppLanguage.KHASI -> "Kynjoh hangne: Ngin pynkynmaw hangno phi don"
            AppLanguage.MIZO -> "Hetah hmet rawh: I awmna kan hrilh ang che"
            AppLanguage.GARO -> "Iano nang·atbo: Nang·a bano donga agangen"
            AppLanguage.HINDI -> "यहाँ टैप करें: हम याद दिलाएंगे कि आप कहाँ हैं"
            AppLanguage.ENGLISH -> "Tap here: We will gently remind you where you are"
        }
    }

    fun getRightNowTitle(language: AppLanguage): String {
        return when (language) {
            AppLanguage.ASSAMESE -> "এই মুহূৰ্তত আপোনাৰ বাবে"
            AppLanguage.BODO -> "दा सानथौ नोंथांनि थाखाय"
            AppLanguage.MEITEI -> "হৌজিক মতমদা অদোমগীদমক"
            AppLanguage.BENGALI -> "এই মুহূর্তে আপনার জন্য করণীয়"
            AppLanguage.KHASI -> "Mynta ka por na ka bynta jong phi"
            AppLanguage.MIZO -> "Tun huna i tih tur pawimawh"
            AppLanguage.GARO -> "Da·o somoio nang·na namgipa"
            AppLanguage.HINDI -> "इस समय आपके लिए ज़रूरी"
            AppLanguage.ENGLISH -> "Right Now For You"
        }
    }

    fun getSaveLanguageButton(language: AppLanguage): String {
        return when (language) {
            AppLanguage.ASSAMESE -> "✓ ভাষা সংৰক্ষণ কৰক (Save & Apply)"
            AppLanguage.BODO -> "✓ राव थिनाय (Save & Apply)"
            AppLanguage.MEITEI -> "✓ লোন সেভ তৌবিয়ু (Save & Apply)"
            AppLanguage.BENGALI -> "✓ ভাষা পরিবর্তন সংরক্ষণ করুন (Save)"
            AppLanguage.KHASI -> "✓ Pynskhem ia ka Ktien (Save)"
            AppLanguage.MIZO -> "✓ Tawng thlanna vawng rawh (Save)"
            AppLanguage.GARO -> "✓ Ku·sik rakkibo (Save)"
            AppLanguage.HINDI -> "✓ भाषा सुरक्षित करें (Save & Apply)"
            AppLanguage.ENGLISH -> "✓ Save & Apply Language"
        }
    }

    fun getLanguageSavedToast(language: AppLanguage): String {
        return when (language) {
            AppLanguage.ASSAMESE -> "ভাষা সফলতাৰে সলনি কৰা হ'ল: অসমীয়া"
            AppLanguage.BODO -> "राव सोलायबाय: बर'"
            AppLanguage.MEITEI -> "লোন হোংদোক্লে: মৈতৈলোন্"
            AppLanguage.BENGALI -> "ভাষা পরিবর্তন করা হয়েছে: বাংলা"
            AppLanguage.KHASI -> "La pynkylla ktien: Khasi"
            AppLanguage.MIZO -> "Tawng thlak a ni e: Mizo"
            AppLanguage.GARO -> "Ku·sik dingtangataha: A·chik"
            AppLanguage.HINDI -> "भाषा बदल दी गई है: हिन्दी"
            AppLanguage.ENGLISH -> "Language changed to English"
        }
    }
}
