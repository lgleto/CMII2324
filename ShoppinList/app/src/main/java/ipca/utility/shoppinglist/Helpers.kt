package ipca.utility.shoppinglist

import java.text.Normalizer

fun String.removeAccentsLowerCase(): String {
    return Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "").lowercase()
}