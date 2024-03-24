import { Box, Button, LinearProgress, Stack } from "@mui/material";
import React, { useEffect, useState } from "react";
import Text from "../../UI/Text";
import Dropdown from "../../UI/Dropdown";
import Window from "../../UI/Window";
import ManageChatroom from "./ManageChatroom";
import { getChatrooms } from "../../api/Chatroom";
import ChatroomCard from "./ChatroomCard";

function Chatrooms({ activeChatroom, onRoomEnter }) {
  const [rooms, setRooms] = useState([]);
  const [loading, setLoading] = useState(false);
  const [alert, setAlert] = useState(null);
  const [state, setState] = useState({ roomType: "H" });
  const [modal, setModal] = useState(null);

  const updateState = (value, key) => {
    setState((prevState) => ({ ...prevState, [key]: value }));
  };

  useEffect(() => {
    console.log(
      state.roomType && modal === null,
      state.roomType,
      modal,
      "THISRECORD"
    );
    if (state.roomType && modal === null) {
      setLoading(true);
      getChatrooms(state.roomType).then((res) => {
        setLoading(false);
        if (res.status === "OK") {
          const finalRooms = res.data.rooms;
          setRooms(finalRooms);
          if (finalRooms.length === 0) {
            setAlert({ color: "info", text: "No rooms available" });
          }
        } else {
          setAlert({ color: "error", text: res.message });
        }
      });
    }
    return () => {
      setAlert(null);
    };
  }, [state.roomType, modal]);

  return (
    <Box>
      <Box
        sx={{
          display: "flex",
          alignItems: "flex-start",
          justifyContent: "space-between",
          width: "100%",
        }}
      >
        <Text color={"primary"} text={"Chatrooms"} variant={"h6"} />
        <Box sx={{ display: "flex", alignItems: "center", gap: "1rem" }}>
          <Button
            sx={{ textTransform: "none" }}
            variant="outlined"
            color="primary"
            onClick={() => {
              setModal({ type: "CREATE" });
            }}
            size="small"
          >
            Create
          </Button>
          <Dropdown
            label={"type"}
            menuItems={[
              { label: "Host", value: "H" },
              { label: "Guest", value: "P" },
            ]}
            value={state.roomType}
            onChange={(value) => {
              updateState(value, "roomType");
            }}
            width={"5rem"}
            size="small"
          />
        </Box>
      </Box>
      {loading ? (
        <Box>
          <LinearProgress color="primary" sx={{ backgroundColor: "black" }} />
        </Box>
      ) : (
        <Stack rowGap="1rem" margin={"1rem 0"}>
          {rooms.map((room) => (
            <ChatroomCard
              type={state.roomType}
              room={room}
              key={room.roomId}
              active={activeChatroom?.roomId === room.roomId}
              onEnter={onRoomEnter}
              setModal={setModal}
            />
          ))}
          <Box sx={{ mb: "1rem" }}>
            {alert && (
              <Box>
                <Text {...alert} />
              </Box>
            )}
          </Box>
        </Stack>
      )}
      {modal && (
        <Window
          onClose={() => {
            setModal(null);
          }}
          open={modal}
        >
          <ManageChatroom
            type={modal.type}
            roomId={modal?.roomId}
            onRetry={() => {
              const value = modal;
              setModal(null);
              setTimeout(() => {
                setModal(value);
              }, 0);
            }}
            onClose={() => {
              setModal(null);
            }}
          />
        </Window>
      )}
    </Box>
  );
}

export default Chatrooms;
