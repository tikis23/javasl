package com.javasl;

import com.javasl.runtime.types.Int32_T;
import com.javasl.runtime.types.Type_T;

public class Main {
    public static void main(String[] args) {
        String sourceString = "int32 i = 0; while (i < 10) {";
        sourceString += "i = i + externalFunc(i);";
        sourceString += "} print(i);";
        Script script = new Script();
        script.addDefaultFunctionPrint(false);
        script.addExternalFunction("externalFunc", true, new Int32_T(), new Type_T[]{new Int32_T()}, (p) -> {
            int p0 = Script.intParam(p[0]);
            return new Int32_T(p0 + 2);
        });

        try {
            script.compileFromSource(sourceString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (script.isReady()) {
            while (!script.isFinished()) {
                System.out.println("Executing script until it yields ...");
                script.execute();
            }
        }
    }
}