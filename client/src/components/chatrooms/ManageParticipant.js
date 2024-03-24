import { Box, Button, LinearProgress } from "@mui/material";
import React, { useState } from "react";
import Input from "../../UI/Input";
import { Replay } from "@mui/icons-material";
import Text from "../../UI/Text";
import { sendInvitation } from "../../api/Invitation";

function ManageParticipant({ type, room, onRetry, onClose }) {
  const [alert, setAlert] = useState(null);
  const [loading, setLoading] = useState(false);

  const onAdd = (emailAddress) => {
    if (emailAddress) {
      setLoading(true);
      sendInvitation(emailAddress, room?.roomId).then((res) => {
        if (res.status === "OK") {
          setAlert({ color: "success", text: res.message });
        } else {
          setAlert({ color: "error", text: res.message });
        }
        setLoading(false);
      });
    } else {
      setAlert({ color: "info", text: "Email address is required" });
    }
  };

  return (
    <Box width={"40rem"} padding={"2rem"}>
      <Input
        label={"Enter the email address of the user"}
        onEnter={(value) => {
          if (type === "ADD") {
            onAdd(value);
          }
        }}
      />
      {alert && (
        <Box sx={{ mb: "1rem" }}>
          <Text {...alert} />
        </Box>
      )}
      {alert?.color === "error" && (
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

export default ManageParticipant;
