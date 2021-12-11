package com.plcoding.soundrecognition.data.models

data class DataSoundParameters(val typeName:String, val signalEnvelope:List<Integer>,
                          val rootMeanSquareEnergy:List<Integer>, val zeroCrossingDensity:Integer,
                          val powerSpectrum:List<Integer>, val spectralCentroids:List<Double>,
                          val spectralFluxes:List<Integer>, val spectralRollOffPoints:List<Double>) {
    override fun toString(): String {
        return "Params(zeroCrossingDensity=$zeroCrossingDensity \n" +
                "signalEnvelope=$signalEnvelope \n " +
                "rootMeanSquareEnergy=$rootMeanSquareEnergy) \n" +
                "powerSpectrum=$powerSpectrum \n " +
                "spectralCentroids=$spectralCentroids \n " +
                "spectralFluxes=$spectralFluxes \n " +
                "spectralRollOffPoints=$spectralRollOffPoints \n "
    }
}