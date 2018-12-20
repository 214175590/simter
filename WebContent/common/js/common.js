/**
 * 
 */
define(function(require, exports, module) {
	
	return {
		
		getUserIconByWinid: function(winid){
			var result = null;
			if(top.SYSTEM && top.SYSTEM.iconData){
				var winobj = top.SYSTEM.os.control.getWindow(winid);
				if(winobj){
					var icon = top.SYSTEM.os.control.getDesktopIcon(winobj.desktopIconId);
					if(icon){
						var iconid = icon.body.data('id');
						if(iconid){
							var data = null;
							for(var i = 0, k = top.SYSTEM.iconData.length; i < k; i++){
								data = top.SYSTEM.iconData[i];
								if(data.deskIconId == iconid){
									result = data;
									break;
								}
							}
						}
					}
				}
			}
			return result;
		}
		
	};
});