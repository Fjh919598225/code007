function showLogin(){
	layer.open({
	  type: 2,
	  title: '用户登录',
	  area: ['480px', '500px'],
	  content: '/login.html' //iframe的url
	}); 
}

function reloadPage(){
	window.location.reload();
}

function showRegister(){
	layer.open({
		  type: 2,
		  title: '用户注册',
		  area: ['480px', '500px'],
		  content: '/register.html' //iframe的url
		}); 
}

function showModifyPassword(){
	layer.open({
		  type: 2,
		  title: '修改用户密码',
		  area: ['480px', '500px'],
		  content: '/modifyPassword.html' //iframe的url
		}); 
}

function showModifyUserImage(){
	layer.open({
		  type: 2,
		  title: '修改用户头像',
		  area: ['480px', '600px'],
		  content: '/modifyUserImage.html' //iframe的url
		}); 
}


function showFindPassword(){
	layer.closeAll('iframe');
	layer.open({
		  type: 2,
		  title: '找回用户密码',
		  area: ['480px', '600px'],
		  content: '/findPassword.html' //iframe的url
		}); 
}


function IsURL (str_url) { 
	var strRegex = '^((https|http|ftp|rtsp|mms)?://)'
	+ '?(([0-9a-z_!~*\'().&=+$%-]+: )?[0-9a-z_!~*\'().&=+$%-]+@)?' //ftp的user@ 
	+ '(([0-9]{1,3}.){3}[0-9]{1,3}' // IP形式的URL- 199.194.52.184 
	+ '|' // 允许IP和DOMAIN（域名） 
	+ '([0-9a-z_!~*\'()-]+.)*' // 域名- www. 
	+ '([0-9a-z][0-9a-z-]{0,61})?[0-9a-z].' // 二级域名 
	+ '[a-z]{2,6})' // first level domain- .com or .museum 
	+ '(:[0-9]{1,4})?' // 端口- :80 
	+ '((/?)|' // a slash isn't required if there is no file name 
	+ '(/[0-9a-z_!~*\'().;?:@&=+$,%#-]+)+/?)$'; 
	var re=new RegExp(strRegex); 
	//re.test() 
	if (re.test(str_url)) { 
	return (true); 
	} else { 
	return (false); 
	} 
}

function ResizeImages()
{
   var myimg,oldwidth,oldheight;
   var maxwidth=800;
   var maxheight=1000
   var imgs = document.getElementById('content').getElementsByTagName('img');   //如果你定义的id不是article，请修改此处


   for(i=0;i<imgs.length;i++){
     myimg = imgs[i];


     if(myimg.width > myimg.height)
     {
         if(myimg.width > maxwidth)
         {
            oldwidth = myimg.width;
            myimg.height = myimg.height * (maxwidth/oldwidth);
            myimg.width = maxwidth;
         }
     }else{
         if(myimg.height > maxheight)
         {
            oldheight = myimg.height;
            myimg.width = myimg.width * (maxheight/oldheight);
            myimg.height = maxheight;
         }
     }
   }
}

function sign(){
	$.get("/user/sign",{},function(result){
		if(!result.success){
			alert(result.errorInfo);
		}else{
			alert("签到成功!");
			window.location.reload();
		}
	},"json");
	

}