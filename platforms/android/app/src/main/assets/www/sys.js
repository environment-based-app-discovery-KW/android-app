window.sys={
    hello:function(){
        console.log("hello");
    },
    toast:function(text){
        window.plugins.toast.showLongBottom(text)
    },
    getUserInfo:function(successCallback, failCallback){
        cordova.exec(successCallback, failCallback, 'Auth', 'getUserInfo', []);
    },
};