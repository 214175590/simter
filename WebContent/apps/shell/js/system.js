/**
 * 
 */
define(function(require, exports, module) {
	
	function PageScript(){
		this.user = {};
		this.os = {};
		this.isOpen = false;
		this.loaded = false;
		this.iconData = [];
		this.timeArr = [3, 2, 3, 3, 4, 4, 3, 4, 4, 3, 4, 4, 8, 2, 6, 10, 20, 30];
		this.events = {};
		this.pageid = "";
	}
	
	PageScript.prototype = {
		
		init: function(){
			page.serverIp = page.getServerIp();
			
			var su = consts.WEB_BASE.replace("http", "ws") + "ws";
			page.wsurl = su;
			
			$.useModule(['yxos', 'clippy', 'yswebsocket'], function(){
				page.os = YXOs;
				// 禁用窗口拉伸
				page.os.globalConfig.disabledWindowResize = true;

				page.os.initStartMenu([
					{
						title: '应用中心',
						icon: 'icon-chrome',
						cssClass: 'app-center'
					},
					{
						title: '退出系统',
						icon: 'icon-off',
						cssClass: 'logout'
					}
				], function(startMenu, e){
					var $menu = $(e.target);
					if($menu.hasClass('modify-pwd')){
						var pwdWin = new YXOs.YXWindow({
							title: "修改密码",
							types: 'exe',
							url: consts.WEB_BASE + 'apps/yxos/user_modify_pwd.html',
							width: 600,
							height: 450,
							needMax: false,
							needMin: false,
							desktopIconId: '',
							icon: consts.WEB_BASE + 'images/icons/abb1.png'
						});
						
						pwdWin.open();
					} else if($menu.hasClass('logout')){
						ajax.post({
							url: 'linux/exit',
							data: {serverIp: page.serverIp}
						}).done(function(){
							document.location.reload();
						});
					} else {
						alert("建设中...");
					}
					startMenu.show();
				});
				
				if(page.os.addEvent){
					page.os.addEvent('WindowStartMenuClick', function(startMenu){
						startMenu.show();					
					});
				}
				
				// 获取到用户信息
				ajax.getUserInfo().done(function(user){
					page.user = user;
					if(page.user && page.user.userId){
						page.loadLinuxInfo();
					} else {
						parent.document.location.replace(consts.WEB_BASE + 'apps/shell/login.html');
					}
				});
				
			});
			
		},
		
		wsSuccess: function(){
			page.connstate = true;
			page.socket.send('linux.login', {userId: page.user.userId, serverIp: page.serverIp});
		},
		
		wsError: function(){
			alert("error");
		},
		
		wsClose: function(){
			document.location.reload();
		},
		
		wsMessage: function(message){
			var data = JSON.parse(message);
	      	if(data.req == "linux.login"){

	      		page.os.desktop.open();
				
				page.loadDesktopIcon();
				
				page.loadClippy();
	      		
	      	} else {
	      		for(var k in page.events){
	      			if(page.events[k] && page.events[k].flag == data.flag){
	      				try{
	      					page.events[k].call(data);
	      				}catch(e){}
	      			}
	      		}
	      	}
		},
		
		regEvent: function(key, flag, callback){
			page.events[key] = {
				flag: flag,
				call: callback
			};
		},
		
		loadLinuxInfo: function(){
			ajax.post({
	    		url: "linux/isLogin",
	    		data: {serverIp: page.serverIp},
	    		dataType: 'json',
	    		type: 'post'
	    	}).done(function(res, rtn, state, msg){
	    		if(state){
	    			page.account = rtn.data;
					//创建socket对象
				  	page.socket = new YSWebSocket({
						url: page.wsurl,
						success: page.wsSuccess, 
						error: page.wsError,
						close: page.wsClose,
						message: page.wsMessage
					});
	    		} else {
	    			document.location.replace(consts.WEB_BASE + 'apps/shell/loginLinux.html?serverIp=' + page.serverIp);
	    		}
	    	}).fail(function(){
	    		log.error("失败:", arguments);
	    		error("数据加载失败");
	    	});
		},
		
		
		/** 加载桌面图标 */
		loadDesktopIcon: function(){
			ajax.post({
	    		url: "linux/load",
	    		data: {serverIp: page.serverIp},
	    		dataType: 'json',
	    		type: 'post'
	    	}).done(function(res, rtn){
	    		if(res.code == 1000 && rtn){
	    			page.loaded = true;
	    			if(rtn.data && rtn.data.length){
	    				var deskIcon, obj, w = 0, h = 0;
	    				for(var i = 0, k = rtn.data.length; i < k; i++){
	    					obj = rtn.data[i];
	    					deskIcon = new YXOs.YXDesktopIcon({
	    						id: obj.id,
	    						name : obj.name,
	    						types : obj.types,
	    						windowWidth: obj.windowWidth,
	    						windowHeight: obj.windowHeight,
	    						X: 20 + (w * (page.os.globalConfig.desktopIconWidth + 10)),
	    						Y: 20 + (h * (page.os.globalConfig.desktopIconHeight + 10)),
	    						icon : consts.WEB_BASE + obj.icon,
	    						title : obj.title,
	    						hosts: obj.hosts,
	    						location: obj.hosts ? consts.WEB_BASE + obj.location : obj.location,
	    						levels: obj.levels,
	    						isDrag: obj.isDrag == 'true',
	    						isShow: obj.isShow == 'true',
	    						needClose: obj.needClose == 'true',
	    						needMinimize: obj.needMinimize == 'true',
	    						needMaximize: obj.needMaximize == 'true',
	    						closeFunction : {
	    							func : function(e, id) {
	    								// obj.closeFunction
	    							},
	    							order : 'before'
	    						},
	    						minFunction : {
	    							func : function(e, id) {
	    								// obj.minFunction
	    							},
	    							order : 'before'
	    						},
	    						maxFunction : {
	    							func : function(e, id) {
	    								// obj.maxFunction
	    							},
	    							order : 'before'
	    						},
	    						status: obj.status,
	    						createTime: obj.createTime,
	    						belong: obj.belong
	    					});
	    					
	    					page.os.desktop.addDesktopIcon(deskIcon);
	    					
	    					h++;
	    					if(h > 5){
	    						w++;
	    						h = 0;
	    					}
	    				}
	    				
	    				//page.loadUserIconPrivate();
	    				
	    			}
	    		} else {
		    		error("数据加载失败:" + res.msg);
	    		}	    		
	    	}).fail(function(){
	    		log.error("失败:", arguments);
	    		error("数据加载失败");
	    	}).always(function(){
	    		page.openOs();
	    	});
			
		},
		
		loadClippy: function(){
			if(page.clippyAgent){
				return;
			}
			var clippyArr = ['Bonzi', 'Clippy', 'F1', 'Genie', 'Genius', 'Links'
				, 'Merlin', 'Peedy', 'Rocky', 'Rover'];
			var index = parseInt(Math.random() * clippyArr.length + 1) - 1;
			clippy.BASE_PATH = consts.WEB_BASE + 'lib/clippy/agents/'
		    clippy.load(clippyArr[index], function(agent) {
		    	page.clippyAgent = agent;
		    	/*
		    	 // play a given animation
				agent.play('Searching');
				
				// play a random animation
				agent.animate();
				
				// get a list of all the animations
				agent.animations();
				// => ["MoveLeft", "Congratulate", "Hide", "Pleased", "Acknowledge", ...]
				
				// Show text balloon
				agent.speak('When all else fails, bind some paper together. My name is Clippy.');
				
				// move to the given point, use animation if available
				agent.moveTo(100,100);
				
				// gesture at a given point (if gesture animation is available)
				agent.gestureAt(200,200);
				
				// stop the current action in the queue
				agent.stopCurrent();
				
				// stop all actions in the queue and go back to idle mode
				agent.stop();
		    	 */
		        agent.show();
		        
	        	setTimeout(function(){
	        		page.clippyAgent.speak(page.account + "您好，欢迎进入Yisin Web Os.")
	        	}, 1000);
	        	
		        setTimeout(alippyAnimate, 2000);
		        
		        //alippyAnimate();
		    });
		},
		
		openOs: function(host, acc){
			if(!page.isOpen){
				document.title = host + "@" + acc;
				page.isOpen = true;
				page.os.desktop.open();
			}
		},
		
		setWindowTitle: function(wid, title){
			var win = page.os.control.getWindow(wid);
			if(win){
				win.setTitle(title);
			}
		},
		
		getServerIp: function(){
			var params = utils.getUrlParam();
			if(params['serverIp']){
				return params['serverIp'];
			}
			return '';
		},
		
		openWindow: function(ops){
			var window = new YXOs.YXWindow({
				title: ops.title,
				types: ops.types,
				url: ops.location,
				width: ops.windowWidth,
				height: ops.windowHeight,
				desktopIconId: ops.id,
				icon: ops.icon
			});
			var winHand = new YXOs.YXWindowHandle(ops);
			winHand.window = window;
			window.winHandId = winHand.id;
			window.open();
		}
		
		
	};
	
	function alippyAnimate(){		
		var index = parseInt(Math.random() * page.timeArr.length + 1) - 1;
		page.clippyAgent.animate();		
		setTimeout(alippyAnimate, page.timeArr[index] * 1000);
	}
	
	var page = new PageScript();
	page.init();
	
	top['SYSTEM'] = page;
	
});