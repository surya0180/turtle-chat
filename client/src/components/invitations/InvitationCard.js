import { Box, IconButton, Paper, useTheme } from "@mui/material";
import React from "react";
import Text from "../../UI/Text";
import { CancelOutlined, CheckCircleOutlineRounded } from "@mui/icons-material";

function InvitationCard({ by, status, invite, setModal }) {
  const { palette } = useTheme();

  return (
    <Paper
      variant="outlined"
      sx={{
        borderColor: palette.primary.main,
        boxSizing: "border-box",
        padding: "1rem",
        width: "100%",
        backgroundColor: "black",
      }}
    >
      <Box
        sx={{
          display: "flex",
          alignItems: "center",
          justifyContent: "space-between",
        }}
      >
        <Box>
          <Text color={"primary"} variant={"body1"} text={invite?.roomName} />
          <Text
            color={"primary"}
            variant={"caption"}
            text={`(${invite?.senderUserName})`}
          />
        </Box>
        {by === "received" && status === "P" && (
          <Box sx={{ display: "flex", gap: "1rem" }}>
            <IconButton
              onClick={() => {
                setModal({ type: "R", inviteId: invite?.inviteId });
              }}
            >
              <CancelOutlined color="error" fontSize="large" />
            </IconButton>
            <IconButton
              onClick={() => {
                setModal({ type: "A", inviteId: invite?.inviteId });
              }}
            >
              <CheckCircleOutlineRounded color="success" fontSize="large" />
            </IconButton>
          </Box>
        )}
      </Box>
    </Paper>
  );
}

export default InvitationCard;
