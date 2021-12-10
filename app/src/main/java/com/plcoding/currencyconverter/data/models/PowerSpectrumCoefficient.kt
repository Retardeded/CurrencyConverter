package com.plcoding.currencyconverter.data.models

class PowerSpectrumCoefficient(val powerSpectrumCoefficient:Double, val mergedCoefficient:Double) {
    override fun toString(): String {
        return "Coefficients:(\nmergedCoefficient=$mergedCoefficient\n" +
                "powerSpectrumCoefficient=$powerSpectrumCoefficient\n"
    }
}