package com.pramit.rmh.auth;

import android.content.Context;
import android.widget.Toast;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.pramit.rmh.AWSConnection;
import com.pramit.rmh.Home;
import com.pramit.rmh.ui.UIUtils;

import java.util.HashMap;
import java.util.Map;

public class LoginHandler implements AuthenticationHandler {

    private final Context context;
    private final String userPassword;
    private final CognitoCachingCredentialsProvider credentialsProvider;
    private final CognitoUserPool userPool;

    public LoginHandler(Context context, String userPassword, CognitoCachingCredentialsProvider credentialsProvider, CognitoUserPool userPool) {
        this.context = context;
        this.userPassword = userPassword;
        this.credentialsProvider = credentialsProvider;
        this.userPool = userPool;
    }

    @Override
    public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
        String idToken = userSession.getIdToken().getJWTToken();
        Map<String, String> logins = new HashMap<>();
        logins.put("cognito-idp." + AWSConnection.REGION.toString().toLowerCase() + ".amazonaws.com/" + userPool.getUserPoolId(), idToken);
        credentialsProvider.setLogins(logins);
        UIUtils.changeActivity(context, Home.class);
    }

    @Override
    public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String userId) {
        AuthenticationDetails authenticationDetails = new AuthenticationDetails(userId, userPassword, null);
        authenticationContinuation.setAuthenticationDetails(authenticationDetails);
        authenticationContinuation.continueTask();
    }

    @Override
    public void getMFACode(MultiFactorAuthenticationContinuation continuation) {
        continuation.continueTask();
    }

    @Override
    public void authenticationChallenge(ChallengeContinuation continuation) {
    }

    @Override
    public void onFailure(Exception exception) {
        Toast.makeText(context, "There was an error authenticating", Toast.LENGTH_LONG).show();
    }
}
