package com.bfds.ach.calc.exe.service;

import com.bfds.ach.calc.exe.domain.Executable;

import java.util.Map;


public class TestExecutable implements Executable {

     final boolean throwError;

     public TestExecutable(boolean throwError) {
        this.throwError= throwError;
     }

    @Override
    public void execute(Map<String, String> validationParameters) {
        if(throwError) {
            StringBuilder veryLongErrorMessage = new StringBuilder("error....");
            for(int i = 0; i< 1000; i++) {
                veryLongErrorMessage.append("--"+ i);
            }
            throw new IllegalStateException(veryLongErrorMessage.toString());
        }
    }
}
