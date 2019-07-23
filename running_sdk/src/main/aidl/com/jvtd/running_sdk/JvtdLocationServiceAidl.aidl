// JvtdLocationServiceAidl.aidl
package com.jvtd.running_sdk;

// Declare any non-default types here with import statements
interface JvtdLocationServiceAidl {
    /** hook when other service has already binded on it */
    void onFinishBind();
}
