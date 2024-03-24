import { Box, Button, LinearProgress, Stack } from "@mui/material";
import React, { useEffect } from "react";
import { useState } from "react";
import Input from "../../UI/Input";
import { saveUsername, sendOtp, verifyOtp } from "../../api/Authentication";
import Text from "../../UI/Text";
import { v4 as uuid } from "uuid";
import { Replay } from "@mui/icons-material";
import Cookies from "js-cookie";
import { useNavigate } from "react-router-dom";

function Login({ onRetry }) {
  const [user, setUser] = useState({
    userId: "",
    userEmail: "",
    OTP: "",
    userName: "",
  });
  const [messages, setMessages] = useState({
    userEmail: null,
    OTP: null,
    userName: null,
  });

  const updateUser = (value, key) => {
    setUser((prevState) => ({ ...prevState, [key]: value }));
  };

  const updateMessages = (value, key) => {
    setMessages((prevState) => ({
      ...prevState,
      [key]: { id: uuid(), ...value },
    }));
  };

  const [loading, setLoading] = useState(null);

  useEffect(() => {
    return () => {
      setUser({ userId: "", userEmail: "", userName: "", OTP: "" });
      setLoading(false);
      setMessages({ userEmail: null, userName: null, OTP: null });
    };
  }, []);

  const navigate = useNavigate();

  return (
    <Stack
      minHeight={"15rem"}
      width={"40rem"}
      padding={"2rem"}
      sx={{ position: "relative" }}
      rowGap={"1.5rem"}
      alignItems={"flex-start"}
    >
      <Box>
        <Input
          color={"primary"}
          label={"Enter user email"}
          onEnter={(email) => {
            setLoading(true);
            sendOtp(email).then((res) => {
              setLoading(false);
              if (res.status === "OK") {
                updateUser(email, "userEmail");
                updateMessages(
                  { color: "success", text: res.message },
                  "userEmail"
                );
              } else {
                updateMessages(
                  { color: "error", text: res.message },
                  "userEmail"
                );
              }
            });
          }}
          input={user.userEmail ? false : true}
          value={user.userEmail}
        />
        {messages.userEmail && (
          <Box key={messages.userEmail.id}>
            <Text {...messages.userEmail} />
          </Box>
        )}
      </Box>
      {user.userEmail && (
        <Box>
          <Input
            color={"primary"}
            label={"Enter the OTP sent to this email address"}
            onEnter={(otp) => {
              setLoading(true);
              verifyOtp(user.userEmail, otp).then((res) => {
                setLoading(false);
                if (res.status === "OK" || res.status === "CREATED") {
                  updateUser(res.data.user.userId, "userId");
                  updateMessages(
                    { color: "success", text: res.message },
                    "OTP"
                  );

                  if (res.data.user.userName) {
                    updateMessages(
                      { color: "info", text: "Redirecting to home page ..." },
                      "OTP"
                    );
                    Cookies.set("AccessToken", res.data.accessToken, {
                      expires: 1,
                    });
                    setTimeout(() => {
                      navigate("/home");
                    }, 1000);
                  } else {
                    updateUser(otp, "OTP");
                  }
                  Cookies.set(
                    "UserData",
                    JSON.stringify({ ...res.data.user }),
                    { expires: 1 }
                  );
                } else {
                  updateMessages({ color: "error", text: res.message }, "OTP");
                }
              });
            }}
            input={true}
            value={user.OTP}
          />

          {messages.OTP && (
            <Box key={messages.OTP.id}>
              <Text {...messages.OTP} />
            </Box>
          )}
        </Box>
      )}
      {user.OTP && (
        <Box>
          <Input
            color={"primary"}
            label={"Enter a username to complete registration"}
            onEnter={(userName) => {
              setLoading(true);
              saveUsername(user.userId, userName, user.userEmail).then(
                (res) => {
                  setLoading(false);
                  if (res.status === "OK") {
                    let userData = Cookies.get("UserData");
                    if (userData) {
                      userData = JSON.parse(userData);
                      const newUserData = { ...userData, userName };
                      Cookies.set("UserData", JSON.stringify(newUserData), {
                        expires: 1,
                      });
                    }
                    Cookies.set("AccessToken", res.data.accessToken, {
                      expires: 1,
                    });
                    updateMessages(
                      {
                        color: "success",
                        text: "Username updated successfully! Redirecting to home page ...",
                      },
                      "userName"
                    );

                    setTimeout(() => {
                      navigate("/home");
                    }, 1000);
                  } else {
                    updateMessages(
                      { color: "error", text: res.message },
                      "userName"
                    );
                  }
                }
              );
            }}
            input={true}
            value={user.userName}
          />
          {messages.userName && (
            <Box key={messages.userName.id}>
              <Text {...messages.userName} />
            </Box>
          )}
        </Box>
      )}
      {Object.values(messages).filter((m) => m?.color === "error").length >
        0 && (
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
    </Stack>
  );
}

export default Login;
