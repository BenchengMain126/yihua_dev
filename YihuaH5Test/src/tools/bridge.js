// /**
//  * android系统js桥接口设置
//  * @param {Object} callback 桥接口回调函数 桥接方式固定写法
//  */
// const setUpAndroidBridge = (callback) => {
//
//   if (window.WebViewJavascriptBridge) {
//     // //初始化
//     // window.WebViewJavascriptBridge.init(function (message, resultCallback) {
//     //   var data = {
//     //     'Javascript Responds': 'Wee!'
//     //   };
//     //   resultCallback(data);
//     // });
//     return callback(WebViewJavascriptBridge);
//   } else {
//     document.addEventListener('WebViewJavascriptBridgeReady', function () {
//
//       // //初始化
//       // window.WebViewJavascriptBridge.init(function (message, resultCallback) {
//       //   var data = {
//       //     'Javascript Responds': 'Wee!'
//       //   };
//       //   resultCallback(data);
//       // });
//       return callback(WebViewJavascriptBridge);
//     }, false);
//   }
//
//
// };
//
//
// const callHandler = (name, data, resultCallback) => {
//
//   //安卓手机交互方式
//   setUpAndroidBridge(
//     function (bridge) {
//       bridge.callHandler(name, data, function (response) {
//         resultCallback(response);
//       });
//     }
//   );
//
// };
//
//
// module.exports = {
//   callHandler
// }



/**
 * android系统js桥接口设置
 * @param {Object} callback 桥接口回调函数 桥接方式固定写法
 */



const callHandler = (name, data, resultCallback) => {

  //初始化
  window.WebViewJavascriptBridge.init(function (message, resultCallback) {
    var data = {
      'Javascript Responds': 'Wee!'
    };
    resultCallback(data);
  });

  window.WebViewJavascriptBridge.callHandler(name, data, function (response) {
    resultCallback(response);
  });

};


module.exports = {
  callHandler
}