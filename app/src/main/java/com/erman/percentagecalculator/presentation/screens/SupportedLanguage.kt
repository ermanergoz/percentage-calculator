package com.erman.percentagecalculator.presentation.screens

enum class SupportedLanguage(
    val code: String,
    val nativeName: String,
) {
    SYSTEM("", ""),
    ARABIC("ar", "العربية"),
    BENGALI("bn", "বাংলা"),
    CHINESE("zh-rCN", "中文 (简体)"),
    DUTCH("nl", "Nederlands"),
    ENGLISH("en", "English"),
    FILIPINO("fil", "Filipino"),
    FRENCH("fr", "Français"),
    GERMAN("de", "Deutsch"),
    HINDI("hi", "हिन्दी"),
    INDONESIAN("id", "Bahasa Indonesia"),
    ITALIAN("it", "Italiano"),
    JAPANESE("ja", "日本語"),
    KOREAN("ko", "한국어"),
    PORTUGUESE_BR("pt-rBR", "Português (Brasil)"),
    PORTUGUESE_PT("pt-rPT", "Português (Portugal)"),
    RUSSIAN("ru", "Русский"),
    SPANISH("es", "Español"),
    TURKISH("tr-rTR", "Türkçe"),
    URDU("ur", "اردو"),
}
