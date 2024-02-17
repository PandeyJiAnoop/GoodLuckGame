package com.akp.aonestar.RetrofitAPI;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("Login")
    Call<String> getLogin(
            @Body String body);
    @POST("Registration")
    Call<String> signupAPI(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("PlanEmiRepayment")
    Call<String> PaymentaddEMI(
            @Body String body);


    @POST("ChangePassword")
    Call<String> changepassAPI(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("Dashboard")
    Call<String> getDashboard(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("UPITransfer")
    Call<String> getUPITransfer(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("ProfileView")
    Call<String> getProfile(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("TopupDetails")
    Call<String> getTopupDetails(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("GetWallet")
    Call<String> getWallet(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("MyRefferal")
    Call<String> getReferral(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("MyTeam")
    Call<String> getMyTeam(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("DirectIncome")
    Call<String> getDirectincome(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("LevelIncome")
    Call<String> getLevelincome(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("ROIBonus")
    Call<String> getContractincome(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("OpenWithdrawal")
    Call<String> getwithdrwar(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("WithdrawalHistory")
    Call<String> getWalletHistory(
            @Body String body);

    @Headers("Content-Type: application/json")
    @GET("GetServiceList")
    Call<String> OperatorList();

    @Headers("Content-Type: application/json")
    @POST("GetProviderList")
    Call<String> GetProviderListService(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("GetWallet")
    Call<String> GetWalletService(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("GetOpratorList")
    Call<String> OperatorService(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("PostRecharge")
    Call<String> PostRecharge(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("AddSender")
    Call<String> AddSenderAPI(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("Otpverify")
    Call<String> Otpverify(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("GetCustomer")
    Call<String> GetCustomerAPI(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("AllTransHistory")
    Call<String> TransactionReportAPI(
            @Body String body);


    @Headers("Content-Type: application/json")
    @POST("SavedAllTypeConsolationProduct_New")
    Call<String> SavedAllTypeConsolationProduct_NewAPI(
            @Body String body);
    @Headers("Content-Type: application/json")
    @GET("BChartFolderDetailsList")
    Call<String> BChartFolderDetailsListApi();


    @Headers("Content-Type: application/json")
    @POST("ShoppitProduct_AddToCart")
    Call<String> ShoppitProduct_AddToCartAPI(
            @Body String body);



    @Headers("Content-Type: application/json")
    @POST("ShoppitTradingProduct_AddToCart")
    Call<String> ShoppitTradingProduct_AddToCartAPI(
            @Body String body);


    @Headers("Content-Type: application/json")
    @POST("GetExcelDataNew")
    Call<String> GetExcelDataNewAPI(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("Shoppit_WelcomePrime_New_Scratch")
    Call<String> Shoppit_WelcomePrime_New_Scratch(
            @Body String body);

//    Ecomerrce API

    @Headers("Content-Type: application/json")
    @POST("GetBanner")
    Call<String> GetBanner(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("getrootcategorymaster")
    Call<String> getrootcategorymaster(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("DashboardList")
    Call<String> DashboardList(
            @Body String body);




    @Headers("Content-Type: application/json")
    @POST("SubCategoryList_ECOM")
    Call<String> SubCategoryList_ECOM(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("AddtowishList")
    Call<String> AddtowishList(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("ProductDetails")
    Call<String> ProductDetails(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("ProductDetails_Varation")
    Call<String> ProductDetailsVar(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("Addtocart_ECOM")
    Call<String> Addtocart(
            @Body String body);


    @Headers("Content-Type: application/json")
    @GET("RootCategoryList_ECOM")
    Call<String> RootCategoryList_ECOM();


    @Headers("Content-Type: application/json")
    @GET("EcomBannerList")
    Call<String> EcomBannerList();

    @Headers("Content-Type: application/json")
    @GET("RefferalImagesList")
    Call<String> RefferalImagesList();

    @Headers("Content-Type: application/json")
    @POST("CategoryList_ECOM")
    Call<String> getCategorymaster(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("ProductListbycategoryid")
    Call<String> ProductListbycategoryid(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("ViewCartList_ECOM")
    Call<String> ViewCartList(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("OrderList_ECOM")
    Call<String> OrderList_ECOM(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("UpdateCartdetail_ECOM")
    Call<String> UpdateCartdetail(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("DeleteCartdetail_ECOM")
    Call<String> DeleteCartdetail(
            @Body String body);


    @Headers("Content-Type: application/json")
    @POST("State_CityList")
    Call<String> State_CityList(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("InsertUpdateCustomerAddress")
    Call<String> CustomerAddress(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("OrderItemDetails_ECOM")
    Call<String> OrderItemDetails_ECOM(
            @Body String body);

}