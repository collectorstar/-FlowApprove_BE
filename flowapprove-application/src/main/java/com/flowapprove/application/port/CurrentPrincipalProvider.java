package com.flowapprove.application.port;

import com.flowapprove.shared.security.CurrentPrincipal;

public interface CurrentPrincipalProvider {
    CurrentPrincipal getCurrentPrincipal();
}
