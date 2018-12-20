/**
 * 
 */
define(function(require, exports, module) {
	var data__key = "__menu_data_key__";
	var GroupMenu = [{
		rightId: '10',
		rightLevel: 1,
		rightName: "开发环境"
	},{
		rightId: '11',
		rightLevel: 1,
		rightName: "测试环境"
	},{
		rightId: '12',
		rightLevel: 1,
		rightName: "体验环境"
	},{
		rightId: '13',
		rightLevel: 1,
		rightName: "生产环境"
	},{
		rightId: '14',
		rightLevel: 1,
		rightName: "其他环境"
	}];
	var menuIdNumber = 10000;
	
	function getPage(h, f){
		return (f ? '' : consts.WEB_BASE) + 'apps/shell/' + (top.platform == 'web' ? h : 'm' + h);
	}
	
	function PageScript(){
		this.user = {};
		this.os = {};
		this.menuJson = {};
		this.system_page = getPage('system.html', true);
	}
	
	PageScript.prototype = {
		
		init: function(){
			
			$('#mainFrame').attr('src', getPage('main.html')).load(function(){
				$('.wait-panel').remove();
			});
			
			page.loadMenus();
		},
		
		loadMenus: function(){
			ajax.post({
	    		url: "linuxHost/loadLinuxList",
	    		data: {},
	    		dataType: 'json'
	    	}).done(function(res, rtn, state, msg){
	    		if(state){
	    			var datas = rtn.data;
	    			var menus = [];
	    			for(var i = 0; i < GroupMenu.length; i++){
	    				menus.push(GroupMenu[i]);
	    			}
	    			for(var i = 0; i < datas.length; i++){
	    				menus.push({
	    					parentRightId: datas[i].groupId,
	    					rightId: menuIdNumber++,
	    					rightLevel: 2,
	    					rightName: datas[i].name,
	    					rightUrl: datas[i].host
	    				});
	    			}
	    			
	    			page.renderMenu(menus);
	    		} else {
	    			error("书籍加载失败:"+ msg);
	    		}
	    	}).fail(function(){
	    		log.error("失败:", arguments);
	    		error("数据加载失败");
	    	});
		},
		
		renderMenu: function(menus){
			var menu = {}, menuHtml = "", menuJson = [], itemJson = {}, items = "";
			var menuNameJson = {}, nameJson = {};
			for(var i = 0, k = menus.length; i < k; i++){
				menu = menus[i];
				if(menu.rightLevel == '1'){
					menuJson.push({
						menuName: menu.rightName,
						menuId: menu.rightId,
						index: i
					});
				} else if(menu.rightLevel == '2'){
					if(!itemJson[menu.parentRightId]){
						itemJson[menu.parentRightId] = "";
					}
					items = itemJson[menu.parentRightId];
					items += laytpl('menu_item.tpl').render({
						menuName: menu.rightName,
						menuId: menu.rightId,
						menuUrl: page.system_page,
						index: i
					});
					itemJson[menu.parentRightId] = items;
					
					if(!nameJson[menu.parentRightId]){
						nameJson[menu.parentRightId] = [];
					}
					nameJson[menu.parentRightId].push({
						id: menu.rightId,
						name: menu.rightName,
						ip: menu.rightUrl
					});
				}
			}
			var obj, size = menuJson.length;
			for(var j = 0; j < size; j++){
				obj = menuJson[j];
				menuHtml += laytpl('menu_line.tpl').render({
					menuName: obj.menuName,
					menuId: obj.menuId,
					items: itemJson[obj.menuId] || "",
					index: obj.index
				});
				
				var arr = nameJson[obj.menuId], twoMenu = {};
				if(arr && arr.length){
					for(var i = 0; i < arr.length; i++){
						twoMenu = arr[i];
						menuNameJson[twoMenu.id] = [obj.menuName, twoMenu.name, twoMenu.ip];
					}
				}
			}
			$('#side-menu').html(menuHtml);
			
			if(window['sessionStorage']){
				sessionStorage.setItem('_MENU_NAME_JSON', utils.toJSON(menuNameJson));
			}
			page.menuJson = menuNameJson;
			page.bindEvent();
		},
		
		bindEvent: function(){
			
			// 一级菜单
			$('#side-menu').on('click', '.nav-menu', function(){
				var $this = $(this),
					$parent = $this.parent();
				if($parent.hasClass('active')){ // 已展开 -> 收缩
					$parent.removeClass('active');
					$this.next().removeClass('in');
					$this.find('.arrow').addClass('icon-angle-left').removeClass('icon-angle-down');
					$this.find('.icon').addClass('icon-folder-close').removeClass('icon-folder-open');
				} else { // 已收缩 -> 展开
					var $siblings = $parent.siblings(),
						$child = $siblings.find('ul.nav-second-level');
					$siblings.removeClass('active');
					$siblings.find('.nav-menu .arrow').addClass('icon-angle-left').removeClass('icon-angle-down');
					$siblings.find('.nav-menu .icon').addClass('icon-folder-close').removeClass('icon-folder-open');
					$child.removeClass('in');
					
					$parent.addClass('active');
					$this.next().addClass('in');
					$this.find('.arrow').removeClass('icon-angle-left').addClass('icon-angle-down');
					$this.find('.icon').addClass('icon-folder-open').removeClass('icon-folder-close');
				}
				
			});
			
			// 点击菜单项，新增tab或者定位tab
			$('#side-menu').on('click', '.J_menuItem', function(){
				var $this = $(this),
					id = $this.data('id'),
					src = $this.data('href'),
					menuName = $this.text();
				
				$('.J_menuItem').removeClass('active');
				$this.addClass('active');
				
				if($('.page-tabs-content .J_menuTab.' + id).length == 0){ // 没有tab，添加
					var index = src.indexOf("#"),
						serverIp = page.getServerIp(id);
					if(index != -1){
						src = src.substring(0, index) + "?serverIp=" + serverIp + src.substring(index);
					} else {
						src = src + "?serverIp=" + serverIp;
					}
					
					$('.page-tabs-content .J_menuTab').removeClass('active');
					$('.rightframe').hide();
					$('.page-tabs-content').append(laytpl('page_tab.tpl').render({"id": id, "text": menuName}));
					$('.page-tabs-content .J_menuTab i').tooltip();
					$('.body').append(laytpl('iframe.tpl').render({"fid": id, "src": consts.WEB_BASE + src}));
					page.showWaitAndHidden(id, true);
					var $avtiveMenuTab = $('.page-tabs-content .J_menuTab.' + id),
						tabswid = $('.head-block.tabs').width() - page.pageTabDiff,
						pagetabswid = $('.head-block.tabs .page-tabs-content').width();
					if(pagetabswid > tabswid){
						var $menuTabs = $('.head-block.tabs .J_menuTab'),
							len = $menuTabs.length,
							avtivewid = $avtiveMenuTab.width();
						for(var i = 0; i < len; i++){
							var $this = $($menuTabs[i]),
								thiswid = $this.width();
							if(!$this.hasClass('hidden')){
								$this.addClass('hidden');
								if(thiswid < avtivewid){
									if(!$this.next().hasClass('hidden')){
										$this.next().addClass('hidden');
									}
								}
								return;
							}
						}
					}
				} else { // 已有tab，设置焦点
					$('.page-tabs-content .J_menuTab').removeClass('active');
					$('.rightframe').hide();
					
					var $avtiveMenuTab = $('.page-tabs-content .J_menuTab.' + id);
					$avtiveMenuTab.addClass('active');
					$('#rightFrame-' + id).show();
					
					var index = $avtiveMenuTab.index(),
						$JMenuTabs = $('.page-tabs-content .J_menuTab'),
						len = $JMenuTabs.length,
						allwid = 0,
						tabswid = $('.head-block.tabs').width() - page.pageTabDiff;
					if($avtiveMenuTab.hasClass('hidden')){
						for(var i = index - 1; i < len; i++){
							var $menuTab = $($JMenuTabs[i]);
							$menuTab.removeClass('hidden');
						}
					} else {
						var end = index - 5 < 0 ? 0 : index - 5;
						for(var i = 0; i < end; i++){
							var $menuTab = $($JMenuTabs[i]);
							$menuTab.addClass('hidden');
						}
					}
				}
			});
			
			// 点击page tab，切换页面，并改变相应菜单状态
			$('.page-tabs-content').on('click', '.J_menuTab', function(){
				var $this = $(this),
					id = $this.data('id'),
					$siblings = $this.siblings();
				$siblings.removeClass('active');
				$this.addClass('active');
				page.changeMenuState(id);
				$('.rightframe').hide();
				$('#rightFrame-' + id).show();
				
				if($this.hasClass('main')){
					$('#side-menu .J_menuItem').removeClass('active');
				}
				
				$('.wait-panel').addClass('hidden').data('id', '').data('time', new Date().getTime());
			});
			
			// 点击page tab上的关闭图标
			$('.page-tabs-content').on('click', '.J_menuTab > i', function(e){
				var $this = $(this).parent(), // 当前点击的节点
					$prev = $this.prev(), // 上一个节点
					id = $this.data('id'), // 当前点击的节点ID
					pid = $prev.data('id'); // 上一个节点ID
				// 判断当前节点是否为选中状态，若选中则显示的到上一个节点页面，否则不变
				if($this.hasClass('active')){
					$prev.addClass('active');
					page.changeMenuState(pid);
					$('#rightFrame-' + pid).show();
				}
				$this.remove(); // 删除当前节点
				$('#rightFrame-' + id).remove(); // 删除当前页面 
				
				$('.wait-panel').addClass('hidden').data('id', '').data('time', new Date().getTime());
				
				var tabswid = $('.head-block.tabs').width() - page.pageTabDiff,
					pagetabswid = $('.head-block.tabs .page-tabs-content').width();
				if(pagetabswid < (tabswid - 250)){
					var $JMenuTabs = $('.page-tabs-content .J_menuTab.hidden'),
						len = $JMenuTabs.length;
					$($JMenuTabs[len - 1]).removeClass('hidden');
				}
				
				e.stopPropagation();
			});
			
			// 关闭全部
			$('.head-block.tabs').on('click', '.J_tabCloseAll', function(){
				$('.head-block.tabs .J_menuTab').each(function(i, e){
					var $this = $(this),
						id = $this.data('id');
					if(!$this.hasClass('main')){
						$this.remove(); // 删除当前节点
						$('#rightFrame-' + id).remove(); // 删除当前页面 
					}
				});
				$('.head-block.tabs .J_menuTab.main').addClass('active').removeClass('hidden');
				$('#rightFrame-main').show(); // 显示首页 
				page.changeMenuState(null);
			});
			
			// 关闭其他
			$('.head-block.tabs').on('click', '.J_tabCloseOther', function(){
				$('.head-block.tabs .J_menuTab').each(function(i, e){
					var $this = $(this),
						id = $this.data('id');
					if(!$this.hasClass('active') && !$this.hasClass('main')){
						$this.remove(); // 删除当前节点
						$('#rightFrame-' + id).remove(); // 删除当前页面 
					}
				});
				$('.head-block.tabs .J_menuTab.main').removeClass('hidden');
			});
			
			// 定位当前选项
			$('.head-block.tabs').on('click', '.J_tabShowActive', function(){
				var $avtiveMenuTab = $('.page-tabs-content .J_menuTab.active');
				var index = $avtiveMenuTab.index(),
					$JMenuTabs = $('.page-tabs-content .J_menuTab'),
					len = $JMenuTabs.length,
					allwid = 0,
					tabswid = $('.head-block.tabs').width() - page.pageTabDiff;
				if($avtiveMenuTab.hasClass('hidden')){
					for(var i = index - 1; i < len; i++){
						var $menuTab = $($JMenuTabs[i]);
						$menuTab.removeClass('hidden');
					}
				} else {
					for(var i = 0; i < len; i++){
						var $menuTab = $($JMenuTabs[i]),
							width = $menuTab.width();
						if(i <= index){
							allwid += width + 31;
							if(allwid > tabswid){
								$('.head-block.tabs .J_tabRight').click();
							}
						} else {
							break;
						}
					}
				}
			});
			
			// 关闭当前页面
			if(top.regShortKey){
				top.regShortKey("Alt67", function(e, code){
					var $this = $('.page-tabs-content .J_menuTab.active'), // 当前点击的节点
						$prev = $this.prev(), // 上一个节点
						id = $this.data('id'), // 当前点击的节点ID
						pid = $prev.data('id'); // 上一个节点ID
					if($this.hasClass('main')){
						return;
					}
					// 判断当前节点是否为选中状态，若选中则显示的到上一个节点页面，否则不变
					if($this.hasClass('active')){
						$prev.addClass('active');
						page.changeMenuState(pid);
						$('#rightFrame-' + pid).show();
					}
					$this.remove(); // 删除当前节点
					$('#rightFrame-' + id).remove(); // 删除当前页面 
					
					$('.wait-panel').addClass('hidden').data('id', '').data('time', new Date().getTime());
					
					var tabswid = $('.head-block.tabs').width() - page.pageTabDiff,
						pagetabswid = $('.head-block.tabs .page-tabs-content').width();
					if(pagetabswid < (tabswid - 250)){
						var $JMenuTabs = $('.page-tabs-content .J_menuTab.hidden'),
							len = $JMenuTabs.length;
						$($JMenuTabs[len - 1]).removeClass('hidden');
					}
				});
			}
			
			// 定位首页选项卡
			$('.head-block.tabs').on('click', '.J_tabHome', function(){
				$('.head-block.tabs .J_menuTab').removeClass('hidden').removeClass('active');
				$('.head-block.tabs .J_menuTab.main').addClass('active');
				$('.rightframe').hide(); // 隐藏所有页面
				$('#rightFrame-main').show(); // 显示首页
				$('#side-menu .J_menuItem').removeClass('active'); // 所有菜单去掉状态
				
				$('.wait-panel').addClass('hidden').data('id', '').data('time', new Date().getTime());
			});
			
			// 刷新当前页面
			$('.head-block.tabs').on('click', '.J_tabFlush', function(){
				$('.head-block.tabs .J_tabShowActive').click();
				var $avtiveMenuTab = $('.page-tabs-content .J_menuTab.active'),
					id = $avtiveMenuTab.data('id'),
					$frame = $('#rightFrame-' + id);
				$frame.attr('src', $frame.attr('src')); // 删除当前页面 
				page.showWaitAndHidden(id, false);			
			});		
			
			// 移动page tab -> 向左移动
			$('.head-block.tabs').on('click', '.J_tabLeft', function(){
				var $tempTab = null;
				$('.head-block.tabs .J_menuTab').each(function(i, e){
					var $this = $(this);
					if($this.hasClass('hidden')){
						$tempTab = $this;
					} else {
						if($tempTab){
							$tempTab.removeClass('hidden');
						}
						return;
					}
				});
			});
			
			// 移动page tab -> 向右移动
			$('.head-block.tabs').on('click', '.J_tabRight', function(){
				var allwid = $('.head-block.tabs').width(),
				tabswid = allwid - page.pageTabDiff,
				pagetabswid = $('.head-block.tabs .page-tabs-content').width();
				if(pagetabswid > tabswid){
					var $menuTabs = $('.head-block.tabs .J_menuTab'),
						len = $menuTabs.length;
					for(var i = 0; i < len; i++){
						var $this = $($menuTabs[i]);
						if(!$this.hasClass('hidden')){
							$this.addClass('hidden');
							return;
						}
					}
				}
			});
			
			
			$('.sidebar-switch .switch-left').on('click', function(){
				var $switch = $('.sidebar-switch'),
					$switchRight = $('.sidebar-switch .switch-right'),
					$bodyleft = $('.main-body .body-left'),
					$bodyright = $('.main-body .body-right');
				if(!$bodyleft.hasClass('hidden')){
					$switch.css('left', (top.platform == 'web' ? '-16px' : '-20px'));
					$bodyleft.addClass('float-left');
					$switchRight.removeClass('hidden');
					$bodyright.css('width', '100%');
				}
			});
			
			$('.sidebar-switch .switch-right').on('click', function(){
				var $switch = $('.sidebar-switch'), 
					$bodyleft = $('.main-body .body-left'),
					$bodyright = $('.main-body .body-right');
				if($bodyleft.hasClass('float-left')){
					$(this).addClass('hidden');
					$switch.css('left', (top.platform == 'web' ? '244px' : '240px'));
					$bodyleft.removeClass('float-left');
					$bodyright.css('width', 'calc(100% - 260px)');
				}
			});
			
			$('.main-body .body-fullscreen').on('click', function(){
				var $this = $(this),
					$right = $('.main-body .body'),
					isfull = $this.data('isfull');
				if(isfull){
					$('.main-body .body').removeClass('fullscreen');
					$this.data('isfull', false);
				} else {
					$('.main-body .body').addClass('fullscreen');
					$this.data('isfull', true);
				}
			});
			
			$('.J_tabExit').on('click', function(){
				$.confirm({
					title: '提示',
					yesText: '确认退出',
					msg: '您确认要退出系统吗？',
					yesClick: function($modal){
						if(window['sessionStorage']){
			        		sessionStorage.removeItem("authen");
			        		sessionStorage.removeItem("USER-INFO");
			        	}
						$modal.modal('hide');
						$.ajax({
				    		url: consts.WEB_BASE + 'lg/lotos',
				    		data: {},
				    		dataType: 'json',
				    		type: 'post'
				    	}).always(function(res, flag){
				    		document.location.replace(getPage('login.html'));
				    	});
					}
				});
			});
			
			$('.tool-box .icon-btn.btn-new').on('click', function(){
				page.showNewItem('', '192.168.');
			});
			
			$('.tool-box .icon-btn.btn-mgt').on('click', function(){
				$('#mgtModal').modal({
					backdrop: 'static',
					moveable: true
				});
			});
			
			$('.tool-box .icon-btn.btn-client').on('click', function(){
				window.open(consts.WEB_BASE + 'apps/promon/index.html');
			});
			
		},
		
		showNewItem: function(name, ip){
			var $dialog = $.confirm({
				msg: laytpl('new.tpl').render({name: name, ip: ip}),
				title: '新增节点',
				yesText: '保存',
				yesClick: function(m, e){
					e.stopPropagation();
					var namev = $dialog.find('#new-name').val();
					var ipv = $dialog.find('#new-ip').val();
					var groupv = $dialog.find('#new-group').val() || "14";
					if(namev && ipv){
						ajax.post({
				    		url: "linuxHost/exist",
				    		data: {serverIp: ipv},
				    		dataType: 'json',
				    		type: 'post'
				    	}).done(function(res, rtn, state, msg){
				    		if(state){
				    			error("该节点已存在：" + ipv);
				    		} else {
				    			ajax.post({
						    		url: "linuxHost/addYxosLinux",
						    		data: {serverIp: ipv, name: namev, groupId: groupv},
						    		dataType: 'json',
						    		type: 'post'
						    	}).done(function(res, rtn, state, msg){
						    		if(state){
						    			var gname = (function(){
											for(var j = 0; j < GroupMenu.length; j++){
												if(GroupMenu[j].rightId == groupv){
													return GroupMenu[j].rightName;
												}
											}
											return groupv;
										})();
										var menuItem = {
											menuId: menuIdNumber++,
											index: menuIdNumber - 9999,
											menuName: namev,
											menuUrl: page.system_page
										};
										page.menuJson[menuItem.menuId] = [gname, namev, ipv];;
										$('#om-' + groupv).append(laytpl('menu_item.tpl').render(menuItem));
										m.modal('hide');
						    		} else {
						    			error("添加失败:"+ msg);
						    		}
						    	}).fail(function(){
						    		log.error("失败:", arguments);
						    		error("数据加载失败");
						    	});
				    		}
				    	}).fail(function(){
				    		error("添加失败");
				    		m.modal('hide');
				    	});
					} else {
						alert("请输入名称与IP");
					}
				}
			});
		},
		
		showWaitAndHidden: function(id, flag){
			var $wait = $('.wait-panel'),
				did = $wait.data('id'),
				$frame = $('#rightFrame-'+id);
			$wait.removeClass('hidden').data('id', id).data('time', new Date().getTime());
			if(flag){
				$frame.on('load', function(){
					var wid = $wait.data('id');
					if(wid == id){
						$wait.addClass('hidden').data('id', '').data('time', new Date().getTime());
					}
				});
			}
			if(did != id){
				setTimeout(function(){
					var wid = $wait.data('id'),
					startTime = $wait.data('time'),
					endTime = new Date().getTime();
					if(wid == id && (endTime - startTime) > 10000){
						$wait.addClass('hidden').data('id', '').data('time', endTime);
					}
				}, 10000);
			}
		},
		
		changeMenuState: function(fid){
			if(fid){
				$('#side-menu .J_menuItem').each(function(){
					var $this = $(this),
						did = $this.data('id'),
						$navMenu = $this.parent().parent().prev()
					if(did == fid){
						$('#side-menu .J_menuItem').removeClass('active');
						$this.addClass('active');
						
						$navMenu.parent().removeClass('active');
						$navMenu.click();
					}
				});
			} else {
				$('#side-menu .J_menuItem').removeClass('active');
			}
		},
		
		getServerIp: function(rid){
			if(page.menuJson[rid]){
				return page.menuJson[rid][2];
			}
			return '';
		}
		
	};
	
	var page = new PageScript();
	page.init();
	
});