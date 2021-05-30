//
//
// TOP Development
// AdminDialogCallback.java
//
//

package com.top.greenbeans.Interfaces;

import com.top.greenbeans.CustomObjects.GBUser;

public interface AdminDialogCallback extends DialogCallback {

    void addUserResponse(GBUser user);
    void editUserResponse(GBUser user);

}
