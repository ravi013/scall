/*
 Licensed to the Apache Software Foundation (ASF) under one
 or more contributor license agreements.  See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership.  The ASF licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at
 
 http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.
 */
package org.apache.cordova.scall;

import java.util.TimeZone;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaInterface;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.provider.Settings;

import com.sightcall.universal.Universal;
import com.sightcall.universal.event.UniversalStatusEvent;

import net.rtccloud.sdk.Event;

public class Scall extends CordovaPlugin {
    public static final String TAG = "Scall";
    
    public static String platform;                            // Scall OS
    public static String uuid;                                // Scall UUID
    
    private static final String ANDROID_PLATFORM = "Android";
    private static final String AMAZON_PLATFORM = "amazon-fireos";
    private static final String AMAZON_DEVICE = "Amazon";
    
    /**
     * Constructor.
     */
    public Scall() {
    }
    
    /**
     * Sets the context of the Command. This can then be used to do things like
     * get file paths associated with the Activity.
     *
     * @param cordova The context of the main Activity.
     * @param webView The CordovaWebView Cordova is running in.
     */
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        Scall.uuid = getUuid();
    }
    @Event
    public final void onUniversalStatusEvent(UniversalStatusEvent event) {
        switch (event.status()) {
            case INITIALIZING:
                // Universal SDK is loading: Notify the user
                break;
            case IDLE:
                // Universal SDK becomes idle: Remove any pending notification
                break;
        }
    }
    /**
     * Executes the request and returns PluginResult.
     *
     * @param action            The action to execute.
     * @param args              JSONArry of arguments for the plugin.
     * @param callbackContext   The callback id used when calling back into JavaScript.
     * @return                  True if the action was valid, false if not.
     */
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if ("register".equals(action)) {
            Universal.register(this);
            callbackContext.success();
        }
        else if ("unregister".equals(action)) {
            Universal.unregister(this);
            callbackContext.success();
        }
        else  if ("call".equals(action)) {
           
            final String url = args.getString(0);
            
            Universal.start(url  /*java.lang.String*/);
            callbackContext.success();
        }
        else {
            return false;
        }
        return true;
    }
    
    //--------------------------------------------------------------------------
    // LOCAL METHODS
    //--------------------------------------------------------------------------
    
    /**
     * Get the OS name.
     *
     * @return
     */
    public String getPlatform() {
        String platform;
        if (isAmazonDevice()) {
            platform = AMAZON_PLATFORM;
        } else {
            platform = ANDROID_PLATFORM;
        }
        return platform;
    }
    
    /**
     * Get the device's Universally Unique Identifier (UUID).
     *
     * @return
     */
    public String getUuid() {
        String uuid = Settings.Secure.getString(this.cordova.getActivity().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        return uuid;
    }
    
    public String getModel() {
        String model = android.os.Build.MODEL;
        return model;
    }
    
    public String getProductName() {
        String productname = android.os.Build.PRODUCT;
        return productname;
    }
    
    public String getManufacturer() {
        String manufacturer = android.os.Build.MANUFACTURER;
        return manufacturer;
    }
    
    public String getSerialNumber() {
        String serial = android.os.Build.SERIAL;
        return serial;
    }
    
    /**
     * Get the OS version.
     *
     * @return
     */
    public String getOSVersion() {
        String osversion = android.os.Build.VERSION.RELEASE;
        return osversion;
    }
    
    public String getSDKVersion() {
        @SuppressWarnings("deprecation")
        String sdkversion = android.os.Build.VERSION.SDK;
        return sdkversion;
    }
    
    public String getTimeZoneID() {
        TimeZone tz = TimeZone.getDefault();
        return (tz.getID());
    }
    
    /**
     * Function to check if the device is manufactured by Amazon
     *
     * @return
     */
    public boolean isAmazonDevice() {
        if (android.os.Build.MANUFACTURER.equals(AMAZON_DEVICE)) {
            return true;
        }
        return false;
    }
    
    public boolean isVirtual() {
        return android.os.Build.FINGERPRINT.contains("generic") ||
        android.os.Build.PRODUCT.contains("sdk");
    }
    
}
