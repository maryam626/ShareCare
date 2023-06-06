package com.example.sharecare.Logic;

public class User {

 private String userName;
 private String id;
 private String phone;
 private String email;
 private String password;
 private String address;
 private int numOfKids;
 private String maritalStatus;
 private String gender;
 private String type;

 public User(){

 }
 public User(String userName, String id, String phone, String email, String password, String address, int numOfKids, String maritalStatus, String gender, String type) {
  this.userName = userName;
  this.id = id;
  this.phone = phone;
  this.email = email;
  this.password = password;
  this.address = address;
  this.numOfKids = numOfKids;
  this.maritalStatus = maritalStatus;
  this.gender = gender;
  this.type = type;
 }

 public String getUserName() {
  return userName;
 }

 public void setUserName(String userName) {
  this.userName = userName;
 }

 public String getId() {
  return id;
 }

 public void setId(String id) {
  this.id = id;
 }

 public String getPhone() {
  return phone;
 }

 public void setPhone(String phone) {
  this.phone = phone;
 }

 public String getEmail() {
  return email;
 }

 public void setEmail(String email) {
  this.email = email;
 }

 public String getPassword() {
  return password;
 }

 public void setPassword(String password) {
  this.password = password;
 }

 public String getAddress() {
  return address;
 }

 public void setAddress(String address) {
  this.address = address;
 }

 public int getNumOfKids() {
  return numOfKids;
 }

 public void setNumOfKids(int numOfKids) {
  this.numOfKids = numOfKids;
 }

 public String getMaritalStatus() {
  return maritalStatus;
 }

 public void setMaritalStatus(String maritalStatus) {
  this.maritalStatus = maritalStatus;
 }

 public String getGender() {
  return gender;
 }

 public void setGender(String gender) {
  this.gender = gender;
 }

 public String getType() {
  return type;
 }

 public void setType(String type) {
  this.type = type;
 }
}
