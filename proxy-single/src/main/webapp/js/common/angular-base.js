var app=angular.module("app",["ui.router"]);

/**
 * 异步请求(使用统一的接口用来切换测试环境与真实环境)
 */
app.provider('$base', function() {
	var baseUrl="/";
	var debug=false;
	var loaded=false;
	this.setBaseUrl=function(baseUrl){
		this.baseUrl=baseUrl;
	}
	this.setDebug=function(debug){
		this.debug=debug;
	}
	this.$get = function ($http,$location,$rootScope){
		var service={};
		service.ajax=function(url,param,cache){
			var type="get";
			if(param){
				type="post";
			}
			if(debug){
				type="get";
			}
			service.loading();
			var fun={
				then:function(callBack){
					fun.prototype.then(function(p1,p2,p3){
						service.loaded();
						callBack.apply(callBack,[p1,p2,p3]);
					});
				},
				success:function(callBack){
					fun.prototype.success(function(p1,p2,p3){
						service.loaded();
						callBack.apply(callBack,[p1,p2,p3]);
					});
				},
				error:function(callBack){
					fun.prototype.error(function(p1,p2,p3){
						service.loaded();
						callBack.apply(callBack,[p1,p2,p3]);
					});
				}
			};
			if(type=="get"){
				fun.prototype = this.get(url);
			}else{
				fun.prototype = this.post(url,param);
			}
			return fun;
		};
		service.post=function(url,param,cache){
			if(url.indexOf("?")>=0){
				url=baseUrl+url+"&t"+new Date().getTime();
			}else{
				url=baseUrl+url+"?t"+new Date().getTime();
			}
			return $http.post(url,param);
		};
		service.get=function(url,cache){
			if(url.indexOf("?")>=0){
				url=baseUrl+url+"&t"+new Date().getTime();
			}else{
				url=baseUrl+url+"?t"+new Date().getTime();
			}
			return $http.get(url);
		};
		service.path=function(path){
			service.loading();
			$location.path(path);
			service.loaded();
		};
		service.loading=function(){
			loaded=false;
			$rootScope.$broadcast("loading.change");
		};
		service.loaded=function(){
			loaded=true;
			$rootScope.$broadcast("loading.change");
		};
		service.getLoadState=function(){
			return loaded;
		};
		
		
		return service;
	};
});
/**
 * 定间隔运行（window.setInterval有bug，每次scope都会新增一个定时任务）
 */
app.provider('$interval', function() {
	var started=false;
	var funs={};
	this.$get = function (){
		var service={};
		service.add=function(name,fun){
			funs[name]=fun;
			if(!started){
				started=true;
				window.setInterval(function(){
					for(var name in funs){
						funs[name].apply();
					}
				}, 1000);
			}
		};
		return service;
	};
});
/**
 * 全局会话缓存
 */
app.provider("$session", function() {
	var data={};
	
	this.$get = function (){
		var service={};

		service.put=function(key,value){
			data[key]=value;
		};
		service.get=function(key){
			return data[key];
		};
		return service;
	}
	
});


/**
 * 加载栏
 */
app.controller("loading",function($scope,$base) {
	$scope.$on("loading.change",function(){
		$scope.loaded=$base.getLoadState();
//		$scope.$apply();
	});
});

/**
 * 异步加载数据
 */
app.directive("ngDataHttp",function($base,$rootScope){
	return ({
		restrict:"A",
		link:function($scope, $elem, $attrs){
			
			var match = $attrs.ngDataHttp.match(/^\s*([\s\S]+?)\s+(get|post)+?\s+([\s\S]+?)?(\s+by\s+([\s\S]+?)?)?\s*$/);
			if (!match) {
				throw "ng-data-http format error";
			}
		
			var field = match[1];
			var method,url,param = match[4];
			if(match.length>2){
				method = match[2];
			}
			if(match.length>3){
				url = match[3];
			}
			if(match.length>4){
				param = match[4];
			}
			
			var load=function(){
				$base.ajax(url,$scope[param]).success(function(result){
					$scope[field]=result;
					$rootScope.$broadcast(field+".loaded");
				});
			};
			
			load();
			$scope.$on(field+".reload",load);
		}
	});
});

/**
 * 异步加载数据
 */
app.directive("ngScrollDataHttp",function($base,$rootScope,$window, $document){
	return ({
		restrict:"A",
		link:function($scope, $elem, $attrs){
			
			var match = $attrs.ngScrollDataHttp.match(/^\s*([\s\S]+?)\s+(get|post)+?\s+([\s\S]+?)?(\s+by\s+([\s\S]+?)?)?\s*$/);
			if (!match) {
				throw "ng-data-http format error";
			}
		
			var field = match[1];
			var method,url,param = match[4];
			if(match.length>2){
				method = match[2];
			}
			if(match.length>3){
				url = match[3];
			}
			if(match.length>4){
				param = match[4];
			}

			$scope[field]=[];
			var pageNum=0;
			var pageSize=10;
			var nexting=false;
			
			var offset=200;
			var $win=$($window);
			var $doc=$($document);
			var $element=$($elem);
			
			var scroll=function(){

				var documentHeight = $doc.height();
				var scrollTop = $win.scrollTop();
				var windowHeight = $win.height();
				var windowBottom = scrollTop+windowHeight;
				
				var height = $element.height();
				var top = $element.offset().top;
				var bottom = ( top + height );
				if(pageNum==0){
					if(windowBottom>top){
						next();
					}
				}
				if(bottom-windowBottom<offset){
					next();
				}
				
			};
			$win.on("scroll.phone-page",scroll);
			$win.on("resize.phone-page",scroll);
			
			var next=function(){
				if(!nexting){
					$element.attr("scroll-status","nexting");
					nexting=true;
					pageNum++;

					var u=url+"";
					if(u.indexOf("?")<0){
						u+="?1=1";
					}
					u+="$pageNum="+pageNum;
					u+="$pageSize="+pageSize;
					$base.ajax(u,$scope[param]).success(function(result){
						$element.attr("scroll-status","nexted");
						if(result.data.length<pageSize){
							$element.attr("scroll-status","none");
						}else{
							nexting=false;
						}
						for(var index in result.data){
							$scope[field].push(result.data[index]);
						}
						$rootScope.$broadcast(field+".loaded");
					});
				}
			}
			
			var reload=function(){
				$scope[field]=[];
				pageNum=0;
				next();
			};

			scroll();
			$scope.$on(field+".reload",reload);
		}
	});
});

/**
 * 读取cookie数据
 */
app.directive("ngDataCookie",function($cookieStore){
	return ({
		restrict:"A",
		link:function($scope, $elem, $attrs){
			
			var match = $attrs.ngDataHttp.match(/^\s*([\s\S]+?)\s+get\s+([\s\S]+?)?\s*$/);
			if (!match) {
				throw "ng-data-cookie format error";
			}
		
			var field = match[1];
			var key = match[2];
			
			var load=function(){
				$scope[field]=$cookieStore.get(key);
			};
			
			load();
			$scope.$on(field+".reload",load);
		}
	});
});

/**
 * 读取缓存数据
 */
app.directive("ngDataCache",function($session){
	return ({
		restrict:"A",
		link:function($scope, $elem, $attrs){
			
			var match = $attrs.ngDataHttp.match(/^\s*([\s\S]+?)\s+get\s+([\s\S]+?)?\s*$/);
			if (!match) {
				throw "ng-data-cache format error";
			}
		
			var field = match[1];
			var key = match[2];
			
			var load=function(){
				$scope[field]=$session.get(key);
			};
			
			load();
			$scope.$on(field+".reload",load);
		}
	});
});