package com.yinsin.simter.frame.security;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * JS解密工具类，主要是用来解密前端加密的参数
 * @author Yisin
 *
 */
public class DesJS {
    public static String encrypt(String str, String key) throws ScriptException, NoSuchMethodException {
        ScriptEngine engine = loadScriptEngine();
        Invocable invokeEngine = (Invocable) engine;
        Object encObj = invokeEngine.invokeFunction("encrypt", new Object[] { str, key });

        return String.valueOf(encObj);
    }

    private static ScriptEngine loadScriptEngine() {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        try {
            InputStream is = DesJS.class.getClassLoader().getResourceAsStream("des.js");
            engine.eval(new InputStreamReader(is));
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        return engine;
    }

    public static String decrypt(String str, String key) throws RuntimeException {
        try {
            ScriptEngine engine = loadScriptEngine();
            Invocable invokeEngine = (Invocable) engine;

            Object decObj = invokeEngine.invokeFunction("decrypt", new Object[] { str, key });

            String decStr = String.valueOf(decObj);
            int zeroChrIndex = decStr.indexOf(0);
            if (zeroChrIndex > 0)  
            	return decStr.substring(0, zeroChrIndex);
            else 
            	return decStr;
        } catch (ScriptException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public static Map getDecryptMap(Map map) {
        for (Iterator iter = map.keySet().iterator(); iter.hasNext();) {
            String key = (String) iter.next();
            String value = (String) map.get(key);
            map.put(key, decrypt(value, "888888"));
        }

        return map;
    }

    public static void main(String[] args) throws ScriptException, NoSuchMethodException {
        String encryptStr = encrypt(
                "[{req:[{service:\"testService\",conf:\"true\",pageoffset:\"0\",pagecount:\"20\",category:\"-1\"}]}]",
                "888888");

        System.out.println("encryptStr:" + encryptStr);
        String decryptStr = decrypt(encryptStr, "888888");

        System.out.println("decryptStr:" + decryptStr);
    }
}
