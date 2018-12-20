/**
 * 
 */
define(function(require, exports, module) {
	var back1 = require('./back-1.js');
	//require('./curve.js');
	
	function PageScript(){
		this.isOpen = false;
	}
	
	PageScript.prototype = {
		
		init: function(){
			back1.start();
			
			page.serverIp = page.getServerIp();
			$('.sip').text(page.serverIp);
			
			$('#logo-box').css('left', ($(window).width() - 500)/2 + 'px');
			$('#login-body').css('left', ($(window).width() - 400)/2 + 'px').show();
			$('.wait-box').css('left', ($(window).width() - 500)/2 + 'px');
			
			page.loadLinuxInfo();
			
			page.bindEvent();
		},
		
		loadLinuxInfo: function(){
			ajax.post({
	    		url: "linuxHost/getLinuxInfo",
	    		data: {serverIp: page.serverIp},
	    		dataType: 'json',
	    		type: 'post'
	    	}).done(function(res, rtn, state, msg){
	    		if(state){
	    			if(rtn.data && rtn.data.length){
	    				var obj = rtn.data[0];
	    				var pwd = '';
	    				if(obj.password){
	    					pwd = utils.decrypt(obj.password, obj.userId);
	    				}
	    				$('#account').val(obj.account);
	    				$('#port').val(obj.port || '22');
	    				$('#password').val(pwd);
	    			}
	    		}
	    	}).fail(function(){
	    	});
		},
		
		login: function(){
			var $acc = $('#account'),
				$pwd = $('#password'),
				$port = $('#port'),
				acc = $acc.val(),
				pwd = $pwd.val(),
				port = $port.val();
		    if(!acc){
		    	alert('请输入用户名');
		    	return;
		    }
		    if(!pwd){
		    	alert('请输入密码');
		    	return;
		    }
		    var resetForm = function(){
		    	$('.login-body').removeClass('test');
			    $('.wait-box').animate({ top: 180 }, 500).hide();
		    };
		    if(acc && pwd){
		    	$('.login-body').addClass('test');
			    $('.wait-box').show().animate({ top: 220 }, 500);
			    
		    	$.ajax({
		    		url: consts.WEB_BASE + 'linux/' + page.serverIp + "/" + (port || 22),
		    		data: {acc: acc, pwd: pwd, serverIp: page.serverIp},
		    		dataType: 'json',
		    		type: 'post'
		    	}).done(function(res, flag){
		    		if(flag == 'success'){
		    			if(res.code == 1000){
		    				$('.wait-box .spinner').hide();
		    				$('.wait-box .msg').html(acc + '登录成功！');
		    				setTimeout(function(){
		    					document.location.replace(page.getPage('system.html?serverIp=' + page.serverIp + '&acc=' + acc));
						    }, 1000);
		    			} else {
		    				alert(res.message);
		    				resetForm();
		    			}
		    		} else {
		    			error("登录失败，请稍候重试");
		    			resetForm();
		    		}
		    	}).fail(function(){
		    		error("登录失败，请稍候重试");
		    		resetForm();
		    	});
		    }
		},	
		
		getPage: function(h){
			return consts.WEB_BASE + 'apps/shell/' + (top.platform == 'web' ? h : 'm' + h);
		},
		
		bindEvent: function(){
			var $acc = $('#account'),
				$pwd = $('#password');
			
			$acc.on('keyup', function(e){
				var acc = $(this).val(),
					$vali = $('.login_fields__user .validation'),
					code = e.keyCode || e.witch;
				if(acc){
					$vali.removeClass('hidden');
				} else {
					$vali.addClass('hidden');
				}
				if(code == 13){
					page.login();
				}
			});
			
			$pwd.on('keyup', function(e){
				var acc = $(this).val(),
					$vali = $('.login_fields__password .validation'),
					code = e.keyCode || e.witch;
				if(acc){
					$vali.removeClass('hidden');
				} else {
					$vali.addClass('hidden');
				}
				if(code == 13){
					page.login();
				}
			});
			
			$('#login-btn').on('click', function(){
			    page.login();			    
			});
			
		},
		
		getServerIp: function(){
			var params = utils.getUrlParam();
			if(params['serverIp']){
				return params['serverIp'];
			}
			return '';
		}
		
	};
	
	var page = new PageScript();
	page.init();
	
});