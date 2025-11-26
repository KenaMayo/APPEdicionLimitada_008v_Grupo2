package com.vivitasol.carcasamvvm.utils

import java.text.NumberFormat
import java.util.Locale

fun formatPrice(price: Double): String {
    return NumberFormat.getCurrencyInstance(Locale("es", "CL")).format(price)
}
