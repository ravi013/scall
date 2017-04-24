
var argscheck = require('cordova/argscheck'),
    channel = require('cordova/channel'),
    utils = require('cordova/utils'),
    exec = require('cordova/exec'),
    cordova = require('cordova');

channel.createSticky('onCordovaScallReady');
// Tell cordova channel to wait on the CordovaInfoReady event
channel.waitForInitialization('onCordovaScallReady');

/**
 * This represents the mobile device, and provides properties for inspecting the model, version, UUID of the
 * phone, etc. 
 * @constructor
 */
function Scall() {
    this.available = false;
    this.platform = null;
    this.version = null;
    this.uuid = null;
    this.cordova = null;
    this.model = null;
    this.manufacturer = null;
    this.isVirtual = null;
    this.serial = null;

    var me = this;
  console.log("scall subscribe");
    channel.onCordovaReady.subscribe(function() {
          console.log("scall ready");
		// channel.onCordovaInfoReady.fire();
         channel.initializationComplete('onCordovaScallReady');      
    });
}

/**
 * Get device info
 *
 * @param {Function} successCallback The function to call when the heading data is available
 * @param {Function} errorCallback The function to call when there is an error getting the heading data. (OPTIONAL)
 */
Scall.prototype.register = function(successCallback, errorCallback) {
    argscheck.checkArgs('fF', 'Scall.register', arguments);
    exec(successCallback, errorCallback, "Scall", "register", []);
};
Scall.prototype.unregister = function(successCallback, errorCallback) {
    argscheck.checkArgs('fF', 'Scall.unregister', arguments);
    exec(successCallback, errorCallback, "Scall", "unregister", []);
};
Scall.prototype.call = function(url,successCallback, errorCallback) {
    exec(successCallback, errorCallback, "Scall", "call", [{url:url}]);
};

module.exports = new Scall();
