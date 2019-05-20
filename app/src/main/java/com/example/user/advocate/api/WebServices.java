package com.example.user.advocate.api;

import com.example.user.advocate.models.AdvocateNewtorkInput;
import com.example.user.advocate.models.CheckMobInput;
import com.example.user.advocate.models.CheckMobResult;
import com.example.user.advocate.models.GetAdvocatesNetWorkResult;
import com.example.user.advocate.models.GetAdvocatesResult;
import com.example.user.advocate.models.ResetPasswordInput;
import com.example.user.advocate.models.ResetPasswordResult;
import com.example.user.advocate.models.UpdateAdvocateInput;
import com.example.user.advocate.models.UpdateAdvocateResult;
import com.example.user.advocate.models.UserLoginInput;
import com.example.user.advocate.models.UserLoginResult;
import com.example.user.advocate.models.UserRegisterInput;
import com.example.user.advocate.models.UserRegisterResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface WebServices {
    @POST("UserLogin_c/userRegister")
    Call<UserRegisterResult> userRegister(@Body UserRegisterInput input);

    @POST("UserLogin_c/userLogin")
    Call<UserLoginResult> userLogin(@Body UserLoginInput input);

    @POST("UserLogin_c/resetPassword")
    Call<ResetPasswordResult> resetPassword(@Body ResetPasswordInput input);

    @POST("UserLogin_c/GetUserId")
    Call<CheckMobResult> checkmob(@Body CheckMobInput input);

    @POST("Users_c/getAdvocates")
    Call<GetAdvocatesResult> getAdvocates();

    @POST("Users_c/advocatesInNetwork")
    Call<GetAdvocatesNetWorkResult> getAdvocatesInNetwork();

    @POST("Users_c/updateUser")
    Call<UpdateAdvocateResult> updateUser(@Body UpdateAdvocateInput input);

    @POST("Users_c/updateNetwork")
    Call<UpdateAdvocateResult> addToNetwork(@Body AdvocateNewtorkInput input);

}
