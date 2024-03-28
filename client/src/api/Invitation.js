import axios from "axios";
import Cookies from "js-cookie";
import { API_HOST, API_PORT } from "../env";

export const getInvitations = async (by, status) => {
  try {
    const response = await axios.get(
      `http://${API_HOST}:${API_PORT}/invitation/${by}-invitations/${status}`,
      {
        headers: {
          Authorization: Cookies.get("AccessToken"),
        },
      }
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

export const updateInvitation = async (inviteId, inviteStatus) => {
  try {
    const response = await axios.put(
      `http://${API_HOST}:${API_PORT}/invitation/update-invite`,
      { inviteId, inviteStatus },
      {
        headers: {
          Authorization: Cookies.get("AccessToken"),
        },
      }
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

export const sendInvitation = async (userEmail, roomId) => {
  try {
    const response = await axios.post(
      `http://${API_HOST}:${API_PORT}/invitation/invite-user`,
      { recipientEmail: userEmail, roomId },
      {
        headers: {
          Authorization: Cookies.get("AccessToken"),
        },
      }
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
