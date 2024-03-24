import React, { useState } from "react";
import { updateInvitation } from "../../api/Invitation";
import { Box, Button, LinearProgress } from "@mui/material";
import Input from "../../UI/Input";
import Text from "../../UI/Text";
import { Replay } from "@mui/icons-material";

function ManageInvitation({ type, inviteId, onRetry, onClose }) {
  const [loading, setLoading] = useState(false);
  const [alert, setAlert] = useState(null);

  const onAction = () => {
    setLoading(true);
    updateInvitation(inviteId, type).then((res) => {
      setLoading(false);
      if (res.status === "OK") {
        setAlert({ color: "success", text: res.message });
        setTimeout(() => {
          onClose();
        }, 1000);
      } else {
        setAlert({ color: "error", text: res.message });
      }
    });
  };

  let label = "";
  if (type === "A")
    label = "Are you sure you want to accept this invitation [Y/N]";
  if (type === "R")
    label = "Are you sure you want to reject this invitation [Y/N]";

  return (
    <Box width={"40rem"} position={"relative"} padding={"2rem"}>
      <Input
        color={"primary"}
        label={label}
        onEnter={(value) => {
          if (value !== "Y" && value !== "N") {
            setAlert({
              color: "error",
              text: "Invalid input given! Please type Y(Yes) or N(No)",
            });
            return;
          }
          onAction();
        }}
      />
      <Box sx={{ mb: "1rem" }}>
        {alert && (
          <Box>
            <Text {...alert} />
          </Box>
        )}
      </Box>
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

export default ManageInvitation;
