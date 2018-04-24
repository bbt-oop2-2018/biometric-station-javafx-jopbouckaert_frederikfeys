/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biometricstationclient;

/**
 *
 * @author jopbo_000
 */
public class BioMetricStationStringParser {

    public SensorData parse(String dataString) {

        if (!isValidString(dataString)) {
            return null;
        }else{
        
        int temperatureStart = dataString.indexOf("|");
        int heartBeatStart = dataString.indexOf("[");
        int heartBeatEnd = dataString.indexOf("]");
        String waterlevelString = dataString.substring(temperatureStart+1,heartBeatStart);
        String PUBGRankingString = dataString.substring(heartBeatStart +1,heartBeatEnd);
        double temperature = Double.parseDouble(waterlevelString);
        double heartBeat = Double.parseDouble(PUBGRankingString);
        
        return new SensorData(temperature, heartBeat);
        
        }

    }

    private boolean isValidString(String dataString) {
        return (dataString.indexOf("[") != -1
                && dataString.indexOf("]") != -1
                && dataString.indexOf("|") != -1);
    }

}
