package com.yinsin.simter.controller.frame;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.yinsin.simter.modal.frame.UserData;

public class UserDataHandle {

    private static class SingletonHolder {  
        private static final UserDataHandle INSTANCE = new UserDataHandle();
        
        private static Map<String, UserData> USERDATA = new HashMap<String, UserData>();
    }  

    private UserDataHandle (){
        //new Thread(new HandleThread()).start();
    }

    public static final UserDataHandle getInstance() {
        return SingletonHolder.INSTANCE;
    }
    
    public Object getUserData(String key) {
    	UserData data = SingletonHolder.USERDATA.get(key);
        return data != null ? data.getData() : data;
    }
    
    public UserData setUserData(String key, Object data) {
        UserData udata = new UserData();
        udata.setData(data);
        udata.setKey(key);
        udata.setTime(new Date().getTime());
        return SingletonHolder.USERDATA.put(key, udata);
    }
    
    private class HandleThread implements Runnable {
        @Override
        public void run() {
            while(true){
                try {
                    Thread.sleep(1000 * 60 * 60);
                } catch (InterruptedException e) {
                }
                long end = new Date().getTime();
                long start = 0;
                for (Map.Entry<String, UserData> umap : SingletonHolder.USERDATA.entrySet()) {
                    start = umap.getValue().getTime();
                    if(end - start > (1000 * 60 * 60)){
                        SingletonHolder.USERDATA.remove(umap.getKey());
                    }
                }
            }
        }
    }

}
