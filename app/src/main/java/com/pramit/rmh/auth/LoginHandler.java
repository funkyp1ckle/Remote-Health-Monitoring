package com.pramit.rmh.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.pramit.rmh.AWSConnection;
import com.pramit.rmh.Home;

import java.util.HashMap;
import java.util.Map;

public class LoginHandler implements AuthenticationHandler {

    private final Context context;
    private final String userPassword;
    private final String userPoolId;
    private final CognitoCachingCredentialsProvider credentialsProvider;

    public LoginHandler(Context context, String userPassword, String userPoolId, CognitoCachingCredentialsProvider credentialsProvider) {
        this.context = context;
        this.userPassword = userPassword;
        this.userPoolId = userPoolId;
        this.credentialsProvider = credentialsProvider;
    }

    @Override
    public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
        String username = userSession.getUsername();
        SharedPreferences sharedPreferences = context.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        sharedPreferencesEditor.putString("user_id", username);
        sharedPreferencesEditor.apply();
        Map<String, String> logins = new HashMap<>();
        logins.put("cognito-idp." + AWSConnection.REGION.getName() + ".amazonaws.com/" + userPoolId, userSession.getIdToken().getJWTToken());
        credentialsProvider.setLogins(logins);
        createUserProfile(userSession.getUsername());
        Intent newActivity = new Intent(context, Home.class);
        newActivity.putExtra("user_id", username);
        newActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(newActivity);
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

    public void createUserProfile(String userId) {
        AWSConnection aws = AWSConnection.getInstance(context.getApplicationContext());
        Map<String, AttributeValue> userProfile = new HashMap<>();
        userProfile.put("user_id", new AttributeValue(userId));
        PutItemRequest request = new PutItemRequest(AWSConnection.DB_TABLE_NAME, userProfile);
        request.setConditionExpression("attribute_not_exists(user_id)");
        aws.putItem(request, null);
    }
}
