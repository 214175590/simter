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
			
			$('#logo-box').css('left', ($(window).width() - 500)/2 + 'px');
			$('#login-body').css('left', ($(window).width() - 400)/2 + 'px').show();
			$('.wait-box').css('left', ($(window).width() - 500)/2 + 'px');
			
			page.bindEvent();
		},
		
		login: function(){
			var $acc = $('#account'),
				$pwd = $('#password'),
				acc = $acc.val(),
				pwd = $pwd.val();
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
		    		url: consts.LOGIN_URL,
		    		data: {acc: acc, pwd: $.md5(pwd)},
		    		dataType: 'json',
		    		type: 'post'
		    	}).done(function(res, flag){
		    		if(flag == 'success'){
		    			if(res.code == 1000){
		    				if(window['localStorage']){
		    					localStorage.setItem("YXOS_USER_ACC", acc);
		    				}
		    				$('.wait-box .spinner').hide();
		    				$('.wait-box .msg').html(res.rtn.name + '您好，欢迎回来<br>精彩即将呈现');
		    				setTimeout(function(){
		    					document.location.replace(page.getPage('index.html'));
						    }, 1500);
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
			
		}
		
	};
	
	var page = new PageScript();
	page.init();
	
});