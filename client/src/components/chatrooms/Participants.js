import { Box, Button, LinearProgress, Stack } from "@mui/material";
import React, { useEffect, useState } from "react";
import Text from "../../UI/Text";
import { Crown } from "../../assets/icons";
import { Add, FaceOutlined } from "@mui/icons-material";
import { v4 as uuid } from "uuid";
import { getParticipants } from "../../api/Chatroom";
import Window from "../../UI/Window";
import ManageParticipant from "./ManageParticipant";
import Cookies from "js-cookie";

function Participants({ room }) {
  const user = JSON.parse(Cookies.get("UserData"));
  const [participants, setParticipants] = useState([]);
  const [loading, setLoading] = useState(false);
  const [alert, setAlert] = useState(null);
  const updateAlert = (value) => {
    setAlert({ id: uuid(), ...value });
  };

  const [modal, setModal] = useState(null);

  useEffect(() => {
    if (room && room.roomId) {
      setLoading(true);
      getParticipants(room.roomId).then((res) => {
        setLoading(false);
        if (res.status === "OK") {
          const finalParticipants = res.data.users;
          console.log(finalParticipants);
          const index = finalParticipants?.findIndex(
            (p) => p.userId === room.hostId
          );
          if (index && index !== -1) {
            const removed = finalParticipants?.splice(index, 1);
            finalParticipants.unshift(...removed);
          }
          setParticipants(finalParticipants);
          if (finalParticipants.length === 0) {
            updateAlert({
              color: "info",
              text: "No Participants available",
            });
          }
        } else {
          updateAlert({ color: "error", text: res.message });
        }
      });
    }
  }, [room]);

  return loading ? (
    <Box>
      <LinearProgress color="primary" sx={{ backgroundColor: "black" }} />
    </Box>
  ) : (
    <Box sx={{ padding: "1rem" }}>
      <Text color={"primary"} text={"Participants"} variant={"h6"} />
      <Stack mt={"1rem"} rowGap={"1rem"}>
        {participants.map((p) => {
          return (
            <Box
              key={p.userId}
              sx={{
                display: "flex",
                gap: "0.5rem",
                alignItems: "center",
              }}
            >
              {room.hostId === p.userId ? (
                <span style={{ marginLeft: "0.2rem" }}>
                  <Crown />
                </span>
              ) : (
                <FaceOutlined color="primary" />
              )}
              <Text
                color={"primary"}
                variant={"caption"}
                text={`${p.userName} (#${p.userId})`}
              />
            </Box>
          );
        })}
      </Stack>
      <Box sx={{ mb: "1rem" }}>
        {alert && (
          <Box>
            <Text {...alert} />
          </Box>
        )}
      </Box>
      {user.userId === room.hostId && (
        <Button
          variant="text"
          startIcon={<Add />}
          onClick={() => {
            setModal("ADD");
          }}
          sx={{ textTransform: "none" }}
          color="info"
        >
          Add Participants
        </Button>
      )}
      {modal && (
        <Window
          open={modal}
          onClose={() => {
            setModal(null);
          }}
        >
          <ManageParticipant
            type={modal}
            room={room}
            onRetry={() => {
              const value = modal;
              setModal(null);
              setTimeout(() => {
                setModal(value);
              }, 0);
            }}
            onClose={() => {
              setTimeout(() => {
                setModal(null);
              }, 1000);
            }}
          />
        </Window>
      )}
    </Box>
  );
}

export default Participants;
