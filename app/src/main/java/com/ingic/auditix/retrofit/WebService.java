package com.ingic.auditix.retrofit;


import com.ingic.auditix.entities.AdvertisementEnt;
import com.ingic.auditix.entities.AutoDownloadEnt;
import com.ingic.auditix.entities.BookCategoriesEnt;
import com.ingic.auditix.entities.BookCategoryDetailEnt;
import com.ingic.auditix.entities.BookCategoryEnt;
import com.ingic.auditix.entities.BookDetailEnt;
import com.ingic.auditix.entities.BookFilterEnt;
import com.ingic.auditix.entities.BookGenreEnt;
import com.ingic.auditix.entities.CMSEnt;
import com.ingic.auditix.entities.DownloadDetailEnt;
import com.ingic.auditix.entities.FavoriteBookEnt;
import com.ingic.auditix.entities.NewItemDetailEnt;
import com.ingic.auditix.entities.NewsCategoryEnt;
import com.ingic.auditix.entities.NewsChannelDetailEnt;
import com.ingic.auditix.entities.NewsFilterEnt;
import com.ingic.auditix.entities.NotificaitonsSettingsEnt;
import com.ingic.auditix.entities.NotificationEnt;
import com.ingic.auditix.entities.PodcastCategoriesEnt;
import com.ingic.auditix.entities.PodcastCategoryListEnt;
import com.ingic.auditix.entities.PodcastDetailEnt;
import com.ingic.auditix.entities.PodcastDetailHomeEnt;
import com.ingic.auditix.entities.PodcastEpisodeEnt;
import com.ingic.auditix.entities.PodcastFavoriteEnt;
import com.ingic.auditix.entities.PodcastFilterEnt;
import com.ingic.auditix.entities.PodcastHomeEnt;
import com.ingic.auditix.entities.PodcastLocationEnt;
import com.ingic.auditix.entities.ResponseWrapper;
import com.ingic.auditix.entities.SearchEnt;
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
                                                  @Field("Gender") int gender,
                                                  @Field("Country") String Country,
                                                  @Field("PhoneNo") String PhoneNo);

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

    @FormUrlEncoded
    @POST("GuestRegistration")
    Call<ResponseWrapper<UserModel>> guestRegistration(@Field("DeviceName") String DeviceName,
                                                       @Field("UniversalDeviceID") String UniversalDeviceID,
                                                       @Field("IsAndroidPlatform") boolean IsAndroidPlatform,
                                                       @Field("IsPlayStore") boolean IsPlayStore,
                                                       @Field("IsProduction") boolean IsProduction,
                                                       @Field("AuthToken") String AuthToken);

    @FormUrlEncoded
    @POST("ResendSMSCode")
    Call<ResponseWrapper> resendSMSCode(@Field("PhoneNo") String PhoneNo,
                                        @Header(WebServiceConstants.HEADER_KEY) String AuthToken);

    @FormUrlEncoded
    @POST("VerifySMSCode")
    Call<ResponseWrapper> verifySMSCode(@Field("Code") String Code,
                                        @Header(WebServiceConstants.HEADER_KEY) String AuthToken);
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
                                      @Field("IsGuest") boolean IsGuest,
                                      @Header(WebServiceConstants.HEADER_KEY) String header);


    @POST("GetUnReadCount")
    Call<ResponseWrapper<Integer>> getUnreadCount(@Header(WebServiceConstants.HEADER_KEY) String header);


    @POST("GetAllNotifications")
    Call<ResponseWrapper<ArrayList<NotificationEnt>>> getAllNotifications(@Header(WebServiceConstants.HEADER_KEY) String header);


    @POST("RemoveNotifications")
    Call<ResponseWrapper> deleteNotification(@Header(WebServiceConstants.HEADER_KEY) String header,
                                                                         @Query("NotificationId") String NotificationId);


    //endregion

    //region Podcast Module
    @GET("GetPodCastByCategoryId")
    Call<ResponseWrapper<PodcastCategoryListEnt>> getPodcastsByCategory(@Query("categoryId") Integer categoryId,
                                                                        @Query("limit") Integer limit,
                                                                        @Query("countryCode") String countryCode,
                                                                        @Query("offset") Integer offset,
                                                                        @Header(WebServiceConstants.HEADER_KEY) String header);

    @POST("GetFilterResponse")
    Call<ResponseWrapper<ArrayList<PodcastDetailHomeEnt>>> getPodcastsByfilter(@Query("MinDuration") Integer MinDuration,
                                                                               @Query("MaxDuration") Integer MaxDuration,
                                                                               @Query("MinSubscriber") Integer MinSubscriber,
                                                                               @Query("MaxSubscriber") Integer MaxSubscriber,
                                                                               @Query("Type") Integer Type,
                                                                               @Query("pageNumber") Integer pageNumber,
                                                                               @Query("count") Integer count,
                                                                               @Query("CountryIds") String CountryIds,
                                                                               @Header(WebServiceConstants.HEADER_KEY) String header);

    @GET("GetFilterData?Type=1")
    Call<ResponseWrapper<PodcastFilterEnt>> getPodcastFilterData(@Header(WebServiceConstants.HEADER_KEY) String header);

    @GET("GetDefaultPodCast")
    Call<ResponseWrapper<PodcastHomeEnt>> getDefaultPodcast(@Query("pageNumber") Integer pageNumber, @Query("totalCount") Integer totalCount, @Query("categoriesIds") String categoriesIds,
                                                            @Header(WebServiceConstants.HEADER_KEY) String header);

    @GET("GetPodcastFeedByTrackId")
    Call<ResponseWrapper<PodcastDetailEnt>> getPodcastDetailByTrack(@Query("trackId") Integer trackId,
                                                                    @Query("categoryId") Integer categoryId,
                                                                    @Header(WebServiceConstants.HEADER_KEY) String header);

    @GET("GetPodcastEpisodesByPodcastID")
    Call<ResponseWrapper<ArrayList<PodcastEpisodeEnt>>> getPodcastEpisodesByPodcastID(@Query("PodcastID") Integer PodcastID,
                                                                                      @Header(WebServiceConstants.HEADER_KEY) String header);

    @GET("SubscribePodcast")
    Call<ResponseWrapper> subscribePodcast(@Query("trackId") Integer trackId,
                                           @Query("CategoryId") Integer CategoryId,
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
    Call<ResponseWrapper<ArrayList<PodcastCategoriesEnt>>> getAllPodcastCategories(@Header(WebServiceConstants.HEADER_KEY) String header);

    @GET("AddListeningEventPodcast")
    Call<ResponseWrapper> addListeningEventPodcast(@Query("PodcastID") Integer PodcastID,
                                                   @Query("EpisodeID") String EpisodeID, @Header(WebServiceConstants.HEADER_KEY) String header);

    @GET("GetLocationList")
    Call<ResponseWrapper<ArrayList<PodcastLocationEnt>>> getAllFilter(@Header(WebServiceConstants.HEADER_KEY) String header);
    //endregion

    //region Walkthrough Module
    @GET("GetAllWalkThrough")
    Call<ResponseWrapper<ArrayList<WalkthroughEnt>>> getAllWalkthrough();
    //endregion

    //region Book

    @GET("DefaultCategoryBooks")
    Call<ResponseWrapper<ArrayList<BookCategoryEnt>>> getDefaultBooks(@Query("pageNumber") Integer pageNumber,
                                                                      @Query("totalCount") Integer totalCount,
                                                                      @Query("genreIdList") String genreIdList,
                                                                      @Query("culture") String culture,
                                                                      @Header(WebServiceConstants.HEADER_KEY) String header);
    @POST("GetFilterResponse")
    Call<ResponseWrapper<ArrayList<BookDetailEnt>>> getBooksByfilter(@Query("MinDuration") Integer MinDuration,
                                                                               @Query("MaxDuration") Integer MaxDuration,
                                                                               @Query("MinSubscriber") Integer MinSubscriber,
                                                                               @Query("MaxSubscriber") Integer MaxSubscriber,
                                                                               @Query("Type") Integer Type,
                                                                               @Query("pageNumber") Integer pageNumber,
                                                                               @Query("count") Integer count,
                                                                               @Query("CountryIds") String CountryIds,
                                                                               @Header(WebServiceConstants.HEADER_KEY) String header);
    @GET("GetFeaturedBooks")
    Call<ResponseWrapper<BookCategoryEnt>> getFeaturesBooks(@Header(WebServiceConstants.HEADER_KEY) String header);

    @GET("GetFilterData?Type=2")
    Call<ResponseWrapper<BookFilterEnt>> getBooksFilterData(@Header(WebServiceConstants.HEADER_KEY) String header);

    @GET("BooksByCategoryId")
    Call<ResponseWrapper<BookCategoryDetailEnt>> getAllCategoryBooks(@Query("categoryId") Integer categoryId,
                                                                     @Query("PageNo") Integer PageNo,
                                                                     @Query("PageSize") Integer PageSize,
                                                                     @Query("culture") String culture,
                                                                     @Header(WebServiceConstants.HEADER_KEY) String header);

    @GET("GetFavoriteBooks")
    Call<ResponseWrapper<FavoriteBookEnt>> getBooksAllFavorite(@Query("culture") String culture, @Header(WebServiceConstants.HEADER_KEY) String header);

    @GET("BookDetail")
    Call<ResponseWrapper<BookDetailEnt>> getBookDetails(@Query("BookID") Integer BookID, @Header(WebServiceConstants.HEADER_KEY) String header);

    @GET("RemoveBookFromFavorite")
    Call<ResponseWrapper> RemoveBookFromFavorite(@Query("bookId") Integer bookId, @Header(WebServiceConstants.HEADER_KEY) String header);

    @GET("AddBookToFavorite")
    Call<ResponseWrapper> AddBookToFavorite(@Query("bookId") Integer bookId, @Header(WebServiceConstants.HEADER_KEY) String header);

    @GET("AddBooksToLibrary")
    Call<ResponseWrapper> AddBookToLibrary(@Query("BookIds") String bookId, @Header(WebServiceConstants.HEADER_KEY) String header);

    @GET("GetAllGenre")
    Call<ResponseWrapper<ArrayList<BookGenreEnt>>> getAllBooksFilters(@Query("culture") String culture, @Header(WebServiceConstants.HEADER_KEY) String header);

    @GET("GetListeningEventPodcast")
    Call<ResponseWrapper<ArrayList<PodcastEpisodeEnt>>> getNewAndNoteworthy(@Header(WebServiceConstants.HEADER_KEY) String header);

    @GET("GetBookCategories")
    Call<ResponseWrapper<ArrayList<BookCategoriesEnt>>> getBooksCategories(@Header(WebServiceConstants.HEADER_KEY) String header);

    @GET("MyLibraryBooks")
    Call<ResponseWrapper<ArrayList<BookDetailEnt>>> getLibraryBooks(@Query("PageNo") Integer pageNumber,
                                                                    @Query("PageSize") Integer pageSize,
                                                                    @Query("culture") String culture,
                                                                    @Header(WebServiceConstants.HEADER_KEY) String header);

    @GET("BookRating")
    Call<ResponseWrapper> rateBook(@Query("BookID") Integer BookID,
                                   @Query("RatingScore") Integer RatingScore,
                                   @Header(WebServiceConstants.HEADER_KEY) String header);
    //endregion

    //region Search
    @GET("GetSearch")
    Call<ResponseWrapper<SearchEnt>> getSearchItem(@Query("pageNumber") Integer pageNumber,
                                                   @Query("totalCount") Integer totalCount,
                                                   @Query("Text") String Text,
                                                   @Header(WebServiceConstants.HEADER_KEY) String header);

    //endregion

    //region Content
    @GET("GetAllContent")
    Call<ResponseWrapper<ArrayList<CMSEnt>>> getStaticContents();

    //endregion

    //region Advertisement
    @GET("GetAllAdvertisement")
    Call<ResponseWrapper<ArrayList<AdvertisementEnt>>> getAllAdvertisement(@Header(WebServiceConstants.HEADER_KEY) String header);
    //endregion

    //region News
    @GET("GetAllNewsCategory")
    Call<ResponseWrapper<ArrayList<NewsCategoryEnt>>> getAllNewsCategories(@Header(WebServiceConstants.HEADER_KEY) String userToken);
    @POST("GetFilterResponse")
    Call<ResponseWrapper<ArrayList<NewItemDetailEnt>>> getNewsByfilter(@Query("MinDuration") Integer MinDuration,
                                                                               @Query("MaxDuration") Integer MaxDuration,
                                                                               @Query("MinSubscriber") Integer MinSubscriber,
                                                                               @Query("MaxSubscriber") Integer MaxSubscriber,
                                                                               @Query("Type") Integer Type,
                                                                               @Query("pageNumber") Integer pageNumber,
                                                                               @Query("count") Integer count,
                                                                               @Query("CountryIds") String CountryIds,
                                                                               @Header(WebServiceConstants.HEADER_KEY) String header);
    @GET("GetFilterData?Type=3")
    Call<ResponseWrapper<NewsFilterEnt>> getNewsFilterData(@Header(WebServiceConstants.HEADER_KEY) String header);

    @GET("GetAllNewsByCategoryId")
    Call<ResponseWrapper<ArrayList<NewItemDetailEnt>>> getAllNewsByCategory(@Query("NewsCategoryId") int Id, @Header(WebServiceConstants.HEADER_KEY) String userToken);

    @GET("SubscribeNews")
    Call<ResponseWrapper> subscribeNews(@Query("NewsId") int NewsId, @Header(WebServiceConstants.HEADER_KEY) String userToken);

    @GET("GetSortedNewsEpisodes")
    Call<ResponseWrapper<NewsChannelDetailEnt>> getAllChannelEpisode(@Query("NewsId") int NewsId, @Header(WebServiceConstants.HEADER_KEY) String userToken);

    @GET("UnSubscribeNews ")
    Call<ResponseWrapper> unsubscribeNews(@Query("NewsId") int NewsId, @Header(WebServiceConstants.HEADER_KEY) String userToken);

    @GET("FavoriteNews")
    Call<ResponseWrapper> favoriteNews(@Query("NewsCategoryId") int Id, @Header(WebServiceConstants.HEADER_KEY) String userToken);

    @GET("UnFavoriteNews")
    Call<ResponseWrapper> unFavoriteNews(@Query("NewsCategoryId") int Id, @Header(WebServiceConstants.HEADER_KEY) String userToken);

    @GET("GetAllNewsSubscriptions")
    Call<ResponseWrapper<ArrayList<NewItemDetailEnt>>> getAllSubscribeNews(@Header(WebServiceConstants.HEADER_KEY) String userToken);

    @GET("GetAllFavoriteNews")
    Call<ResponseWrapper<ArrayList<NewsCategoryEnt>>> getAllFavoriteNews(@Header(WebServiceConstants.HEADER_KEY) String userToken);


    //endregion

    //region AutoDownlaod
    @GET("GetPodcastNewsDownloadDetail")
    Call<ResponseWrapper<AutoDownloadEnt>> getAllAutoDownloads(@Header(WebServiceConstants.HEADER_KEY) String userToken);
    @GET("GetPodcastNewsAndBookByEpisodesId")
    Call<ResponseWrapper<DownloadDetailEnt>> getAllDownloadDetails(@Query("PodcastEpisodeIds") String PodcastEpisodeIds,
                                                                    @Query("NewsEpisodeIds") String NewsEpisodeIds,
                                                                    @Header(WebServiceConstants.HEADER_KEY) String userToken);

    //endregion

    @GET("GetAllPodcastsAndNews")
    Call<ResponseWrapper<NotificaitonsSettingsEnt>> getAllPodcastsAndNews(@Header(WebServiceConstants.HEADER_KEY) String userToken);


    @GET("SetNotificationSetting")
    Call<ResponseWrapper> setNotificationSetting(@Header(WebServiceConstants.HEADER_KEY) String userToken,
                                                 @Query("EntityId") String EntityId,
                                                 @Query("EntityType") String EntityType);

    @GET("UnsetNotificationSetting")
    Call<ResponseWrapper> unsetNotificationSetting(@Header(WebServiceConstants.HEADER_KEY) String userToken,
                                                   @Query("EntityId") String EntityId,
                                                   @Query("EntityType") String EntityType);

    @GET("SetAutodownloadPodcast")
    Call<ResponseWrapper> setAutodownloadPodcast(@Header(WebServiceConstants.HEADER_KEY) String userToken,
                                                 @Query("PodcastId") String PodcastId);

    @GET("UnsetAutodownloadPodcast")
    Call<ResponseWrapper> unsetAutodownloadPodcast(@Header(WebServiceConstants.HEADER_KEY) String userToken,
                                                 @Query("PodcastId") String PodcastId);

    @GET("SetAutodownloadNews")
    Call<ResponseWrapper> setAutodownloadNews(@Header(WebServiceConstants.HEADER_KEY) String userToken,
                                                 @Query("NewsId") String NewsId);

    @GET("UnsetAutodownloadNews")
    Call<ResponseWrapper> unsetAutodownloadNews(@Header(WebServiceConstants.HEADER_KEY) String userToken,
                                                   @Query("NewsId") String NewsId);

}