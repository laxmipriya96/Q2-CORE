
import com.vm.qsmart2.utils.PasswordHider;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author AC Sekhar
 */
public class TestDecript {
    
    public static void main(String[] args) {
        try {
      String str=  PasswordHider.decript("U2FsdGVkX19k624wLWZCDlDnhe171BpO7JZz8eil57k=");
        System.out.println("==>"+str);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
