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
    private double xAccelero;
    private double yAccelero;
    private double zAccelero;
    
    public SensorData(double temperature , double heartBeat , double xAccelero , double yAccelero , double zAccelero){
        
        this.temperature = temperature;
        this.heartBeat = heartBeat;
        this.xAccelero = xAccelero;
        this.yAccelero = yAccelero;
        this.zAccelero = zAccelero;
    }
    
    public double getTemperature() {
        return temperature;
    }
    
    public double getHeartBeat(){
        return heartBeat;
    }
    
    @Override
    public String toString(){
        return "Temperature: " + temperature + " | "+"Heartbeat: " + heartBeat + " | " + "X: " + xAccelero + " | " + "Y:" + yAccelero + " | " + "Z:" + zAccelero;
    }
}
