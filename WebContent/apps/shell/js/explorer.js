/**
 * 
 */
define(function(require, exports, module) {
	var upload_dir = '',
		pager = require('../../../lib/zuiplugin/zui.pager');
	
	function PageScript(){
		this.user = {};
		this.os = {};
		this.curr_pwd = '';
		this.windowId = '';
		this.currcmd = '';
		this.page_size = 30;
		this.page_index = 0;
	}
	
	PageScript.prototype = {
		
		init: function(){
			
			var params = utils.getUrlParam();
			var path = params['path'] || '/';
			page.serverIp = params['serverIp'] || '';
			page.account = params['account'] || '';
			
			page.windowId = document.location.hash.substring(1);
			
			top['SYSTEM'].regEvent('explorer', page.serverIp, function(data){
				if(data && data.req){
					var name = data.req.replace(/[\.]/g, "_");
					if(page[name]){
						page[name](data.value);
					} else if(data.value){
						alert(data.value);
					}
				}
			});
			
			$('.container').css('height', ($(window).height() - 50) + 'px');
			
			$.useModule(['chosen', 'datatable', 'uploadify'], function(){
				page.loadFiles(path);
				
				page.renderUploadify();
			});
			
			// 初始化分页组件
			pager.init(page.page_size, page.renderGrid);
			
			page.bindEvent();
		},
		
		renderUploadify: function(){
			$('#upload').uploadify({
                width: 16,
                height: 16,
                multi: false,
                fileSizeLimit: '512MB',
                buttonText: 'F',
                formData: {
                    serverIp: page.serverIp
                }
			}).on("uploadSuccess", function(e, file, result){
				//成功提交至服务端后的事件
				console.log("uploadSuccess", file, result);
				var json = JSON.parse(result);
				if(json.code == '0000'){
					$('#progressModal').modal('hide');
					$('.progress-bar').css('width', '0%');
				}
			}).on("openDialog", function(e){
				if(upload_dir){
					$('#upload').uploadify('addFormData', {rpath: upload_dir, fileCode: 'fc' + new Date().getTime()});
				}				
	        }).on("closeDialog", function(e, file){
	        	$('#progressModal').modal('show', 'fit');
				$('.modal-title').text('1、上传至中转服务器');
	        }).on("initComplete", function(e){
	        }).on("progress", function(e, value){
	        	console.log("progress", e, value);
	        	$('#progressModal .progress-bar').css('width', value + '%');
	        }).on("error", function(e, s){
	        	console.error("error", arguments);
	        }).on('timeout', function(e){
	        	console.error("timeout", e);
	        });
		},
		
		loadFiles: function(path){
			pager._pageSize = page.page_size;
			ajax.post({
	    		url: "linux/loadFiles",
	    		data: {path: path, serverIp: page.serverIp},
	    		dataType: 'json',
	    		type: 'post',
	    		pager: pager
	    	}).done(function(res, rtn, state, msg){
	    		if(state){
	    			page.curr_pwd = path;
	    			if(path != "/"){
						$('.btn-parent').removeClass('hidden');
					}
	    			$('#location').val(page.curr_pwd);
	    			//top['SYSTEM'].setWindowTitle(page.windowId, page.curr_pwd);
	    			page.renderGrid(rtn.data);
	    		}
	    	}).fail(function(){
	    		log.error("失败:", arguments);
	    		error("数据加载失败");
	    	});
		},
		
		renderGrid: function(data){
			var trHtmls = '', obj = {}, index = "01", rowId = "";
			page.page_index = pager._pageIndex;
			if(data.numberOfElements && data.content){
				page.RowData = {};
				for(var i = 0; i < data.content.length; i++){
					index = utils.upzore(i, 2);
					obj = data.content[i];
					
					page.RowData["row-" + ("index-" + index)] = obj;
					
					trHtmls += laytpl('list-tr.tpl').render({
						"index": index,
						"rowId": ("index-" + index),
						"name": obj.filename,
						"size": page.formatSize(obj.attrs.size),
						"type": obj.attrs.dir ? 'dir' : 'file',
						"filetype": obj.attrs.dir ? 'dir' : 'file',
						"modified": utils.formatDate(Number(obj.attrs.mtime + '000')),
						"attr": obj.attrs.permissionsString,
						"owner": page.getOwner(obj.longname),
						"buttons": (function(){
							var btnHtml = '';
							if(obj.attrs.dir){
								btnHtml += laytpl('list-btn.tpl').render({
									"class": "btn-open-folder",
									"icon": "icon-folder-open",
									"title": "打开",
									"rightCode": "open"
								}) + '&nbsp;';
								btnHtml += laytpl('list-btn.tpl').render({
									"class": "btn-upload",
									"icon": "icon-cloud-upload",
									"title": "上传文件到" + (obj.filename == '..' ? '当前目录' : obj.filename),
									"rightCode": "upload"
								}) + '&nbsp;';
							} else {
								btnHtml += laytpl('list-btn.tpl').render({
									"class": "btn-run",
									"icon": "icon-play",
									"title": "运行",
									"rightCode": "run"
								}) + '&nbsp;';
								btnHtml += laytpl('list-btn.tpl').render({
									"class": "btn-download",
									"icon": "icon-cloud-download",
									"title": "下载",
									"rightCode": "download"
								}) + '&nbsp;';
								btnHtml += laytpl('list-btn.tpl').render({
									"class": "btn-edit",
									"icon": "icon-edit",
									"title": "编辑",
									"rightCode": "edit"
								}) + '&nbsp;';
							}
							if(obj.filename != '..'){
								btnHtml += laytpl('list-btn.tpl').render({
									"class": "btn-del",
									"icon": "icon-remove",
									"title": "删除",
									"rightCode": "del"
								}) + '&nbsp;';
							}
							return btnHtml;
						})()
					});
				}
			}
			
			if(!trHtmls){
				trHtmls += laytpl('list-tr.tpl').render({
					"index": '1',
					"rowId": '1',
					"name": '目录不存在或者无权限访问此目录',
					"size": '',
					"type": '',
					"filetype": '',
					"modified": '',
					"attr": '',
					"owner": '',
					"buttons": ''
				});
			}
			
			$('#data-body').html(trHtmls);
			setTimeout(function(){
				$('[data-toggle="tooltip"]').tooltip();
			}, 200);
			
			if(page.firstLoad){
				page.firstLoad = false;
				$('table.datatable').datatable({checkable: false});
			} else {
				$('table.datatable').datatable('load');
			}
			// 设定导出配置
			var expCig = {
				showExport: false,
				adjust: true,
				resType: 'array',
				resIndex: []
			};
			// 创建分页条
			pager.create('.pager-box', data, expCig);
		},
		
		getOwner: function(longname){
			//drwx------    5 gxyj     root         4096 Jul 11  2017 .
			longname = longname.replace(/[\t\s]+/g, ' ');
			var arr = longname.split(' ');
			return (arr && arr[2]) || '';
		},
		
		formatSize: function(size){
			if (size < 1024) {
                return size + "Bytes";
            } else if (size < 1024 * 1024) {
                return (size / 1024).toFixed(2) + "KB";
            } else if (size < 1024 * 1024 * 1024) {
                return (size / (1024 * 1024)).toFixed(2) + "MB";
            } else if ((size / 1024) < 1024 * 1024 * 1024) {
                return (size / (1024 * 1024 * 1024)).toFixed(2) + "GB";
            } else if ((size / (1024 * 1024)) < 1024 * 1024 * 1024) {
                return ((size / (1024 * 1024) / 1024)).toFixed(2) + "TB";
            }
			return "";
		},
		
		runShell: function(shell){
			if(shell){
				page.currcmd = shell;
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
		    		error("执行失败");
		    	});
			}
		},
		
		linux_data: function(value){
			var p = new RegExp('^' + page.currcmd + '[\r\n\s\S]+\-bash', "g");
			var arr = value.split('\n'),
				str = '', aa;
			for(var i = 0; i < arr.length; i++){
				aa = arr[i].match(/[[\w]+@[\w\W]+]/g);
				if(!aa || !aa.length){
					str +=  arr[i] + '<br>';
				}
			}
			
			if(p.test(value) && str){
				error(str);
			} else if(/^[\s]?rm[\s]+-rf/g.test(value)){
				alert('删除完成：' + str.replace(/^[\s]?rm[\s]+-rf/g, ""));
				page.loadFiles(page.curr_pwd);
			} else if(str){
				alert(str);
			}
		},
		
		linux_down: function(value){
			try{
				var json = utils.parseJSON(value);
				if(json && json.type){
					if(json.type == 'start'){
						$('#progressModal').modal('show', 'fit');
						$('.modal-title').text('加载进度');
					} else if(json.type == 'download'){
						$('#progressModal').modal('show', 'fit');
						$('.modal-title').text('下载进度');
					} else if(json.type == 'trans'){
						var v = Number(json.value.toFixed(2)) * 100;
						$('#progressModal .progress-bar').css('width', v + '%');
					} else if(json.type == 'editfile'){
						$('#progressModal').modal('hide');
						$('.progress-bar').css('width', '0%');
						top['SYSTEM'].openWindow({
							title: json.path,
							types: "exe",
							location: consts.WEB_BASE + 'apps/shell/editor.html?rpath=' + json.path + 
								'&lpath=' + json.value + '&serverIp=' + page.serverIp,
							windowWidth: $(window).width(),
							windowHeight: $(window).height(),
							id: 'edit' + new Date().getTime(),
							icon: consts.WEB_BASE + "images/icons/editor.png"
						});
					} else if(json.type == 'finish'){
						$('#progressModal').modal('hide');
						$('.progress-bar').css('width', '0%');
					}
				}
			}catch(e){}
		},
		
		linux_upload: function(value){
			try{
				var json = utils.parseJSON(value);
				if(json && json.type){
					if(json.type == 'start'){
						$('#progressModal').modal('show', 'fit');
						$('.modal-title').text('2、上传至' + upload_dir);
					} else if(json.type == 'trans'){
						var v = Number(json.value.toFixed(2)) * 100;
						$('#progressModal .progress-bar').css('width', v + '%');
					} else if(json.type == 'upload'){
						$('#progressModal').modal('hide');
						$('.progress-bar').css('width', '0%');
						
						var dir = $('#location').val();
						if(upload_dir == dir || upload_dir == dir + '/'){
							page.loadFiles(page.curr_pwd);
						}
					} else if(json.type == 'error'){
						$('#progressModal').modal('hide');
						$('.progress-bar').css('width', '0%');
						error('上传错误：' + json.msg);
					}
				}
			}catch(e){}
		},
		
		getCurrpath: function(){
			var path = page.curr_pwd;
			if(!/[\s]?\/$/.test(path)){
				path += "/";
			}
			return path;
		},
		
		bindEvent: function(){
			
			$(window).on('resize', function(){
				var bodyHeight = $(window).height();
				$('.container').css('height', (bodyHeight - 50) + 'px');
			});
			
			$('.result-panel').on('click', 'table.table-datatable tr.hover td.name-field', function(){
				var $td = $(this),
					$tr = $td.parent();
				var name = $td.text();
				var path = page.getCurrpath();
				if(name == '..'){
					path = path.substring(0, path.length - 1);
					path = path.substring(0, path.lastIndexOf('/') + 1);
					page.loadFiles(path);
				} else if($tr.hasClass('dir')) {
					path = path + name;
					page.loadFiles(path);
				}				
			});
			
			$('#location').on('keyup', function(e){
				if(e.keyCode == 13){
					page.loadFiles($('#location').val());
				}
			});
			
			$('.btn-open').on('click', function(e){
				page.loadFiles($('#location').val());
			});
			
			$('.btn-parent').on('click', function(e){
				var path = $('#location').val();
				path = path.substring(0, path.length - 1);
				path = path.substring(0, path.lastIndexOf('/') + 1);
				page.loadFiles(path);
				if(path == "/"){
					$('.btn-parent').addClass('hidden');
				}
			});
			
			$('.result-panel').on('click', 'table .btn-open-folder', function(){
				var path = page.getCurrpath();
				var $btn = $(this),
					$tr = $btn.parent().parent(),
					name = $tr.find('.name-field').text();
				if(name == '..'){
					path = path.substring(0, path.length - 1);
					path = path.substring(0, path.lastIndexOf('/') + 1);
					page.loadFiles(path);
				} else if($tr.hasClass('dir')) {
					path = path + name;
					page.loadFiles(path);
				}
			});
			
			$('.result-panel').on('click', 'table .btn-run', function(){
				var path = page.getCurrpath();
				var $btn = $(this),
					$tr = $btn.parent().parent(),
					name = $tr.find('.name-field').text();
				var $dialog = $.confirm({
					msg: '<textarea id="run-args" class="form-control" rows="5">' + path + name + '</textarea>',
					title: '运行程序 - 参数',
					yesText: '运行',
					yesClick: function(){
						var cmd = $dialog.find('#run-args').val();
						if(cmd){
							page.runShell(cmd);
						}
					}
				});
			});
			
			$('.result-panel').on('click', 'table .btn-edit', function(){
				var path = page.getCurrpath();
				var $btn = $(this),
					$tr = $btn.parent().parent(),
					name = $tr.find('.name-field').text();
				ajax.post({
		    		url: "linux/editfile",
		    		data: {path: path, name: name, serverIp: page.serverIp},
		    		dataType: 'json',
		    		type: 'post'
		    	}).done(function(res, rtn, state, msg){
		    		if(state){
		    		}
		    	}).fail(function(){
		    		log.error("失败:", arguments);
		    		error("请求失败");
		    	});
			});
			
			$('.result-panel').on('click', 'table .btn-del', function(){
				var path = page.getCurrpath();
				var $btn = $(this),
					$tr = $btn.parent().parent(),
					name = $tr.find('.name-field').text(),
					type = $tr.find('.type-field').text();
				
				var $dialog = $.confirm({
					msg: (type == 'dir' ? '删除目录将会把目录下所有文件都删除<br>' : '') + '您确定要删除【' + path + name + '】吗？',
					title: type == 'dir' ? '删除目录' : '删除文件',
					yesText: '确定删除',
					cancelText: '不了',
					yesClick: function(){
						page.runShell("rm -rf '" + path + name + "'");
					}
				});
			});
			
			$('.result-panel').on('click', 'table .btn-upload', function(){
								
			}).on('mouseover mouseout', 'table .btn-upload', function(e){
				if(e.type == 'mouseover'){
					var $box = $('.upload-box').css({left: e.clientX -(e.offsetX - 35), top: e.clientY - (e.offsetY - 7)});
					
					var path = page.getCurrpath();
					var $btn = $(this),
						$tr = $btn.parent().parent(),
						name = $tr.find('.name-field').text();
					upload_dir = path + (name == '..' ? '' : name);
					log.info(upload_dir);
				}
			});
			
			$('.result-panel').on('click', 'table .btn-download', function(){
				var path = page.getCurrpath();
				var $btn = $(this),
					$tr = $btn.parent().parent(),
					name = $tr.find('.name-field').text();
				ajax.post({
		    		url: "linux/download",
		    		data: {path: path, name: name, serverIp: page.serverIp},
		    		dataType: 'json',
		    		type: 'post'
		    	}).done(function(res, rtn, state, msg){
		    		if(state){
		    			var json = {path: rtn.data, name: name};
						var param = 'jsonValue=' + utils.toJSON(json);
						$('#down-frame').attr('src', consts.WEB_BASE + "linux/downfile?" + encodeURI(encodeURI(param)));	
		    		} else {
		    			error("下载失败：" + msg);
		    		}
		    	}).fail(function(){
		    		log.error("失败:", arguments);
		    		error("下载失败");
		    	});	
			});
		}
		
	};
	
	var page = new PageScript();
	page.init();
	
});