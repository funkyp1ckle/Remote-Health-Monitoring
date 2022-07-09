package com.pramit.rmh;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.*;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.*;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.*;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidentityprovider.model.SignUpResult;

import java.io.Serializable;
import java.util.Map;

public class AWSConnection implements Serializable {
    private static final Regions REGION = Regions.US_EAST_1;
    private static final String USER_POOL_ID = "us-east-1_XD7LWYRpW";
    private static final String CLIENT_ID = "7sb607motb1es94lv4kovf12v6";
    private static final String SHARED_PREFERENCE = "SavedValues";
    private static final String PREFERENCE_USER_NAME = "awsUserName";
    private static final String PREFERENCE_USER_EMAIL = "awsUserEmail";
    private final String ATTR_EMAIL = "email";
    private final Context context;
    private final CognitoUserPool userPool;

    private final AWSConnection aws;

    public AWSConnection(Context context) {
        this.context = context;
        CognitoCachingCredentialsProvider provider = new CognitoCachingCredentialsProvider(
                context,
                USER_POOL_ID,
                REGION);
        this.userPool = new CognitoUserPool(context, USER_POOL_ID, CLIENT_ID, null, REGION);
        this.aws = this;
    }

    public void userLogin(String userId, String userPassword) {
        if (userId != null && userPassword != null) {
            CognitoUser user = userPool.getUser(userId);
            user.getSessionInBackground(new AuthenticationHandler() {
                @Override
                public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
                    user.getDetailsInBackground(new GetDetailsHandler() {
                        @Override
                        public void onSuccess(CognitoUserDetails cognitoUserDetails) {
                            SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE).edit();
                            String email = cognitoUserDetails.getAttributes().getAttributes().get(ATTR_EMAIL);
                            editor.putString(PREFERENCE_USER_EMAIL, email);
                            editor.apply();
                        }

                        @Override
                        public void onFailure(Exception exception) {
                            exception.printStackTrace();
                        }
                    });
                    SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE).edit();
                    editor.putString(PREFERENCE_USER_NAME, user.getUserId());
                    editor.apply();

                    Intent home = new Intent(context, Home.class);
                    home.putExtra("AWS", aws);
                    home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(home);
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
            });
        }
    }

    public void signUp(String userId, String password, Map<String, String> userMetadata) {
        if (userId != null && password != null && userMetadata != null) {
            CognitoUserAttributes attributes = new CognitoUserAttributes();
            for (Map.Entry<String, String> keyVal : userMetadata.entrySet()) {
                attributes.addAttribute(keyVal.getKey(), keyVal.getValue());
            }
            userPool.signUpInBackground(userId, password, attributes, null, null, new SignUpHandler() {
                @Override
                public void onSuccess(CognitoUser user, SignUpResult signUpResult) {
                    if (!signUpResult.isUserConfirmed()) {
                    }
                }

                @Override
                public void onFailure(Exception exception) {
                    Toast.makeText(context, "There was an error creating the account", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void confirmUser(String userId, String code) {
        if (userId != null && code != null) {
            userPool.getUser(userId).confirmSignUpInBackground(code, false, new GenericHandler() {
                @Override
                public void onSuccess() {
                    context.startActivity(new Intent(context, Home.class));
                }

                @Override
                public void onFailure(Exception exception) {
                    Toast.makeText(context, "User registration failed", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void forgotPassword(String userId) {
        if (userId != null) {
            userPool.getUser(userId).forgotPasswordInBackground(new ForgotPasswordHandler() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void getResetCode(ForgotPasswordContinuation continuation) {
                    continuation.continueTask();
                    Toast.makeText(context, R.string.resetInstructions, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Exception exception) {
                }
            });
        }
    }

    public void confirmForgotPassword(String userId, String newPassword, String otp) {
        if (userId != null && newPassword != null && otp != null) {
            userPool.getUser(userId).confirmPasswordInBackground(otp, newPassword, new ForgotPasswordHandler() {
                @Override
                public void onSuccess() {
                    Toast.makeText(context, "Password has been reset", Toast.LENGTH_LONG).show();
                }

                @Override
                public void getResetCode(ForgotPasswordContinuation continuation) {
                    continuation.setVerificationCode(otp);
                    continuation.setPassword(newPassword);

                }

                @Override
                public void onFailure(Exception exception) {
                    Toast.makeText(context, "Could not change password", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
