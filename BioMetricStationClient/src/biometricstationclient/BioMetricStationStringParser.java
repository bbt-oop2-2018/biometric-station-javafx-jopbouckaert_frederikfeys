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

        int temperatureStart = dataString.indexOf("ABCDE");
        int heartBeatStart = dataString.indexOf("|");
        int xAcceleroStart = dataString.indexOf("XXX");
        int yAcceleroStart = dataString.indexOf("YYY");
        int zAcceleroStart = dataString.indexOf("ZZZ");
        int zAcceleroEnd = dataString.indexOf("EDCBA");
        String temperatureString = dataString.substring(temperatureStart+5,heartBeatStart);
        String heartBeatString = dataString.substring(heartBeatStart +1,xAcceleroStart);
        String xAcceleroString = dataString.substring(xAcceleroStart +3,yAcceleroStart);
        String yAcceleroString = dataString.substring(yAcceleroStart +3,zAcceleroStart);
        String zAcceleroString = dataString.substring(zAcceleroStart +3,zAcceleroEnd);

        double temperature = Double.parseDouble(temperatureString);
        double heartBeat = Double.parseDouble(heartBeatString);
        double xAccelero = Double.parseDouble(xAcceleroString);
        double yAccelero = Double.parseDouble(yAcceleroString);
        double zAccelero = Double.parseDouble(zAcceleroString);

        return new SensorData(temperature, heartBeat, xAccelero, yAccelero, zAccelero);
        
        
        }

    }

    private boolean isValidString(String dataString) {
        return (dataString.indexOf("ABCDE") != -1
                && dataString.indexOf("EDCBA") != -1
                && dataString.indexOf("XXX") != -1
                && dataString.indexOf("YYY") != -1
                && dataString.indexOf("ZZZ") != -1
                && dataString.indexOf("|") != -1);
    }

}
