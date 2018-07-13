package com.ingic.auditix.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ingic.auditix.global.AppConstants;
import com.ingic.auditix.helpers.DateHelper;

/**
 * Created on 1/8/2018.
 */

public class UserModel {
    @SerializedName("AccountID")
    @Expose
    private Integer accountID;
    @SerializedName("MasterAccountID")
    @Expose
    private Integer masterAccountID;
    @SerializedName("AccountRole")
    @Expose
    private Integer accountRole;
    @SerializedName("FullName")
    @Expose
    private String fullName;
    @SerializedName("EmailAddress")
    @Expose
    private String emailAddress;
    @SerializedName("Password")
    @Expose
    private String password;
    @SerializedName("Token")
    @Expose
    private TokenModel token;
    @SerializedName("AuthType")
    @Expose
    private Integer authType;
    @SerializedName("DOB")
    @Expose
    private String dOB;
    @SerializedName("Credits")
    @Expose
    private Integer credits;
    @SerializedName("Gender")
    @Expose
    private Integer gender;
    @SerializedName("ImageName")
    @Expose
    private String imageName;
    @SerializedName("CreatedBy")
    @Expose
    private Integer createdBy;
    @SerializedName("UpdateBy")
    @Expose
    private Integer updateBy;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;
    @SerializedName("UpdatedDate")
    @Expose
    private String updatedDate;
    @SerializedName("Deleted")
    @Expose
    private Boolean deleted;
    @SerializedName("InActive")
    @Expose
    private Boolean inActive;
    @SerializedName("ResetInfo")
    @Expose
    private Object resetInfo;
    @SerializedName("IsGuest")
    @Expose
    private Boolean isGuest;
    @SerializedName("Country")
    @Expose
    private String country;
    @SerializedName("PhoneNo")
    @Expose
    private String phoneNo;
    @SerializedName("IsVerified")
    @Expose
    private Boolean isVerified;
    @SerializedName("VerificationCode")
    @Expose
    private String verificationCode;
    @SerializedName("AccountType")
    @Expose
    private Integer accountType;
    @SerializedName("UserType")
    @Expose
    private Integer userType;
    @SerializedName("AccountDescription")
    @Expose
    private Object accountDescription;
    @SerializedName("CommaSeperatedCategoryIds")
    @Expose
    private Object commaSeperatedCategoryIds;

    public Object getResetInfo() {
        return resetInfo;
    }

    public void setResetInfo(Object resetInfo) {
        this.resetInfo = resetInfo;
    }

    public Boolean getGuest() {
        return isGuest;
    }

    public void setGuest(Boolean guest) {
        isGuest = guest;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public Boolean getVerified() {
        return isVerified;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Object getAccountDescription() {
        return accountDescription;
    }

    public void setAccountDescription(Object accountDescription) {
        this.accountDescription = accountDescription;
    }

    public Object getCommaSeperatedCategoryIds() {
        return commaSeperatedCategoryIds;
    }

    public void setCommaSeperatedCategoryIds(Object commaSeperatedCategoryIds) {
        this.commaSeperatedCategoryIds = commaSeperatedCategoryIds;
    }

    public Integer getAccountID() {
        return accountID;
    }

    public void setAccountID(Integer accountID) {
        this.accountID = accountID;
    }

    public Integer getMasterAccountID() {
        return masterAccountID;
    }

    public void setMasterAccountID(Integer masterAccountID) {
        this.masterAccountID = masterAccountID;
    }

    public Integer getAccountRole() {
        return accountRole;
    }

    public void setAccountRole(Integer accountRole) {
        this.accountRole = accountRole;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public TokenModel getToken() {
        return token;
    }

    public void setToken(TokenModel token) {
        this.token = token;
    }

    public Integer getAuthType() {
        return authType;
    }

    public void setAuthType(Integer authType) {
        this.authType = authType;
    }

    public String getdOB() {
        return DateHelper.getFormatedDate(AppConstants.SERVER_DATE_FORMAT, AppConstants.DATE_FORMAT_APP, dOB);
    }

    public void setdOB(String dOB) {
        this.dOB = dOB;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getInActive() {
        return inActive;
    }

    public void setInActive(Boolean inActive) {
        this.inActive = inActive;
    }
}
