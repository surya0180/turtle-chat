import { FaceOutlined } from "@mui/icons-material";
import { Box, Stack, useTheme } from "@mui/material";
import React from "react";
import { Crown } from "../../assets/icons";
import Text from "../../UI/Text";
import Cookies from "js-cookie";

function ChatMessage({ room, message }) {
  const { palette } = useTheme();

  const userData = JSON.parse(Cookies.get("UserData"));

  const getMessageTime = (timestamp) => {
    let date = new Date(timestamp);
    let timeString = date.toLocaleTimeString("en-US", { hour12: false });
    return timeString.split(":").slice(0, 2).join(":");
  };

  return (
    <Box
      key={message.timestamp}
      sx={{
        display: "flex",
        alignItems: "flex-start",
        gap: "1rem",
        alignSelf:
          message.type === "N"
            ? "center"
            : userData?.userId === message.userId
            ? "flex-end"
            : "flex-start",
      }}
    >
      {message.type !== "N" ? (
        <Stack
          rowGap={"0.3rem"}
          sx={{
            minHeight: "3rem",
            maxHeight: "10rem",
            minWidth: "10rem",
            maxWidth: "30rem",
            padding: "1rem",
            border: `1px solid ${palette.primary.main}`,
            borderRadius: "1rem",
            borderTopRightRadius:
              userData?.userId === message.userId ? 0 : null,
            borderTopLeftRadius: userData?.userId !== message.userId ? 0 : null,

            backgroundColor:
              userData?.userId === message.userId
                ? `${palette.primary.main}30`
                : "black",
          }}
        >
          <Box
            sx={{
              display: "flex",
              alignItems: "center",
              gap: "1rem",
              alignSelf:
                userData?.userId === message.userId ? "flex-start" : "flex-end",
            }}
          >
            {message.userId !== room.hostId ? (
              <FaceOutlined color="primary" />
            ) : (
              <span style={{ marginLeft: "0.2rem" }}>
                <Crown />
              </span>
            )}
            <Text text={message.userName} color="info" />
            <Text text={`#${message.userId}`} color="info" />
          </Box>
          <Box sx={{ width: "100%", wordWrap: "break-word" }}>
            <Text text={message.text} variant={"subtitle2"} />
          </Box>
          <Box
            sx={{
              alignSelf:
                userData?.userId === message.userId ? "flex-end" : "flex-start",
            }}
          >
            <Text
              text={getMessageTime(message.timestamp)}
              color="success"
              variant={"overline"}
            />
          </Box>
        </Stack>
      ) : (
        <Text color={"info"} variant={"subtitle2"} text={message.text} />
      )}
    </Box>
  );
}

export default ChatMessage;
