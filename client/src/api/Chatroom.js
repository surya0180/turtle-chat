import axios from "axios";
import Cookies from "js-cookie";

export const createChatroom = async (roomName) => {
  try {
    const response = await axios.post(
      "http://ec2-13-233-85-190.ap-south-1.compute.amazonaws.com:8080/chatroom/create-chatroom",
      { roomName },
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

export const updateChatroom = async (roomId, roomName) => {
  try {
    const response = await axios.put(
      "http://ec2-13-233-85-190.ap-south-1.compute.amazonaws.com:8080/chatroom/update-chatroom",
      { roomId, roomName },
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

export const deleteChatroom = async (roomId) => {
  try {
    const response = await axios.delete(
      "http://ec2-13-233-85-190.ap-south-1.compute.amazonaws.com:8080/chatroom/deactivate-chatroom",
      {
        data: { roomId },
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

export const exitChatroom = async (roomId) => {
  try {
    const response = await axios.delete(
      "http://ec2-13-233-85-190.ap-south-1.compute.amazonaws.com:8080/chatroom/exit-chatroom",
      {
        data: { roomId },
        headers: {
          Authorization: Cookies.get("AccessToken"),
        },
      }
    );
    console.log(response);
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

export const getChatrooms = async (type) => {
  try {
    const response = await axios.get(
      `http://ec2-13-233-85-190.ap-south-1.compute.amazonaws.com:8080/chatroom/${
        type === "H" ? "hosting" : "participating"
      }-chatrooms`,
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

export const getParticipants = async (roomId) => {
  try {
    const response = await axios.get(
      `http://ec2-13-233-85-190.ap-south-1.compute.amazonaws.com:8080/chatroom/participants/${roomId}`,
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

export const getChatMessages = async (roomId, pageNumber, pageLimit) => {
  try {
    const response = await axios.get(
      `http://ec2-13-233-85-190.ap-south-1.compute.amazonaws.com:8080/chatroom/messages/${roomId}?pageNumber=${pageNumber}&pageLimit=${pageLimit}`,
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
