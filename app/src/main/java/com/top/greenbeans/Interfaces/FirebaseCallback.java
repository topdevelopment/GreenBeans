//
//
// TOP Development
// FirebaseCallback.java
//
//

package com.top.greenbeans.Interfaces;

import com.top.greenbeans.Enums.FirebaseMethod;

import java.util.List;

public interface FirebaseCallback {

    void successfulResponse(FirebaseMethod method);
    void successfulObjectResponse(FirebaseMethod method, Object obj);
    void errorResponse(FirebaseMethod method);

}
