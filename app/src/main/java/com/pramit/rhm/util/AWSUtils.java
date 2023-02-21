package com.pramit.rhm.util;

import android.content.Context;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.handlers.AsyncHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClient;
import com.amazonaws.services.dynamodbv2.model.*;
import com.amazonaws.util.VersionInfoUtils;
import com.pramit.rhm.BuildConfig;
import com.pramit.rhm.auth.*;

import java.util.Map;
import java.util.concurrent.Future;

public class AWSUtils {
    public static final Regions REGION = BuildConfig.REGION;
    public static final String USER_POOL_ID = BuildConfig.USER_POOL_ID;
    public static final String IDENTITY_POOL_ID = BuildConfig.IDENTITY_POOL_ID;
    public static final String CLIENT_ID = BuildConfig.CLIENT_ID;
    public static final String DB_TABLE_NAME = BuildConfig.DB_TABLE_NAME;
    private static AWSUtils aws;
    private final CognitoCachingCredentialsProvider credentialsProvider;
    private final CognitoUserPool userPool;
    private final AmazonDynamoDBAsync dynamoDB;
    private final DynamoDBMapper dynamoDBMapper;

    private AWSUtils(Context context) {
        credentialsProvider = new CognitoCachingCredentialsProvider(
                context,
                IDENTITY_POOL_ID,
                REGION);
        userPool = new CognitoUserPool(context, USER_POOL_ID, CLIENT_ID, null, REGION);
        dynamoDB = new AmazonDynamoDBAsyncClient(credentialsProvider);
        dynamoDBMapper = DynamoDBMapper.builder().dynamoDBClient(dynamoDB).build();
    }

    public static synchronized AWSUtils getInstance(Context context) {
        if (aws == null)
            aws = new AWSUtils(context);
        return aws;
    }

    public <T> T dynamoMapToPOJO(Class<T> cls, Map<String, AttributeValue> attr) {
        return dynamoDBMapper.marshallIntoObject(cls, attr);
    }

    public void continueSession(Context context) {
        if (credentialsProvider.getCachedIdentityId() != null)
            userLogin(context, userPool.getCurrentUser().getUserId(), null);
    }

    public Future<GetItemResult> getItem(GetItemRequest getItemRequest, AsyncHandler<GetItemRequest, GetItemResult> asyncHandler) {
        getItemRequest.getRequestClientOptions().appendUserAgent("DynamoDBv2Document/" + VersionInfoUtils.getVersion());
        if (asyncHandler != null)
            return dynamoDB.getItemAsync(getItemRequest, asyncHandler);
        else
            return dynamoDB.getItemAsync(getItemRequest);
    }

    public Future<PutItemResult> putItem(PutItemRequest putItemRequest, AsyncHandler<PutItemRequest, PutItemResult> asyncHandler) {
        putItemRequest.getRequestClientOptions().appendUserAgent("DynamoDBv2Document/" + VersionInfoUtils.getVersion());
        if (asyncHandler != null)
            return dynamoDB.putItemAsync(putItemRequest, asyncHandler);
        else
            return dynamoDB.putItemAsync(putItemRequest);
    }

    public void userLogin(Context context, String userId, String userPassword) {
        if (userId != null)
            userPool.getUser(userId).getSessionInBackground(new LoginHandler(context, userPassword, credentialsProvider));
    }

    public void signUp(Context context, String userId, String password, Map<String, String> metadata) {
        if (password == null)
            password = "";
        CognitoUserAttributes attributes;
        if (metadata != null) {
            attributes = new CognitoUserAttributes();
            for (Map.Entry<String, String> keyVal : metadata.entrySet()) {
                attributes.addAttribute(keyVal.getKey(), keyVal.getValue());
            }
        } else {
            attributes = new CognitoUserAttributes();
        }
        if (userId == null)
            userId = "";
        userPool.signUpInBackground(userId, password, attributes, null, null, new RegistrationHandler(context));
    }

    public void confirmUser(Context context, String userId, String otpCode) {
        if (userId != null)
            userPool.getUser(userId).confirmSignUpInBackground(otpCode, false, new RegistrationConfirmationHandler(context));
    }

    public void forgotPassword(Context context, String userId) {
        if (userId != null)
            userPool.getUser(userId).forgotPasswordInBackground(new PasswordResetHandler(context));
    }

    public void confirmForgotPassword(Context context, String userId, String newPassword, String otp) {
        if (userId != null)
            userPool.getUser(userId).confirmPasswordInBackground(otp, newPassword, new PasswordResetConfirmationHandler(context, newPassword, otp));
    }
}
