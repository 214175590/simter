/**
 * 
 */
define(function(require, exports, module) {
	var shellutil = require('./shellutil');
	
	//获取元素的纵坐标 
	function getTop(e){ 
		var offset=e.offsetTop; 
		if(e.offsetParent!=null) offset+=getTop(e.offsetParent); 
		return offset; 
	} 
	//获取元素的横坐标 
	function getLeft(e){ 
		var offset=e.offsetLeft; 
		if(e.offsetParent!=null) offset+=getLeft(e.offsetParent); 
		return offset; 
	} 
	
	function PageScript(){
		this.user = {};
		this.os = {};
		this.user_pwd = '';
		this.curr_pwd = '';
		this.windowId = '';
		this.curr_cmd = '';
		this.isfirst = true;
	}
	
	PageScript.prototype = {
		
		init: function(){
			
			var params = utils.getUrlParam();
			page.curr_pwd = params['path'] || '/';
			page.serverIp = params['serverIp'] || '';
			page.account = params['account'] || '';
			page.user_pwd = "/home/" + page.account;
			
			page.excuteShell('pwd');
			
			$('.base.prompt').html("[" + page.account + "@localhost ~]$&nbsp;");
			
			page.windowId = document.location.hash.substring(1);
			
			top['SYSTEM'].regEvent('terminal', page.serverIp, function(data){
				if(page.$shell && data.req){
					var name = data.req.replace(/[\.]/g, "_");
					if(page[name]){
						page[name](data.value);
					} else {
						log.info(data.value);
					}
				}
			});
			
			page.bindEvent();
		},
		
		excuteShell: function(shell){
			ajax.post({
	    		url: "linux/shell",
	    		data: {shell: shell, serverIp: page.serverIp},
	    		dataType: 'json',
	    		type: 'post'
	    	}).done(function(res, rtn, state, msg){
	    		if(state){
	    		}
	    	}).fail(function(){
	    		log.error("失败:", arguments);
	    		error("数据加载失败");
	    	});
		},
		
		linux_data: function(value){
			//log.info("--1-->", "'" + value + "'");
			var arrs = value.split('\n'), msgs, aa, html, cmd, ispwd = false;
			for(var i = 0, k = arrs.length; i < k; i++){
				try{
					if(/^[\s]?cd[\s]+/.test(arrs[i])){
						if(!/-bash: cd/g.test(value)){
							cmd = $.trim(arrs[i]);
							cmd = cmd.replace(/^[\s]?cd[\s]+/, '');
							if(/^\//.test(cmd)){
								page.curr_pwd = cmd;
							} else {
								if(/^~[\s]?/.test(cmd)){
									page.curr_pwd = page.user_pwd;
								} else {
									if(!/\/$/.test(page.curr_pwd)){
										page.curr_pwd += "/";
									}
									if(/^.\//.test(cmd)){
										cmd = cmd.substring(2);
									}
									page.curr_pwd = page.curr_pwd + cmd;
								}
							}
						}
                    }
					if(page.isfirst){
						cmd = $.trim(arrs[i]); 
						if(/^pwd/.test(cmd)){
							continue;
						} else if(!/[[\w]+@[\w\W]+]/g.test(cmd)){
							page.curr_pwd = cmd;
							page.$shell.before(laytpl('output.html').render({separate: '', value: 'pwd：' + page.curr_pwd, error: ''}));
							continue;
						}
					}
					msgs = shellutil.formatMessage([], arrs[i]);
					html = [];
					ispwd = false;
					for(var j = 0, u = msgs.length; j < u; j++){
						aa = msgs[j].text.match(/[[\w]+@[\w\W]+]/g);
						//log.info("---->", "'" + msgs[j].text + "'", "'" + page.curr_cmd + "'", page.curr_cmd == msgs[j].text);
						if(!aa || !aa.length){
							if(page.curr_cmd == msgs[j].text){
								ispwd = true;
								html.push($('.base.prompt').html() + $.trim(laytpl('text.html').render({color: 'currcmd', value: msgs[j].text})));
							} else {
								html.push($.trim(laytpl('text.html').render({color: msgs[j].textColor, value: msgs[j].text})));
							}
						} else if(aa && aa.length){
							$('.base.prompt').html(aa[0] + "$&nbsp;");
						}
					}
					if(html.length){
						page.$shell.before(laytpl('output.html').render({separate:'', value: html.join(''), error: ispwd ? 'base' : ''}) + '');
					}
					
				}catch(e){
					log.error("error：", e);
				}						
			}
			$("#panel-shell").scrollTop($("#panel-shell").height() + $("#panel-shell").scrollTop() + 20);
			page.isfirst = false;
		},
		
		autoCompletion: function(){
			var word = '';
			var path = page.curr_pwd;
			var cmd = page.$left.text();
            cmd = cmd.replace(/[ ]/g, " ");
            cmd = cmd.replace(/\/\//g, "/");
            if(!/\/$/.test(path)){
            	path += "/";
            }
            var index = cmd.lastIndexOf(" ");
            if(index >= 0 && cmd){
            	cmd = cmd.substring(index + 1);
            }
            if(cmd){
            	if(/\/$/.test(cmd)){
            		if(/^\//.test(cmd)){
            			path = cmd;
            		} else {
            			path = path + cmd;
            		}
            	} else {
            		word = (function(){
            			var c = '', s = '', f = false;
            			if(!/ $/g.test(cmd)){
            				for (var i = cmd.length - 1; i >= 0; i--){
            					c = cmd.charAt(i);
            					if(/[/|]/.test(c)){
            						f = true;
            						s = cmd.substring(i + 1);
            						break;
            					}
                            }
            				if(!f && !s){
            					s = cmd;
            				}
            			}
            			return s;
            		})();
            		// path
        			if(/^\//.test(cmd)){
            			path = cmd.substring(0, cmd.lastIndexOf('/') + 1);
            		} else if(cmd.indexOf("/") != -1){
            			path = path + cmd.substring(0, cmd.lastIndexOf('/') + 1);
            		}
            	}
            }
            // 获取
            log.info(path, word);
            ajax.post({
	    		url: "linux/loadFiles",
	    		data: {path: path, serverIp: page.serverIp},
	    		dataType: 'json',
	    		type: 'post'
	    	}).done(function(res, rtn, state, msg){
	    		if(state){
	    			page.showCandidate(rtn.data, word);
	    		}
	    	}).fail(function(){
	    		log.error("失败:", arguments);
	    	});
		},
		
		showCandidate: function(datas, word){
			//log.info(datas);
			if(datas && datas.length){
				var obj = {}, list = [];
				for(var i = 0; i < datas.length; i++){
					obj = datas[i];
					if(obj.filename != '..' && obj.filename != '.'){
						if(word){
							if(obj.filename.length >= word.length && obj.filename.substring(0, word.length) == word){
								list.push({
									dir: obj.attrs.dir,
									name: obj.filename
								});
							}
						} else {
							list.push({
								dir: obj.attrs.dir,
								name: obj.filename
							});
						}
					}
				}
				if(list.length == 1 && list[0].name != word){
					var str = list[0].name;
					str = str.substring(word.length);
					page.$left.append(str);
				} else if(list.length > 1){
					var htmls = [];
					for(var i = 0; i < list.length; i++){
						htmls.push(laytpl('citem.html').render({text: list[i].name, css: i == 0 ? 'active' : ''}));
					}
					var left = getLeft(page.$cursor[0]),
						top = getTop(page.$cursor[0]),
						width = $(window).width(),
						height = $(window).height(),
						scrollTop = $('.candidate-box').scrollTop() || 0;
					top = top - scrollTop;
					var cbhei = $('.candidate-box').height();
					$('.candidate-box').html(htmls.join('')).removeClass('hidden').css({
						left: (function(){
							var lf = left + 350 > width ? left - 350 : left;
							return lf < 0 ? 5 : lf;
						})(),
						top: height - cbhei - 40
					});
					page.curr_word = word;
				}
			}
		},
		
		chooseItem: function(){
			var name = $.trim($('.candidate-box .citem.active').text());
			if(name){
				if(page.curr_word != name){
					name = name.substring(page.curr_word.length);
					page.$left.append(name);
				}
			}
			$('.candidate-box').addClass('hidden');
		},
		
		setChooseItem: function(flag){
			var index = $('.candidate-box .citem.active').index();
			index = index + flag;
			if(index < 0){
				index = $('.candidate-box .citem').length - 1;
			} else if(index >= $('.candidate-box .citem').length){
				index = 0;
			}
			$(".candidate-box").scrollTop(index * 22);
			$('.candidate-box .citem').removeClass('active');
			$('.candidate-box .citem:eq(' + index + ')').addClass('active');
		},
		
		bindEvent: function(){
			var cmd_cache = [];
            var cmd_pos = 0;

            page.$left = $('.left');
            page.$right = $('.right');
            page.$cursor = $('.cursor');
            page.$shell = $('.shell-view');
            page.$input = $('.input');

            var str_left = '';
            var str_cursor = '';
            var str_right = '';
            var str_tmp_cursor = '';
            var flag_end = false;

            // 光标闪烁效果
            setInterval(function(){
            	page.$cursor.toggleClass('blink');
            }, 400);

            // keypress 按下字符键时触发
            // keydown 按下任意键触发

            // 获取键盘输入 (keydown 与 keypress 获取的 keyCode 值不一样, 其中keydown不区分大小写)
            $(document).keypress(function(e) {
                // jQuery 标准化了 event.keyCode(IE) event.which(W3C) event.charCode(事件为keypress下除IE)
                // console.log(e.which);
                // console.log(String.fromCharCode(e.which));

                if (e.which === 32) {       // space
                	page.$left.append('&nbsp;');
                } else if(e.which !== 13) { // enter
                	var chars = String.fromCharCode(e.which);
                	if(chars != ''){
                		page.$left.append(chars);
                	}
                }

            });        
	    
            // 功能键
            $(document).keydown(function(e) {
                //console.log(e);
                if (e.which === 13) {// enter
                	if(!$('.candidate-box').hasClass('hidden')){
                		page.chooseItem();
                		return;
                	}
                    var cmd = $.trim(page.$input.text());
                    cmd = cmd.replace(/[ ]/g, " ");
                    var val_ouput = '';
                    var err_class = '';
                    var is_print = true;
                    
                    if (cmd !== '') {
                        cmd_cache.push(cmd);
                        cmd_cache = _.uniq(cmd_cache);
                    }
                    if (cmd_cache.length > 0) {
                        cmd_pos = cmd_cache.length - 1;
                    }
                    
                    page.curr_cmd = cmd;
                    
                    if (cmd === 'help') {
                        val_ouput = 'Type JavaScript syntax for interactive console, or type "clear" to clear terminal.';
                    } else if (cmd === 'clear') {
                    	page.$shell.siblings().remove();
                        is_print = false;
                    } else if(/^[\s]?(vi$|vi[\s]+|vim$|vim[\s]+|top$|tail |tail$|tail2f |cat$|cat )/g.test(cmd)) {
                    	val_ouput = '\'' + cmd + '\': command not support.';
                        err_class = ' error';
                    } else {
                    	try {
                        	if(cmd){
                        		val_ouput = page.excuteShell(cmd);
                        		is_print = false;
                        	} else {
                        		val_ouput = '';
                                err_class = '';
                        	}
                        } catch (e) {
                            val_ouput = '\'' + cmd + '\': command not found.';
                            err_class = ' error';
                        }
                    }
                    
                    page.$left.text('');
                    page.$cursor.html('&nbsp;');
                    page.$right.text('');

                    if (is_print) {
                    	page.$shell.before(laytpl('output.html').render({
                    		separate: $('.base.prompt').text() + " ", 
                    		value: $.trim(laytpl('text.html').render({color: 'currcmd', value: cmd})), 
                    		error: 'base'
                    	}));
                    	page.$shell.before(laytpl('output.html').render({separate:'&gt;', value: val_ouput, error: err_class}) + '');
                    }

                } else if (e.which === 8) {     // backspace
                    str_left = page.$left.text();
                    if (str_left.length === 0) {
                        return;
                    }
                    str_left = str_left.substring(0, str_left.length - 1);
                    page.$left.text(str_left);

                } else if (e.which === 37) {    // 向左方向键
                    str_left = page.$left.text();
                    str_right = page.$right.text();
                    str_cursor = page.$cursor.text();
                    str_tmp_cursor = '';

                    if (str_left.length === 0) {
                        return;
                    }
                    str_tmp_cursor = str_left.substring(str_left.length - 1, str_left.length);
                    str_left = str_left.substring(0, str_left.length - 1);
                    if (!(page.$cursor.html() === '&nbsp;' && str_right.length === 0 && $.trim(str_tmp_cursor) !== '')) {
                        str_right = str_cursor + str_right;
                    }

                    page.$left.text(str_left);
                    page.$cursor.text(str_tmp_cursor);
                    page.$right.text(str_right);

                } else if (e.which === 39) {    // 向右方向键
                    str_left = page.$left.text();
                    str_right = page.$right.text();
                    str_cursor = page.$cursor.text();
                    flag_end = false;

                    if (str_right.length === 0) {
                        if (page.$cursor.html() === '&nbsp;') {
                            return;
                        }
                        flag_end = true;
                    }
                    str_left += str_cursor;
                    if (flag_end) {
                    	page.$cursor.html('&nbsp;');
                        str_right = '';
                    } else {
                    	page.$cursor.text(str_right.substring(0,1));
                        str_right = str_right.substring(1);
                    }

                    page.$left.text(str_left);
                    page.$right.text(str_right);

                } else if (e.which === 38) {    // 向上方向键
                	if(!$('.candidate-box').hasClass('hidden')){
                		page.setChooseItem(-1);
                		return;
                	}
                    if (cmd_pos < 0) {
                        return;
                    }

                    page.$left.text(cmd_cache[cmd_pos]);
                    cmd_pos--;
                    page.$cursor.html('&nbsp;');
                    page.$right.text('');
                    
                    e.preventDefault();
                } else if (e.which === 40) {    // 向下方向键
                	if(!$('.candidate-box').hasClass('hidden')){
                		page.setChooseItem(1);
                		return;
                	}
                    if (cmd_pos >= cmd_cache.length - 1) {
                    	page.$left.text('');
                    } else {
                        cmd_pos++;
                        page.$left.text(cmd_cache[cmd_pos]);
                    }
                    
                    page.$cursor.html('&nbsp;');
                    page.$right.text('');
                } else if (e.which === 46) {    // delete
                    str_right = page.$right.text();

                    if (str_right.length === 0) {
                        if (page.$cursor.html() === '&nbsp;') {
                            return;
                        }
                        flag_end = true;
                    }

                    if (flag_end) {
                    	page.$cursor.html('&nbsp;');
                    } else {
                    	page.$cursor.text(str_right.substring(0, 1));
                    	page.$right.text(str_right.substring(1));
                    }
                } else if (e.which === 35) {    // end
                    str_right = page.$right.text();
                    str_cursor = page.$cursor.text();
                    var str_all = page.$input.text();

                    if (str_right.length === 0 && $.trim(str_cursor).length === 0) {
                        return;
                    }
                    page.$left.text(str_all);
                    page.$cursor.html('&nbsp;');
                    page.$right.text('');

                } else if (e.which === 36) {    // home
                    str_left = page.$left.text();
                    var str_all = page.$input.text();

                    if (str_left.length === 0) {
                        return;
                    }
                    page.$left.text('');
                    page.$cursor.text(str_all.substring(0, 1));
                    page.$right.text(str_all.substring(1, str_all.length));

                } else if (e.which === 85 && e.ctrlKey) {   // Ctrl + U
                    e.preventDefault();

                    page.$left.text('');
                } else if (e.which === 76 && e.ctrlKey) {   // Ctrl + L
                    e.preventDefault();

                    page.$shell.siblings().remove();
                } else if (e.which === 86 && e.ctrlKey) {   // Ctrl + V
                	$('#inputbox').val('').focus();
                } else if(e.which === 9){ // Tab
                	e.preventDefault();
                	
                	page.autoCompletion();
                }
                
                $('.candidate-box').addClass('hidden');
				page.curr_word = '';
            });  
	      
            var $box = $('#inputbox');
            
            if(top.platform == 'web'){
            	$('.pop-i').addClass('hidden');
            	$box.keyup(function(e){
                	if (e.which === 86 && e.ctrlKey) {   // Ctrl + V
    					e.preventDefault();
    					var text = $box.val();
                        var old = page.$left.text();
                        page.$left.text(old + text);
                        $box.val('');
                    }
    			});
            	
            	$box.on('click', function(){
    	        	$box.blur();
    	        });
            } else {
            	// mobile
            	$('.pop-i').removeClass('hidden');
            	$box.bind('input propertychange', function(e) {
            		e.preventDefault();
            		var text = $box.val();
            		var old = page.$left.text();
            		page.$left.text(old + text);
            		$box.val('');
            	});
            	$box.focus();
            	
            	$('.pop-i').on('click', function(){
            		page.autoCompletion();
            	});
            	
            	$box.on('focus', function(){
            		$('.pop-i').addClass('hidden');
            	}).on('blur', function(){
            		$('.pop-i').removeClass('hidden');
            	});
            }
            
            $('#panel-shell').on('clicl', function(){
            	$('.candidate-box').addClass('hidden');
            });
            
            $('.candidate-box').on('click', '.citem', function(){
            	var $this = $(this),
            		text = $this.text();
            	if(page.curr_word != text){
            		text = text.substring(page.curr_word.length);
					page.$left.append(text);
				}
            	$('.candidate-box').addClass('hidden');
            });
		}
		
	};
	
	var page = new PageScript();
	page.init();
	
});