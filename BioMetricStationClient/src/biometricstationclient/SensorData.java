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
public class SensorData {
    private double heartBeat;
    private double temperature;
    
    public SensorData(double temperature , double heartBeat){
        
        this.temperature = temperature;
        this.heartBeat = heartBeat;
    }
    
    public double getTemperature() {
        return temperature;
    }
    
    public double getHeartBeat(){
        return heartBeat;
    }
    
    @Override
    public String toString(){
        return "Temperature: " + temperature + " | Heartbeat: " + heartBeat;
    }
}
