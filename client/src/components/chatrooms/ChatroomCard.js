import {
  Box,
  IconButton,
  ListItemIcon,
  Menu,
  MenuItem,
  Paper,
  Stack,
  Typography,
  useTheme,
} from "@mui/material";
import React, { useState } from "react";
import Text from "../../UI/Text";
import {
  Edit,
  ExitToApp,
  Group,
  HighlightOff,
  Login,
  MoreVert,
} from "@mui/icons-material";
import { Crown } from "../../assets/icons";

function ChatroomCard({ room, onEnter, onExit, type, active, setModal }) {
  const { palette } = useTheme();
  const [anchorEl, setAnchorEl] = useState(null);
  const open = Boolean(anchorEl);
  const handleClick = (event) => {
    setAnchorEl(event.currentTarget);
  };
  const handleClose = () => {
    setAnchorEl(null);
  };

  const options = [
    {
      id: "1",
      label: "Edit",
      icon: <Edit fontSize="small" color="info" />,
      onClick: () => {
        setModal({ type: "UPDATE", roomId: room?.roomId });
      },
    },
    type === "H"
      ? {
          id: "2",
          label: "Deactivate",
          icon: <HighlightOff fontSize="small" color="error" />,
          onClick: () => {
            setModal({ type: "DELETE", roomId: room?.roomId });
          },
        }
      : {
          id: "2",
          label: "Exit",
          icon: <ExitToApp fontSize="small" color="error" />,
          onClick: () => {
            setModal({ type: "EXIT", roomId: room?.roomId });
          },
        },
  ];

  return (
    <Paper
      variant="outlined"
      sx={{
        borderColor: palette.primary.main,
        boxSizing: "border-box",
        padding: "1rem",
        width: "100%",
        backgroundColor: active ? palette.primary.main + "50" : "black",
      }}
    >
      <Box
        sx={{
          display: "flex",
          alignItems: "center",
          justifyContent: "space-between",
        }}
      >
        <Stack rowGap={"0.5rem"} alignItems={"flex-start"}>
          <Text color={"primary"} variant={"body1"} text={room?.roomName} />
          <Box sx={{ display: "flex", gap: "2rem" }}>
            <Box sx={{ display: "flex", alignItems: "center", gap: "0.3rem" }}>
              <Crown />
              <Text
                color={"primary"}
                text={room?.hostName}
                variant={"caption"}
              />
              <Text
                color={"primary"}
                text={`(#${room?.hostId})`}
                variant={"caption"}
              />
            </Box>
            <Box
              sx={{ display: "flex", alignItems: "flex-end", gap: "0.5rem" }}
            >
              <Group fontSize="small" color="primary" />
              <Text
                color={"primary"}
                text={room?.participantCount}
                variant={"caption"}
              />
            </Box>
          </Box>
        </Stack>
        <Box sx={{ display: "flex", gap: "1rem" }}>
          <IconButton
            onClick={() => {
              onEnter(room);
            }}
          >
            <Login color="success" />
          </IconButton>
          <Box>
            <IconButton
              aria-label="more"
              id="long-button"
              aria-controls={open ? "long-menu" : undefined}
              aria-expanded={open ? "true" : undefined}
              aria-haspopup="true"
              onClick={handleClick}
            >
              <MoreVert color="info" />
            </IconButton>
            <Menu
              id="long-menu"
              MenuListProps={{
                "aria-labelledby": "long-button",
              }}
              anchorEl={anchorEl}
              open={open}
              onClose={handleClose}
              PaperProps={{
                style: {
                  maxHeight: 48 * 4.5,
                  width: "20ch",
                  backgroundColor: "black",
                  border: `1px solid ${palette.primary.main}`,
                },
              }}
            >
              {options.map((option) => (
                <MenuItem
                  key={option}
                  selected={option === "Pyxis"}
                  onClick={() => {
                    option.onClick();
                    handleClose();
                  }}
                >
                  <ListItemIcon>{option.icon}</ListItemIcon>
                  <Typography variant="caption" color={"primary"}>
                    {option.label}
                  </Typography>
                </MenuItem>
              ))}
            </Menu>
          </Box>
        </Box>
      </Box>
    </Paper>
  );
}

export default ChatroomCard;
