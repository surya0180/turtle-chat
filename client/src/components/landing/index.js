import { Box, Button, Stack } from "@mui/material";
import React, { useEffect, useState } from "react";
import { Turtle1 } from "../../assets/icons";
import TypedText from "../../UI/TypedText";
import Window from "../../UI/Window";
import Login from "../login";
import { useNavigate } from "react-router-dom";
import Cookies from "js-cookie";
import Header from "../../UI/Header";

const title = "Turtle Chat";
const summary =
  "Real-Time Chat Application built using ReactJS, Spring Boot, PostgreSQL, Redis and Websockets.";

function LandingPage() {
  const [login, setLogin] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    let userData = Cookies.get("UserData");
    if (userData) {
      userData = JSON.parse(userData);
      if (
        Cookies.get("AccessToken") &&
        userData.userId &&
        userData.userEmail &&
        userData.userName
      ) {
        navigate("/home");
      }
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return (
    <Box>
      <Header />
      <Box
        sx={{
          display: "flex",
          alignItems: "center",
          gap: "10rem",
          height: "25rem",
          marginTop: "10rem",
          padding: "2rem",
        }}
      >
        <Turtle1 />
        <Stack
          rowGap={"3rem"}
          width={"40rem"}
          marginTop={"-5rem"}
          alignItems={"flex-start"}
        >
          <TypedText text={title} variant={"h1"} />
          <TypedText text={summary} delay={25} />
          <Button
            variant="outlined"
            sx={{ textTransform: "none" }}
            onClick={() => {
              setLogin(true);
            }}
          >
            Login
          </Button>
        </Stack>
      </Box>
      {login && (
        <Window
          onClose={() => {
            setLogin(false);
          }}
          open={login}
        >
          <Login
            onRetry={() => {
              setLogin(false);
              setTimeout(() => {
                setLogin(true);
              }, 0);
            }}
          />
        </Window>
      )}
    </Box>
  );
}

export default LandingPage;
