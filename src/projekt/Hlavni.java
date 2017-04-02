/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekt;

/**
 *
 * @author student
 */
public class Hlavni {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        boolean stavOnOff = false;   // deklarace a inicializace
        boolean stavBeh = false;
        int valueOnOff;
        int valueBeh;
        int valuePot = 0;
                
        GPIO onOffButton = new GPIO(51); // Vypnutí zapnutí Vytvorení nové instance GPIO pro pin 51
        onOffButton.exportPin(); // Export pinu 51, po exportu je ve vstupnim rezim
        
        GPIO startStopButton = new GPIO(22); // Start Stop
        startStopButton.exportPin();
        
        ADC potenciometr = new ADC(0);         //provest jenom jednou??
        potenciometr.adcInit();
        valuePot = (potenciometr.getVoltage());
                    
        int pwmPeriod;
        int pwmPart;
        PWM pwmPot = new PWM(PWM.PWM_PIN.P9_14);
        pwmPot.enablePWM();
        pwmPot.enablePin();
        pwmPot.setRun(0);
        
        onOffButton.waitForValue(1, 50); // Cekani na nabeznou hranu Zapnutí vypnutí
        onOffButton.waitForValue(0, 50); // cekani na pusteni tlacitka
                      
        System.out.println("Zapínaní");
            
        LED zapinaniLed = new LED(LED.LED0_PATH);            
        zapinaniLed.setDelayOff(1000);
        zapinaniLed.setDelayOn(1000);
        zapinaniLed.setLedMode(LED.LED_MODE.LED_BLINK);
            
        try {
            Thread.sleep(6000);                 //5000 ms 
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
            
        zapinaniLed.setLedMode(LED.LED_MODE.LED_OFF);
        
        System.out.println("Zapnuto");
        stavOnOff = true;
        
        GPIO ledOnOff = new GPIO(66);           
        ledOnOff.exportPin();
        ledOnOff.setDirection(GPIO.GPIO_DIRECTION.OUT);
        ledOnOff.setValue(1);  //sviti led 1
        
        while (stavOnOff == true ){            
            valueOnOff = (onOffButton.getValue()); 
            
            if (valueOnOff == 1){           // kontrola jestli uživatel nevypnul pristroj   
                onOffButton.waitForValue(1, 50); 
                stavOnOff = false;            //když je tlačitko zmacknute dojde ke zmene stavu
            }            
            else {
                 valueBeh = (startStopButton.getValue());
                
                 if (stavBeh == true){
                     if (valueBeh == 1){
                         startStopButton.waitForValue(0, 50);   //cekani na jeho pusteni aby se neprovadelo
                         stavBeh = false;                       //stop
                         System.out.println("Stop");
                     }
                     else{                                       //beh pwm
                         pwmPeriod = 10000000;        
                         pwmPart = pwmPeriod/10;     
                         pwmPot.setPeriod(pwmPeriod);
                        
                         if (valuePot < 199){
                             pwmPot.setDuty(pwmPart);
                         } else if (valuePot < 399){
                             pwmPot.setDuty(pwmPart*2);
                         } else if (valuePot < 599){
                             pwmPot.setDuty(pwmPart*3);
                         } else if (valuePot < 799){
                             pwmPot.setDuty(pwmPart*4);
                         } else if (valuePot < 999){
                             pwmPot.setDuty(pwmPart*5);
                         } else if (valuePot < 1199){
                             pwmPot.setDuty(pwmPart*6);
                         } else if (valuePot < 1399){
                             pwmPot.setDuty(pwmPart*7);
                         } else if (valuePot < 1599){
                             pwmPot.setDuty(pwmPart*8);
                         } else {
                             pwmPot.setDuty(pwmPart*9);
                         }
            
                         pwmPot.setPolarity(0);
                         pwmPot.setRun(1);
                        
                     }
                 }
                 else{           //stavBeh == false
                     if (valueBeh == 1){
                         startStopButton.waitForValue(0, 50);  //cekani na jeho pusteni aby se neprovadelo
                         stavBeh = true;                        //start
                         System.out.println("Start");   
                     }     
                 }
                
             }
                
         }
            
            
            
          
        
        System.out.println("Vypinani");
        zapinaniLed.setLedMode(LED.LED_MODE.LED_BLINK);
            
        try {
            Thread.sleep(6000);                 //5000 ms 
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
            
        zapinaniLed.setLedMode(LED.LED_MODE.LED_OFF);
        System.out.println("Vypnuto");
        
        
      
            
            
        ///ošetřit jednak vypnutí a jednak smyčka pro zapisovaní hodnot dokud neni zmaknute stop
            
      
    }
    
}
