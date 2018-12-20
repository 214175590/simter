/**
 * 
 */
define(function(require, exports, module) {
	var upload_dir = '',
		pager = require('../../../lib/zuiplugin/zui.pager');
	var GroupMenu = {
		 '10': "开发环境",
		 '11': "测试环境",
		 '12': "体验环境",
		 '13': "生产环境",
		 '14': "其他环境"
	};
	
	function PageScript(){
		this.user = {};
		this.os = {};
		this.page_size = 30;
		this.page_index = 0;
	}
	
	PageScript.prototype = {
		
		init: function(){
			
			var params = utils.getUrlParam();
			page.serverIp = params['serverIp'] || '';
			page.account = params['account'] || '';
			
			page.windowId = document.location.hash.substring(1);
			
			$('.container').css('height', ($(window).height() - 50) + 'px');
			
			$.useModule(['chosen', 'datatable'], function(){
				page.loadData();
			});
			
			page.bindEvent();
		},
		
		loadData: function(path){
			var group = $("input[name='options']:checked").val();
			ajax.post({
	    		url: "linuxHost/loadLinuxList",
	    		data: {groupId: group},
	    		dataType: 'json',
	    		type: 'post'
	    	}).done(function(res, rtn, state, msg){
	    		if(state){
	    			page.renderGrid(rtn.data);
	    		}
	    	}).fail(function(){
	    		log.error("失败:", arguments);
	    		error("数据加载失败");
	    	});
		},
		
		renderGrid: function(data){
			var trHtmls = '', obj = {}, index = "01", rowId = "";
			if(data && data.length){
				page.RowData = {};
				for(var i = 0; i < data.length; i++){
					index = utils.upzore(i, 2);
					obj = data[i];
					
					page.RowData["row-" + ("index-" + index)] = obj;
					
					trHtmls += laytpl('list-tr.tpl').render({
						"index": index,
						"rowId": ("index-" + index),
						"name": obj.name,
						"size": obj.host,
						"type": obj.account,
						"attr": obj.port,
						"owner": GroupMenu[obj.groupId],
						"buttons": (function(){
							var btnHtml = '';
							btnHtml += laytpl('list-btn.tpl').render({
								"class": "btn-edit",
								"icon": "icon-edit",
								"title": "编辑",
								"rightCode": "edit"
							}) + '&nbsp;';
							btnHtml += laytpl('list-btn.tpl').render({
								"class": "btn-del",
								"icon": "icon-remove",
								"title": "删除",
								"rightCode": "del"
							}) + '&nbsp;';
							return btnHtml;
						})()
					});
				}
			}
			
			if(!trHtmls){
				trHtmls += laytpl('list-tr.tpl').render({
					"index": '1',
					"rowId": '1',
					"name": '无任何节点',
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
		},
		
		
		bindEvent: function(){
			
			$(window).on('resize', function(){
				var bodyHeight = $(window).height();
				$('.container').css('height', (bodyHeight - 50) + 'px');
			});
			
			$('.btn-add').on('click', function(){
				var $dialog = $.confirm({
					msg: laytpl('edit.tpl').render({
						name: '',
						host: '192.168.',
						account: '',
						password: '',
						port: '22',
						groupId: '14'
					}),
					title: '新增节点',
					yesText: '保存',
					yesClick: function(m, e){
						e.stopPropagation();
						var name = $dialog.find('#new-name').val();
						var host = $dialog.find('#new-host').val();
						var account = $dialog.find('#new-account').val();
						var password = $dialog.find('#new-password').val();
						var port = $dialog.find('#new-port').val() || 22;
						var group = $dialog.find('#new-group').val() || "14";
						if(name && host){
							ajax.post({
					    		url: "linuxHost/addYxosLinux",
					    		data: {
					    			name: name,
					    			host: host,
					    			account: account,
					    			password: password,
					    			port: port,
					    			groupId: group
					    		},
					    		dataType: 'json',
					    		type: 'post'
					    	}).done(function(res, rtn, state, msg){
					    		if(state){
					    			alert("新增成功");
					    			m.modal('hide');
					    			page.loadData();
					    		} else {
					    			error("新增失败：" + msg);
					    		}
					    	}).fail(function(){
					    		error("新增失败");
					    		m.modal('hide');
					    	});
						}
					}
				});
			});
			
			$('.result-panel').on('click', 'table .btn-edit', function(){
				var $btn = $(this),
					$tr = $btn.parent().parent(),
					index = $tr.data('id');
				var obj = page.RowData["row-" + index];
				var $dialog = $.confirm({
					msg: laytpl('edit.tpl').render({
						name: obj.name,
						host: obj.host,
						account: obj.account,
						password: utils.decrypt(obj.password, obj.userId),
						port: obj.port,
						groupId: obj.groupId
					}),
					title: '修改节点',
					yesText: '保存',
					yesClick: function(m, e){
						e.stopPropagation();
						var name = $dialog.find('#new-name').val();
						var host = $dialog.find('#new-host').val();
						var account = $dialog.find('#new-account').val();
						var password = $dialog.find('#new-password').val();
						var port = $dialog.find('#new-port').val() || 22;
						var group = $dialog.find('#new-group').val() || "14";
						if(name && host){
							if(password){
								password = utils.encrypt(password, obj.userId);
							}
							ajax.post({
					    		url: "linuxHost/editYxosLinux",
					    		data: {
					    			id: obj.id,
					    			name: name,
					    			host: host,
					    			account: account,
					    			password: password,
					    			port: port,
					    			groupId: group
					    		},
					    		dataType: 'json',
					    		type: 'post'
					    	}).done(function(res, rtn, state, msg){
					    		if(state){
					    			alert("保存成功");
					    			m.modal('hide');
					    			page.loadData();
					    		} else {
					    			error("保存失败：" + msg);
					    		}
					    	}).fail(function(){
					    		error("保存失败");
					    		m.modal('hide');
					    	});
						}
					}
				});
			});
			
			$('.result-panel').on('click', 'table .btn-del', function(){
				var $btn = $(this),
					$tr = $btn.parent().parent(),
					index = $tr.data('id');
				var obj = page.RowData["row-" + index];
				var $dialog = $.confirm({
					msg: '您确定要删除【' + obj.name + '(' + obj.host + ')' + '】节点吗？',
					title: '删除节点',
					yesText: '确定删除',
					cancelText: '不了',
					yesClick: function(){
						ajax.post({
				    		url: "linuxHost/delYxosLinux",
				    		data: {
				    			id: obj.id,
				    			host: obj.host
				    		},
				    		dataType: 'json',
				    		type: 'post'
				    	}).done(function(res, rtn, state, msg){
				    		if(state){
				    			alert("删除成功");
				    			page.loadData();
				    		} else {
				    			error("删除失败：" + msg);
				    		}
				    	}).fail(function(){
				    		error("删除失败");
				    	});
					}
				});
			});
			
			$('.btn-group .btn').on('click', function(){
				setTimeout(page.loadData, 100);
			});
			
		}
		
	};
	
	var page = new PageScript();
	page.init();
	
});