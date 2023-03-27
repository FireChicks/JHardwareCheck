/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hardWare;

/**
 *
 * @author LeeJanyun
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class HardwareScanner {
     Map<String, String> hardwareMap = new HashMap<>();
    public HardwareScanner() {          
        try {           
               hardwareMap.put("CPU", scanHardware("Win32_Processor", "Name"));
               hardwareMap.put("GPU", scanHardware("Win32_VideoController", "Name"));
               hardwareMap.put("Mainboard", scanHardware("Win32_BaseBoard", "Product"));
               hardwareMap.put("Storage", scanHardware("Win32_DiskDrive", "Model"));
               hardwareMap.put("RAM", scanHardware("Win32_PhysicalMemory", "PartNumber"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    private static String scanHardware(String wmicClass, String property) throws Exception {
        String line;

        Process process = Runtime.getRuntime().exec("wmic path " + wmicClass + " get " + property);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder hardwareBuilder = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            if (line.contains(property)) {
                continue;
            }
            String hardware = line.trim(); // 공백 제거
            if (!hardware.isEmpty()) { // 빈 문자열은 추가하지 않음
                if (hardwareBuilder.length() > 0) { // 이미 하드웨어 정보가 추가된 경우
                    hardwareBuilder.append("/");
                }
                hardwareBuilder.append(hardware);
            }
        }
        reader.close();            
        return hardwareBuilder.toString();
    }
    
    public Map<String,String> getHardwareMap() {        
        return hardwareMap;
    }
    

}

