<template>

  <div class="container">
  
  

    <van-button type="primary" @click="saveCache()">saveCache</van-button>
    <van-button type="info" @click="getCache()">getCache</van-button>
    <van-button type="primary" @click="removeCache()">removeCache</van-button>
    <van-button type="info" @click="removeAllCache()">removeAllCache</van-button>

    <van-button type="primary" @click="qrCodeScanning()">qrCodeScanning</van-button>
    <van-button type="info" @click="openCamera()">openCamera</van-button>
    <van-button type="primary" @click="openPhoto()">openPhoto</van-button>

    <van-button type="info" @click="logOut()">logOut</van-button>
    <van-button type="primary" @click="reLogin()">reLogin</van-button>


    <van-cell-group >
      <van-field label="结果输出栏" readonly/>

      <van-field
          v-model="result"
          rows="1"
          autosize
          type="textarea"
          placeholder="请输入留言"
      />
    </van-cell-group>


  </div>

</template>

<script>
  import {
    XButton, Group, XTextarea
  } from 'vux'

  function setupWebViewJavascriptBridge(callback) {
    if (window.WebViewJavascriptBridge) {
      return callback(WebViewJavascriptBridge);
    }
    if (window.WVJBCallbacks) {
      return window.WVJBCallbacks.push(callback);
    }
    window.WVJBCallbacks = [callback];
    var WVJBIframe = document.createElement("iframe");
    WVJBIframe.style.display = "none";
    WVJBIframe.src = "https://__bridge_loaded__";
    document.documentElement.appendChild(WVJBIframe);
    setTimeout(function () {
      document.documentElement.removeChild(WVJBIframe);
    }, 0);
  }

  var ua = navigator.userAgent.toLowerCase() || window.navigator.userAgent.toLowerCase();
  //判断user-agent
  var myUser = {
    // isWX : /MicroMessenger/i.test(ua), //微信端
    isWX: false,
    isIOS: /(iPhone|iPad|iPod|iOS)/i.test(ua), //苹果家族
    isAndroid: /(android|nexus)/i.test(ua), //安卓家族
    isWindows: /(Windows Phone|windows[\s+]phone)/i.test(ua), //微软家族
    isBlackBerry: /BlackBerry/i.test(ua) //黑莓家族
  };

  //注册事件监听
  function connectWebViewJavascriptBridge(callback) {
    if (window.WebViewJavascriptBridge) {
      callback(WebViewJavascriptBridge)
    } else {
      document.addEventListener(
        'WebViewJavascriptBridgeReady'
        , function () {
          callback(WebViewJavascriptBridge)
        },
        false
      );
    }
  }

  export default {
    components: {
      XButton, Group, XTextarea
    },
    data() {
      return {
	  "test":'test',
        "result": '初始化数据初始化数据初始化数据初始化数据初始化数据初始化数据初始化数据初始化数据初始化数据初始化数据初始化数据初始化数据初始化数据初始化数据初始化数据初始化数据'
      }
    },

    methods: {
      saveCache() {
        let that = this
        if (myUser.isAndroid) {
          window.WebViewJavascriptBridge.callHandler(
            "saveCache",
            {'key': 'bencheng', 'value': this.result},
            function (str) {
              if (str) {
                that.result = str;
              }
            }
          );
          //注册回调函数，第一次连接时调用 初始化函数
          connectWebViewJavascriptBridge(function (bridge) {
            //初始化
            bridge.init(function (message, responseCallback) {
              var data = {
                'Javascript Responds': 'Wee!'
              };
              responseCallback(data);
            });
            //接收安卓发来的消息   并返回给安卓通知
            bridge.registerHandler("saveCache", function (str, responseCallback) {
              var responseData = "我接受到了安卓的调用";
              responseCallback(responseData);
            });
          })

        }
      },

      getCache() {
        let that = this
        if (myUser.isAndroid) {
          window.WebViewJavascriptBridge.callHandler(
            "getCache",
            {'key': 'loginInfos'},
            function (str) {
              if (str) {
                that.result = str;
              }
            }
          );
          //注册回调函数，第一次连接时调用 初始化函数
          connectWebViewJavascriptBridge(function (bridge) {
            //初始化
            bridge.init(function (message, responseCallback) {
              var data = {
                'Javascript Responds': 'Wee!'
              };
              responseCallback(data);
            });
            //接收安卓发来的消息   并返回给安卓通知
            bridge.registerHandler("getCache", function (str, responseCallback) {
              var responseData = "我接受到了安卓的调用";
              responseCallback(responseData);
            });
          })
        }
      },

      removeCache() {
        let that = this
        if (myUser.isAndroid) {
          window.WebViewJavascriptBridge.callHandler(
            "removeCache",
            {'key': 'loginInfos'},
            function (str) {
              if (str) {
                that.result = str;
              }
            }
          );
          //注册回调函数，第一次连接时调用 初始化函数
          connectWebViewJavascriptBridge(function (bridge) {
            //初始化
            bridge.init(function (message, responseCallback) {
              var data = {
                'Javascript Responds': 'Wee!'
              };
              responseCallback(data);
            });
            //接收安卓发来的消息   并返回给安卓通知
            bridge.registerHandler("removeCache", function (str, responseCallback) {
              var responseData = "我接受到了安卓的调用";
              responseCallback(responseData);
            });
          })

        }
      },

      removeAllCache() {
        let that = this
        if (myUser.isAndroid) {
          window.WebViewJavascriptBridge.callHandler(
            "removeCache",
            {'key': 'all'},
            function (str) {
              if (str) {
                that.result = str;
              }
            }
          );
          //注册回调函数，第一次连接时调用 初始化函数
          connectWebViewJavascriptBridge(function (bridge) {
            //初始化
            bridge.init(function (message, responseCallback) {
              var data = {
                'Javascript Responds': 'Wee!'
              };
              responseCallback(data);
            });
            //接收安卓发来的消息   并返回给安卓通知
            bridge.registerHandler("removeCache", function (str, responseCallback) {
              var responseData = "我接受到了安卓的调用";
              responseCallback(responseData);
            });
          })

        }
      },

      logOut() {
        let that = this
        if (myUser.isAndroid) {
          // android
          window.WebViewJavascriptBridge.callHandler(
            "logOut",
            {param: ""},
            function (str) {
              if (str) {
                that.result = str;
              }
            }
          );
          //注册回调函数，第一次连接时调用 初始化函数
          connectWebViewJavascriptBridge(function (bridge) {
            //初始化
            bridge.init(function (message, responseCallback) {
              var data = {
                'Javascript Responds': 'Wee!'
              };
              responseCallback(data);
            });
            //接收安卓发来的消息   并返回给安卓通知
            bridge.registerHandler("logOut", function (str, responseCallback) {
              var responseData = "我接受到了安卓的调用";
              responseCallback(responseData);
            });
          })
        }
      },
      reLogin() {
        let that = this
        if (myUser.isAndroid) {
          // android
          window.WebViewJavascriptBridge.callHandler(
            "reLogin",
            {param: ""},
            function (str) {
              if (str) {
                that.result = str;
              }
            }
          );
          //注册回调函数，第一次连接时调用 初始化函数
          connectWebViewJavascriptBridge(function (bridge) {
            //初始化
            bridge.init(function (message, responseCallback) {
              var data = {
                'Javascript Responds': 'Wee!'
              };
              responseCallback(data);
            });
            //接收安卓发来的消息   并返回给安卓通知
            bridge.registerHandler("reLogin", function (str, responseCallback) {
              var responseData = "我接受到了安卓的调用";
              responseCallback(responseData);
            });
          })
        }
      },


      qrCodeScanning() {
        let that = this
        if (myUser.isAndroid) {
          // android
          window.WebViewJavascriptBridge.callHandler(
            "qrCodeScanning",
            {param: ""},
            function (str) {
              if (str) {
                that.result = str;
              }
            }
          );
          //注册回调函数，第一次连接时调用 初始化函数
          connectWebViewJavascriptBridge(function (bridge) {
            //初始化
            bridge.init(function (message, responseCallback) {
              var data = {
                'Javascript Responds': 'Wee!'
              };
              responseCallback(data);
            });
            //接收安卓发来的消息   并返回给安卓通知
            bridge.registerHandler("qrCodeScanning", function (str, responseCallback) {
              var responseData = "我接受到了安卓的调用";
              responseCallback(responseData);
            });
          })
        }
      },

      openCamera() {
        let that = this
        if (myUser.isAndroid) {
          window.WebViewJavascriptBridge.callHandler(
            "openCamera",
            {param: ""},
            function (str) {
              if (str) {
                that.result = str;
              }
            }
          );
          //注册回调函数，第一次连接时调用 初始化函数
          connectWebViewJavascriptBridge(function (bridge) {
            //初始化
            bridge.init(function (message, responseCallback) {
              var data = {
                'Javascript Responds': 'Wee!'
              };
              responseCallback(data);
            });
            //接收安卓发来的消息   并返回给安卓通知
            bridge.registerHandler("openCamera", function (str, responseCallback) {
              var responseData = "我接受到了安卓的调用";
              responseCallback(responseData);
            });
          })

        }
      },

      openPhoto() {
        let that = this
        if (myUser.isAndroid) {
          // android
          window.WebViewJavascriptBridge.callHandler(
            "openPhoto",
            {param: ""},
            function (str) {
              if (str) {
                that.result = str;
              }
            }
          );
          //注册回调函数，第一次连接时调用 初始化函数
          connectWebViewJavascriptBridge(function (bridge) {
            //初始化
            bridge.init(function (message, responseCallback) {
              var data = {
                'Javascript Responds': 'Wee!'
              };
              responseCallback(data);
            });
            //接收安卓发来的消息   并返回给安卓通知
            bridge.registerHandler("openPhoto", function (str, responseCallback) {
              var responseData = "我接受到了安卓的调用";
              responseCallback(responseData);
            });
          })
        }
      }
    }
  }
</script>


<style lang="less" scoped>


  .container {
    background-color: white;
    height: 100%;
    width: 100%;
    display: flex;
    flex-direction: column;
    box-sizing: border-box;
    margin: 0;
    padding: 16px;
  }


</style>
