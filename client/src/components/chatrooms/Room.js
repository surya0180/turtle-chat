import { Box, Grid, IconButton, LinearProgress, Stack } from "@mui/material";
import React, { useEffect, useState } from "react";
import Text from "../../UI/Text";
import { Turtle2 } from "../../assets/icons";
import { HeadsetMicOutlined, LogoutOutlined } from "@mui/icons-material";
import { v4 as uuid } from "uuid";
import Participants from "./Participants";
import { getChatMessages } from "../../api/Chatroom";
import ChatInput from "../../UI/ChatInput";
import Cookies from "js-cookie";
import ChatMessage from "./ChatMessage";

function Room({ room, onRoomExit }) {
  const [loading, setLoading] = useState(false);
  const [alert, setAlert] = useState(null);
  const updateAlert = (value) => {
    setAlert({ id: uuid(), ...value });
  };
  const [chat, setChat] = useState("");
  const [chatMessages, setChatMessages] = useState([]);
  const [socket, setSocket] = useState(null);
  const [update, setUpdate] = useState(null);

  useEffect(() => {
    if (room && room.roomId) {
      setLoading(true);
      getChatMessages(room.roomId, 1, 100).then((res) => {
        setLoading(false);
        if (res.status === "OK") {
          console.log(res.data.messages, "THIS ARE MESSAGES");
          setChatMessages(res.data.messages);
        } else {
          updateAlert({ color: "error", text: res.message });
        }
      });

      return () => {
        setChatMessages([]);
      };
    }
  }, [room]);

  useEffect(() => {
    const token = Cookies.get("AccessToken");
    if (token && room?.roomId) {
      const webSocket = new WebSocket(
        `ws://localhost:80/chatroom?roomId=${room.roomId}&token=${token}`
      );
      webSocket.onopen = (...props) => {
        console.log(props, "ONOPEN PROPS");
      };
      webSocket.onclose = (...props) => {
        console.log(props, "ONCLOSE PROPS");
      };
      webSocket.onmessage = (message) => {
        setChatMessages((prevState) => [
          ...prevState,
          JSON.parse(message.data),
        ]);
      };
      setSocket(webSocket);

      return () => {
        webSocket.close();
        setSocket(null);
      };
    }
  }, [room?.roomId]);

  const sendMessage = (text) => {
    socket.send(JSON.stringify({ text }));
  };

  return !room ? (
    <Box height={"100%"} width={"100%"} position={"relative"}>
      <Box
        sx={{
          position: "absolute",
          left: 0,
          right: 0,
          top: 0,
          bottom: 0,
          maxHeight: "5rem",
          margin: "auto",
          opacity: 0.3,
          textAlign: "center",
        }}
      >
        <Turtle2 />
        <Text
          color={"primary"}
          variant={"h6"}
          text={"You haven't entered any Chatroom yet!"}
        />
      </Box>
    </Box>
  ) : (
    <Box sx={{ height: "100%" }}>
      <Grid container sx={{ height: "100%" }}>
        <Grid item md="9">
          <Box
            sx={{
              borderBottom: "1px solid white",
              padding: "1rem",
              display: "flex",
              alignItems: "center",
              justifyContent: "space-between",
              height: "5rem",
            }}
          >
            <Box
              sx={{
                display: "flex",
                alignItems: "center",
                gap: "1rem",
              }}
            >
              <HeadsetMicOutlined color="primary" fontSize="large" />
              <Text
                color={"primary"}
                variant={"body1"}
                text={room?.roomName || "N/A"}
              />
            </Box>
            {update && (
              <Box>
                <Text
                  color={"info"}
                  text={
                    update?.userId ===
                    JSON.parse(Cookies.get("UserData")).userId
                      ? "You are now online"
                      : `${update?.userName} ${
                          update?.status === "J" ? "joined" : "left"
                        } the chat`
                  }
                  variant={"subtitle2"}
                />
              </Box>
            )}
            <Box>
              <IconButton
                onClick={() => {
                  onRoomExit();
                }}
              >
                <LogoutOutlined color="error" />
              </IconButton>
            </Box>
          </Box>
          {loading && (
            <LinearProgress color="primary" sx={{ backgroundColor: "black" }} />
          )}
          <Box sx={{ height: "calc(100vh - 15rem)" }}>
            {alert && (
              <Box mt="1rem" textAlign={"center"}>
                <Text {...alert} />
              </Box>
            )}
            <Stack
              sx={{
                height: "100%",
                width: "100%",
                overflowY: "scroll",
              }}
              flexDirection={"column-reverse"}
              rowGap={"1rem"}
              alignItems={"stretch"}
              padding={"1rem"}
            >
              <Stack rowGap={"1rem"}>
                {chatMessages.map((message) => (
                  <ChatMessage
                    key={message.messageId || message.timestamp}
                    room={room}
                    message={message}
                  />
                ))}
              </Stack>
            </Stack>
          </Box>
          <ChatInput
            value={chat}
            onChange={setChat}
            onSend={(text) => {
              if (text) {
                sendMessage(text);
              }
            }}
            onEnter={(text) => {
              if (text) {
                sendMessage(text);
              }
            }}
          />
        </Grid>
        <Grid item md="3" sx={{ borderLeft: "1px solid white" }}>
          <Participants room={room} />
        </Grid>
      </Grid>
    </Box>
  );
}

export default Room;
