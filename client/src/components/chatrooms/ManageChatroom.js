import { Box, Button, LinearProgress } from "@mui/material";
import Input from "../../UI/Input";
import React, { useState } from "react";
import {
  createChatroom,
  deleteChatroom,
  exitChatroom,
  updateChatroom,
} from "../../api/Chatroom";
import Text from "../../UI/Text";
import { Replay } from "@mui/icons-material";

function ManageChatroom({ type, onRetry, onClose, roomId }) {
  const [loading, setLoading] = useState(false);
  const [alert, setAlert] = useState(null);

  const onCreate = (roomName) => {
    setLoading(true);
    createChatroom(roomName).then((res) => {
      setLoading(false);
      if (res.status === "CREATED") {
        setAlert({ color: "success", text: res.message });
        onClose();
      } else {
        setAlert({ color: "error", text: res.message });
      }
    });
  };

  const onUpdate = (roomId, roomName) => {
    setLoading(true);
    updateChatroom(roomId, roomName).then((res) => {
      setLoading(false);
      if (res.status === "OK") {
        setAlert({ color: "success", text: res.message });
        // onClose();
      } else {
        setAlert({ color: "error", text: res.message });
      }
    });
  };

  const onDelete = (roomId) => {
    setLoading(true);
    deleteChatroom(roomId).then((res) => {
      setLoading(false);
      if (res.status === "OK") {
        setAlert({ color: "success", text: res.message });
        // onClose();
      } else {
        setAlert({ color: "error", text: res.message });
      }
    });
  };

  const onExit = (roomId) => {
    setLoading(true);
    exitChatroom(roomId).then((res) => {
      setLoading(false);
      if (res.status === "OK") {
        setAlert({ color: "success", text: res.message });
        onClose();
      } else {
        setAlert({ color: "error", text: res.message });
      }
    });
  };

  let label = "";
  if (type === "CREATE") label = "Enter the chatroom name";
  if (type === "UPDATE") label = "Enter the new chatroom name";
  if (type === "DELETE")
    label = "Are you sure you want to deactivate this chatroom [Y/N]";
  if (type === "EXIT")
    label = "Are you sure you want to exit this chatroom [Y/N]";

  return (
    <Box width={"40rem"} position={"relative"} padding={"2rem"}>
      <Input
        color={"primary"}
        label={label}
        onEnter={(value) => {
          if (type === "CREATE") {
            if (!value) {
              setAlert({
                color: "error",
                text: "Name is required for creating chatroom",
              });
              return;
            }
            onCreate(value);
          }
          if (type === "UPDATE") {
            if (!value) {
              setAlert({
                color: "error",
                text: "New name is required for updating chatroom",
              });
              return;
            }
            onUpdate(roomId, value);
          }
          if (type === "DELETE") {
            if (value !== "Y" && value !== "N") {
              setAlert({
                color: "error",
                text: "Invalid input given! Please type Y(Yes) or N(No)",
              });
              return;
            }
            if (value === "Y") {
              onDelete(roomId);
            } else {
              onClose();
            }
          }
          if (type === "EXIT") {
            if (value !== "Y" && value !== "N") {
              setAlert({
                color: "error",
                text: "Invalid input given! Please type Y(Yes) or N(No)",
              });
              return;
            }
            if (value === "Y") {
              onExit(roomId);
            } else {
              onClose();
            }
          }
        }}
      />
      <Box sx={{ mb: "1rem" }}>
        {alert && (
          <Box>
            <Text {...alert} />
          </Box>
        )}
      </Box>
      {alert && alert.color === "error" && (
        <Button
          variant="outlined"
          startIcon={<Replay />}
          onClick={() => {
            onRetry();
          }}
        >
          Retry
        </Button>
      )}
      {loading && (
        <Box position={"absolute"} bottom={"0"} left={"0"} width={"100%"}>
          <LinearProgress color="primary" sx={{ backgroundColor: "black" }} />
        </Box>
      )}
    </Box>
  );
}

export default ManageChatroom;
