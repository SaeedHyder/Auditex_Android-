package com.ingic.auditix.retrofit;


import com.ingic.auditix.entities.BookFavoriteEnt;
import com.ingic.auditix.entities.PodcastCategoriesEnt;
import com.ingic.auditix.entities.PodcastCategoryListEnt;
import com.ingic.auditix.entities.PodcastDetailEnt;
import com.ingic.auditix.entities.PodcastFavoriteEnt;
import com.ingic.auditix.entities.PodcastHomeEnt;
import com.ingic.auditix.entities.ResponseWrapper;
import com.ingic.auditix.entities.SubscribePodcastEnt;
import com.ingic.auditix.entities.UserModel;
import com.ingic.auditix.entities.WalkthroughEnt;
import com.ingic.auditix.global.WebServiceConstants;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface WebService {
    //region UserModule
    @FormUrlEncoded
    @POST("Login")
    Call<ResponseWrapper<UserModel>> loginUser(@Field("Email") String email, @Field("Password") String password);

    @FormUrlEncoded
    @POST("Register")
    Call<ResponseWrapper<UserModel>> registerUser(@Field("FullName") String fullName,
                                                  @Field("Email") String email,
                                                  @Field("Password") String password,
                                                  @Field("ConfirmPassword") String confirmPassword,
                                                  @Field("DOB") String dob,
                                                  @Field("Gender") int gender);

    @FormUrlEncoded
    @POST("SocialLogin")
    Call<ResponseWrapper<UserModel>> socialLoginUser(@Field("FullName") String fullName,
                                                     @Field("ProviderKey") String ProviderKey,
                                                     @Field("DOB") String dob,
                                                     @Field("Gender") int gender,
                                                     @Field("AuthProvider") int authType,
                                                     @Field("ImageUrl") String imageUrl);

    @FormUrlEncoded
    @POST("ChangePassword")
    Call<ResponseWrapper> ChangePassword(@Field("OldPassword") String oldPassword, @Field("NewPassword") String NewPassword,
                                         @Field("ConfirmPassword") String ConfirmPassword, @Header("Authorization") String header);

    @Multipart
    @POST("EditProfile")
    Call<ResponseWrapper<UserModel>> EditProfile(@Part("FullName") RequestBody fullName,
                                                 @Part("DOB") RequestBody dob,
                                                 @Part MultipartBody.Part userprofileImage,
                                                 @Header(WebServiceConstants.HEADER_KEY) String header);

    @GET("ResetPassword")
    Call<ResponseWrapper> ResetPassword(@Query("Email") String email);

    @GET("ValidateCode")
    Call<ResponseWrapper<UserModel>> ResetPassword(@Query("Email") String email, @Query("Code") String code);
    //endregion

    //region Notification Module
    @FormUrlEncoded
    @POST("RegisterPushNotification")
    Call<ResponseWrapper> updateToken(@Field("DeviceName") String deviceName,
                                      @Field("UniversalDeviceID") String UniversalDeviceID,
                                      @Field("IsAndroidPlatform") boolean IsAndroidPlatform,
                                      @Field("IsPlayStore") boolean IsPlayStore,
                                      @Field("AccountID") Integer AccountID,
                                      @Field("IsProduction") boolean IsProduction,
                                      @Field("AuthToken") String firebaseToken,
                                      @Header(WebServiceConstants.HEADER_KEY) String header);

    @POST("GetUnReadCount")
    Call<ResponseWrapper<Integer>> getUnreadCount(@Header(WebServiceConstants.HEADER_KEY) String header);

    @FormUrlEncoded
    @POST("GetAllNotifications")
    Call<ResponseWrapper<ArrayList<Object>>> getAllNotifications(@Field("AccountDeviceId") Integer accountID,
                                                                 @Header(WebServiceConstants.HEADER_KEY) String header);
    //endregion

    //region Podcast Module
    @GET("GetPodCastByCategoryId")
    Call<ResponseWrapper<PodcastCategoryListEnt>> getPodcastsByCategory(@Query("categoryId") String categoryId,
                                                                        @Query("limit") String limit,
                                                                        @Query("countryCode") String countryCode,
                                                                        @Query("offset") String offset,
                                                                        @Header(WebServiceConstants.HEADER_KEY) String header);

    @GET("GetDefaultPodCast")
    Call<ResponseWrapper<PodcastHomeEnt>> getDefaultPodcast(@Query("pageNumber") Integer pageNumber, @Query("totalCount") Integer totalCount, @Query("categoriesIds") String categoriesIds,
                                                            @Header(WebServiceConstants.HEADER_KEY) String header);

    @GET("GetPodcastFeedByTrackId")
    Call<ResponseWrapper<PodcastDetailEnt>> getPodcastDetailByTrack(@Query("trackId") Integer trackId,
                                                                    @Header(WebServiceConstants.HEADER_KEY) String header);

    @GET("SubscribePodcast")
    Call<ResponseWrapper> subscribePodcast(@Query("trackId") Integer trackId,
                                           @Header(WebServiceConstants.HEADER_KEY) String header);

    @GET("UnSubscribePodcast")
    Call<ResponseWrapper> unSubscribePodcast(@Query("trackId") Integer trackId,
                                             @Header(WebServiceConstants.HEADER_KEY) String header);

    @GET("GetAccountPodcasts")
    Call<ResponseWrapper<ArrayList<SubscribePodcastEnt>>> getSubscribePodcasts(@Header(WebServiceConstants.HEADER_KEY) String header);

    @GET("AddPodcastRating")
    Call<ResponseWrapper> ratePodcast(@Query("trackId") Integer trackId, @Query("rating") Integer rating,
                                      @Header(WebServiceConstants.HEADER_KEY) String header);

    @GET("AddUpdateFavoritePodcast")
    Call<ResponseWrapper> changeFavoriteStatus(@Query("TrackId") Integer trackId, @Query("favorite") boolean isFavorite,
                                               @Header(WebServiceConstants.HEADER_KEY) String header);

    @GET("GetFavoriteAccountPodcasts")
    Call<ResponseWrapper<ArrayList<PodcastFavoriteEnt>>> getAllFavorite(@Header(WebServiceConstants.HEADER_KEY) String header);

    @GET("GetAllCategories")
    Call<ResponseWrapper<ArrayList<PodcastCategoriesEnt>>> getAllFilters(@Header(WebServiceConstants.HEADER_KEY) String header);
    //endregion

    //region Walkthrough Module
    @GET("GetAllWalkThrough")
    Call<ResponseWrapper<ArrayList<WalkthroughEnt>>> getAllWalkthrough();
    //endregion

    //region Book
    @GET("GetFavoriteBooks")
    Call<ResponseWrapper<ArrayList<BookFavoriteEnt>>> getBooksAllFavorite(@Query("culture") int culture, @Header(WebServiceConstants.HEADER_KEY) String header);

    @GET("RemoveBookFromFavorite")
    Call<ResponseWrapper> RemoveBookFromFavorite(@Query("bookId") Integer bookId, @Header(WebServiceConstants.HEADER_KEY) String header);

    @GET("AddBookToFavorite")
    Call<ResponseWrapper> AddBookToFavorite(@Query("bookId") Integer bookId, @Header(WebServiceConstants.HEADER_KEY) String header);
    //endregion
}