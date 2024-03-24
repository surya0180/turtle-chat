import axios from "axios";
import Cookies from "js-cookie";

export const sendOtp = async (email) => {
  try {
    const response = await axios.post(
      "http://localhost:8080/authentication/send-otp",
      { userEmail: email }
    );
    return { ...response.data };
  } catch (e) {
    console.log(e);
    const data = e.response?.data;
    return {
      status: data?.status || e.code,
      message: data?.message || e.message,
      data: data,
    };
  }
};

export const verifyOtp = async (email, otp) => {
  try {
    const response = await axios.post(
      "http://localhost:8080/authentication/verify-otp",
      { userEmail: email, OTP: otp }
    );
    return { ...response.data };
  } catch (e) {
    console.log(e);
    const data = e.response?.data;
    return {
      status: data?.status || e.code,
      message: data?.message || e.message,
      data: data,
    };
  }
};

export const saveUsername = async (userId, userName, userEmail) => {
  try {
    const response = await axios.put(
      "http://localhost:8080/authentication/save-username",
      { userId, userName, userEmail }
    );
    return { ...response.data };
  } catch (e) {
    console.log(e);
    const data = e.response?.data;
    return {
      status: data?.status || e.code,
      message: data?.message || e.message,
      data: data,
    };
  }
};
