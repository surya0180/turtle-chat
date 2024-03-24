import { Box, Grid, Stack } from "@mui/material";
import Cookies from "js-cookie";
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Chatrooms from "../chatrooms";
import Invitations from "../invitations";
import Room from "../chatrooms/Room";
import Header from "../../UI/Header";

function Home() {
  const navigate = useNavigate();
  const [user, setUser] = useState(null);
  useEffect(() => {
    let userData = Cookies.get("UserData");
    if (!userData) navigate("/");
    else {
      userData = JSON.parse(userData);
      if (
        !Cookies.get("AccessToken") ||
        !userData ||
        !userData.userId ||
        !userData.userEmail ||
        !userData.userName
      ) {
        navigate("/");
      } else {
        setUser(userData);
      }
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const [activeRoom, setActiveRoom] = useState(null);

  return (
    <>
      <Header userData={user} />
      <Grid container height={"calc(100vh - 5rem)"}>
        <Grid item md="4" borderRight={"1px solid white"}>
          <Stack rowGap={"1rem"}>
            <Box
              sx={{
                height: "calc(50vh - 3rem)",
                borderBottom: "1px solid white",
                overflowY: "scroll",
                padding: "1rem",
              }}
            >
              <Chatrooms
                onRoomEnter={(room) => {
                  setActiveRoom(room);
                }}
                activeChatroom={activeRoom}
              />
            </Box>
            <Box
              sx={{
                height: "calc(50vh - 3rem)",
                overflowY: "scroll",
                padding: "1rem",
              }}
            >
              <Invitations />
            </Box>
          </Stack>
        </Grid>
        <Grid item md="8">
          <Room
            room={activeRoom}
            onRoomExit={() => {
              setActiveRoom(null);
            }}
          />
        </Grid>
      </Grid>
    </>
  );
}

export default Home;
