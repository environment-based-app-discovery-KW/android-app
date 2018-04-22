window.sys={
    hello:function(){
        console.log("hello");
    },
    toast:function(text){
        window.plugins.toast.showLongBottom(text)
    }
};