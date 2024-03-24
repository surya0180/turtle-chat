import { Box, IconButton, LinearProgress, Stack } from "@mui/material";
import React, { useEffect, useState } from "react";
import Text from "../../UI/Text";
import Dropdown from "../../UI/Dropdown";
import Window from "../../UI/Window";
import ManageInvitation from "./ManageInvitation";
import { getInvitations } from "../../api/Invitation";
import InvitationCard from "./InvitationCard";
import { Replay } from "@mui/icons-material";

function Invitations() {
  const [invitations, setInvitations] = useState([]);
  const [loading, setLoading] = useState(false);
  const [alert, setAlert] = useState(null);
  const [modal, setModal] = useState(null);
  const [state, setState] = useState({
    inviteStatus: "P",
    inviteBy: "received",
  });
  const [ping, setPing] = useState(false);

  const updateState = (value, key) => {
    setState((prevState) => ({ ...prevState, [key]: value }));
  };

  useEffect(() => {
    if (state.inviteBy && state.inviteStatus && modal === null) {
      setLoading(true);
      getInvitations(state.inviteBy, state.inviteStatus).then((res) => {
        setLoading(false);
        if (res.status === "OK") {
          const finalInvitations = res.data.invitations;
          setInvitations(finalInvitations);
          if (finalInvitations.length === 0) {
            setAlert({ color: "info", text: "No Invitations available" });
          }
        } else {
          setAlert({ color: "error", text: res.message });
        }
      });
    }
    return () => {
      setAlert(null);
    };
  }, [state.inviteBy, state.inviteStatus, modal, ping]);

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
        <Text color={"primary"} text={"Invitations"} variant={"h6"} />
        <Box sx={{ display: "flex", alignItems: "center", gap: "1rem" }}>
          <IconButton
            variant="outlined"
            size="small"
            onClick={() => {
              setPing((prevState) => !prevState);
            }}
          >
            <Replay color="primary" />
          </IconButton>
          <Dropdown
            label={"type"}
            menuItems={[
              { label: "Sent", value: "sent" },
              { label: "Recieved", value: "received" },
            ]}
            value={state.inviteBy}
            onChange={(value) => {
              updateState(value, "inviteBy");
            }}
            size="small"
          />
          <Dropdown
            label={"type"}
            menuItems={[
              { label: "Pending", value: "P" },
              { label: "Accepted", value: "A" },
              { label: "Rejected", value: "R" },
            ]}
            value={state.inviteStatus}
            onChange={(value) => {
              updateState(value, "inviteStatus");
            }}
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
          {invitations.map((invite) => (
            <InvitationCard
              by={state.inviteBy}
              status={state.inviteStatus}
              invite={invite}
              key={invite.inviteId}
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
          <ManageInvitation
            inviteId={modal?.inviteId}
            type={modal?.type}
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

export default Invitations;
