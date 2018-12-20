/**
 * 
 */
define(function(require, exports, module) {
	
	function PageScript(){
		this.user = {};
		this.os = {};
	}
	
	PageScript.prototype = {
		
		init: function(){
			top['platform'] = page.isPC() ? 'web' : 'mobile';
			
			// 获取到用户信息
			ajax.getUserInfo().done(function(user){
				page.user = user;
				
				if(page.user && page.user.userId){
					$('#mainFrame').attr('src', consts.WEB_BASE + 'apps/shell/mmain.html').load(function(){
						$('.wait-panel').remove();
					});
				} else {
					document.location.replace(consts.WEB_BASE + 'apps/shell/mlogin.html');
				}
			});
		},
		
		getPage: function(h){
			return consts.WEB_BASE + 'apps/shell/' + (top.platform == 'web' ? h : 'm' + h);
		},
		
		isPC: function() {
		    var userAgentInfo = navigator.userAgent;
		    var Agents = ["Android", "iPhone",
		                "SymbianOS", "Windows Phone",
		                "iPad", "iPod"];
		    var flag = true;
		    for (var v = 0; v < Agents.length; v++) {
		        if (userAgentInfo.indexOf(Agents[v]) > 0) {
		            flag = false;
		            break;
		        }
		    }
		    return flag;
		}
		
	};
	
	var page = new PageScript();
	page.init();
	
});