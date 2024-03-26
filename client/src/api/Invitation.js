import axios from "axios";
import Cookies from "js-cookie";

export const getInvitations = async (by, status) => {
  try {
    const response = await axios.get(
      `http://ec2-13-233-85-190.ap-south-1.compute.amazonaws.com:8080/invitation/${by}-invitations/${status}`,
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
      `http://ec2-13-233-85-190.ap-south-1.compute.amazonaws.com:8080/invitation/update-invite`,
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
      "http://ec2-13-233-85-190.ap-south-1.compute.amazonaws.com:8080/invitation/invite-user",
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
