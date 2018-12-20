/**
 * 格式化shell
 */
define(function(require, exports, module) {
	
	var T0m = "[0m";
	var Tm = "[m";
	var TK = "[K";
	var T0131 = "[01;31m";
	var T0132 = "[01;32m";
	var T0134 = "[01;34m";
	var T0136 = "[01;36m";
	var T3042 = "[30;42m";
	
	function PageScript(){
	}
	
	PageScript.prototype = {
		
		formatMessage: function(msgList, message){
			if (!msgList) {
                msgList = [];
            }
            var msg = null;
            if (message.indexOf(T0m) == -1 && message.indexOf(Tm) == -1 && message.indexOf(TK) == -1) {
                msg = new Message();
                msg.text = message.replace(/[\r\n]/g, "");
                msgList.push(msg);
            } else {
                message = page.replaceAll(message, TK, "");
                message = page.replaceAll(message, Tm, T0m);

                var arrs = message.split(T0m);
                var str = null, line;
                var index = -1;
                for(var i = 0, k = arrs.length; i < k; i++){
                	line = arrs[i];
                    str = line.replace(/[\r\n]/g, "");
                    if ((index = str.indexOf(T0131)) != -1) {
                        page.splitMsg(msgList, str, index, T0131, 'ct0131');                        
                    } else if ((index = str.indexOf(T0132)) != -1) {
                    	page.splitMsg(msgList, str, index, T0132, 'ct0132');
                    } else if ((index = str.indexOf(T0134)) != -1) {
                    	page.splitMsg(msgList, str, index, T0134, 'ct0133');
                    } else if ((index = str.indexOf(T0136)) != -1) {
                    	page.splitMsg(msgList, str, index, T0136, 'ct0136');
                    } else if ((index = str.indexOf(T3042)) != -1) {
                    	page.splitMsg(msgList, str, index, T3042, 'ct3042');
                    } else {
                        msg = new Message();
                        msg.text = str;
                        msgList.push(msg);
                    }
                }
            }            
            return msgList;
		},
		
		splitMsg: function(msgList, str, index, flag, textColor) {
            var msg = null;
            if (index > 0) {
                var str1 = str.substring(0, index);
                msg = new Message();
                msg.text = str1;
                msgList.push(msg);
            }

            var str2 = str.substring(index + flag.length);
            msg = new Message();
            msg.text = str2;
            msg.textColor = textColor;
            msgList.push(msg);
        },
        
        replaceAll: function(res, reg, str){
        	while(res.indexOf(reg) != -1){
        		res = res.replace(reg, str);
        	}
        	return res;
        }
		
	};
	
	function Message() {
		this.text = "";
		this.textColor = "#00fe00";
	}
	
	var page = new PageScript();
	return page;	
});