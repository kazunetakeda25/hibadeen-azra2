package com.azra2.common;

import com.azra2.model.YMAgentList;
import com.azra2.model.YMNotificationList;
import com.azra2.model.YMResponse;
import com.azra2.model.YMUserMinuteResponse;
import com.azra2.model.YMUserPaymentResponse;
import com.azra2.model.YMUserResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface YMRetrofitService
{
    @POST("user_login")
    @FormUrlEncoded
    Call<YMUserResponse> userLogin(@Field("email_phone") String emailOrPhone,
                                   @Field("password") String password,
                                   @Field("device_token") String deviceToken);

    @POST("user_signup")
    @Multipart
    Call<YMUserResponse> userSignup(@Part("first_name") RequestBody firstName,
                                    @Part("last_name") RequestBody lastName,
                                    @Part("nick_name") RequestBody nickName,
                                    @Part("phone") RequestBody phone,
                                    @Part("email") RequestBody email,
                                    @Part("birthday") RequestBody birthday,
                                    @Part("gender") RequestBody gender,
                                    @Part("country") RequestBody country,
                                    @Part("password") RequestBody password,
                                    @Part("device_token") RequestBody deviceToken,
                                    @Part MultipartBody.Part profileImage);

    @POST("user_logout")
    @FormUrlEncoded
    Call<YMUserResponse> userLogout(@Field("id") int id);

    @POST("user_token_login")
    @FormUrlEncoded
    Call<YMUserResponse> userTokenLogin(@Field("user_id") int userId,
                                        @Field("app_token") String appToken);


    @POST("user_update_profile")
    @Multipart
    Call<YMUserResponse> userUpdateProfile(@Part("user_id") RequestBody userId,
                                           @Part("first_name") RequestBody firstName,
                                           @Part("last_name") RequestBody lastName,
                                           @Part("nick_name") RequestBody nickName,
                                           @Part("bio") RequestBody bio,
                                           @Part("app_token") RequestBody appToken,
                                           @Part MultipartBody.Part profileImage);

    @POST("user_change_password")
    @FormUrlEncoded
    Call<YMUserResponse> userChangePassword(@Field("user_id") int userId,
                                            @Field("current_password") String currentPassword,
                                            @Field("new_password") String newPassword,
                                            @Field("app_token") String appToken);
    @POST("user_change_email")
    @FormUrlEncoded
    Call<YMUserResponse> userChangeEmail(@Field("user_id") int userId,
                                         @Field("new_email") String newEmail,
                                         @Field("app_token") String appToken);

    @POST("user_change_phone")
    @FormUrlEncoded
    Call<YMUserResponse> userChangePhone(@Field("user_id") int userId,
                                         @Field("new_phone") String newEmail,
                                         @Field("app_token") String appToken);

    @POST("user_forgot_password")
    @FormUrlEncoded
    Call<YMUserResponse> userForgotPassword(@Field("email") String email,
                                            @Field("new_password") String newPassword,
                                            @Field("app_token") String appToken);

    @POST("get_agents")
    @FormUrlEncoded
    Call<YMAgentList> getAgents(@Field("limit") int limit,
                                @Field("offset") int offset,
                                @Field("room_type") int roomType,
                                @Field("app_token") String appToken);

    @POST("get_all_agents")
    @FormUrlEncoded
    Call<YMAgentList> getAgentsIgnoreRtype(@Field("limit") int limit,
                                           @Field("offset") int offset,
                                           @Field("app_token") String appToken);

    @POST("notify_agent")
    @FormUrlEncoded
    Call<YMResponse> notifyAgent(@Field("user_id") int userId,
                                 @Field("agent_id") int agentId,
                                 @Field("room_type") int roomType,
                                 @Field("app_token") String appToken);

    @POST("leave_review")
    @FormUrlEncoded
    Call<YMResponse> leaveReview(@Field("agent_id") int agentId,
                                 @Field("user_id") int userId,
                                 @Field("star") float star,
                                 @Field("spicy") int spicy,
                                 @Field("review") String review,
                                 @Field("app_token") String appToken);

    @POST("get_user_notifications")
    @FormUrlEncoded
    Call<YMNotificationList> getUserNotifications(@Field("id") int uid);

    @POST("buy_coin")
    @FormUrlEncoded
    Call<YMUserResponse> buyCoin(@Field("user_id") int userId,
                                 @Field("coin_count") int coinCount,
                                 @Field("money") double price,
                                 @Field("app_token") String appToken);

    @POST("share_coin_gift")
    @FormUrlEncoded
    Call<YMUserResponse> shareCoinGift(@Field("user_id") int userId,
                                       @Field("agent_id") int agentId,
                                       @Field("coin_count") int coinCount,
                                       @Field("app_token") String appToken);

    @POST("coin_tip")
    @FormUrlEncoded
    Call<YMUserResponse> coinTip(@Field("user_id") int userId,
                                       @Field("agent_id") int agentId,
                                       @Field("coin_count") int coinCount,
                                       @Field("app_token") String appToken);

    @POST("join_live")
    @FormUrlEncoded
    Call<YMResponse> joinLive(@Field("user_id") int userId,
                              @Field("agent_id") int agentId,
                              @Field("app_token") String appToken);

    @POST("leave_live")
    @FormUrlEncoded
    Call<YMResponse> leaveLive(@Field("user_id") int userId,
                               @Field("agent_id") int agentId,
                               @Field("app_token") String appToken);

    @POST("get_payment_history")
    @FormUrlEncoded
    Call<YMUserPaymentResponse> getPaymentHistory(@Field("user_id") int userId,
                                                  @Field("app_token") String appToken);

    @POST("get_minute_history")
    @FormUrlEncoded
    Call<YMUserMinuteResponse> getMinuteHistory(@Field("user_id") int userId,
                                                @Field("app_token") String appToken);


}
