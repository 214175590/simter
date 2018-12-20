/**
 * 格式化shell
 */
define(function(require, exports, module) {
	var oldcode = "", ischange = false;
	
	function PageScript(){
		this.rpath = '';
		this.lpath = '';
	}
	
	PageScript.prototype = {
		
		init: function(){
			CodeMirror.commands.autocomplete = function(cm) {
				if(cm.options.mode == 'text/html'){
					cm.showHint({hint: CodeMirror.hint.jshint});
				} else if(cm.options.mode == 'css'){
					cm.showHint({hint: CodeMirror.hint.csshint});
				} else if(cm.options.mode == 'javascript'){
					cm.showHint({hint: CodeMirror.hint.jshint});
				}
			};

			page.code = CodeMirror.fromTextArea(document.getElementById("code-edit"), {
				lineNumbers: true,
				mode: "javascript",
				gutters: ["CodeMirror-lint-markers"],
				lint: false, 
				lineWrapping: true,
				theme: "ambiance", 
				autoMatchParens: true,
				extraKeys: { 
					"Alt": "autocomplete", 
					"Ctrl-S": function(cm){
						if(ischange){
							page.savetl(cm.getValue());
						}
					},
					"Ctrl-R": function(cm){
						if(ischange){
							page.syncts(cm.getValue());
						}
					}
				}
			});
			
			var bodyHeight = $(document).height();
			$('.CodeMirror').css('height', (bodyHeight - 30) + 'px');
			$('.CodeMirror .CodeMirror-gutters').css('height', (bodyHeight - 30) + 'px');
			
			page.loadFileContent();
			
			page.bindEvent();
		},
		
		loadFileContent: function(){
			var param = utils.getUrlParam();
			if(param['lpath']){
				page.rpath = param['rpath'];
				page.lpath = param['lpath'];
				page.serverIp = param['serverIp'];
				ajax.post({
		    		url: "linux/getFileContent",
		    		data: {filepath: page.lpath},
		    		dataType: 'json',
		    		type: 'post'
		    	}).done(function(res, rtn, state, msg){
		    		if(state){
		    			oldcode = rtn.data;
		    			page.code.setValue(oldcode);
		    		} else {
		    			error(msg);
		    		}
		    	}).fail(function(){
		    		log.error("失败:", arguments);
		    		error("执行失败");
		    	});
			}			
		},
		
		filterChars: function(v){
			v = v.replace(/\+/g, '≡');
			v = v.replace(/\%/g, '∮');
			return v;
		},
		
		savetl: function(content){
			ajax.post({
	    		url: "linux/save",
	    		data: {lpath: page.lpath, serverIp: page.serverIp, content: page.filterChars(content)},
	    		dataType: 'json',
	    		type: 'post'
	    	}).done(function(res, rtn, state, msg){
	    		if(state){
	    			$('.btn-save').addClass('disabled');
					$('.btn-sync').removeClass('disabled');
					
	    		} else {
	    			error("保存失败：" + msg);
	    		}
	    	}).fail(function(){
	    		log.error("保存失败:", arguments);
	    		error("保存失败");
	    	});
		},
		
		syncts: function(content){
			var data = {
				rpath: page.rpath, 
				lpath: page.lpath,
				save: false,
				serverIp: page.serverIp
			};
			if(ischange){
				data.save = true;
				data.content = page.filterChars(content);
			}
			ajax.post({
	    		url: "linux/sync",
	    		data: data,
	    		dataType: 'json',
	    		type: 'post'
	    	}).done(function(res, rtn, state, msg){
	    		if(state){
	    			$('.btn-save').addClass('disabled');
					$('.btn-sync').addClass('disabled');
					
	    		} else {
	    			error("同步失败：" + msg);
	    		}
	    	}).fail(function(){
	    		log.error("同步失败:", arguments);
	    		error("同步失败");
	    	});
		},
		
		bindEvent: function(){
			
			$(window).on('resize', function(){
				var bodyHeight = $(window).height();
				$('.CodeMirror').css('height', (bodyHeight - 30) + 'px');
				$('.CodeMirror .CodeMirror-gutters').css('height', (bodyHeight - 30) + 'px');
			});
			
			page.code.on("change", function(e){
				var va = page.code.getValue();
				ischange = va != oldcode;
				if(!ischange){
					$('.btn-save').addClass('disabled');
					$('.btn-sync').addClass('disabled');
				} else {
					$('.btn-save').removeClass('disabled');
					$('.btn-sync').removeClass('disabled');
				}
				
			});
			
			$('.btn-save').on('click', function(){
				if(ischange){
					page.savetl(page.code.getValue());
				}
			});
			
			$('.btn-sync').on('click', function(){
				if(ischange){
					page.syncts(page.code.getValue());
				}
			});
		}
		
	};
	
	var page = new PageScript();
	page.init();
	
});