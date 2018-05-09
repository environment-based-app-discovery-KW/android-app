window.sys={
    hello:function(){
        console.log("hello");
    },
    toast:function(text){
        window.plugins.toast.showLongBottom(text)
    },
    getUserInfo:function(fields,successCallback, failCallback){
        // fields: [ "name", "mobile", "email" ]
        cordova.exec(successCallback, failCallback, 'Auth', 'getUserInfo', fields);
    },
};