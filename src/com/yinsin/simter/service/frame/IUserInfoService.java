package com.yinsin.simter.service.frame;

import com.yinsin.simter.frame.Argument;

public interface IUserInfoService {
    
    public Argument insert(Argument arg);
    
    public Argument select(Argument arg);
    
    public Argument selectPaging(Argument arg);
    
    public Argument update(Argument arg);
    
    public Argument delete(Argument arg);

}